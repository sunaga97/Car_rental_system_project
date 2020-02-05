/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.Employee;
import java.util.List;
import javax.ejb.Local;
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

public interface EmployeeSessionBeanLocal {

    public Employee createNewEmployee(Employee newStaffEntity) throws EmployeeUsernameExistException, UnknownPersistenceException;

    public List<Employee> retrieveAllStaffs();

    public Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException;

    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;

    public Employee login(String username, String password) throws InvalidLoginCredentialException;

    public void updateEmployee(Employee employee) throws EmployeeNotFoundException, UpdateEmployeeException;

    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException, DeleteEmployeeException;


    
}
