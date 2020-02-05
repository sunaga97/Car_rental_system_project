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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarCategoryNotFoundException;
import util.exception.DeleteCarCategoryException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarCategoryException;

/**
 *
 * @author Goh Chun Teck
 */
@Stateless
@Local (CarCategorySessionBeanLocal.class)
@Remote (CarCategorySessionBeanRemote.class)

public class CarCategorySessionBean implements CarCategorySessionBeanRemote, CarCategorySessionBeanLocal {
    
    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager entityManager;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public CarCategorySessionBean(){
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public CarCategory createNewCarCategory(CarCategory newCarCategory) throws InputDataValidationException,UnknownPersistenceException {
         try
        {
            Set<ConstraintViolation<CarCategory>>constraintViolations = validator.validate(newCarCategory);
        
            if(constraintViolations.isEmpty())
            {
                entityManager.persist(newCarCategory);
                entityManager.flush();

                return newCarCategory;
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
    public List<CarCategory> retrieveAllCarCategory() {
        Query query = entityManager.createQuery("SELECT c FROM CarCategory c");
        
        return query.getResultList();
    }
    
    @Override
    public List<Car> retrieveCarsUnderCategory(Long carCategoryId) {
        CarCategory carCategory = null;
        try {
            carCategory = retrieveCarCategoryByCarCategoryId(carCategoryId);
        } catch (CarCategoryNotFoundException ex) {
            Logger.getLogger(CarCategorySessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        carCategory.getCarModels().size();
        List<CarModel> carModels = carCategory.getCarModels();
        carModels.size();
        List<Car> returningCars = new ArrayList<Car>();
        for (CarModel carModel : carModels) {
            carModel.getCars().size();
            for (Car car : carModel.getCars()) {
                returningCars.add(car);
            }
        }
        return returningCars;
    }
    
    @Override
    public List<RentalRate> retrieveRentalRatesOfCategory(Long carCategoryId) {
        CarCategory carCategory = null;
        try {
            carCategory = retrieveCarCategoryByCarCategoryId(carCategoryId);
        } catch (CarCategoryNotFoundException ex) {
            Logger.getLogger(CarCategorySessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println(carCategory.getCategoryName());
        carCategory.getRentalRates().size();
        List<RentalRate> rentalRates = carCategory.getRentalRates();
        rentalRates.size();
        return rentalRates;
    }

    @Override
    public CarCategory retrieveCarCategoryByCarCategoryId(Long carCategoryId) throws CarCategoryNotFoundException {
        CarCategory carCategory = entityManager.find(CarCategory.class, carCategoryId);
        
        if(carCategory != null)
        {
            return carCategory;
        }
        else
        {
            throw new CarCategoryNotFoundException("Car Category ID " + carCategoryId + " does not exist!");
        }
    }

    @Override
    public void updateCarCategory(CarCategory carCategory) throws CarCategoryNotFoundException, UpdateCarCategoryException, InputDataValidationException {
        if(carCategory != null && carCategory.getCarCategoryId() != null)
        {
            Set<ConstraintViolation<CarCategory>>constraintViolations = validator.validate(carCategory);
        
            if(constraintViolations.isEmpty())
            {
                CarCategory carCategoryToUpdate = retrieveCarCategoryByCarCategoryId(carCategory.getCarCategoryId());

                    carCategoryToUpdate.setCategoryName(carCategory.getCategoryName());
            } 
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new CarCategoryNotFoundException("Car Category Id provided is incorrect/not present");
        }
    }

    @Override
    public void deleteCarCategory(Long CarCategoryId) throws CarCategoryNotFoundException, DeleteCarCategoryException {
        CarCategory carCategoryToRemove = retrieveCarCategoryByCarCategoryId(CarCategoryId);
        
        if(carCategoryToRemove.getCarModels().isEmpty())
        {
            entityManager.remove(carCategoryToRemove);
        }
        else
        {
            throw new DeleteCarCategoryException("Car Category ID " + CarCategoryId + " is associated with existing car(s) and cannot be deleted!");
        }
    }

    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CarCategory>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
