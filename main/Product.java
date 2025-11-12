package main;

import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private String productId;
    private String name;
    private String description;
    private double price;
    private String ownerId;  // 商品所有者ID
    private boolean isPublished;  // 是否已发布
    
    // 默认构造函数
    public Product() {
        this.isPublished = false;
    }
    
    // 构造函数
    public Product(String productId, String name, String description, double price, String ownerId) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.ownerId = ownerId;
        this.isPublished = false;
    }
    
    // Getter和Setter方法
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public String getOwnerId() {
        return ownerId;
    }
    
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    
    public boolean isPublished() {
        return isPublished;
    }
    
    public void setPublished(boolean published) {
        isPublished = published;
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", ownerId='" + ownerId + '\'' +
                ", isPublished=" + isPublished +
                '}';
    }
}