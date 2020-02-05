package ejb.session.singleton;

import Entity.Car;
import Entity.CarCategory;
import Entity.CarModel;
import Entity.Employee;
import Entity.Outlet;
import Entity.Partner;
import Entity.RentalRate;
import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.CarModelSessionBeanLocal;
import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import util.enumeration.AccessRightEnum;
import util.enumeration.CarStatusEnum;
import util.exception.CarModelIsDisabledException;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.OutletExistsException;
import util.exception.PartnerUsernameExistException;
import util.exception.UnknownPersistenceException;

@Singleton
@LocalBean
@Startup

public class DataInitializationSessionBean {

    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBean;

    @EJB
    private CarSessionBeanLocal carSessionBean;

    @EJB
    private CustomerSessionBeanLocal customerSessionBean;

    @EJB
    private CarModelSessionBeanLocal carModelSessionBean;

    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBean;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    @EJB
    private OutletSessionBeanLocal outletSessionBean;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    
    

    public DataInitializationSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {

            employeeSessionBeanLocal.retrieveEmployeeByUsername("manager");
        } catch (EmployeeNotFoundException ex) {

            initializeData();
        }
    }

    private void initializeData() {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Outlet a = null;
        Outlet b = null;
        Outlet c = null;
        try {
            a = outletSessionBean.createNewOutlet(new Outlet("Address A", "Outlet A", null, null));
            b = outletSessionBean.createNewOutlet(new Outlet("Address B", "Outlet B", null, null));
            c = outletSessionBean.createNewOutlet(new Outlet("Address C", "Outlet C", dateFormat.parse("1000"), dateFormat.parse("2200")));
        } catch (InputDataValidationException ex) {
            ex.printStackTrace();
        } catch (OutletExistsException ex) {
            ex.printStackTrace();
        } catch (UnknownPersistenceException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        
        
        try {
            employeeSessionBeanLocal.createNewEmployee(new Employee("Manager", AccessRightEnum.SALESMANAGER, a, "manager", "password"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A1", AccessRightEnum.SALESMANAGER, a, "A1", "password"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A2", AccessRightEnum.OPERATIONSMANAGER, a, "A2", "password"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A3", AccessRightEnum.CUSTOMERSERVICEEXECUTIVE, a, "A3", "password"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A4", AccessRightEnum.EMPLOYEE, a, "A4", "password"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee A5", AccessRightEnum.EMPLOYEE, a, "A5", "password"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee B1", AccessRightEnum.SALESMANAGER, b, "B1", "password"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee B2", AccessRightEnum.OPERATIONSMANAGER, b, "B2", "password"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee B3", AccessRightEnum.CUSTOMERSERVICEEXECUTIVE, b, "B3", "password"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee C1", AccessRightEnum.SALESMANAGER, c, "C1", "password"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee C2", AccessRightEnum.OPERATIONSMANAGER, c, "C2", "password"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Employee C3", AccessRightEnum.CUSTOMERSERVICEEXECUTIVE, c, "C3", "password"));
        } catch (UnknownPersistenceException | EmployeeUsernameExistException ex) {
            ex.printStackTrace();
        }
        
        CarCategory ls = null;
        CarCategory fs = null;
        CarCategory ss = null;
        CarCategory suv = null;
        try {
            ls = carCategorySessionBean.createNewCarCategory(new CarCategory("Luxury Sedan"));
            fs = carCategorySessionBean.createNewCarCategory(new CarCategory("Family Sedan"));
            ss = carCategorySessionBean.createNewCarCategory(new CarCategory("Standard Sedan"));
            suv = carCategorySessionBean.createNewCarCategory(new CarCategory("SUV and Minivan"));
        } catch (InputDataValidationException ex) {
            ex.printStackTrace();
        } catch (UnknownPersistenceException ex) {
            ex.printStackTrace();
        }
        
        CarModel corolla = null;
        CarModel civic = null;
        CarModel sunny = null;
        CarModel eClass = null;
        CarModel series5 = null;
        CarModel a6 = null;
        
        try {
            corolla = carModelSessionBean.createNewCarModel(new CarModel("Toyota", "Corolla", ss));
            civic = carModelSessionBean.createNewCarModel(new CarModel("Honda", "Civic", ss));
            sunny = carModelSessionBean.createNewCarModel(new CarModel("Nissan", "Sunny", ss));
            eClass = carModelSessionBean.createNewCarModel(new CarModel("Mercedes", "E class", ls));
            series5 = carModelSessionBean.createNewCarModel(new CarModel("BMW", "5 series", ls));
            a6 = carModelSessionBean.createNewCarModel(new CarModel("Audi", "A6", ls));
        } catch (InputDataValidationException ex) {
            ex.printStackTrace();
        } catch (UnknownPersistenceException ex) {
            ex.printStackTrace();
        }
        
        try {
            carSessionBean.createCar(new Car("SS00A1TC", corolla, "White", a, CarStatusEnum.AVAILABLE));
            carSessionBean.createCar(new Car("SS00A2TC", corolla, "Black", a, CarStatusEnum.AVAILABLE)); 
            carSessionBean.createCar(new Car("SS00A3TC", corolla, "Red", a, CarStatusEnum.AVAILABLE));
            carSessionBean.createCar(new Car("SS00B1HC", civic, "White", b, CarStatusEnum.AVAILABLE));   
            carSessionBean.createCar(new Car("SS00B2HC", civic, "Black", b, CarStatusEnum.AVAILABLE));
            carSessionBean.createCar(new Car("SS00B3HC", civic, "Red", b, CarStatusEnum.AVAILABLE));   
            carSessionBean.createCar(new Car("SS00C1NS", sunny, "White", c, CarStatusEnum.AVAILABLE));
            carSessionBean.createCar(new Car("SS00C2NS", sunny, "White", c, CarStatusEnum.AVAILABLE));   
            carSessionBean.createCar(new Car("SS00C3NS", sunny, "White", c, CarStatusEnum.REPAIR));
            carSessionBean.createCar(new Car("LS00A4ME", eClass, "White", a, CarStatusEnum.AVAILABLE));   
            carSessionBean.createCar(new Car("LS00B4B5", series5, "White", b, CarStatusEnum.AVAILABLE));
            carSessionBean.createCar(new Car("LS00C4A6", a6, "White", c, CarStatusEnum.AVAILABLE));   
        } catch (CarModelIsDisabledException ex){
            ex.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        
        try {
            rentalRateSessionBean.createNewRentalRate(new RentalRate("Standard Sedan - Default", null, null, 100, ss));
            rentalRateSessionBean.createNewRentalRate(new RentalRate("Standard Sedan - Weekend Promo", dateFormat1.parse("06/12/2019 12:00"), dateFormat1.parse("08/12/2019 00:00"), 80, ss));
            rentalRateSessionBean.createNewRentalRate(new RentalRate("Family Sedan - Default", null, null, 200, fs));
            rentalRateSessionBean.createNewRentalRate(new RentalRate("Luxury Sedan - Monday", dateFormat1.parse("02/12/2019 00:00"), dateFormat1.parse("02/12/2019 23:59"), 310, ls));
            rentalRateSessionBean.createNewRentalRate(new RentalRate("Luxury Sedan - Tuesday,", dateFormat1.parse("03/12/2019 00:00"), dateFormat1.parse("03/12/2019 23:59"), 320, ls));
            rentalRateSessionBean.createNewRentalRate(new RentalRate("Luxury Sedan - Wednesday", dateFormat1.parse("04/12/2019 00:00"), dateFormat1.parse("04/12/2019 23:59"), 330, ls));
            rentalRateSessionBean.createNewRentalRate(new RentalRate("Luxury Sedan - Weekday Promo", dateFormat1.parse("04/12/2019 00:00"), dateFormat1.parse("05/12/2019 23:59"), 250, ls));
        } catch (InputDataValidationException ex) {
            ex.printStackTrace();
        } catch (UnknownPersistenceException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            partnerSessionBean.createNewPartner(new Partner("Holiday.com", "holiday", "password"));
        } catch (InputDataValidationException ex) {
            ex.printStackTrace();
        } catch (PartnerUsernameExistException ex) {
            ex.printStackTrace();
        } catch (UnknownPersistenceException ex) {
            ex.printStackTrace();
        }
        CarCategory carCategory = null;



        


    }

}
