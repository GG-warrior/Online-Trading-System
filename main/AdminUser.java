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
    
    // 管理员特有功能：封禁用户
    public boolean banUser(String userId) {
        // 实现真正的封禁用户功能
        System.out.println("Admin " + getUsername() + " is banning user " + userId);
        // 这里将在UserService中实现具体的封禁逻辑
        return true;
    }
    
    // 管理员特有功能：解封用户
    public boolean unbanUser(String userId) {
        // 实现真正的解封用户功能
        System.out.println("Admin " + getUsername() + " is unbanning user " + userId);
        // 这里将在UserService中实现具体的解封逻辑
        return true;
    }
    
    @Override
    public String toString() {
        return "AdminUser{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", adminLevel='" + adminLevel + '\'' +
                ", banned=" + isBanned() +
                '}';
    }
}