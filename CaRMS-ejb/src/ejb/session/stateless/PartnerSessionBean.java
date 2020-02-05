/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.Partner;
import Entity.Reservation;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.DeletePartnerException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;
import util.exception.PartnerUsernameExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdatePartnerException;

/**
 *
 * @author sunag
 */
@Stateless
@Local(PartnerSessionBeanLocal.class)
@Remote(PartnerSessionBeanRemote.class)
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public PartnerSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Partner createNewPartner(Partner newPartner) throws InputDataValidationException, UnknownPersistenceException, PartnerUsernameExistException {
        try {
            Set<ConstraintViolation<Partner>> constraintViolations = validator.validate(newPartner);

            if (constraintViolations.isEmpty()) {
                entityManager.persist(newPartner);
                entityManager.flush();

                return newPartner;
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new PartnerUsernameExistException("Partner Username already exist!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    public Partner login(String username, String password) throws InvalidLoginCredentialException {
        try {
            Partner partner = retrievePartnerByUsername(username);

            if (partner.getPassword().equals(password)) {
                partner.getReservations().size();
                return partner;
            } else {
                throw new InvalidLoginCredentialException("Invalid password!");
            }
        } catch (PartnerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }

    @Override
    public List<Partner> retrieveAllPartners() {
        Query query = entityManager.createQuery("SELECT p FROM Partner p");

        return query.getResultList();
    }

    @Override
    public Partner retrievePartnerById(Long PartnerId) throws PartnerNotFoundException {
        Partner partner = entityManager.find(Partner.class, PartnerId);

        if (partner != null) {
            return partner;
        } else {
            throw new PartnerNotFoundException("Partner ID " + PartnerId + " does not exist!");
        }
    }

    @Override
    public void updatePartner(Partner partner) throws InputDataValidationException, PartnerNotFoundException, UpdatePartnerException {
        if (partner != null && partner.getId() != null) {
            Set<ConstraintViolation<Partner>> constraintViolations = validator.validate(partner);

            if (constraintViolations.isEmpty()) {
                Partner partnerToUpdate = retrievePartnerById(partner.getId());

                partnerToUpdate.setEmail(partner.getEmail());
                partnerToUpdate.setName(partner.getName());
                partnerToUpdate.setPassword(partner.getPassword());

            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new PartnerNotFoundException("Partner Id provided is incorrect/not present");
        }
    }

    @Override
    public void deletePartner(Long partnerId) throws PartnerNotFoundException, DeletePartnerException {
        Partner partnerToRemove = retrievePartnerById(partnerId);

        if (partnerToRemove.getReservations().isEmpty()) {
            entityManager.remove(partnerToRemove);
        } else {
            throw new DeletePartnerException("Partner ID " + partnerId + " is associated with existing car(s)/employee(s) and cannot be deleted!");
        }
    }



    @Override
    public Partner retrievePartnerByUsername(String username) throws PartnerNotFoundException {
        Query query = entityManager.createQuery("SELECT p FROM Partner p WHERE p.email = :inUsername");
        query.setParameter("inUsername", username);

        try {
            return (Partner) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new PartnerNotFoundException("Partner Username " + username + " does not exist!");
        }
    }
    
    @Override
    public List<Reservation> retrieveReservationsFromPartner(Long partnerId){
        Partner partner = entityManager.find(Partner.class, partnerId);
        entityManager.flush();
        partner.getReservations().size();

        return partner.getReservations();
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Partner>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
