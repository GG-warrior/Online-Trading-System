package main;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

// 交易记录类
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String transactionId;
    private String buyerId;
    private String productId;
    private double amount;
    private Date transactionDate;
    
    public Transaction(String transactionId, String buyerId, String productId, double amount) {
        this.transactionId = transactionId;
        this.buyerId = buyerId;
        this.productId = productId;
        this.amount = amount;
        this.transactionDate = new Date();
    }
    
    // Getters
    public String getTransactionId() { return transactionId; }
    public String getBuyerId() { return buyerId; }
    public String getProductId() { return productId; }
    public double getAmount() { return amount; }
    public Date getTransactionDate() { return transactionDate; }
    
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", buyerId='" + buyerId + '\'' +
                ", productId='" + productId + '\'' +
                ", amount=" + amount +
                ", transactionDate=" + sdf.format(transactionDate) +
                '}';
    }
}