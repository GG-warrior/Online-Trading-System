package main;

import java.io.Serializable;

public class RegularUser extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean isVerified;  // 用户是否已验证
    
    // 默认构造函数
    public RegularUser() {
        super();
        this.isVerified = false;
    }
    
    // 构造函数
    public RegularUser(String userId, String username, String password) {
        super(userId, username, password);
        this.isVerified = false;
    }
    
    public RegularUser(String userId, String username, String password, String email, String phoneNumber) {
        super(userId, username, password, email, phoneNumber);
        this.isVerified = false;
    }
    
    // Getter和Setter方法
    public boolean isVerified() {
        return isVerified;
    }
    
    public void setVerified(boolean verified) {
        isVerified = verified;
    }
    
    // 普通用户特有的功能：创建商品
    public Product createProduct(String productId, String name, String description, double price) {
        System.out.println("User " + username + " is creating product: " + name);
        // 这里应该实际创建一个Product对象并返回
        // 由于Product类尚未完全实现，暂时返回null
        return new Product(); // 需要Product类有相应的构造函数
    }
    
    // 普通用户特有的功能：发布商品
    public boolean publishProduct(Product product) {
        System.out.println("User " + username + " is publishing product: " + product);
        // 这里可以添加发布商品的逻辑
        // 例如：将商品添加到商品列表中，设置商品状态为已发布等
        return true;
    }
    
    // 普通用户特有的功能：移除商品
    public boolean removeProduct(String productId) {
        System.out.println("User " + username + " is removing product: " + productId);
        // 这里可以添加移除商品的逻辑
        // 例如：从商品列表中删除商品，或者将商品标记为已移除等
        return true;
    }
    
    // 普通用户特有的功能：搜索用户
    public User searchUser(String userId) {
        System.out.println("User " + username + " is searching for user: " + userId);
        // 这里应该实现搜索用户的逻辑
        // 例如：从用户数据库中查找指定ID的用户
        // 暂时返回null，需要配合用户服务类实现
        return null;
    }
    
    // 普通用户特有的功能：搜索商品
    public Product searchProduct(String productId) {
        System.out.println("User " + username + " is searching for product: " + productId);
        // 这里应该实现搜索商品的逻辑
        // 例如：从商品数据库中查找指定ID的商品
        // 暂时返回null，需要配合商品服务类实现
        return null;
    }
    
    // 普通用户特有的功能：购买商品
    public boolean purchaseProduct(String productId) {
        // 这里可以添加购买逻辑
        System.out.println("User " + username + " is purchasing product " + productId);
        return true;
    }
    
    // 普通用户特有的功能：出售商品
    public boolean sellProduct(String productId) {
        // 这里可以添加出售逻辑
        System.out.println("User " + username + " is selling product " + productId);
        return true;
    }
    
    // 普通用户特有的功能：收藏商品
    public void favoriteProduct(String productId) {
        System.out.println("User " + username + " has favorited product " + productId);
    }
    
    // 普通用户特有的功能：评价商品
    public void rateProduct(String productId, int rating, String comment) {
        System.out.println("User " + username + " rated product " + productId + 
                          " with " + rating + " stars and comment: " + comment);
    }
    
    @Override
    public String toString() {
        return "RegularUser{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isVerified=" + isVerified +
                '}';
    }
}