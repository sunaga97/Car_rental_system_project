/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Goh Chun Teck
 */
@Entity
public class Outlet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outletId;
    @NotNull
    @Column (nullable = false, length = 70)
    private String address;
    @NotNull
    @Column (nullable = false, length = 50)
    private String name;
    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date openFrom;
    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date openTo;
    
    @OneToMany(mappedBy = "outlet",fetch = FetchType.EAGER)
    private List<Employee> employees;
    
    @OneToMany(mappedBy = "outlet",fetch = FetchType.EAGER)
    private List<Car> cars;
    
    @OneToMany(mappedBy = "outlet")
    private List<TransitRecord> transitRecords;

    public Outlet() {
        employees = new ArrayList<Employee>();
        cars = new ArrayList<Car>();
        transitRecords = new ArrayList<>();
    }

    public Outlet(String address, String name, Date openFrom, Date openTo) {
        this();
        this.address = address;
        this.name = name;
        this.openFrom = openFrom;
        this.openTo = openTo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (outletId != null ? outletId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the OutletId fields are not set
        if (!(object instanceof Outlet)) {
            return false;
        }
        Outlet other = (Outlet) object;
        if ((this.outletId == null && other.outletId != null) || (this.outletId != null && !this.outletId.equals(other.outletId))) {
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
    @XmlTransient
    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public String toString() {
        return "Entity.Outlet[ id=" + outletId + " ]";
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    public Date getOpenFrom() {
        return openFrom;
    }

    public void setOpenFrom(Date openFrom) {
        this.openFrom = openFrom;
    }

    public Date getOpenTo() {
        return openTo;
    }

    public void setOpenTo(Date openTo) {
        this.openTo = openTo;
    }
    
}
