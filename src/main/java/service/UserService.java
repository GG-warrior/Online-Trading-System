package src.main.java.service;

import java.util.*;
import java.nio.file.*;

import src.main.java.main.AdminUser;
import src.main.java.main.RegularUser;
import src.main.java.main.User;

import java.io.*;

public class UserService {
    // 使用文件存储替代数据库
    private static final Path DATA_DIR = Paths.get("src", "main", "resources");
    private static final Path USER_FILE = DATA_DIR.resolve("users.dat");
    private Map<String, User> users = new HashMap<>();
    private Map<String, RegularUser> regularUsers = new HashMap<>();
    private Map<String, AdminUser> adminUsers = new HashMap<>();
    
    public UserService() {
        // 从文件加载用户数据
        loadUsersFromFile();
        // 初始化默认用户（如果文件中没有用户）
        if (users.isEmpty()) {
            initializeDefaultUsers();
            saveUsersToFile();
        }
    }
    
    private void initializeDefaultUsers() {
        // 添加默认用户用于测试
        RegularUser regularUser = new RegularUser("user001", "regularUser", "password123");
        AdminUser adminUser = new AdminUser("admin001", "adminUser", "admin123", "super");
        
        users.put(regularUser.getUserId(), regularUser);
        users.put(adminUser.getUserId(), adminUser);
        regularUsers.put(regularUser.getUserId(), regularUser);
        adminUsers.put(adminUser.getUserId(), adminUser);
    }
    
    // 用户注册
    public boolean registerUser(String userId, String username, String password, String userType) {
        // 检查用户是否已存在（在内存中检查）
        if (users.containsKey(userId)) {
            return false; // 用户ID已存在
        }
        
        User user;
        if ("regular".equalsIgnoreCase(userType)) {
            user = new RegularUser(userId, username, password);
            regularUsers.put(userId, (RegularUser) user);
        } else if ("admin".equalsIgnoreCase(userType)) {
            user = new AdminUser(userId, username, password);
            adminUsers.put(userId, (AdminUser) user);
        } else {
            return false;
        }
        
        users.put(userId, user);
        // 保存到文件
        saveUsersToFile();
        return true;
    }
    
    // 用户登录
    public User login(String userId, String password) {
        User user = users.get(userId);
        if (user != null && password != null && password.equals(user.getPassword())) {
            // 检查用户是否被封禁
            if (user.isBanned()) {
                System.out.println("该用户已被封禁，无法登录");
                return null;
            }
            return user;
        }
        return null;
    }
    
    // 根据用户ID查找用户
    public User findUserById(String userId) {
        return users.get(userId);
    }
    
    // 获取所有用户
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    // 删除用户（管理员功能）
    public boolean deleteUser(String userId) {
        if (users.containsKey(userId)) {
            User user = users.remove(userId);
            if (user instanceof RegularUser) {
                regularUsers.remove(userId);
            } else if (user instanceof AdminUser) {
                adminUsers.remove(userId);
            }
            saveUsersToFile();
            return true;
        }
        return false;
    }
    
    // 封禁用户（管理员功能）
    public boolean banUser(String userId) {
        User user = users.get(userId);
        if (user != null) {
            user.setBanned(true);
            saveUsersToFile();
            return true;
        }
        return false;
    }
    
    // 解封用户（管理员功能）
    public boolean unbanUser(String userId) {
        User user = users.get(userId);
        if (user != null) {
            user.setBanned(false);
            saveUsersToFile();
            return true;
        }
        return false;
    }
    
    // 更新用户信息
    public boolean updateUser(String userId, String username, String email, String phoneNumber) {
        User user = users.get(userId);
        if (user != null) {
            user.setUsername(username);
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            saveUsersToFile();
            return true;
        }
        return false;
    }
    
    // 保存用户数据到文件
    private void saveUsersToFile() {
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            System.err.println("创建资源目录失败: " + e.getMessage());
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(USER_FILE))) {
            oos.writeObject(users);
            oos.writeObject(regularUsers);
            oos.writeObject(adminUsers);
        } catch (IOException e) {
            System.err.println("保存用户数据失败: " + e.getMessage());
        }
    }
    
    // 从文件加载用户数据
    private void loadUsersFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(USER_FILE))) {
            users = (Map<String, User>) ois.readObject();
            regularUsers = (Map<String, RegularUser>) ois.readObject();
            adminUsers = (Map<String, AdminUser>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // 文件不存在或损坏，将在构造函数中初始化默认用户
            System.out.println("用户数据文件不存在或损坏，将使用默认用户");
        }
    }
}