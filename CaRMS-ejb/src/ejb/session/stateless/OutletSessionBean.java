/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.Car;
import Entity.Outlet;
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
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.DeleteOutletException;
import util.exception.InputDataValidationException;
import util.exception.OutletExistsException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateOutletException;

/**
 *
 * @author Goh Chun Teck
 */
@Stateless
@Local(OutletSessionBeanLocal.class)
@Remote(OutletSessionBeanRemote.class)

public class OutletSessionBean implements OutletSessionBeanRemote, OutletSessionBeanLocal {
@PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager entityManager;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public OutletSessionBean(){
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Outlet createNewOutlet(Outlet newOutlet) throws InputDataValidationException, OutletExistsException, UnknownPersistenceException {
        try
        {
            Set<ConstraintViolation<Outlet>>constraintViolations = validator.validate(newOutlet);
        
            if(constraintViolations.isEmpty())
            {
                entityManager.persist(newOutlet);
                entityManager.flush();

                return newOutlet;
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }            
        }
        catch(PersistenceException ex){
                throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    @Override
    public List<Outlet> retrieveAllOutlets() {
       Query query = entityManager.createQuery("SELECT o FROM Outlet o");
        
        return query.getResultList();
    }

    @Override
    public Outlet retrieveOutletById(Long OutletId) throws OutletNotFoundException {
        Outlet Outlet = entityManager.find(Outlet.class, OutletId);
        
        if(Outlet != null)
        {
            return Outlet;
        }
        else
        {
            throw new OutletNotFoundException("Outlet ID " + OutletId + " does not exist!");
        }
    }
    @Override
    public void updateOutlet(Outlet outlet) throws InputDataValidationException, OutletNotFoundException, UpdateOutletException {
       if(outlet != null && outlet.getOutletId()!= null)
        {
            Set<ConstraintViolation<Outlet>>constraintViolations = validator.validate(outlet);
        
            if(constraintViolations.isEmpty())
            {
                Outlet outletToUpdate = retrieveOutletById(outlet.getOutletId());

                    outletToUpdate.setAddress(outlet.getAddress());
                    outletToUpdate.setOpenFrom(outlet.getOpenFrom());
                    outletToUpdate.setOpenTo(outlet.getOpenTo());
                    outletToUpdate.setName(outlet.getName());
            } 
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new OutletNotFoundException("Outlet Id provided is incorrect/not present");
        }
    }

    @Override
    public void deleteOutlet(Long outletId) throws OutletNotFoundException, DeleteOutletException {
        Outlet outletToRemove = retrieveOutletById(outletId);
        
        if(outletToRemove.getCars().isEmpty() && outletToRemove.getEmployees().isEmpty())
        {
            entityManager.remove(outletToRemove);
        }
        else
        {
            throw new DeleteOutletException("Outlet ID " + outletId + " is associated with existing car(s)/employee(s) and cannot be deleted!");
        }
    }
    
    @Override
    public Outlet retrieveOutletByName(String name) throws OutletNotFoundException
    {
        Query query = entityManager.createQuery("SELECT o FROM Outlet o WHERE o.name = :inName");
        query.setParameter("inName", name);
        
        try
        {
            return (Outlet)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new OutletNotFoundException("Outlet name " + name + " does not exist!");
        }
    }
    
    public List<Car> searchAvailableCars(String pickupOutletName, Date pickupDate, Date returnDate, String returnOutletName) throws OutletNotFoundException{
        List<Car> available = new ArrayList<Car>();
        Outlet pickupOutlet = retrieveOutletByName(pickupOutletName);
        Outlet returnOutlet = retrieveOutletByName(returnOutletName);
        
        
        return available;
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Outlet>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
