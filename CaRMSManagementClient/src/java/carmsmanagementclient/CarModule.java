/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import Entity.Car;
import Entity.CarCategory;
import Entity.CarModel;
import Entity.Customer;
import Entity.Outlet;
import Entity.RentalRate;
import Entity.Reservation;
import Entity.TransitRecord;
import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import java.time.temporal.TemporalUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import static java.util.concurrent.TimeUnit.HOURS;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.CarStatusEnum;
import util.exception.CarModelIsDisabledException;
import util.exception.CarModelNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.OutletNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.UpdateReservationException;

/**
 *
 * @author sunag
 */
public class CarModule {
    private CarSessionBeanRemote carSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private CarModelSessionBeanRemote carModelSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBean;
    
    public CarModule(CarSessionBeanRemote carSessionBeanRemote, CarCategorySessionBeanRemote carCategorySessionBeanRemote, CarModelSessionBeanRemote carModelSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote) {
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.carModelSessionBeanRemote = carModelSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.reservationSessionBean = reservationSessionBeanRemote;
    }
    
    public void clientCreateNewCar() {
        Scanner scanner = new Scanner(System.in);
        List<Outlet> outlets = outletSessionBeanRemote.retrieveAllOutlets();
        System.out.println("Choose outlet from available outlet: ");
        for (Outlet outlet : outlets) {
            System.out.printf("%3s%30s\n", outlet.getOutletId(), outlet.getName());
        }
        System.out.println("Enter outlet id>");
        Long outletId = scanner.nextLong();
        scanner.nextLine();
        Outlet outlet = null;
        try {
            outlet = outletSessionBeanRemote.retrieveOutletById(outletId);
        } catch (OutletNotFoundException ex) {
            ex.printStackTrace();
        }
        System.out.println("Enter License Plate Number of Car you wish to create: ");
        String licensePlateNo = scanner.nextLine();
        System.out.println("Enter Colour: ");
        String colour = scanner.nextLine();
        System.out.println("Enter Car Model id from available carModel");
        List<CarModel> carModels = carModelSessionBeanRemote.retrieveAllCarModel();
        for (CarModel carModel : carModels) {
             System.out.printf("%3s%15s%15s%15s%15s\n", carModel.getCarModelId(), carModel.getCarCategory().toString(), carModel.getCarMakeName(), carModel.getCarModelName(), carModel.isDisabled());
        }
        System.out.print(">");
        Long carModelId = scanner.nextLong();
        scanner.nextLine();
        CarModel carModel = null;
        try {
            carModel = carModelSessionBeanRemote.retrieveCarModelByCarModelId(carModelId);
        } catch (CarModelNotFoundException ex) {
            ex.printStackTrace();
        }
        try {
            carSessionBeanRemote.createCar(new Car(licensePlateNo, carModel, colour, outlet, CarStatusEnum.AVAILABLE));
        } catch (CarModelIsDisabledException ex) {
            ex.printStackTrace();
        }
    }
    
    public void clientViewAllCar() {
        List<Car> retrievedList = carSessionBeanRemote.retrieveAllCar();
        Collections.sort(retrievedList);
        for (Car car : retrievedList) {
            System.out.printf("%3s%12s%12s%16s\n", car.getCarId(), car.getCarPlateNo(), car.getCarModel().getCarModelName(), car.getCarModel().getCarCategory().toString());
        } 
    }
    
