/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.RentalRate;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.DeleteRentalRateException;
import util.exception.InputDataValidationException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRentalRateException;

/**
 *
 * @author Goh Chun Teck
 */
@Remote
public interface RentalRateSessionBeanRemote {
    
    public RentalRate createNewRentalRate(RentalRate newRentalRate)throws InputDataValidationException,UnknownPersistenceException ;
    
    List<RentalRate> retrieveAllRentalRates();
    
    RentalRate retrieveRentalRateByRentalRateId(Long RentalRateId) throws RentalRateNotFoundException;
    
    void updateRentalRate(RentalRate RentalRate) throws RentalRateNotFoundException, UpdateRentalRateException, InputDataValidationException;
    
    void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException, DeleteRentalRateException;

    public RentalRate retrieveRentalRateByRentalRateName(String name) throws RentalRateNotFoundException;

    public List<RentalRate> ratesAvailable(List<RentalRate> allRates, Date pickDateTime, Date returnDateTime);

}
