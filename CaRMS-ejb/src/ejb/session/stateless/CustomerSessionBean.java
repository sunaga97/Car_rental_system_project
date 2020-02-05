/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.Customer;
import Entity.Reservation;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
@Stateless
@Local(CustomerSessionBeanLocal.class)
@Remote(CustomerSessionBeanRemote.class)

public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager entityManager;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public CustomerSessionBean(){
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Customer createNewCustomer(Customer newCustomer) throws InputDataValidationException, CustomerUsernameExistException, UnknownPersistenceException {
         try
        {
            Set<ConstraintViolation<Customer>>constraintViolations = validator.validate(newCustomer);
        
            if(constraintViolations.isEmpty())
            {
                entityManager.persist(newCustomer);
                entityManager.flush();

                return newCustomer;
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }            
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new CustomerUsernameExistException("Customer Username already exist!");
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
            else
            {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public List<Customer> retrieveAllCustomers() {
       Query query = entityManager.createQuery("SELECT c FROM Customer c");
        
        return query.getResultList();
    }

    @Override
    public Customer retrieveCustomerById(Long CustomerId) throws CustomerNotFoundException {
        Customer customer = entityManager.find(Customer.class, CustomerId);
        
        if(customer != null)
        {
            return customer;
        }
        else
        {
            throw new CustomerNotFoundException("Customer ID " + CustomerId + " does not exist!");
        }
    }
    @Override
    public void updateCustomer(Customer customer) throws InputDataValidationException, CustomerNotFoundException, UpdateCustomerException {
       entityManager.merge(customer);
       entityManager.flush();
    }

    @Override
    public void deleteCustomer(Long customerId) throws CustomerNotFoundException, DeleteCustomerException {
        Customer customerToRemove = retrieveCustomerById(customerId);
        
        if(customerToRemove.getReservations().isEmpty()){
            entityManager.remove(customerToRemove);
        }
        else
        {
            throw new DeleteCustomerException("Customer ID " + customerId + " is associated with existing car(s)/employee(s) and cannot be deleted!");
        }
    }

    @Override
    public Customer login(String username, String password) throws InvalidLoginCredentialException
    {
        try
        {
            Customer customer = retrieveCustomerByUsername(username);
            
            if(customer.getPassword().equals(password))
            {
                customer.getReservations().size();                
                return customer;
            }
            else
            {
                throw new InvalidLoginCredentialException("Invalid password!");
            }
        }
        catch(CustomerNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    @Override
    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException
    {
        Query query = entityManager.createQuery("SELECT c FROM Customer c WHERE c.email = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (Customer)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new CustomerNotFoundException("Customer Username " + username + " does not exist!");
        }
    }
    

    
    @Override
    public List<Reservation> getReservationsByCustomerId(Long cusId){
        Customer customer = entityManager.find(Customer.class, cusId);
        entityManager.flush();
        customer.getReservations().size();

        return customer.getReservations();
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Customer>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
