package main;

public class RegularUser extends User {
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