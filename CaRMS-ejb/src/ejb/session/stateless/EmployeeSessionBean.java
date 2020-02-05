/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.Employee;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.DeleteEmployeeException;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEmployeeException;

/**
 *
 * @author sunag
 */
@Stateless
@Local(EmployeeSessionBeanLocal.class)
@Remote(EmployeeSessionBeanRemote.class)
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {
    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em; 
    
    
    public EmployeeSessionBean()
    {
    }
    
    @Override
    public Employee createNewEmployee(Employee newEmployee) throws EmployeeUsernameExistException, UnknownPersistenceException
    {
        try
        {
            em.persist(newEmployee);
            em.flush();

            return newEmployee;
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new EmployeeUsernameExistException();
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
    public List<Employee> retrieveAllStaffs()
    {
        Query query = em.createQuery("SELECT e FROM Employee e");
        
        return query.getResultList();
    }
    

    public Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException
    {
        Employee employee = em.find(Employee.class, employeeId);
        
        if(employee != null)
        {
            return employee;
        }
        else
        {
            throw new EmployeeNotFoundException("Employee does not exist: " + employeeId);
        }
    }
    
    @Override
    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException
    {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (Employee)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new EmployeeNotFoundException("Employee Username " + username + " does not exist!");
        }
    }
   

    @Override
    public Employee login(String username, String password) throws InvalidLoginCredentialException
    {
        try
        {
            Employee employee = retrieveEmployeeByUsername(username);
            
            if(employee.getPassword().equals(password))
            {
//                staffEntity.getSaleTransactionEntities().size();                
                return employee;
            }
            else
            {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        }
        catch(EmployeeNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    @Override
    public void updateEmployee(Employee employee) throws EmployeeNotFoundException, UpdateEmployeeException
    {
        em.merge(employee);
    }
    
    @Override
    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException, DeleteEmployeeException
    {
        Employee staffEntityToRemove = retrieveEmployeeById(employeeId);
        
//        if(staffEntityToRemove.getSaleTransactionEntities().isEmpty())
//        {
//  IMPORTANT TO SAFELY REMOVE
            em.remove(staffEntityToRemove);
//        }
        if (false)
        {
            // New in v4.1 to prevent deleting staff with existing sale transaction(s)
            throw new DeleteEmployeeException("Employee ID " + employeeId + " is associated with existing sale transaction(s) and cannot be deleted!");
        }
    }
    
    
    
}
