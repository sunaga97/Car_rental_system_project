/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.Car;
import Entity.CarCategory;
import Entity.CarModel;
import Entity.RentalRate;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import util.exception.CarModelNotFoundException;
import util.exception.DeleteCarModelException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarModelException;

/**
 *
 * @author Goh Chun Teck
 */
@Stateless
@Local (CarModelSessionBeanLocal.class)
@Remote (CarModelSessionBeanRemote.class)

public class CarModelSessionBean implements CarModelSessionBeanRemote, CarModelSessionBeanLocal {
    
     @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager entityManager;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public CarModelSessionBean(){
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public CarModel createNewCarModel(CarModel newCarModel) throws InputDataValidationException, UnknownPersistenceException {
         try
        {
            Set<ConstraintViolation<CarModel>>constraintViolations = validator.validate(newCarModel);
        
            if(constraintViolations.isEmpty())
            {
                entityManager.persist(newCarModel);
                entityManager.flush();

                return newCarModel;
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
    public List<CarModel> retrieveAllCarModel() {
       Query query = entityManager.createQuery("SELECT c FROM CarModel c");
        
        return query.getResultList();
    }

    @Override
    public CarModel retrieveCarModelByCarModelId(Long carModelId) throws CarModelNotFoundException {
        CarModel carModel = entityManager.find(CarModel.class, carModelId);
        
        if(carModel != null)
        {
            return carModel;
        }
        else
        {
            throw new CarModelNotFoundException("Car Model ID " + carModelId + " does not exist!");
        }
    } 
    
     @Override
    public CarModel retrieveCarModelByCarModelName(String carModelName) throws CarModelNotFoundException
    {
        Query query = entityManager.createQuery("SELECT cm FROM CarModel cm WHERE cm.carModelName = :inCarModelName");
        query.setParameter("inCarModelName", carModelName);
        
        try
        {
            return (CarModel)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new CarModelNotFoundException("Car Model Name " + carModelName + " does not exist!");
        }
    }
    
    @Override
    public void updateCarModel(CarModel carModel) throws CarModelNotFoundException, UpdateCarModelException, InputDataValidationException {

        entityManager.merge(carModel);
    }

    @Override
    public void deleteCarModel(Long carModelId) throws CarModelNotFoundException, DeleteCarModelException {
        CarModel carModelToRemove = retrieveCarModelByCarModelId(carModelId);
        
        if(carModelToRemove.getCars().isEmpty())
        {
            entityManager.remove(carModelToRemove);
        }
        else
        {
            carModelToRemove.setDisabled(true);
            System.err.println("Car Model cannot be deleted due to associated car(s) and will be disabled instead");
            entityManager.flush();
        }
    }
    
     @Override
    public List<Car> retrieveCarsUnderCarModel(Long carModelId) {
        CarModel carModel = null;
        
         try {
             carModel = retrieveCarModelByCarModelId(carModelId);
         } catch (CarModelNotFoundException ex) {
             Logger.getLogger(CarModelSessionBean.class.getName()).log(Level.SEVERE, null, ex);
         }
        carModel.getCars().size();
        List<Car> returningCars = carModel.getCars();
        return returningCars;
    }
    
     @Override
    public List<RentalRate> retrieveRentalRatesOfCarModel(Long carModelId) {
        CarModel carModel = null;
        
         try {
             carModel = retrieveCarModelByCarModelId(carModelId);
         } catch (CarModelNotFoundException ex) {
             Logger.getLogger(CarModelSessionBean.class.getName()).log(Level.SEVERE, null, ex);
         }
        CarCategory carCategory = carModel.getCarCategory();
        List<RentalRate> rentalRates = carCategory.getRentalRates();

        return rentalRates;
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CarModel>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}

