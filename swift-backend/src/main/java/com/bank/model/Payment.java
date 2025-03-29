package com.bank.model;

import jakarta.persistence.*;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

    @Column(nullable = false)
    private String payer;

    @Column(nullable = false)
    private String payee;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "transaction_date", nullable = false)
    private String transactionDate;

    @Column(name = "sender_bic", nullable = false, length = 11)
    private String senderBIC;

    @Column(name = "receiver_bic", nullable = false, length = 11)
    private String receiverBIC;

    @Column(name = "debit_account", nullable = false)
    private String debitAccount;

    @Column(name = "credit_account", nullable = false)
    private String creditAccount;

    @Column(name = "xml_message", columnDefinition = "TEXT")
    private String xmlMessage;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at")
    private java.sql.Timestamp createdAt;

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getPayer() { return payer; }
    public void setPayer(String payer) { this.payer = payer; }

    public String getPayee() { return payee; }
    public void setPayee(String payee) { this.payee = payee; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getTransactionDate() { return transactionDate; }
    public void setTransactionDate(String transactionDate) { this.transactionDate = transactionDate; }

    public String getSenderBIC() { return senderBIC; }
    public void setSenderBIC(String senderBIC) { this.senderBIC = senderBIC; }

    public String getReceiverBIC() { return receiverBIC; }
    public void setReceiverBIC(String receiverBIC) { this.receiverBIC = receiverBIC; }

    public String getDebitAccount() { return debitAccount; }
    public void setDebitAccount(String debitAccount) { this.debitAccount = debitAccount; }

    public String getCreditAccount() { return creditAccount; }
    public void setCreditAccount(String creditAccount) { this.creditAccount = creditAccount; }

    public String getXmlMessage() { return xmlMessage; }
    public void setXmlMessage(String xmlMessage) { this.xmlMessage = xmlMessage; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}