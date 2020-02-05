/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.Car;
import Entity.Customer;
import Entity.Outlet;
import Entity.Reservation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ReservationNotFoundException;
import util.exception.ReservationUsernameExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateReservationException;

/**
 *
 * @author Goh Chun Teck
 */
@Stateless
@Local(ReservationSessionBeanLocal.class)
@Remote(ReservationSessionBeanRemote.class)
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBean;

    @EJB
    private CarModelSessionBeanLocal carModelSessionBean;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ReservationSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Reservation createNewReservationAndUpdateCus(Reservation newReservation, Long cusId) throws InputDataValidationException, ReservationUsernameExistException, UnknownPersistenceException {
        try {
            Customer customer = customerSessionBean.retrieveCustomerById(cusId);
            customer.getReservations().add(newReservation);
        } catch (CustomerNotFoundException ex) {
            ex.printStackTrace();
        }
        entityManager.persist(newReservation);
         entityManager.flush();
         return newReservation;
    }

    @Override
    public Reservation createNewReservation(Reservation newReservation) throws InputDataValidationException, ReservationUsernameExistException, UnknownPersistenceException {
        entityManager.persist(newReservation);
         entityManager.flush();
         return newReservation;
    }
    
    @Override
    public List<Reservation> retrieveAllReservations() {
        Query query = entityManager.createQuery("SELECT c FROM Reservation c");

        return query.getResultList();
    }

    @Override
    public Reservation retrieveReservationById(Long ReservationId) throws ReservationNotFoundException {
        Reservation reservation = entityManager.find(Reservation.class, ReservationId);

        if (reservation != null) {
            return reservation;
        } else {
            throw new ReservationNotFoundException("Reservation ID " + ReservationId + " does not exist!");
        }
    }

    @Override
    public List<Reservation> retriveReservationByStartDate(Date startDate) {

        Query query = entityManager.createQuery("SELECT r FROM Reservation r");
        List<Reservation> list = query.getResultList();
        List<Reservation> filtered = new ArrayList<>();
        for (Reservation reservation : list) {
            if(getDateCeiling(reservation.getStartDate()).equals(getDateCeiling(startDate))){
                filtered.add(reservation);
            }
        }
        
        return filtered;
     }
    
    private Date getDateCeiling(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        Date nextDayCeiling = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE) + 1).getTime();
        return nextDayCeiling;
    }
    
    @Override
    public void updateReservation(Reservation reservation) throws InputDataValidationException, ReservationNotFoundException, UpdateReservationException {
        entityManager.merge(reservation);
        entityManager.flush();
    }

    @Override
    public Car retrieveCarByReservationId(Long id) {
        Reservation r = entityManager.find(Reservation.class, id);
        return r.getCar();
    }

    @Override
    public void deleteReservation(Long reservationId) throws ReservationNotFoundException {
        Reservation reservationToRemove = retrieveReservationById(reservationId);

        if (reservationToRemove.getCustomer() != null) {
            reservationToRemove.setCustomer(null);
            entityManager.merge(reservationToRemove);
        }

        entityManager.remove(reservationToRemove);
        entityManager.flush();

    }

    @Override
    public List<Reservation> getConflictReservations(List<Car> available, Outlet pickUpOutlet, Date pickDateTime, Date returnDateTime, Long type, Long id) {
        List<Reservation> reservations = retrieveAllReservations();
        if (type == 1) {
            Iterator<Reservation> iter = reservations.iterator();
            while (iter.hasNext()) {
                Reservation reservation = iter.next();
                try {
                    if (reservation.getCarModel() == null) {
                        iter.remove();
                    } else if (!reservation.getCarModel().equals(carModelSessionBean.retrieveCarModelByCarModelId(id))) {
                        iter.remove();
                    }
                } catch (CarModelNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            Iterator<Reservation> iter = reservations.iterator();
            while (iter.hasNext()) {
                Reservation reservation = iter.next();
                try {
                    if (reservation.getCarCategory() == null) {
                        iter.remove();
                    } else if (!reservation.getCarCategory().equals(carCategorySessionBean.retrieveCarCategoryByCarCategoryId(id))) {
                        iter.remove();
                    }
                } catch (CarCategoryNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }
        List<Reservation> conflicts = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getDropOffOutlet().equals(pickUpOutlet)) {
                if (r.getEndDate().after(pickDateTime) || r.getStartDate().before(returnDateTime)) {
                    conflicts.add(r);
                }
            } else {
                Date tempResEndDate = (Date) r.getEndDate().clone();
                //change to add 2 hrs
                tempResEndDate.setHours(r.getEndDate().getHours() + 2);
                if (tempResEndDate.after(pickDateTime) || r.getStartDate().before(returnDateTime)) {
                    conflicts.add(r);
                }
            }
        }
        return conflicts;
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Reservation>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
