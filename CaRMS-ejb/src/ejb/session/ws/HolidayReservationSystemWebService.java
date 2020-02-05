/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import Entity.Car;
import Entity.CarCategory;
import Entity.CarModel;
import Entity.Outlet;
import Entity.Partner;
import Entity.RentalRate;
import Entity.Reservation;
import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.CarModelSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.DeleteReservationException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OutletNotFoundException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.ReservationUsernameExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdatePartnerException;
import util.exception.UpdateReservationException;

/**
 *
 * @author sunag
 */
@WebService(serviceName = "HolidayReservationSystemWebService")
@Stateless()
public class HolidayReservationSystemWebService {

    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBean;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @EJB
    private CarModelSessionBeanLocal carModelSessionBean;

    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBean;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;
    
    @WebMethod(operationName = "createReservationObject") 
    public void createReservationObject(@WebParam(name = "startDate")Date startDate, @WebParam(name = "endDate")Date endDate, @WebParam(name = "partner")Partner partner, @WebParam(name = "car")Car car, @WebParam(name = "pickUpOutlet")Outlet pickUpOutlet, @WebParam(name = "dropOffOutlet")Outlet dropOffOutlet, @WebParam(name = "carCategory")CarCategory carCategory, @WebParam(name = "carModel")CarModel carModel, @WebParam(name = "totalCost")double totalCost) {
        Reservation newRes = new Reservation(startDate, endDate, partner, car, pickUpOutlet, dropOffOutlet, carCategory, carModel, totalCost);
        
        try {
            partner = partnerSessionBean.retrievePartnerById(partner.getId());
        } catch (PartnerNotFoundException ex) {
            ex.printStackTrace();
        }
        partner.getReservations().add(newRes);
        try {
            reservationSessionBean.createNewReservation(newRes);
        } catch (InputDataValidationException ex) {
            ex.printStackTrace();
        } catch (ReservationUsernameExistException ex) {
            System.out.println("Reservation already exists!");
        } catch (UnknownPersistenceException ex) {
            ex.printStackTrace();
        }
    }
    
    @WebMethod(operationName = "createReservationObjectForModelCase") 
    public void createReservationObjectForModelCase(@WebParam(name = "startDate")Date startDate, @WebParam(name = "endDate")Date endDate, @WebParam(name = "partner")Partner partner, @WebParam(name = "car")Car car, @WebParam(name = "pickUpOutlet")Outlet pickUpOutlet, @WebParam(name = "dropOffOutlet")Outlet dropOffOutlet, @WebParam(name = "carModel")CarModel carModel, @WebParam(name = "totalCost")double totalCost) {
        Reservation newRes = null;
        try {
            partner = partnerSessionBean.retrievePartnerById(partner.getId());
        } catch (PartnerNotFoundException ex) {
            ex.printStackTrace();
        }
        try {
            newRes = new Reservation(startDate, endDate, partner, car, pickUpOutlet, dropOffOutlet, carModelSessionBean.retrieveCarModelByCarModelId(carModel.getCarModelId()).getCarCategory(), carModel, totalCost);
        } catch (CarModelNotFoundException ex) {
            ex.printStackTrace();
        }
        partner.getReservations().add(newRes);
        try {
            reservationSessionBean.createNewReservation(newRes);
        } catch (InputDataValidationException ex) {
            ex.printStackTrace();
        } catch (ReservationUsernameExistException ex) {
            System.out.println("Reservation already exists!");
        } catch (UnknownPersistenceException ex) {
            ex.printStackTrace();
        }
    }
    
