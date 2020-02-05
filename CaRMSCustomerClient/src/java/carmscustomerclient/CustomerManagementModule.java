/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmscustomerclient;

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
import ejb.session.stateless.TransitRecordSessionBeanRemote;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.HOURS;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.CarStatusEnum;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.DeleteReservationException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OutletNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.ReservationUsernameExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCustomerException;
import util.exception.UpdateReservationException;

/**
 *
 * @author Goh Chun Teck
 */
public class CustomerManagementModule {

    private CustomerSessionBeanRemote customerSessionBeanRemote;

    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;

    private CarModelSessionBeanRemote carModelSessionBeanRemote;

    private OutletSessionBeanRemote outletSessionBeanRemote;

    private ReservationSessionBeanRemote reservationSessionBeanRemote;

    private CarSessionBeanRemote carSessionBeanRemote;

    private Customer currentCustomer;

    private TransitRecordSessionBeanRemote transitRecordSessionBeanRemote;

    public CustomerManagementModule() {

    }

    public CustomerManagementModule(CustomerSessionBeanRemote customerSessionBeanRemote, CarCategorySessionBeanRemote carCategorySessionBeanRemote, CarModelSessionBeanRemote carModelSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, TransitRecordSessionBeanRemote transitRecordSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.carModelSessionBeanRemote = carModelSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.transitRecordSessionBeanRemote = transitRecordSessionBeanRemote;
    }

