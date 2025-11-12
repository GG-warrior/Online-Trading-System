package data;

import main.Product;
import java.sql.*;
import java.util.*;

public class ProductDAO {
    // 创建商品表
    public static void createProductTable() {
        String sql = "CREATE TABLE IF NOT EXISTS products (" +
                     "productId TEXT PRIMARY KEY, " +
                     "name TEXT NOT NULL, " +
                     "description TEXT, " +
                     "price REAL NOT NULL, " +
                     "ownerId TEXT NOT NULL, " +
                     "isPublished BOOLEAN NOT NULL)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("商品表创建成功");
        } catch (SQLException e) {
            System.err.println("创建商品表时出错: " + e.getMessage());
        }
    }
    
    // 插入商品
    public static boolean insertProduct(Product product) {
        String sql = "INSERT INTO products(productId, name, description, price, ownerId, isPublished) VALUES(?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, product.getProductId());
            pstmt.setString(2, product.getName());
            pstmt.setString(3, product.getDescription());
            pstmt.setDouble(4, product.getPrice());
            pstmt.setString(5, product.getOwnerId());
            pstmt.setBoolean(6, product.isPublished());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("插入商品时出错: " + e.getMessage());
            return false;
        }
    }
    
    // 根据商品ID查找商品
    public static Product findProductById(String productId) {
        String sql = "SELECT * FROM products WHERE productId = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, productId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                String ownerId = rs.getString("ownerId");
                boolean isPublished = rs.getBoolean("isPublished");
                
                Product product = new Product(productId, name, description, price, ownerId);
                product.setPublished(isPublished);
                return product;
            }
        } catch (SQLException e) {
            System.err.println("查找商品时出错: " + e.getMessage());
        }
        
        return null;
    }
    
    // 获取所有商品
    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String productId = rs.getString("productId");
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                String ownerId = rs.getString("ownerId");
                boolean isPublished = rs.getBoolean("isPublished");
                
                Product product = new Product(productId, name, description, price, ownerId);
                product.setPublished(isPublished);
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("获取所有商品时出错: " + e.getMessage());
        }
        
        return products;
    }
    
    // 更新商品发布状态
    public static boolean updateProductPublishStatus(String productId, boolean isPublished) {
        String sql = "UPDATE products SET isPublished = ? WHERE productId = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBoolean(1, isPublished);
            pstmt.setString(2, productId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("更新商品发布状态时出错: " + e.getMessage());
            return false;
        }
    }
    
    // 删除商品
    public static boolean deleteProduct(String productId) {
        String sql = "DELETE FROM products WHERE productId = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, productId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("删除商品时出错: " + e.getMessage());
            return false;
        }
    }
}