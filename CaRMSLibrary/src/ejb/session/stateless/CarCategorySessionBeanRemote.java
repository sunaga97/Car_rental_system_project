/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.Car;
import Entity.CarCategory;
import Entity.RentalRate;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CarCategoryNotFoundException;
import util.exception.DeleteCarCategoryException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarCategoryException;

/**
 *
 * @author Goh Chun Teck
 */

public interface CarCategorySessionBeanRemote {
    
   public CarCategory createNewCarCategory(CarCategory newCarCategory)throws InputDataValidationException,UnknownPersistenceException ;
    
    List<CarCategory> retrieveAllCarCategory();
    
    CarCategory retrieveCarCategoryByCarCategoryId(Long carCategoryId) throws CarCategoryNotFoundException;
    
    void updateCarCategory(CarCategory carCategory) throws CarCategoryNotFoundException, UpdateCarCategoryException, InputDataValidationException;
    
    void deleteCarCategory(Long CarCategoryId) throws CarCategoryNotFoundException, DeleteCarCategoryException;

    public List<Car> retrieveCarsUnderCategory(Long carCategoryId);

    public List<RentalRate> retrieveRentalRatesOfCategory(Long carCategoryId);
}