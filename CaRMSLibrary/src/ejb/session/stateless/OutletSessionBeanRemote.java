/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.Outlet;
import java.util.List;
import javax.ejb.Remote;
import util.exception.DeleteOutletException;
import util.exception.InputDataValidationException;
import util.exception.OutletExistsException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateOutletException;

/**
 *
 * @author Goh Chun Teck
 */

public interface OutletSessionBeanRemote {
    public Outlet createNewOutlet(Outlet newOutlet) throws InputDataValidationException, OutletExistsException, UnknownPersistenceException ;
        
    public List<Outlet> retrieveAllOutlets() ;
       
    public Outlet retrieveOutletById(Long OutletId) throws OutletNotFoundException ;
        
    public void updateOutlet(Outlet outlet) throws InputDataValidationException, OutletNotFoundException, UpdateOutletException ;
       
    public void deleteOutlet(Long outletId) throws OutletNotFoundException, DeleteOutletException ;

    public Outlet retrieveOutletByName(String name) throws OutletNotFoundException;
      
}
