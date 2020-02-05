/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.TransitRecord;
import java.util.List;
import javax.ejb.Local;
import util.exception.TransitRecordNotFoundException;

/**
 *
 * @author sunag
 */

public interface TransitRecordSessionBeanLocal {

    public TransitRecord createNewTransitRecord(TransitRecord transitRecord);

    public List<TransitRecord> retrieveAllTransitRecord();

    public TransitRecord retrieveTransitRecordById(Long transitRecordId) throws TransitRecordNotFoundException;

    public void updateTransitRecord(TransitRecord transitRecord);

    public void deleteTransitRecord(TransitRecord transitRecord);

    public void persist(Object object);
    
}
