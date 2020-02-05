/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.Car;
import Entity.CarModel;
import Entity.RentalRate;
import java.util.List;
import util.exception.CarModelNotFoundException;
import util.exception.DeleteCarModelException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarModelException;

/**
 *
 * @author Goh Chun Teck
 */

public interface CarModelSessionBeanRemote {
    
    public CarModel createNewCarModel(CarModel newCarModel)throws InputDataValidationException,UnknownPersistenceException ;
    
    List<CarModel> retrieveAllCarModel();
    
    CarModel retrieveCarModelByCarModelId(Long carModelId) throws CarModelNotFoundException;
    
    void updateCarModel(CarModel carModel) throws CarModelNotFoundException, UpdateCarModelException, InputDataValidationException;
    
    void deleteCarModel(Long carModelId) throws CarModelNotFoundException, DeleteCarModelException;

    public CarModel retrieveCarModelByCarModelName(String carModelName) throws CarModelNotFoundException;

    public List<Car> retrieveCarsUnderCarModel(Long carModelId);

    public List<RentalRate> retrieveRentalRatesOfCarModel(Long carModelId);
}