/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.Car;
import Entity.CarCategory;
import Entity.CarModel;
import Entity.Customer;
import Entity.Reservation;
import Entity.TransitRecord;
import java.time.temporal.TemporalUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import static java.util.concurrent.TimeUnit.HOURS;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CarStatusEnum;
import util.exception.InputDataValidationException;
import util.exception.ReservationNotFoundException;
import util.exception.UpdateReservationException;

/**
 *
 * @author sunag
 */
@Stateless
@Remote(TimerSessionBeanRemote.class)
@Local(TimerSessionBeanLocal.class)
public class TimerSessionBean implements TimerSessionBeanRemote, TimerSessionBeanLocal {

    @EJB
    private TransitRecordSessionBeanLocal transitRecordSessionBean;

    @EJB
    private CarModelSessionBeanLocal carModelSessionBean;

    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBean;

    @EJB
    private CarSessionBeanLocal carSessionBean;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @Schedule(hour = "2")
    public void allocateCarOfDay() {
        List<Reservation> reservations = reservationSessionBean.retriveReservationByStartDate(new Date());
        reservations.sort(new Comparator<Reservation>() {
            @Override
            public int compare(Reservation r1, Reservation r2) {
                if (r1.getCarModel() != null && r2.getCarModel() == null) {
                    return -1;
                }
                return 1;
            }
        });
        for (Reservation reservation : reservations) {
            Customer customer = reservation.getCustomer();
            if (reservation.getCarModel() != null) {
                CarModel carModel = reservation.getCarModel();
                carModel.getCars().size();
                for (Car car : carModel.getCars()) {
                    if (car.getCarStatusEnum() == CarStatusEnum.ON_RENTAL && car.getReservation().getDropOffOutlet().equals(reservation.getPickUpOutlet()) && car.getReservation().getEndDate().before(reservation.getStartDate())) {
                        reservation.setCar(car);
                        try {
                            reservationSessionBean.updateReservation(reservation);
                        } catch (InputDataValidationException ex) {
                            Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ReservationNotFoundException ex) {
                            Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (UpdateReservationException ex) {
                            Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    } else if (car.getCarStatusEnum() == CarStatusEnum.AVAILABLE && car.getOutlet().equals(reservation.getPickUpOutlet())) {
                        reservation.setCar(car);
                        try {
                            reservationSessionBean.updateReservation(reservation);
                        } catch (InputDataValidationException ex) {
                            Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ReservationNotFoundException ex) {
                            Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (UpdateReservationException ex) {
                            Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        car.setCarStatusEnum(CarStatusEnum.TO_BE_RENTED);
                        break;
                    } else if (car.getCarStatusEnum() == CarStatusEnum.ON_RENTAL && !car.getReservation().getDropOffOutlet().equals(reservation.getPickUpOutlet()) && car.getReservation().getEndDate().toInstant().plus(2, (TemporalUnit) HOURS).isBefore(reservation.getStartDate().toInstant())) {
                        reservation.setCar(car);
                        try {
                            reservationSessionBean.updateReservation(reservation);
                        } catch (InputDataValidationException ex) {
                            Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ReservationNotFoundException ex) {
                            Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (UpdateReservationException ex) {
                            Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        transitRecordSessionBean.createNewTransitRecord(new TransitRecord(reservation.getPickUpOutlet(), reservation, reservation.getStartDate()));
                        break;
                    } else if (car.getCarStatusEnum() == CarStatusEnum.AVAILABLE && !car.getOutlet().equals(reservation.getPickUpOutlet())) {
                        reservation.setCar(car);
                        car.setCarStatusEnum(CarStatusEnum.TO_BE_RENTED);
                        try {
                            reservationSessionBean.updateReservation(reservation);
                        } catch (InputDataValidationException ex) {
                            Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ReservationNotFoundException ex) {
                            Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (UpdateReservationException ex) {
                            Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        transitRecordSessionBean.createNewTransitRecord(new TransitRecord(reservation.getPickUpOutlet(), reservation, reservation.getStartDate()));
                        break;
                    }

                }
            } else {
                CarCategory carCategory = reservation.getCarCategory();
                carCategory.getCarModels().size();
                for (CarModel carModel1 : carCategory.getCarModels()) {
                    boolean check = true;
                    carModel1.getCars().size();
                    for (Car car : carModel1.getCars()) {
                        if (car.getCarStatusEnum() == CarStatusEnum.ON_RENTAL && car.getReservation().getDropOffOutlet().equals(reservation.getPickUpOutlet()) && car.getReservation().getEndDate().before(reservation.getStartDate())) {
                            reservation.setCar(car);
                            try {
                                reservationSessionBean.updateReservation(reservation);
                            } catch (InputDataValidationException ex) {
                                Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ReservationNotFoundException ex) {
                                Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (UpdateReservationException ex) {
                                Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            check = false;
                            break;
                        } else if (car.getCarStatusEnum() == CarStatusEnum.AVAILABLE && car.getOutlet().equals(reservation.getPickUpOutlet())) {
                            reservation.setCar(car);
                            try {
                                reservationSessionBean.updateReservation(reservation);
                            } catch (InputDataValidationException ex) {
                                Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ReservationNotFoundException ex) {
                                Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (UpdateReservationException ex) {
                                Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            car.setCarStatusEnum(CarStatusEnum.TO_BE_RENTED);
                            check = false;
                            break;
                        } else if (car.getCarStatusEnum() == CarStatusEnum.ON_RENTAL && !car.getReservation().getDropOffOutlet().equals(reservation.getPickUpOutlet()) && car.getReservation().getEndDate().toInstant().plus(2, (TemporalUnit) HOURS).isBefore(reservation.getStartDate().toInstant())) {
                            reservation.setCar(car);
                            try {
                                reservationSessionBean.updateReservation(reservation);
                            } catch (InputDataValidationException ex) {
                                Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ReservationNotFoundException ex) {
                                Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (UpdateReservationException ex) {
                                Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            transitRecordSessionBean.createNewTransitRecord(new TransitRecord(reservation.getPickUpOutlet(), reservation, reservation.getStartDate()));
                            check = false;
                            break;
                        } else if (car.getCarStatusEnum() == CarStatusEnum.AVAILABLE && !car.getOutlet().equals(reservation.getPickUpOutlet())) {
                            reservation.setCar(car);
                            car.setCarStatusEnum(CarStatusEnum.TO_BE_RENTED);
                            try {
                                reservationSessionBean.updateReservation(reservation);
                            } catch (InputDataValidationException ex) {
                                Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ReservationNotFoundException ex) {
                                Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (UpdateReservationException ex) {
                                Logger.getLogger(TimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            transitRecordSessionBean.createNewTransitRecord(new TransitRecord(reservation.getPickUpOutlet(), reservation, reservation.getStartDate()));
                            check = false;
                            break;
                        }
                    }
                    if (!check) {
                        break;
                    }
                }

            }

        }
    }

    public void persist(Object object) {
        em.persist(object);
    }

    public void persist1(Object object) {
        em.persist(object);
    }

}
