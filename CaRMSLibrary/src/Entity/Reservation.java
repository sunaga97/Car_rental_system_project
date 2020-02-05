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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Goh Chun Teck
 */
@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date endDate;
    
    @ManyToOne(optional = true,fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    private Partner partner;
    
    @ManyToOne(optional = true,fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    private Customer customer;

    @OneToOne(optional = true)
    private Car car;
    
    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private Outlet pickUpOutlet;
    
    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    private Outlet dropOffOutlet;
    
    @ManyToOne(optional = true,fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    private CarCategory carCategory;
    
    @ManyToOne(optional = true,fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    private CarModel carModel;
    
    @OneToOne(optional = true, mappedBy = "reservation")
    private TransitRecord transitRecord;
    
    @Column(nullable = false)
    private boolean paid;
    @Column(nullable = true)
    private double totalCost;

    public TransitRecord getTransitRecord() {
        return transitRecord;
    }

    public void setTransitRecord(TransitRecord transitRecord) {
        this.transitRecord = transitRecord;
    }


    public Reservation(Date startDate, Date endDate, Customer customer, Car car, Outlet pickUpOutlet, Outlet dropOffOutlet, CarCategory carCategory, CarModel carModel, double totalCost) {
        this();
        this.startDate = startDate;
        this.endDate = endDate;
        this.customer = customer;
        this.car = car;
        this.pickUpOutlet = pickUpOutlet;
        this.dropOffOutlet = dropOffOutlet;
        this.carCategory = carCategory;
        this.carModel = carModel;
        this.totalCost= totalCost;
    }

    public Reservation(Date startDate, Date endDate, Partner partner, Car car, Outlet pickUpOutlet, Outlet dropOffOutlet, CarCategory carCategory, CarModel carModel, double totalCost) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.partner = partner;
        this.car = car;
        this.pickUpOutlet = pickUpOutlet;
        this.dropOffOutlet = dropOffOutlet;
        this.carCategory = carCategory;
        this.carModel = carModel;
        this.totalCost = totalCost;
    }
    
    
    public Reservation() {
    }

    public CarCategory getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(CarCategory carCategory) {
        this.carCategory = carCategory;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    
        
    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public CarModel getCarModel() {
        return carModel;
    }

    public void setCarModel(CarModel carModel) {
        this.carModel = carModel;
    }

    
    
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationId fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Reservation[ id=" + reservationId + " ]";
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the car
     */
    @XmlTransient
    public Car getCar() {
        return car;
    }

    /**
     * @param car the car to set
     */
    public void setCar(Car car) {
        this.car = car;
    }
    @XmlTransient
    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Outlet getPickUpOutlet() {
        return pickUpOutlet;
    }

    public void setPickUpOutlet(Outlet pickUpOutlet) {
        this.pickUpOutlet = pickUpOutlet;
    }

    public Outlet getDropOffOutlet() {
        return dropOffOutlet;
    }

    public void setDropOffOutlet(Outlet dropOffOutlet) {
        this.dropOffOutlet = dropOffOutlet;
    }

    
}
