package data;

import main.User;
import main.RegularUser;
import main.AdminUser;
import java.sql.*;
import java.util.*;

public class UserDAO {
    // 创建用户表
    public static void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                     "userId TEXT PRIMARY KEY, " +
                     "username TEXT NOT NULL, " +
                     "password TEXT NOT NULL, " +
                     "email TEXT, " +
                     "phoneNumber TEXT, " +
                     "userType TEXT NOT NULL, " +
                     "adminLevel TEXT)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("用户表创建成功");
        } catch (SQLException e) {
            System.err.println("创建用户表时出错: " + e.getMessage());
        }
    }
    
    // 插入用户
    public static boolean insertUser(User user) {
        String sql = "INSERT INTO users(userId, username, password, email, phoneNumber, userType, adminLevel) VALUES(?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPhoneNumber());
            
            if (user instanceof RegularUser) {
                pstmt.setString(6, "regular");
                pstmt.setNull(7, Types.VARCHAR);
            } else if (user instanceof AdminUser) {
                pstmt.setString(6, "admin");
                pstmt.setString(7, ((AdminUser) user).getAdminLevel());
            }
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("插入用户时出错: " + e.getMessage());
            return false;
        }
    }
    
    // 根据用户ID查找用户
    public static User findUserById(String userId) {
        String sql = "SELECT * FROM users WHERE userId = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phoneNumber");
                String userType = rs.getString("userType");
                String adminLevel = rs.getString("adminLevel");
                
                User user;
                if ("regular".equals(userType)) {
                    user = new RegularUser(userId, username, password);
                } else if ("admin".equals(userType)) {
                    AdminUser admin = new AdminUser(userId, username, password);
                    if (adminLevel != null) {
                        admin.setAdminLevel(adminLevel);
                    }
                    user = admin;
                } else {
                    user = new User(userId, username, password);
                }
                
                user.setEmail(email);
                user.setPhoneNumber(phoneNumber);
                return user;
            }
        } catch (SQLException e) {
            System.err.println("查找用户时出错: " + e.getMessage());
        }
        
        return null;
    }
    
    // 获取所有用户
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String userId = rs.getString("userId");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phoneNumber");
                String userType = rs.getString("userType");
                String adminLevel = rs.getString("adminLevel");
                
                User user;
                if ("regular".equals(userType)) {
                    user = new RegularUser(userId, username, password);
                } else if ("admin".equals(userType)) {
                    AdminUser admin = new AdminUser(userId, username, password);
                    if (adminLevel != null) {
                        admin.setAdminLevel(adminLevel);
                    }
                    user = admin;
                } else {
                    user = new User(userId, username, password);
                }
                
                user.setEmail(email);
                user.setPhoneNumber(phoneNumber);
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("获取所有用户时出错: " + e.getMessage());
        }
        
        return users;
    }
}