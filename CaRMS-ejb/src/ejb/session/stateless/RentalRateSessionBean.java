/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.RentalRate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.DeleteRentalRateException;
import util.exception.InputDataValidationException;
import util.exception.RentalRateNotFoundException;
import util.exception.UpdateRentalRateException;

/**
 *
 * @author Goh Chun Teck
 */
@Stateless
@Local(RentalRateSessionBeanLocal.class)
@Remote(RentalRateSessionBeanRemote.class)
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public RentalRateSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public RentalRate createNewRentalRate(RentalRate newRentalRate) {
        entityManager.persist(newRentalRate);
        entityManager.flush();

        return newRentalRate;
    }

    @Override
    public List<RentalRate> retrieveAllRentalRates() {
        Query query = entityManager.createQuery("SELECT r FROM RentalRate r");

        return query.getResultList();
    }

    @Override
    public RentalRate retrieveRentalRateByRentalRateId(Long rentalRateId) throws RentalRateNotFoundException {
        RentalRate rentalRate = entityManager.find(RentalRate.class, rentalRateId);

        if (rentalRate != null) {
            return rentalRate;
        } else {
            throw new RentalRateNotFoundException("Rental Rate ID " + rentalRateId + " does not exist!");
        }
    }

    @Override
    public RentalRate retrieveRentalRateByRentalRateName(String name) throws RentalRateNotFoundException {
        Query query = entityManager.createNamedQuery("SELECT rr FROM RentalRate rr WHERE rr.name = :inName");
        query.setParameter("inName", name);

        try {
            return (RentalRate) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RentalRateNotFoundException("Rental Rate Name " + name + " does not exist!");
        }
    }

    @Override
    public void updateRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException, UpdateRentalRateException, InputDataValidationException {
        entityManager.merge(rentalRate);
    }

    @Override
    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException, DeleteRentalRateException {
        RentalRate rentalRateToRemove = retrieveRentalRateByRentalRateId(rentalRateId);
        if (rentalRateToRemove.getCarCategory() != null) {
            entityManager.remove(rentalRateToRemove);
        } else {
            rentalRateToRemove.setIsDisabled(true);
            throw new DeleteRentalRateException("Rental Rate ID " + rentalRateId + " is associated with existing car(s) and cannot be deleted but has been disabled!");
        }
    }
    
    @Override
    public List<RentalRate> ratesAvailable(List<RentalRate> allRates, Date pickDateTime, Date returnDateTime) {
        List<RentalRate> ratesApplicable = new ArrayList<>();
        List<LocalDateTime> rentalPeriod = new ArrayList<>();
        LocalDateTime start = pickDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = returnDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        for (LocalDateTime date = start; date.isBefore(end); date = date.plusDays(1)) { // to change the plus one day
            rentalPeriod.add(date);
        }

        for (LocalDateTime d : rentalPeriod) {
            double min = Double.MAX_VALUE;

            RentalRate cheapest = allRates.get(0);
            for (RentalRate r : allRates) {
                if (r.getValidityStartDate() == null || r.getValidityEndDate() == null) {
                    if (r.getDailyRate() < min) {
                        cheapest = r;
                        min = r.getDailyRate();
                    }
                } else {
                    LocalDateTime rentStart = r.getValidityStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime rentEnd = r.getValidityEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    if (d.compareTo(rentStart) >= 0 && d.compareTo(rentEnd) <= 0) {
                        if (r.getDailyRate() < min) {
                            cheapest = r;
                            min = r.getDailyRate();
                        }
                    }
                }
            }
            ratesApplicable.add(cheapest);
        }
        return ratesApplicable;
    }


    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RentalRate>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
