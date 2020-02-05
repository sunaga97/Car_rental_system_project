/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.Car;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CarNotFoundException;

/**
 *
 * @author sunag
 */
@Stateless
@Local(CarSessionBeanLocal.class)
@Remote(CarSessionBeanRemote.class)

public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    
    @Override
   public Long createCar(Car car) {
       em.persist(car);
       em.flush(); 
       
       return car.getCarId();
   }
   
    @Override
   public Car retrieveCarByCarId(Long carId){
       Car car = em.find(Car.class, carId);
       
       return car;
   }
   
    @Override
    public List<Car> retrieveAllCar() {
        Query query = em.createQuery("SELECT c FROM Car c");
        
        return query.getResultList();
    }
    
    @Override
    public Car retrieveCarByCarPlateNo(String carPlateNo) throws CarNotFoundException
    {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.carPlateNo = :inCarPlateNo");
        query.setParameter("inCarPlateNo", carPlateNo);
        
        try
        {
            return (Car)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new CarNotFoundException("Car with car plate: " + carPlateNo + " does not exist!");
        }
    }

   
    @Override
   public void updateCar(Car car) {
       em.merge(car);
   }
   
    @Override
   public void deleteCar(Long carId) {
       Car car = retrieveCarByCarId(carId);
       em.remove(car);
   }

    
}