    @WebMethod(operationName = "login") 
    public Partner login(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws InvalidLoginCredentialException {
        return partnerSessionBean.login(username, password);
        
    }
    
    @WebMethod(operationName = "retrieveAllCarCategory") 
    public List<CarCategory> retrieveAllCarCategory() {
        return carCategorySessionBean.retrieveAllCarCategory();   
    }
    
    @WebMethod(operationName = "retrieveAllCarModel") 
    public List<CarModel> retrieveAllCarModel() {
        return carModelSessionBean.retrieveAllCarModel(); 
    }
    
    @WebMethod(operationName = "retrieveAllOutlet") 
    public List<Outlet> retrieveAllOutlet() {
        return outletSessionBean.retrieveAllOutlets();
    }
    @WebMethod(operationName = "retrieveOutletByOutletId") 
    public Outlet retrieveOutletByOutletId(@WebParam(name = "outletId") Long outletId) throws OutletNotFoundException {
        return outletSessionBean.retrieveOutletById(outletId);
    }
    
    @WebMethod(operationName = "retrieveCarModelByCarModelId") 
    public CarModel retrieveCarModelByCarModelId(@WebParam(name = "carModelId") Long carModelId) throws CarModelNotFoundException {
        return carModelSessionBean.retrieveCarModelByCarModelId(carModelId);
    }
    
    @WebMethod(operationName = "retrieveCarsUnderCarModel") 
    public List<Car> retrieveCarsUnderCarModel(@WebParam(name = "carModelId") Long carModelId){
        return carModelSessionBean.retrieveCarsUnderCarModel(carModelId);
    }
    
    @WebMethod(operationName = "retrieveRentalRatesOfCarModel") 
    public List<RentalRate> retrieveRentalRatesOfCarModel(@WebParam(name = "carModelId") Long carModelId){
        return carModelSessionBean.retrieveRentalRatesOfCarModel(carModelId);
    }
    
    @WebMethod(operationName = "createNewReservation") 
    public void createNewReservation(@WebParam(name = "newReservation") Reservation newReservation) throws InputDataValidationException, ReservationUsernameExistException, UnknownPersistenceException{
        reservationSessionBean.createNewReservation(newReservation);
    }
    
    @WebMethod(operationName = "updatePartner") 
    public void updatePartner(@WebParam(name = "newPartner") Partner newPartner) throws InputDataValidationException, PartnerNotFoundException, UpdatePartnerException{
        partnerSessionBean.updatePartner(newPartner);
    }
    
    @WebMethod(operationName = "retrieveCarCategoryByCarCategoryId") 
    public CarCategory retrieveCarCategoryByCarCategoryId(@WebParam(name = "carCategoryId") Long carCategoryId) throws CarCategoryNotFoundException {
        return carCategorySessionBean.retrieveCarCategoryByCarCategoryId(carCategoryId);
    }
    
    @WebMethod(operationName = "retrieveCarsUnderCarCategory") 
    public List<Car> retrieveCarsUnderCarCategory(@WebParam(name = "carCategoryId") Long carCategoryId){
        return carCategorySessionBean.retrieveCarsUnderCategory(carCategoryId);
    }
    
    @WebMethod(operationName = "retrieveRentalRatesOfCarCategory") 
    public List<RentalRate> retrieveRentalRatesOfCarCategory(@WebParam(name = "carCategoryId") Long carCategoryId){
        return carCategorySessionBean.retrieveRentalRatesOfCategory(carCategoryId);
    }
    
    @WebMethod(operationName = "ratesAvailable") 
    public List<RentalRate> ratesAvailable(@WebParam(name = "allRates")List<RentalRate> allRates, @WebParam(name = "pickDateTime")Date pickDateTime, @WebParam(name = "returnDateTime")Date returnDateTime){
        return rentalRateSessionBean.ratesAvailable(allRates, pickDateTime, returnDateTime);
    }
    
    @WebMethod(operationName = "retrieveAllReservations") 
    public List<Reservation> retrieveAllReservations() {
        return reservationSessionBean.retrieveAllReservations();
    }
    
    @WebMethod(operationName = "getConflictReservations") 
    public List<Reservation> getConflictReservations(@WebParam(name = "available")List<Car> available,@WebParam(name = "pickUpOutlet") Outlet pickUpOutlet,@WebParam(name = "pickDateTime") Date pickDateTime,@WebParam(name = "returnDateTime") Date returnDateTime,@WebParam(name = "type") Long type,@WebParam(name = "id") Long id) {
        return reservationSessionBean.getConflictReservations(available, pickUpOutlet, pickDateTime, returnDateTime, type, id);
    }
    
    @WebMethod(operationName = "getCarCategoryFromCarModel") 
    public CarCategory getCarCategoryFromCarModel(@WebParam(name = "carModel")CarModel carModel) {
        return carModel.getCarCategory();
    }
    
    @WebMethod(operationName = "retrievePartnerByPartnerId") 
    public Partner retrievePartnerByPartnerId(@WebParam(name = "partnerId")Long partnerId) throws PartnerNotFoundException {
        return partnerSessionBean.retrievePartnerById(partnerId);
    }
    
    @WebMethod(operationName = "retrieveReservationsFromPartner") 
    public List<Reservation> retrieveReservationsFromPartner(@WebParam(name = "partner")Partner partner) {
        return partner.getReservations();
    }
    
    @WebMethod(operationName = "retrieveReservationById") 
    public Reservation retrieveReservationById(@WebParam(name = "reservationId")Long reservationId) throws ReservationNotFoundException {
        return reservationSessionBean.retrieveReservationById(reservationId);
    }
    
    @WebMethod(operationName = "updateReservation") 
    public void updateReservation(@WebParam(name = "reservation")Reservation reservation) throws ReservationNotFoundException, InputDataValidationException, UpdateReservationException {
        reservationSessionBean.updateReservation(reservation);
    }
    
    @WebMethod(operationName = "removeReservationFromPartnerAndUpdate") 
    public void removeReservationFromPartnerAndUpdate(@WebParam(name = "partner")Partner partner, @WebParam(name = "reservation")Reservation reservation){
        try {
            partner = partnerSessionBean.retrievePartnerById(partner.getId());
        } catch (PartnerNotFoundException ex) {
            ex.printStackTrace();
        }
        partner.getReservations().remove(reservation);
                
        try {
            partnerSessionBean.updatePartner(partner);
        } catch (InputDataValidationException ex) {
            ex.printStackTrace();
        } catch (PartnerNotFoundException ex) {
            ex.printStackTrace();
        } catch (UpdatePartnerException ex) {
            ex.printStackTrace();
        }
    }
    
    @WebMethod(operationName = "deleteReservation") 
    public void deleteReservation(@WebParam(name = "reservationId")Long reservationId) throws ReservationNotFoundException, DeleteReservationException{
        reservationSessionBean.deleteReservation(reservationId);
    }
    
    @WebMethod(operationName = "retrieveReservationsFromPartnerSessionBean") 
    public List<Reservation> retrieveReservationsFromPartnerSessionBean(@WebParam(name = "partnerId")Long partnerId) {
        return partnerSessionBean.retrieveReservationsFromPartner(partnerId);
    }
}
