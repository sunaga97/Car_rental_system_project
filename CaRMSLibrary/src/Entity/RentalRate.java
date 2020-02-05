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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Goh Chun Teck
 */
@Entity
public class RentalRate implements Serializable , Comparable<RentalRate>{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long rentalRateId;
    @Column(nullable = false, length = 100)
    @NotNull
    private String name;
    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date validityStartDate;
    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date validityEndDate;
    @Column(nullable = false)
    @NotNull
    private double dailyRate;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private CarCategory carCategory;
    
    @Column(nullable = false)
    private boolean isDisabled;

    public RentalRate() {
        isDisabled = false;
    }

    public RentalRate(String name, Date validityStartDate, Date validityEndDate, double dailyRate, CarCategory carCategory) {
        this.name = name;
        this.validityStartDate = validityStartDate;
        this.validityEndDate = validityEndDate;
        this.dailyRate = dailyRate;
        this.carCategory = carCategory;
    }



    public boolean isIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }
    
    
    public Long getRentalRateId() {
        return rentalRateId;
    }

    public void setRentalRateId(Long rentalRateId) {
        this.rentalRateId = rentalRateId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalRateId != null ? rentalRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalRateId fields are not set
        if (!(object instanceof RentalRate)) {
            return false;
        }
        RentalRate other = (RentalRate) object;
        if ((this.rentalRateId == null && other.rentalRateId != null) || (this.rentalRateId != null && !this.rentalRateId.equals(other.rentalRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.rentalRateId + " " + this.name + " " + this.carCategory + " daily rate: " + this.dailyRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

 
    @XmlTransient
    public CarCategory getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(CarCategory carCategory) {
        this.carCategory = carCategory;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    @Override
    public int compareTo(RentalRate o) {
        if (this.carCategory.getCarCategoryId() == o.carCategory.getCarCategoryId()) {
            if (this.validityStartDate != null && o.validityStartDate != null) {
                if (this.validityStartDate.before(o.getValidityStartDate())) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
        return (int) (this.carCategory.getCarCategoryId() - o.carCategory.getCarCategoryId());
       }

    public Date getValidityStartDate() {
        return validityStartDate;
    }

    public void setValidityStartDate(Date validityStartDate) {
        this.validityStartDate = validityStartDate;
    }

    public Date getValidityEndDate() {
        return validityEndDate;
    }

    public void setValidityEndDate(Date validityEndDate) {
        this.validityEndDate = validityEndDate;
    }
    
}
