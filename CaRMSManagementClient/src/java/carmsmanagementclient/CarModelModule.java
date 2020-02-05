/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import Entity.CarCategory;
import Entity.CarModel;
import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.DeleteCarModelException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarModelException;

/**
 *
 * @author sunag
 */
public class CarModelModule {
    private CarModelSessionBeanRemote carModelSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;

    public CarModelModule(CarModelSessionBeanRemote carModelSessionBeanRemote, CarCategorySessionBeanRemote carCategorySessionBeanRemote) {
        this.carModelSessionBeanRemote = carModelSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
    }
    
    public void clientCreateNewCarModel(){
        try {
            Scanner scanner = new Scanner(System.in);
            int newCarCategory = 0;
            System.out.println("Enter New Car Make Name");
            String carMakeName = scanner.nextLine();
            System.out.println("Enter New Car Model Name");
            String carModelName = scanner.nextLine();
            while (true) {
                System.out.println("Choose category of new car model");
                System.out.println("1. Luxury Sedan");
                System.out.println("2. Family Sedan");
                System.out.println("3. Standard Sedan");
                System.out.println("4. SUV/Minivan");
                newCarCategory = scanner.nextInt();
                if (newCarCategory > 0 && newCarCategory < 5) {
                    break;
                } else {
                    scanner.nextLine();
                    System.out.println("Wrong input, try again!");
                }
            }
            scanner.nextLine();
            carModelSessionBeanRemote.createNewCarModel(new CarModel(carMakeName, carModelName, carCategorySessionBeanRemote.retrieveCarCategoryByCarCategoryId(new Long(newCarCategory))));
        } catch (CarCategoryNotFoundException ex) {
            ex.printStackTrace();
        } catch (InputDataValidationException ex) {
            ex.printStackTrace();
        } catch (UnknownPersistenceException ex) {
            ex.printStackTrace();
        }
        

    }
    
    public void clientViewAllCarModel() {
        List<CarModel> retrievedList = carModelSessionBeanRemote.retrieveAllCarModel();
        Collections.sort(retrievedList);
        for (CarModel carModel : retrievedList) {
            System.out.printf("%3s%15s%15s%15s%15s\n", carModel.getCarModelId(), carModel.getCarCategory().toString(), carModel.getCarMakeName(), carModel.getCarModelName(), carModel.isDisabled());
        }
    }
    
    public void clientUpdateCarModel() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Car Model id:");
        Long carModelId = scanner.nextLong();
        scanner.nextLine();
        CarModel carModel = null;
        try {
            carModel = carModelSessionBeanRemote.retrieveCarModelByCarModelId(carModelId);
        } catch (CarModelNotFoundException ex) {
            ex.printStackTrace();
        }
        System.err.println("The car model you wish to update is: ");
        System.out.printf("%3s%15s%15s%15s%15s\n", carModel.getCarModelId(), carModel.getCarCategory().toString(), carModel.getCarMakeName(), carModel.getCarModelName(), "Disabled: " + carModel.isDisabled());
        System.out.println("Enter Updated Car Make Name>");
        String updatedCarMakeName = scanner.nextLine();
        System.out.println("Enter Updated Car Model Name>");
        String updatedCarModelName = scanner.nextLine();
        Long updatedCarCategory;
        while(true) {
            System.out.println("Choose number of updated category of new car model>");
            System.out.println("1. Luxury Sedan");
            System.out.println("2. Family Sedan");
            System.out.println("3. Standard Sedan");
            System.out.println("4. SUV/Minivan");
            updatedCarCategory = scanner.nextLong();
            scanner.nextLine();
            if(updatedCarCategory > 0 && updatedCarCategory < 5) {
                break;
            } else {
                System.out.println("Wrong Input! Try Again!");
                System.out.println("Enter Any Key to Continue>");
                scanner.nextLine();
            }
        }
        CarModel updatedCarModel = null;
        try {
            updatedCarModel = new CarModel(updatedCarMakeName, updatedCarModelName, carCategorySessionBeanRemote.retrieveCarCategoryByCarCategoryId(updatedCarCategory));
        } catch (CarCategoryNotFoundException ex) {
            ex.printStackTrace();
        }
        updatedCarModel.setCarModelId(carModelId);
       
        try {
            carModelSessionBeanRemote.updateCarModel(updatedCarModel);
        } catch (CarModelNotFoundException ex) {
            ex.printStackTrace();
        } catch (UpdateCarModelException ex) {
            ex.printStackTrace();
        } catch (InputDataValidationException ex) {
            ex.printStackTrace();
        }
        System.out.println("Updated car model info: ");
        System.out.printf("%3s%15s%15s%15s%24s\n", updatedCarModel.getCarModelId(), updatedCarModel.getCarCategory().toString(), updatedCarModel.getCarMakeName(), updatedCarModel.getCarModelName(), updatedCarModel.isDisabled());
    }
    
    public void clientDeleteCarModel() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter Car Model Id of Car Model to Delete: ");
            Long carModelId = scanner.nextLong();
            scanner.nextLine();

            try {
                carModelSessionBeanRemote.deleteCarModel(carModelId);
                break;
            } catch (CarModelNotFoundException ex) {
                ex.printStackTrace();
                System.out.println("No such car Id, try again!");
            } catch (DeleteCarModelException ex) {
                ex.printStackTrace();
            }

        }
    }
}