    public void menuCustomerManagement() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            if (response == 4) {
                break;
            }
//            if (currentEmployee.getAccessRightEnum() != AccessRightEnum.CUSTOMERSERVICEEXECUTIVE) {
//                throw new InvalidAccessRightException("You dont have CUSTINER SERVICE rights to access the customer service module.");
//            }
            System.out.println();
            System.out.println("Welcome to CaRMS!");
            System.out.println("*** CaRMS :: Customer Management ***\n");
            System.out.println("You are login as a GUEST. To make bookings, please login to your Customer Account.");
            System.out.println("1: Register as Customer");
            System.out.println("2: Login");
            System.out.println("3: Search for Cars");
            System.out.println("4: Exit\n");

            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    registerCustomer();
                } else if (response == 2) {
                    try {
                        loginCustomer();
                        System.out.println("Login successful as " + currentCustomer.getName() + "!\n");
                        loggedInMenu();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 3) {
                    searchCar(0);
                } else if (response == 4) {
                    break;
                } else if (response == 5) {
                    scanner.nextLine();
                    System.out.println("Enter date");
                    String inpue = scanner.nextLine();
                    Date date = parseDate(inpue);
                    allocateCarOfDay(date);
//                    System.out.println(reservationSessionBeanRemote.retriveReservationByStartDate(date).size());

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
            if (currentCustomer == null) {
                break;
            }
            System.out.println();
            System.out.println("*** CaRMS :: Customer Management ***\n");
            System.out.println("You are login as " + currentCustomer.getName());
            System.out.println("1: Search for cars and Make booking");
            System.out.println("2: View All My Reservation");
            //View my reservation details included in (2) and delete
            System.out.println("3: Logout\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    searchCar(1);
                } else if (response == 2) {
                    viewAllReservations();
                } else if (response == 3) {
                    currentCustomer = null;
                    break;
                } else {
                    System.out.println("Invalid option, please try again!");
                }
            }
        }
    }

    private void loginCustomer() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println();
        System.out.println("*** CaRMS :: Customer Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            currentCustomer = customerSessionBeanRemote.login(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void registerCustomer() {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        String name = "";

        System.out.println();
        System.out.println("*** CaRMS :: Customer Registration ***\n");
        System.out.print("Enter email(this will be the username as well)> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        System.out.print("Enter your full name> ");
        name = scanner.nextLine().trim();

        Customer newCustomer = new Customer(name, username, password);
        try {
            customerSessionBeanRemote.createNewCustomer(newCustomer);

        } catch (UnknownPersistenceException | CustomerUsernameExistException ex) {
            ex.printStackTrace();
        } catch (InputDataValidationException ex) {
            System.out.println("Invalid input, please try again!: " + ex.getMessage() + "\n");
        }
    }

    private void searchCar(int searchAndBook) {
        Scanner scanner = new Scanner(System.in);
        List<CarCategory> categories = carCategorySessionBeanRemote.retrieveAllCarCategory();
        List<CarModel> makemodels = carModelSessionBeanRemote.retrieveAllCarModel();
        Long response;

        System.out.println();
        System.out.println("*** CaRMS :: Search Car ***\n");

        System.out.print("Enter your pick up date & time(in 24hr clock)[DD/MM/YYYY HHMM]> ");
        String date = scanner.nextLine();
        Date pickDateTime = parseDate(date);
        System.out.println(pickDateTime);
        System.out.println();

        System.out.print("Enter your return up date & time(in 24hr clock)[DD/MM/YYYY HHMM]> ");
        date = scanner.nextLine();
        Date returnDateTime = parseDate(date);
        System.out.println(returnDateTime);
        System.out.println();

        List<Outlet> outlets = outletSessionBeanRemote.retrieveAllOutlets();
        for (Outlet outlet : outlets) {
            System.out.println("Outlet Id:  " + outlet.getOutletId() + "   " + outlet.getName());
        }
        System.out.println();
        System.out.print("Select your pick-up outlet by the id number> ");
        response = scanner.nextLong();
        Outlet pickUpOutlet = null;
        try {
            pickUpOutlet = outletSessionBeanRemote.retrieveOutletById(response);
        } catch (OutletNotFoundException ex) {
            ex.printStackTrace();
        }
        System.out.print("Select your return outlet by the id number> ");
        response = scanner.nextLong();
        Outlet returnOutlet = null;
        try {
            returnOutlet = outletSessionBeanRemote.retrieveOutletById(response);
        } catch (OutletNotFoundException ex) {
            ex.printStackTrace();
        }
        System.out.println();

        while (true) {
            System.out.println("Search by : ");
            System.out.println("1. Make & Model");
            System.out.println("2. Category");
            System.out.println();
            response = scanner.nextLong();

            if (response == 1 || response == 2) {
                break;
            } else {
                System.out.println("Invalid Option, please try again!");
            }
        }
        System.out.println();

        List<Car> available = new ArrayList<>();
        if (response == 1) {
            makemodels.sort(new Comparator<CarModel>() {
                @Override
                public int compare(CarModel o1, CarModel o2) {
                    return o1.getCarModelId().compareTo(o2.getCarModelId());
                }
            });
            for (CarModel carModel : makemodels) {
                System.out.println("Make and Model Id: " + carModel.getCarModelId() + "  " + carModel.getCarMakeName() + "  " + carModel.getCarModelName());
            }
            System.out.println();
            System.out.print("Select your make and model by the id number> ");
            Long selection = scanner.nextLong();
            CarModel carModel = null;
            try {
                carModel = carModelSessionBeanRemote.retrieveCarModelByCarModelId(selection);
            } catch (CarModelNotFoundException ex) {
                ex.printStackTrace();
            }
            available = carModelSessionBeanRemote.retrieveCarsUnderCarModel(selection);

            List<RentalRate> rates = carModelSessionBeanRemote.retrieveRentalRatesOfCarModel(selection);
            List<Reservation> conflict = getConflictReservations(available, pickUpOutlet, pickDateTime, returnDateTime, response, selection);
            List<RentalRate> ratesAvail = ratesAvailable(rates, pickDateTime, returnDateTime);
            int availableNum = available.size() - conflict.size();

            double totalRate = 0;
            if (ratesAvail.size() <= 0 || availableNum <= 0) {
                System.out.println("The selected car make and model is not available.");
            } else {
                System.out.println("The " + carModel.getCarMakeName() + " " + carModel.getCarModelName() + " is available. " + availableNum + " cars are available at :");
                System.out.println();
                for (int i = 1; i <= ratesAvail.size(); i++) {
                    totalRate += ratesAvail.get(i - 1).getDailyRate();
                    System.out.println("Day " + i + " : $" + ratesAvail.get(i - 1).getDailyRate());
                }
                System.out.println("Total rate is:" + totalRate);
            }
            if (searchAndBook == 1) {
                System.out.print("Do you want to confirm booking for this item? [Y/N]> ");
                scanner.nextLine();
                String line = scanner.nextLine().trim();
                if (line.equals("Y")) {
                    Reservation newReservation = new Reservation(pickDateTime, returnDateTime, currentCustomer, null, pickUpOutlet, returnOutlet, carModel.getCarCategory(), carModel, totalRate);
//                try {
//                        currentCustomer = customerSessionBeanRemote.retrieveCustomerById(currentCustomer.getId());
//                        currentCustomer.getReservations().add(newReservation);
//                    } catch (CustomerNotFoundException ex) {
//                        ex.printStackTrace();
//                    }
                    try {
                        reservationSessionBeanRemote.createNewReservationAndUpdateCus(newReservation, currentCustomer.getId());
                    } catch (InputDataValidationException ex) {
                        ex.printStackTrace();
                    } catch (ReservationUsernameExistException ex) {
                        ex.printStackTrace();
                    } catch (UnknownPersistenceException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            System.out.println("Press enter to go back to the previous menu.");
            try {
                System.in.read();
            } catch (Exception e) {
            }
            return;
        } else if (response == 2) {
            categories.sort(new Comparator<CarCategory>() {
                @Override
                public int compare(CarCategory o1, CarCategory o2) {
                    return o1.getCarCategoryId().compareTo(o2.getCarCategoryId());
                }
            });
            for (CarCategory carCategory : categories) {
                System.out.println("Category Id:  " + carCategory.getCarCategoryId() + "  " + carCategory.getCategoryName());
            }
            System.out.println();
            System.out.print("Select your category by the id number> ");
            Long selection = scanner.nextLong();
            CarCategory carCategory = null;
            try {
                carCategory = carCategorySessionBeanRemote.retrieveCarCategoryByCarCategoryId(selection);
            } catch (CarCategoryNotFoundException ex) {
                Logger.getLogger(CustomerManagementModule.class.getName()).log(Level.SEVERE, null, ex);
            }
            available = carCategorySessionBeanRemote.retrieveCarsUnderCategory(selection);

            List<RentalRate> rates = carCategorySessionBeanRemote.retrieveRentalRatesOfCategory(selection);
            List<Reservation> conflict = getConflictReservations(available, pickUpOutlet, pickDateTime, returnDateTime, response, selection);
            // consider no rates
            List<RentalRate> ratesAvail = ratesAvailable(rates, pickDateTime, returnDateTime);
            int availableNum = available.size() - conflict.size();

            double totalRate = 0;
            if (ratesAvail.size() <= 0 || availableNum <= 0) {
                System.out.println("The selected car category is not available.");
            } else {
                System.out.println("The " + carCategory.getCategoryName() + " is available. " + availableNum + " cars are available at :");
                System.out.println();
                for (int i = 1; i <= ratesAvail.size(); i++) {
                    totalRate += ratesAvail.get(i - 1).getDailyRate();
                    System.out.println("Day " + i + " : $" + ratesAvail.get(i - 1).getDailyRate());
                }
                System.out.println("Total rate is:" + totalRate);
            }
            System.out.print("Do you want to confirm booking for this item? [Y/N]> ");
            scanner.nextLine();
            String line = scanner.nextLine().trim();
            if (line.equals("Y")) {
                Reservation newReservation = new Reservation(returnDateTime, pickDateTime, currentCustomer, null, pickUpOutlet, returnOutlet, carCategory, null, totalRate);
                try {
                    reservationSessionBeanRemote.createNewReservationAndUpdateCus(newReservation, currentCustomer.getId());
                } catch (InputDataValidationException ex) {
                    ex.printStackTrace();
                } catch (ReservationUsernameExistException ex) {
                    ex.printStackTrace();
                } catch (UnknownPersistenceException ex) {
                    ex.printStackTrace();
                }
            }

            System.out.println("Press enter to go back to the previous menu.");
            try {
                System.in.read();
            } catch (Exception e) {
            }
            return;
        }
    }

    public List<RentalRate> ratesAvailable(List<RentalRate> allRates, Date pickDateTime, Date returnDateTime) {
        List<RentalRate> ratesApplicable = new ArrayList<>();
        List<LocalDateTime> rentalPeriod = new ArrayList<>();
        LocalDateTime start = pickDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = returnDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        for (LocalDateTime date = start; date.isBefore(end); date = date.plusDays(1)) { // to change the plus one day
            rentalPeriod.add(date);
        }

        for (LocalDateTime d : rentalPeriod) {
            double min = Double.MAX_VALUE;

            RentalRate cheapest = allRates.get(0);
            for (RentalRate r : allRates) {
                if (r.getValidityStartDate() == null || r.getValidityEndDate() == null) {
                    if (r.getDailyRate() < min) {
                        cheapest = r;
                        min = r.getDailyRate();
                    }
                } else {
                    LocalDateTime rentStart = r.getValidityStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime rentEnd = r.getValidityEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    if (d.compareTo(rentStart) >= 0 && d.compareTo(rentEnd) <= 0) {
                        if (r.getDailyRate() < min) {
                            cheapest = r;
                            min = r.getDailyRate();
                        }
                    }
                }
            }
            ratesApplicable.add(cheapest);
        }
        return ratesApplicable;
    }

    public List<Reservation> getConflictReservations(List<Car> available, Outlet pickUpOutlet, Date pickDateTime, Date returnDateTime, Long type, Long id) {
        List<Reservation> reservations = reservationSessionBeanRemote.retrieveAllReservations();
        if (type == 1) {
            Iterator<Reservation> iter = reservations.iterator();
            while (iter.hasNext()) {
                Reservation reservation = iter.next();
                try {
                    if (reservation.getCarModel() == null) {
                        iter.remove();
                    } else if (!reservation.getCarModel().equals(carModelSessionBeanRemote.retrieveCarModelByCarModelId(id))) {
                        iter.remove();
                    }
                } catch (CarModelNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            Iterator<Reservation> iter = reservations.iterator();
            while (iter.hasNext()) {
                Reservation reservation = iter.next();
                try {
                    if (reservation.getCarCategory() == null) {
                        iter.remove();
                    } else if (!reservation.getCarCategory().equals(carCategorySessionBeanRemote.retrieveCarCategoryByCarCategoryId(id))) {
                        iter.remove();
                    }
                } catch (CarCategoryNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }
        List<Reservation> conflicts = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getDropOffOutlet().equals(pickUpOutlet)) {
                if (r.getEndDate().after(pickDateTime) || r.getStartDate().before(returnDateTime)) {
                    conflicts.add(r);
                }
            } else {
                Date tempResEndDate = (Date) r.getEndDate().clone();
                //change to add 2 hrs
                tempResEndDate.setHours(r.getEndDate().getHours() + 2);
                if (tempResEndDate.after(pickDateTime) || r.getStartDate().before(returnDateTime)) {
                    conflicts.add(r);
                }
            }
        }
        return conflicts;
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy HHmm").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    private void viewAllReservations() {
        System.out.println();
        System.out.println("*** CaRMS System :: Customer Management :: View All Reservations ***\n");
        Scanner sc = new Scanner(System.in);

        try {
            currentCustomer = customerSessionBeanRemote.retrieveCustomerById(currentCustomer.getId());
        } catch (CustomerNotFoundException ex) {
            ex.printStackTrace();
        }

        List<Reservation> reservations = customerSessionBeanRemote.getReservationsByCustomerId(currentCustomer.getId());
        if (reservations.isEmpty()) {
            System.out.println("You have no reservation records. Returning to the previous menu.\n");
        } else {
            System.out.printf("%10s%50s%50s%30s%30s%30s%30s%30s\n", "Reservation ID", "Start Date & Time", "End Date & Time", "PickUp Outlet", "Return Outlet", "Car Model", "Car Category", "Total Cost");
            for (Reservation r : reservations) {
                if (r.getCarModel() == null) {

                    System.out.printf("%10s%50s%50s%30s%30s%30s%30s%30s\n", r.getReservationId(), r.getStartDate(), r.getEndDate(), r.getPickUpOutlet().getName(), r.getDropOffOutlet().getName(), "NIL", r.getCarCategory().getCategoryName(), "$" + r.getTotalCost());
                } else {
                    System.out.printf("%10s%50s%50s%30s%30s%30s%30s%30s\n", r.getReservationId(), r.getStartDate(), r.getEndDate(), r.getPickUpOutlet().getName(), r.getDropOffOutlet().getName(), r.getCarModel().getCarMakeName() + " " + r.getCarModel().getCarModelName(), r.getCarCategory().getCategoryName(), "$" + r.getTotalCost());
                }
            }
            System.out.println();
            System.out.println("To cancel a reservation record, enter the Reservation ID.");
            System.out.println("To return to the previous menu, enter 0");
            System.out.print("> ");
            int r = sc.nextInt();

            Long max = reservations.stream().mapToLong(x -> x.getReservationId()).max().getAsLong();
            if (r == 0) {
                return;
            } else if (r <= max) {
                cancelReservation((long) r);
            } else {
                System.out.println("Invalid option, try again!");
                System.out.println("Press enter to go back to the previous menu.");
                try {
                    System.in.read();
                } catch (Exception e) {
                }
                return;
            }
        }
    }

    private void cancelReservation(Long reservationId) {
        try {
            currentCustomer.getReservations().remove(reservationSessionBeanRemote.retrieveReservationById(reservationId));
        } catch (ReservationNotFoundException ex) {
            Logger.getLogger(CustomerManagementModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            customerSessionBeanRemote.updateCustomer(currentCustomer);
        } catch (InputDataValidationException ex) {
            Logger.getLogger(CustomerManagementModule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomerNotFoundException ex) {
            Logger.getLogger(CustomerManagementModule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UpdateCustomerException ex) {
            Logger.getLogger(CustomerManagementModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            reservationSessionBeanRemote.deleteReservation(reservationId);
        } catch (ReservationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void allocateCarOfDay(Date date) {
        List<Reservation> reservations = reservationSessionBeanRemote.retriveReservationByStartDate(date);
        reservations.sort(new Comparator<Reservation>() {
            @Override
            public int compare(Reservation r1, Reservation r2) {
                if (r1.getCarModel() != null && r2.getCarModel() == null) {
                    return -1;
                }
                return 1;
            }
        });
        for (Reservation reservation : reservations) {

            if (reservation.getCarModel() != null) {
                CarModel carModel = reservation.getCarModel();
                carModel.getCars().size();
                for (Car car : carModel.getCars()) {
                    if (car.getCarStatusEnum() == CarStatusEnum.ON_RENTAL && car.getReservation().getDropOffOutlet().equals(reservation.getPickUpOutlet()) && car.getReservation().getEndDate().before(reservation.getStartDate())) {
                        reservation.setCar(car);
                        try {
                            reservationSessionBeanRemote.updateReservation(reservation);
                        } catch (InputDataValidationException ex) {
                            ex.printStackTrace();
                        } catch (ReservationNotFoundException ex) {
                            ex.printStackTrace();
                        } catch (UpdateReservationException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    } else if (car.getCarStatusEnum() == CarStatusEnum.AVAILABLE && car.getOutlet().equals(reservation.getPickUpOutlet())) {
                        reservation.setCar(car);
                        try {
                            reservationSessionBeanRemote.updateReservation(reservation);
                        } catch (InputDataValidationException ex) {
                            ex.printStackTrace();
                        } catch (ReservationNotFoundException ex) {
                            ex.printStackTrace();
                        } catch (UpdateReservationException ex) {
                            ex.printStackTrace();
                        }
                        car.setCarStatusEnum(CarStatusEnum.TO_BE_RENTED);
                        carSessionBeanRemote.updateCar(car);
                        break;
                    } else if (car.getCarStatusEnum() == CarStatusEnum.ON_RENTAL && !car.getReservation().getDropOffOutlet().equals(reservation.getPickUpOutlet()) && car.getReservation().getEndDate().toInstant().plus(2, (TemporalUnit) HOURS).isBefore(reservation.getStartDate().toInstant())) {
                        reservation.setCar(car);
                        try {
                            reservationSessionBeanRemote.updateReservation(reservation);
                        } catch (InputDataValidationException ex) {
                            ex.printStackTrace();
                        } catch (ReservationNotFoundException ex) {
                            ex.printStackTrace();
                        } catch (UpdateReservationException ex) {
                            ex.printStackTrace();
                        }
                        transitRecordSessionBeanRemote.createNewTransitRecord(new TransitRecord(reservation.getPickUpOutlet(), reservation, reservation.getStartDate()));
                        break;
                    } else if (car.getCarStatusEnum() == CarStatusEnum.AVAILABLE && !car.getOutlet().equals(reservation.getPickUpOutlet())) {
                        reservation.setCar(car);
                        car.setCarStatusEnum(CarStatusEnum.TO_BE_RENTED);
                        try {
                            reservationSessionBeanRemote.updateReservation(reservation);
                        } catch (InputDataValidationException ex) {
                            ex.printStackTrace();
                        } catch (ReservationNotFoundException ex) {
                            ex.printStackTrace();
                        } catch (UpdateReservationException ex) {
                            ex.printStackTrace();
                        }
                        transitRecordSessionBeanRemote.createNewTransitRecord(new TransitRecord(reservation.getPickUpOutlet(), reservation, reservation.getStartDate()));
                        break;
                    }

                }
            } else {
                CarCategory carCategory = reservation.getCarCategory();
                carCategory.getCarModels().size();
                for (CarModel carModel1 : carCategory.getCarModels()) {
                    boolean check = true;
                    carModel1.getCars().size();

                    for (Car car : carModel1.getCars()) {
                        if (car.getCarStatusEnum() == CarStatusEnum.ON_RENTAL && car.getReservation().getDropOffOutlet().equals(reservation.getPickUpOutlet()) && car.getReservation().getEndDate().before(reservation.getStartDate())) {
                            reservation.setCar(car);
                            try {
                                reservationSessionBeanRemote.updateReservation(reservation);
                            } catch (InputDataValidationException ex) {
                                ex.printStackTrace();
                            } catch (ReservationNotFoundException ex) {
                                ex.printStackTrace();
                            } catch (UpdateReservationException ex) {
                                ex.printStackTrace();
                            }
                            check = false;
                            break;
                        } else if (car.getCarStatusEnum() == CarStatusEnum.AVAILABLE && car.getOutlet().equals(reservation.getPickUpOutlet())) {
                            System.out.println("Here");
                            reservation.setCar(car);
                            try {
                                reservationSessionBeanRemote.updateReservation(reservation);
                            } catch (InputDataValidationException ex) {
                                ex.printStackTrace();
                            } catch (ReservationNotFoundException ex) {
                                ex.printStackTrace();
                            } catch (UpdateReservationException ex) {
                                ex.printStackTrace();
                            }
                            car.setCarStatusEnum(CarStatusEnum.TO_BE_RENTED);
                            check = false;
                            break;
                        } else if (car.getCarStatusEnum() == CarStatusEnum.ON_RENTAL && !car.getReservation().getDropOffOutlet().equals(reservation.getPickUpOutlet()) && car.getReservation().getEndDate().toInstant().plus(2, (TemporalUnit) HOURS).isBefore(reservation.getStartDate().toInstant())) {
                            reservation.setCar(car);
                            try {
                                reservationSessionBeanRemote.updateReservation(reservation);
                            } catch (InputDataValidationException ex) {
                                ex.printStackTrace();
                            } catch (ReservationNotFoundException ex) {
                                ex.printStackTrace();
                            } catch (UpdateReservationException ex) {
                                ex.printStackTrace();
                            }
                            transitRecordSessionBeanRemote.createNewTransitRecord(new TransitRecord(reservation.getPickUpOutlet(), reservation, reservation.getStartDate()));
                            check = false;
                            break;
                        } else if (car.getCarStatusEnum() == CarStatusEnum.AVAILABLE && !car.getOutlet().equals(reservation.getPickUpOutlet())) {
                            reservation.setCar(car);
                            car.setCarStatusEnum(CarStatusEnum.TO_BE_RENTED);
                            try {
                                reservationSessionBeanRemote.updateReservation(reservation);
                            } catch (InputDataValidationException ex) {
                                ex.printStackTrace();
                            } catch (ReservationNotFoundException ex) {
                                ex.printStackTrace();
                            } catch (UpdateReservationException ex) {
                                ex.printStackTrace();
                            }
                            transitRecordSessionBeanRemote.createNewTransitRecord(new TransitRecord(reservation.getPickUpOutlet(), reservation, reservation.getStartDate()));
                            check = false;
                            break;
                        }
                    }
                    if (!check) {
                        break;
                    }
                }

            }

        }
    }
}
