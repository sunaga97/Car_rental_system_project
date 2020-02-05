/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Goh Chun Teck
 */
@Entity
public class RentalTransaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalTransactionId;
    private Date transactionDateTime;
    private BigDecimal totalAmount;
    private Boolean voidRefund;

    public RentalTransaction() {
    }

    public RentalTransaction(Date transactionDateTime, BigDecimal totalAmount, Boolean voidRefund) {
        this();
        this.transactionDateTime = transactionDateTime;
        this.totalAmount = totalAmount;
        this.voidRefund = voidRefund;
    }
    
    
    public Long getRentalTransactionId() {
        return rentalTransactionId;
    }

    public void setRentalTransactionId(Long rentalTransactionId) {
        this.rentalTransactionId = rentalTransactionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalTransactionId != null ? rentalTransactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalTransactionId fields are not set
        if (!(object instanceof RentalTransaction)) {
            return false;
        }
        RentalTransaction other = (RentalTransaction) object;
        if ((this.rentalTransactionId == null && other.rentalTransactionId != null) || (this.rentalTransactionId != null && !this.rentalTransactionId.equals(other.rentalTransactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.RentalTransaction[ id=" + rentalTransactionId + " ]";
    }

    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Boolean getVoidRefund() {
        return voidRefund;
    }

    public void setVoidRefund(Boolean voidRefund) {
        this.voidRefund = voidRefund;
    }
    
}
