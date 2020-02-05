/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.TransitRecord;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InputDataValidationException;
import util.exception.TransitRecordNotFoundException;
import util.exception.UpdateTransitRecordException;

/**
 *
 * @author sunag
 */
@Stateless
@Remote(TransitRecordSessionBeanRemote.class)
@Local(TransitRecordSessionBeanLocal.class)
public class TransitRecordSessionBean implements TransitRecordSessionBeanRemote, TransitRecordSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    public TransitRecord createNewTransitRecord(TransitRecord transitRecord) {
        em.persist(transitRecord);
        em.flush();

        return transitRecord;
    }

    public List<TransitRecord> retrieveAllTransitRecord() {
        Query query = em.createQuery("SELECT c FROM TransitRecord c");

        return query.getResultList();
    }

    public TransitRecord retrieveTransitRecordById(Long transitRecordId) throws TransitRecordNotFoundException {
        TransitRecord transitRecord = em.find(TransitRecord.class, transitRecordId);
        
        if(transitRecord != null)
        {
            return transitRecord;
        }
        else
        {
            throw new TransitRecordNotFoundException("Transit Record ID " + transitRecordId + " does not exist!");
        }
    }

    public void updateTransitRecord(TransitRecord transitRecord) {
        em.merge(transitRecord);
    }
       
    public  void deleteTransitRecord(TransitRecord transitRecord) {
        em.remove(transitRecord);
    }

    public void persist(Object object) {
        em.persist(object);
    }
    
    private Date getDateCeiling(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        Date nextDayCeiling = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE) + 1).getTime();
        return nextDayCeiling;
    }
    
    public List<TransitRecord> retrieveListOfTransitRecordByDate(Date date) {
        Query query = em.createQuery("SELECT tr FROM TransitRecord tr");
        List<TransitRecord> list = query.getResultList();
        List<TransitRecord> filteredList = new ArrayList<>();
        for (TransitRecord transitRecord : list) {
            if(getDateCeiling(transitRecord.getDate()).equals(getDateCeiling(date))) {
                filteredList.add(transitRecord);
            }
        }
        return filteredList;

    }
    

}
