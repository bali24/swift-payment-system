// src/main/java/com/example/swift/entity/Office.java
package com.bank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Office {
    @Id
    private String officeId; // char(3)
    private String officeName; // char(35)
    private String status; // char(1)
    private LocalDate businessDate; // Date
    private String defaultCurrency; // char(3)
    private String transitNo; // char(5)
    private String countryCode; // char(2)
    private String paymentAllowed; // char(1)
    private LocalDateTime lastUpdate; // datetime
    private String updateUser; // char(20)

    // Getters and Setters
    public String getOfficeId() { return officeId; }
    public void setOfficeId(String officeId) { this.officeId = officeId; }
    public String getOfficeName() { return officeName; }
    public void setOfficeName(String officeName) { this.officeName = officeName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getBusinessDate() { return businessDate; }
    public void setBusinessDate(LocalDate businessDate) { this.businessDate = businessDate; }
    public String getDefaultCurrency() { return defaultCurrency; }
    public void setDefaultCurrency(String defaultCurrency) { this.defaultCurrency = defaultCurrency; }
    public String getTransitNo() { return transitNo; }
    public void setTransitNo(String transitNo) { this.transitNo = transitNo; }
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public String getPaymentAllowed() { return paymentAllowed; }
    public void setPaymentAllowed(String paymentAllowed) { this.paymentAllowed = paymentAllowed; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
}