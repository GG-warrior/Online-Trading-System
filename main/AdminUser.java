package main;

import java.io.Serializable;

public class AdminUser extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String adminLevel;  // 管理员级别
    
    // 默认构造函数
    public AdminUser() {
        super();
        this.adminLevel = "normal";
    }
    
    // 构造函数
    public AdminUser(String userId, String username, String password) {
        super(userId, username, password);
        this.adminLevel = "normal";
    }
    
    public AdminUser(String userId, String username, String password, String adminLevel) {
        super(userId, username, password);
        this.adminLevel = adminLevel;
    }
    
    // Getter和Setter方法
    public String getAdminLevel() {
        return adminLevel;
    }
    
    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }
    
    // 管理员特有功能：管理用户
    public boolean manageUser(String userId, String action) {
        System.out.println("Admin " + username + " is " + action + " user " + userId);
        // 这里可以添加具体的用户管理逻辑
        return true;
    }
    
    // 管理员特有功能：管理商品
    public boolean manageProduct(String productId, String action) {
        System.out.println("Admin " + username + " is " + action + " product " + productId);
        // 这里可以添加具体的产品管理逻辑
        return true;
    }
    
    // 管理员特有功能：查看系统报告
    public void viewSystemReport() {
        System.out.println("Admin " + username + " is viewing system report");
        // 这里可以添加查看系统报告的逻辑
    }
    
    // 管理员特有功能：删除用户
    public boolean deleteUser(String userId) {
        System.out.println("Admin " + username + " is deleting user " + userId);
        return true;
    }
    
    // 管理员特有功能：封禁用户
    public boolean banUser(String userId) {
        // 实现真正的封禁用户功能
        System.out.println("Admin " + username + " is banning user " + userId);
        // 这里将在UserService中实现具体的封禁逻辑
        return true;
    }
    
    // 新增功能：查看所有用户
    public void viewAllUsers() {
        System.out.println("Admin " + username + " is viewing all users");
        // 这里将在Main.java中实现具体逻辑
    }
    
    // 新增功能：查看所有商品
    public void viewAllProducts() {
        System.out.println("Admin " + username + " is viewing all products");
        // 这里将在Main.java中实现具体逻辑
    }
    
    @Override
    public String toString() {
        return "AdminUser{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", adminLevel='" + adminLevel + '\'' +
                ", banned=" + banned +
                '}';
    }
}