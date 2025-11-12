package main;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

// 联系方式交换记录类
public class ContactExchangeRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String recordId;
    private String requesterId;  // 请求方ID
    private String productId;    // 商品ID
    private String ownerId;      // 商品所有者ID
    private Date exchangeDate;   // 交换日期
    
    public ContactExchangeRecord(String recordId, String requesterId, String productId, String ownerId) {
        this.recordId = recordId;
        this.requesterId = requesterId;
        this.productId = productId;
        this.ownerId = ownerId;
        this.exchangeDate = new Date();
    }
    
    // Getters
    public String getRecordId() { return recordId; }
    public String getRequesterId() { return requesterId; }
    public String getProductId() { return productId; }
    public String getOwnerId() { return ownerId; }
    public Date getExchangeDate() { return exchangeDate; }
    
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "ContactExchangeRecord{" +
                "recordId='" + recordId + '\'' +
                ", requesterId='" + requesterId + '\'' +
                ", productId='" + productId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", exchangeDate=" + sdf.format(exchangeDate) +
                '}';
    }
}