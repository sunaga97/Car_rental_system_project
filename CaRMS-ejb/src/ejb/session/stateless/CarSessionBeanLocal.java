/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.Car;


public interface CarSessionBeanLocal {

    public Long createCar(Car car);

    public Car retrieveCarByCarId(Long carId);

    public void updateCar(Car car);

    public void deleteCar(Long carId);
    
}
