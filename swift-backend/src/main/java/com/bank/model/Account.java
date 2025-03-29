package com.bank.model;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @Column(name = "account_id")
    private String accountId;

    @Column(nullable = false)
    private double balance;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(nullable = false, length = 11)
    private String bic;

    // Getters and Setters
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getBic() { return bic; }
    public void setBic(String bic) { this.bic = bic; }

    // toString 方法（便于调试）
    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", bic='" + bic + '\'' +
                ", balance=" + balance +
                ", currency='" + currency + '\'' +
                '}';
    }
}