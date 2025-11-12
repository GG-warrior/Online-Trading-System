package main;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String userId;
    protected String username;
    protected String password;
    protected String email;
    protected String phoneNumber;
    protected boolean banned; // 添加封禁状态属性
    
    // 默认构造函数
    public User() {
        this.banned = false; // 默认未封禁
    }
    
    // 构造函数
    public User(String userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.banned = false; // 默认未封禁
    }
    
    public User(String userId, String username, String password, String email, String phoneNumber) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.banned = false; // 默认未封禁
    }
    
    // Getter和Setter方法
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    // 封禁状态的Getter和Setter方法
    public boolean isBanned() {
        return banned;
    }
    
    public void setBanned(boolean banned) {
        this.banned = banned;
    }
    
    // 验证用户登录
    public boolean login(String username, String password) {
        // 如果用户被封禁，不能登录
        if (this.banned) {
            return false;
        }
        return this.username != null && this.username.equals(username) && 
               this.password != null && this.password.equals(password);
    }
    
    // 更新用户资料
    public void updateProfile(String email, String phoneNumber) {
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    
    // 验证密码强度
    public static boolean isPasswordStrong(String password) {
        // 密码至少8位，包含数字和字母
        if (password == null || password.length() < 8) {
            return false;
        }
        return password.matches(".*[0-9].*") && password.matches(".*[a-zA-Z].*");
    }
    
    // 验证邮箱格式
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    // 验证手机号格式
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) return false;
        return phoneNumber.matches("^1[3-9]\\d{9}$");
    }
    
    // 修改密码
    public boolean changePassword(String oldPassword, String newPassword) {
        if (this.password != null && this.password.equals(oldPassword) && isPasswordStrong(newPassword)) {
            this.password = newPassword;
            return true;
        }
        return false;
    }
    
    // 验证用户是否有效
    public boolean isValid() {
        return userId != null && !userId.isEmpty() &&
               username != null && !username.isEmpty() &&
               password != null && !password.isEmpty() &&
               isPasswordStrong(password);
    }
    
    // 获取用户联系信息
    public String getContactInfo() {
        return "Email: " + email + ", Phone: " + phoneNumber;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return userId.equals(user.userId);
    }
    
    @Override
    public int hashCode() {
        return userId.hashCode();
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", banned=" + banned +
                '}';
    }
}