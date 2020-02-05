/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlTransient;
import util.enumeration.CarStatusEnum;
import util.exception.CarModelIsDisabledException;

/**
 *
 * @author sunag
 */
@Entity
public class Car implements Serializable, Comparable<Car> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 9, nullable = false, unique = true)
    private Long carId;
    @Column(length = 32, nullable = false, unique = true)
    private String carPlateNo;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarStatusEnum carStatusEnum;
    @Column(nullable = false)
    private boolean isDisabled;
    @Column(nullable = false)
    private String colour;
    
    @ManyToOne(optional = false,fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private CarModel carModel;
    
    @ManyToOne(optional = true,fetch = FetchType.EAGER)
    private Outlet outlet;
    
    @OneToOne(optional = true,fetch = FetchType.EAGER)
    private Customer customer;
    
    @OneToOne(optional = true, mappedBy = "car")
    private Reservation reservation; 
    
    public Car() {
        customer = null;
        carStatusEnum = CarStatusEnum.AVAILABLE;
        isDisabled = false;
    }

    public Car(String carPlateNo, CarModel carModel, String colour, Outlet outlet, CarStatusEnum carStatusEnum) throws CarModelIsDisabledException{
        this();
        this.outlet = outlet;
        this.carPlateNo = carPlateNo;
        if (carModel.isDisabled()) {
            throw new CarModelIsDisabledException("CarModel is disabled!");
        }
        this.carModel = carModel;
        this.colour = colour;
        this.carStatusEnum = carStatusEnum;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    @XmlTransient
    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
    
    
    
    public boolean isIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }
    
    public CarStatusEnum getCarStatusEnum() {
        return carStatusEnum;
    }

    public void setCarStatusEnum(CarStatusEnum carStatusEnum) {
        this.carStatusEnum = carStatusEnum;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    
    
    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carId != null ? carId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carId fields are not set
        if (!(object instanceof Car)) {
            return false;
        }
        Car other = (Car) object;
        if ((this.carId == null && other.carId != null) || (this.carId != null && !this.carId.equals(other.carId))) {
            return false;
        }
        return true;
    }

    @XmlTransient
    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

    @Override
    public String toString() {
        return "CarID: " + carId + ", Car Category: " + carModel.getCarCategory() + ", Car Plate Number: " + carPlateNo;
    }

    /**
     * @return the carPlateNo
     */
    public String getCarPlateNo() {
        return carPlateNo;
    }

    /**
     * @param carPlateNo the carPlateNo to set
     */
    public void setCarPlateNo(String carPlateNo) {
        this.carPlateNo = carPlateNo;
    }
    @XmlTransient
    public CarModel getCarModel() {
        return carModel;
    }

    public void setCarModel(CarModel carModel) {
        this.carModel = carModel;
    }

    @Override
    public int compareTo(Car o) {
        if (this.getCarModel().getCarCategory().getCarCategoryId() == o.getCarModel().getCarCategory().getCarCategoryId()) {
            if (this.getCarModel().getCarMakeName() == o.getCarModel().getCarMakeName()) {
                return this.getCarPlateNo().compareTo(o.getCarPlateNo());
            }
            return this.getCarModel().getCarMakeName().compareTo(o.getCarModel().getCarMakeName());
        }
        return this.getCarModel().getCarCategory().getCarCategoryId().compareTo(o.getCarModel().getCarCategory().getCarCategoryId());
    }
    
}
