/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.Customer;
import Entity.Reservation;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.DeleteCustomerException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCustomerException;

/**
 *
 * @author Goh Chun Teck
 */
@Remote
public interface CustomerSessionBeanRemote {

    public Customer createNewCustomer(Customer newCustomer) throws InputDataValidationException, CustomerUsernameExistException, UnknownPersistenceException;

    public List<Customer> retrieveAllCustomers();

    public Customer retrieveCustomerById(Long CustomerId) throws CustomerNotFoundException;

    public void updateCustomer(Customer customer) throws InputDataValidationException, CustomerNotFoundException, UpdateCustomerException;

    public void deleteCustomer(Long customerId) throws CustomerNotFoundException, DeleteCustomerException;

    public Customer login(String username, String password) throws InvalidLoginCredentialException;

    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    public List<Reservation> getReservationsByCustomerId(Long cusId);
    
}
