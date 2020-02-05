/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import Entity.Car;
import Entity.Customer;
import Entity.Employee;
import Entity.Reservation;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.AccessRightEnum;
import util.enumeration.CarStatusEnum;
import util.exception.CarNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.ReservationNotFoundException;
import util.exception.UpdateReservationException;

/**
 *
 * @author sunag
 */
public class CustomerServiceModule {
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private Employee currentEmployee;
    
    public CustomerServiceModule() {
        
    }

    public CustomerServiceModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, CarModelSessionBeanRemote carModelSessionBeanRemote, Employee currentEmployee) {
        this();
        
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }

    
    public void menuCustomerService() throws InvalidAccessRightException{
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        while(true)
        {
            if (currentEmployee.getAccessRightEnum() != AccessRightEnum.CUSTOMERSERVICEEXECUTIVE) {
                throw new InvalidAccessRightException("You dont have CUSTINER SERVICE rights to access the customer service module.");
            }
            
            System.out.println("*** CaRMS(Management) :: Customer Service ***\n");
            System.out.println("1: Pick Up Car");
            System.out.println("2: Return Car");
            System.out.println("3: Back\n");
            response = 0;
            
            while (response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();
                scanner.nextLine();
                if (response == 1) {
                    Customer customer = null;
                    while (true) {
                        System.out.println("Enter Customer Email:");
                        System.out.print(">");
                        String customerEmail = scanner.nextLine();
                        try {
                            customer = customerSessionBeanRemote.retrieveCustomerByUsername(customerEmail);
                            break;
                        } catch (CustomerNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                    System.out.println(customer.getName());
                    System.out.println("The following are customer's reservations");
                    customer.getReservations().size();
                    for (Reservation reservation : customer.getReservations()) {
                        System.out.printf("%3s%20s%20s%20s\n", reservation.getReservationId(), reservation.getCar().getCarModel().getCarModelName(), reservation.getPickUpOutlet().getName(), sdf.format(reservation.getStartDate()));
                    }
                    System.out.println("Enter reservation Id>");
                    Long reservationId = scanner.nextLong();
                    Reservation reservation = null;
                    try {
                        reservation = reservationSessionBeanRemote.retrieveReservationById(reservationId);

                    } catch (ReservationNotFoundException ex) {
                        Logger.getLogger(CustomerServiceModule.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(!reservation.isPaid()) {
                        System.out.println("Please pay" + reservation.getTotalCost());
                        reservation.setPaid(true);
                    }
                    reservation.getCar().setCarStatusEnum(CarStatusEnum.ON_RENTAL);
                    reservation.getCar().setCustomer(customer);
                    reservation.getCar().setOutlet(null);
                    try {
                        reservationSessionBeanRemote.updateReservation(reservation);
                        //get the reservation
//                    if (!reservation.getPaid()) {
//                        System.out.println("Customer has to make payment before proceeding!");
//                        System.out.println("Has Customer paid?(Y/N)");
//                        if (scanner.nextLine() == "N") {
//                            System.out.println("Customer cant proceed");
//                            break;
//                        } else {
//                            reservation.setPaid(true);
//                        }
//                    }
//                    set car status to INRENTAL;
//                    set car location to null;
//                    set car customer to customer;
                    } catch (InputDataValidationException ex) {
                        Logger.getLogger(CustomerServiceModule.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ReservationNotFoundException ex) {
                        Logger.getLogger(CustomerServiceModule.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UpdateReservationException ex) {
                        Logger.getLogger(CustomerServiceModule.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                } else if (response == 2) {
                  Customer customer = null;
                    while (true) {
                        System.out.println("Enter Customer Email:");
                        System.out.print(">");
                        String customerEmail = scanner.nextLine();
                        try {
                            customer = customerSessionBeanRemote.retrieveCustomerByUsername(customerEmail);
                            break;
                        } catch (CustomerNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                    System.out.println("Enter reservation ID:");
                    Long reservationId = scanner.nextLong();
                    
                    Reservation reservation = null;
                    try {
                        reservation = reservationSessionBeanRemote.retrieveReservationById(reservationId);
                    } catch (ReservationNotFoundException ex) {
                        Logger.getLogger(CustomerServiceModule.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Car car = reservation.getCar();
                    car.setCarStatusEnum(CarStatusEnum.AVAILABLE);
                    car.setCustomer(null);
                    car.setOutlet(currentEmployee.getOutlet());
                    carSessionBeanRemote.updateCar(car);
                    try {
                        reservationSessionBeanRemote.deleteReservation(reservationId);
                    } catch (ReservationNotFoundException ex) {
                        Logger.getLogger(CustomerServiceModule.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("returned");
                    
                    
                }
                if (response == 3) {
                    break;
                }
                
            }
            
            if (response == 3) {
                break;
            }
        }
    }
    
    
}
