/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmscustomerclient;

import Entity.Partner;
import Entity.Reservation;
import ejb.session.stateless.PartnerSessionBeanRemote;
import java.util.List;
import java.util.Scanner;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Goh Chun Teck
 */
public class PartnerManagementModule {
    private PartnerSessionBeanRemote partnerSessionBeanRemote;

    private Partner currentPartner;

    public PartnerManagementModule() {

    }

    public PartnerManagementModule(PartnerSessionBeanRemote partnerSessionBeanRemote) {
        this();

        this.partnerSessionBeanRemote = this.partnerSessionBeanRemote;
    }

    public void menuPartnerManagement() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
//            if (currentEmployee.getAccessRightEnum() != AccessRightEnum.CUSTOMERSERVICEEXECUTIVE) {
//                throw new InvalidAccessRightException("You dont have CUSTINER SERVICE rights to access the partner service module.");
//            }

            System.out.println("*** CaRMS :: Partner Management ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");

            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        loginPartner();
                        System.out.println("Login successful as " + currentPartner.getName() + "!\n");
                        loggedInMenu();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
        }
    }

    private void loggedInMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CaRMS :: Partner Management ***\n");
            System.out.println("You are login as " + currentPartner.getName());
            System.out.println("1: Search for cars and Make booking");
            System.out.println("2: View All My Reservation");
            //View my reservation details included in (2) and delete
            System.out.println("3: Logout\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    searchCar();
                } else if (response == 2) {
                    viewAllReservations();
                } else if (response == 3) {
                    currentPartner = null;
                    break;
                } else {
                    System.out.println("Invalid option, please try again!");
                }
            }
        }
    }

    private void loginPartner() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** CaRMS :: Partner Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            currentPartner = partnerSessionBeanRemote.login(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void registerPartner() {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        String name = "";

        System.out.println("*** CaRMS :: Partner Registration ***\n");
        System.out.print("Enter email(this will be the username as well)> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        System.out.print("Enter your full name> ");
        name = scanner.nextLine().trim();

        Partner newPartner = new Partner(name, username, password);
        try {
            partnerSessionBeanRemote.createNewPartner(newPartner);

        } catch (UnknownPersistenceException | PartnerUsernameExistException ex) {
            ex.printStackTrace();
        } catch (InputDataValidationException ex) {
            System.out.println("Invalid input, please try again!: " + ex.getMessage() + "\n");
        }
    }

    private void searchCar() {

    }

    private void viewAllReservations() {
        System.out.println("*** CaRMS System :: Partner Management :: View All Reservations ***\n");
        List<Reservation> reservations = currentPartner.getReservations();
        String response = "";
        Scanner sc = new Scanner(System.in);
        
        if (reservations.size() == 0) {
            System.out.println("You have no reservation records.");
        } else {
            System.out.printf("%10s%20s%20s%15s%15s%15s\n", "Reservation ID", "Start Date & Time", "End Date & Time", "Car", "Car Model", "Car Category");
            for (Reservation r : reservations) {
                System.out.printf("%10s%20s%20s%15s%15s%15s\n",r.getReservationId(),r.getStartDate(),r.getEndDate(),r.getCar().getCarPlateNo(), r.getCar().getCarModel(), r.getCar().getCarModel().getCarCategory());
            }
            System.out.println("To cancel a reservation record, enter the Reservation ID.");
            System.out.println("To return to the previous menu, enter 1");
            System.out.print("> ");
            response = sc.nextLine().trim();
            
            if (response.equals("0")){
                return;
            } else {
                int id = Integer.parseInt(response);
                cancelReservation(id);
            }
        }
    }
    private void cancelReservation(int reservationId){
        
    }
}
