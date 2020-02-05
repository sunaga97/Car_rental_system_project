/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Goh Chun Teck
 */
@Entity
public class CarModel implements Serializable, Comparable<CarModel>{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 9, nullable = false, unique = true)
    private Long carModelId;
    @Column(length = 32, nullable = false)
    private String carMakeName;
    @Column(length = 32, nullable = false, unique = true)
    private String carModelName;
    @OneToMany(mappedBy = "carModel",fetch = FetchType.EAGER)
    private List<Car> cars;
    @ManyToOne(optional = false,fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private CarCategory carCategory;
    @Column
    private boolean disabled;


    public CarModel() {
        disabled = false;
        cars = new ArrayList<>();
    }

    public CarModel(String carMakeName, String carModelName, CarCategory carCategory) {
        this();
        this.carMakeName = carMakeName;
        this.carModelName = carModelName;
        this.carCategory = carCategory;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }


    


    public String getCarModelName() {
        return carModelName;
    }

    public void setCarModelName(String carModelName) {
        this.carModelName = carModelName;
    }
    @XmlTransient
    public CarCategory getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(CarCategory carCategory) {
        this.carCategory = carCategory;
    }

    

    public void setCarMakeName(String carMakeName) {
        this.carMakeName = carMakeName;
    }

    public String getCarMakeName() {
        return carMakeName;
    }
        
    public Long getCarModelId() {
        return carModelId;
    }

    public void setCarModelId(Long carModelId) {
        this.carModelId = carModelId;
    }
    @XmlTransient
    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carModelId != null ? carModelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carModelId fields are not set
        if (!(object instanceof CarModel)) {
            return false;
        }
        CarModel other = (CarModel) object;
        if ((this.carModelId == null && other.carModelId != null) || (this.carModelId != null && !this.carModelId.equals(other.carModelId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Car Model Id: " + carModelId + " Category: " + carCategory + " Car Make: " + carMakeName + " Car Model: " + carModelName;
    }

    @Override
    public int compareTo(CarModel o) {
        if(this.carCategory.getCarCategoryId() == o.getCarCategory().getCarCategoryId()) {
            if (this.carMakeName == o.getCarMakeName()) {
                return (this.carModelName.compareTo(o.getCarModelName()));
            }
            return (this.carMakeName.compareTo(o.carMakeName));
        }
        return (int) (this.carCategory.getCarCategoryId() - o.getCarCategory().getCarCategoryId());
    }
    
}
