/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.Car;
import Entity.Outlet;
import Entity.Reservation;
import java.util.Date;
import java.util.List;
import util.exception.InputDataValidationException;
import util.exception.ReservationNotFoundException;
import util.exception.ReservationUsernameExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateReservationException;

/**
 *
 * @author Goh Chun Teck
 */
public interface ReservationSessionBeanLocal {
    
    
    public Reservation createNewReservationAndUpdateCus(Reservation newReservation, Long cusId) throws InputDataValidationException, ReservationUsernameExistException, UnknownPersistenceException;

    public List<Reservation> retrieveAllReservations();

    public Reservation retrieveReservationById(Long ReservationId) throws ReservationNotFoundException;

    public void updateReservation(Reservation reservation) throws InputDataValidationException, ReservationNotFoundException, UpdateReservationException;

    public List<Reservation> retriveReservationByStartDate(Date startDate);

    public Car retrieveCarByReservationId(Long id);

    public List<Reservation> getConflictReservations(List<Car> available, Outlet pickUpOutlet, Date pickDateTime, Date returnDateTime, Long type, Long id);

    public void deleteReservation(Long reservationId) throws ReservationNotFoundException;

    public Reservation createNewReservation(Reservation newReservation) throws InputDataValidationException, ReservationUsernameExistException, UnknownPersistenceException;


}
