/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmscustomerclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.TransitRecordSessionBeanRemote;
import java.util.List;

/**
 *
 * @author Goh Chun Teck
 */

public class MainApp {

    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote; 
    private CarModelSessionBeanRemote carModelSessionBeanRemote; 
    private OutletSessionBeanRemote outletSessionBeanRemote; 
    private CarSessionBeanRemote carSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private TransitRecordSessionBeanRemote transitRecordSessionBeanRemote;
    public MainApp() {
    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, CarCategorySessionBeanRemote carCategorySessionBeanRemote, CarModelSessionBeanRemote carModelSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, TransitRecordSessionBeanRemote transitRecordSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.carModelSessionBeanRemote = carModelSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.transitRecordSessionBeanRemote = transitRecordSessionBeanRemote;
    }

    

    public void runApp() {

        
                    CustomerManagementModule customerManagement = new CustomerManagementModule(customerSessionBeanRemote, carCategorySessionBeanRemote, carModelSessionBeanRemote, outletSessionBeanRemote, reservationSessionBeanRemote, carSessionBeanRemote, transitRecordSessionBeanRemote);
                    customerManagement.menuCustomerManagement();
                
    }

//    private void doLogin() throws InvalidLoginCredentialException {
//        Scanner scanner = new Scanner(System.in);
//        String username = "";
//        String password = "";
//
//        System.out.println("*** CaRMS(Management) :: Login ***\n");
//        System.out.print("Enter username> ");
//        username = scanner.nextLine().trim();
//        System.out.print("Enter password> ");
//        password = scanner.nextLine().trim();
//
//        if (username.length() > 0 && password.length() > 0) {
//            currentEmployee = employeeSessionBeanRemote.login(username, password);
//        } else {
//            throw new InvalidLoginCredentialException("Missing login credential!");
//        }
//    }

//    private void menuMain() {
//        Scanner scanner = new Scanner(System.in);
//        Integer response = 0;
//
//        while (true) {
//            System.out.println("*** CaRMS(Management) ***\n");
//            System.out.println("You are login as " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName() + " with " + currentEmployee.getAccessRightEnum().toString() + " rights\n");
//            System.out.println("1: Sales Management");
//            System.out.println("2: Customer Service");
//            System.out.println("3: Logout\n");
//            response = 0;
//
//            while (response < 1 || response > 3) {
//                System.out.print("> ");
//
//                response = scanner.nextInt();
//
//                if (response == 1) {
//                    try {
//                        salesManagementModule.menuSalesManagement();
//                    } catch (InvalidAccessRightException ex) {
//                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
//                    }
//                } else if (response == 2) {
//                    try {
//                        customerServiceModule.menuCustomerService();
//                    } catch (InvalidAccessRightException ex) {
//                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
//                    }
//                } else if (response == 3) {
//                    break;
//                }
//            }
//        }
//    }
}
