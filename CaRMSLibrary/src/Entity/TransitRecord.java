/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Goh Chun Teck
 */
@Entity
public class TransitRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transitRecordId;
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private Employee driver;
    @Column(nullable = false)
    private Date date;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet outlet;
    @OneToOne(optional = false)
    private Reservation reservation;
    @Column
    private boolean completed;
    @ManyToOne(optional = true)
    private Employee employee;

    public Long getTransitRecordId() {
        return transitRecordId;
    }

    public TransitRecord() {
        completed = false;
    }

    public TransitRecord(Outlet outlet, Reservation reservation, Date date) {
        this();
        this.outlet = outlet;
        this.reservation = reservation;
        this.date = date;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    
    @XmlTransient
    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }
    @XmlTransient
    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
    

 
    public void setTransitRecordId(Long transitRecordId) {
        this.transitRecordId = transitRecordId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transitRecordId != null ? transitRecordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transitRecordId fields are not set
        if (!(object instanceof TransitRecord)) {
            return false;
        }
        TransitRecord other = (TransitRecord) object;
        if ((this.transitRecordId == null && other.transitRecordId != null) || (this.transitRecordId != null && !this.transitRecordId.equals(other.transitRecordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.TransitRecord[ id=" + transitRecordId + " ]";
    }

    public Employee getDriver() {
        return driver;
    }

    public void setDriver(Employee driver) {
        this.driver = driver;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }



 
    
}
