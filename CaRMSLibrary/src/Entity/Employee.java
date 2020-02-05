/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;
import util.enumeration.AccessRightEnum;

/**
 *
 * @author sunag
 */
@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long EmployeeId;
    @Column(nullable = false, length = 32)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccessRightEnum accessRightEnum;
    
    @Column(nullable = false, unique = true, length = 32)
    private String username;
    @Column(nullable = false, length = 32)
    private String password;
    
    @ManyToOne(optional = false,fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Outlet outlet;
    
    @OneToMany(mappedBy = "employee")
    private List<TransitRecord> transitRecords;
    
            
    public Employee(String name, AccessRightEnum accessRightEnum, Outlet outlet, String username, String password) {
        this();
        
        this.name = name;
        this.accessRightEnum = accessRightEnum;
        this.username = username;
        this.password = password;
        this.outlet = outlet;
    }
    
    

    public Employee() {
    }

    public List<TransitRecord> getTransitRecords() {
        return transitRecords;
    }

    public void setTransitRecords(List<TransitRecord> transitRecords) {
        this.transitRecords = transitRecords;
    }

    
    public AccessRightEnum getAccessRightEnum() {
        return accessRightEnum;
    }

    public void setAccessRightEnum(AccessRightEnum accessRightEnum) {
        this.accessRightEnum = accessRightEnum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
 
    
    

    public Long getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(Long EmployeeId) {
        this.EmployeeId = EmployeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (EmployeeId != null ? EmployeeId.hashCode() : 0);
        return hash;
    }
    @XmlTransient
    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the EmployeeId fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.EmployeeId == null && other.EmployeeId != null) || (this.EmployeeId != null && !this.EmployeeId.equals(other.EmployeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Employee[ id=" + EmployeeId + " ]";
    }
    
}