    public void clientViewCarDetail() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter car id of car you like to view>");
        Long carId = scanner.nextLong();
        Car car = null;
        car = carSessionBeanRemote.retrieveCarByCarId(carId);
        scanner.nextLine();
        if (car.getCarStatusEnum() == CarStatusEnum.AVAILABLE) {
            System.out.printf("%10s%12s%12s%12s%15s%20s\n",car.getCarPlateNo(), car.getColour(), car.getCarModel().getCarMakeName(), car.getCarModel().getCarModelName(), car.getCarModel().getCarCategory().toString(), car.getOutlet().getName());
        } else {
            System.out.printf("%10s%12s%12s%12s%15s%20s\n",car.getCarPlateNo(), car.getColour(), car.getCarModel().getCarMakeName(), car.getCarModel().getCarModelName(), car.getCustomer().getName());
        }
        while(true) {
            System.out.println("How would you like to proceed?");
            System.out.println("1. Update Car");
            System.out.println("2. Delete Car");
            System.out.println("3. Back");
            int response = scanner.nextInt();
            scanner.nextLine();
            if (response == 1) {
                System.out.println("Enter Updated License Plate Number of Car you wish to create: ");
                String licensePlateNo = scanner.nextLine();
                System.out.println("Enter Updated Colour: ");
                String colour = scanner.nextLine();
                System.out.println("Enter updated Car Model id from available carModel");
                List<CarModel> carModels = carModelSessionBeanRemote.retrieveAllCarModel();
                for (CarModel carModel : carModels) {
                     System.out.printf("%3s%15s%15s%15s%15s\n", carModel.getCarModelId(), carModel.getCarCategory().toString(), carModel.getCarMakeName(), carModel.getCarModelName(), carModel.isDisabled());
                }
                Long carModelId = scanner.nextLong();
                scanner.nextLine();
                System.out.println("Is the updated car at outlet or customer?");
                System.out.println("1. Customer");
                System.out.println("2. Outlet");
                int response2 = scanner.nextInt();
                scanner.nextLine();
                if (response2 == 1) {
                    System.out.println("Choose the id of the customer the car is at>");
                    List<Customer> customers = customerSessionBeanRemote.retrieveAllCustomers();
                    for (Customer customer : customers) {
                        System.out.printf("%3s%25s\n", customer.getId(), customer.getName());
                    }
                    Long customerId = scanner.nextLong();
                    scanner.nextLine();
                    Car updatedCar = null;
                    try {
                        updatedCar = new Car(licensePlateNo, carModelSessionBeanRemote.retrieveCarModelByCarModelId(carModelId), colour, null, CarStatusEnum.ON_RENTAL);
                    } catch (CarModelNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (CarModelIsDisabledException ex) {
                        ex.printStackTrace();
                    }
                    updatedCar.setCarId(carId);
                    carSessionBeanRemote.updateCar(updatedCar);
                } else {
                    System.out.println("Choose the id of the outlet car is at>");
                    List<Outlet> outlets = outletSessionBeanRemote.retrieveAllOutlets();
                    for (Outlet outlet : outlets) {
                        System.out.printf("%3s%30s\n", outlet.getOutletId(), outlet.getName());
                    }
                    System.out.println("Enter outlet id>");
                    Long outletId = scanner.nextLong();
                    scanner.nextLine();
                   
                    Car updatedCar = null;
                    try {
                        updatedCar = new Car(licensePlateNo, carModelSessionBeanRemote.retrieveCarModelByCarModelId(carModelId), colour, outletSessionBeanRemote.retrieveOutletById(outletId), CarStatusEnum.AVAILABLE);
                    } catch (CarModelNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (OutletNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (CarModelIsDisabledException ex) {
                        ex.printStackTrace();
                    }
                    updatedCar.setCarId(carId);
                    carSessionBeanRemote.updateCar(updatedCar);
                    
                }
                System.out.println("The updated Car details are:");
                if (car.getCarStatusEnum() == CarStatusEnum.AVAILABLE) {
                    System.out.printf("%10s%12s%12s%12s%15s%20s\n",car.getCarPlateNo(), car.getColour(), car.getCarModel().getCarMakeName(), car.getCarModel().getCarModelName(), car.getCarModel().getCarCategory().toString(), car.getOutlet().getName());
                } else {
                    System.out.printf("%10s%12s%12s%12s%15s%20s\n",car.getCarPlateNo(), car.getColour(), car.getCarModel().getCarMakeName(), car.getCarModel().getCarModelName(), car.getCustomer().getName());
                }
            } else if (response == 2) {
                if(car.getCarStatusEnum().equals(CarStatusEnum.AVAILABLE)) {
                    carSessionBeanRemote.deleteCar(car.getCarId());
                } else {
                    car.setIsDisabled(true);
                }
                break;
            } else if (response == 3) {
                break;
            } else {
                System.out.println("Wrong input, try again! Enter any key to continue");
                scanner.nextLine();
            }
        }
    }
    
    
}
