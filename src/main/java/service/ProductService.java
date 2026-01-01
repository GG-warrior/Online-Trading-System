package service;

import java.util.*;
import java.nio.file.*;

import main.Product;

import java.io.*;

public class ProductService {
    // 使用文件存储替代数据库
    private static final Path DATA_DIR = Paths.get("src", "main", "resources");
    private static final Path PRODUCT_FILE = DATA_DIR.resolve("products.dat");
    private Map<String, Product> products = new HashMap<>();
    private Map<String, List<String>> userProducts = new HashMap<>();
    private Map<String, List<String>> publishedProducts = new HashMap<>();
    
    public ProductService() {
        // 从文件加载商品数据
        loadProductsFromFile();
    }
    
    private void initializeDefaultProducts() {
        // 可以在这里添加一些默认商品用于测试
    }
    
    // 创建商品
    public boolean createProduct(String productId, String name, String description, double price, String ownerId) {
        if (products.containsKey(productId)) {
            return false; // 商品ID已存在
        }
        
        Product product = new Product(productId, name, description, price, ownerId);
        products.put(productId, product);
        
        // 添加到用户商品列表
        userProducts.computeIfAbsent(ownerId, k -> new ArrayList<>()).add(productId);
        
        // 保存到文件
        saveProductsToFile();
        return true;
    }
    
    // 发布商品 - 修改为只有商品所有者才能发布，且未被管理员封禁的商品才能发布
    public boolean publishProduct(String productId, String ownerId) {
        Product product = products.get(productId);
        if (product != null && !product.isPublished() && product.getOwnerId().equals(ownerId) && !product.isBannedByAdmin()) {
            product.setPublished(true);
            publishedProducts.computeIfAbsent("published", k -> new ArrayList<>()).add(productId);
            saveProductsToFile();
            return true;
        }
        return false;
    }
    
    // 下架商品
    public boolean unpublishProduct(String productId, String ownerId) {
        Product product = products.get(productId);
        if (product != null && product.isPublished() && product.getOwnerId().equals(ownerId)) {
            product.setPublished(false);
            List<String> publishedList = publishedProducts.get("published");
            if (publishedList != null) {
                publishedList.remove(productId);
            }
            saveProductsToFile();
            return true;
        }
        return false;
    }
    
    // 管理员封禁商品
    public boolean banProductByAdmin(String productId) {
        Product product = products.get(productId);
        if (product != null) {
            product.setBannedByAdmin(true);
            // 如果商品已发布，则同时下架
            if (product.isPublished()) {
                product.setPublished(false);
                List<String> publishedList = publishedProducts.get("published");
                if (publishedList != null) {
                    publishedList.remove(productId);
                }
            }
            saveProductsToFile();
            return true;
        }
        return false;
    }
    
    // 管理员解禁商品
    public boolean unbanProductByAdmin(String productId) {
        Product product = products.get(productId);
        if (product != null && product.isBannedByAdmin()) {
            product.setBannedByAdmin(false);
            saveProductsToFile();
            return true;
        }
        return false;
    }
    
    // 删除商品
    public boolean deleteProduct(String productId, String ownerId) {
        Product product = products.get(productId);
        if (product != null && product.getOwnerId().equals(ownerId)) {
            products.remove(productId);
            
            // 从用户商品列表中移除
            List<String> userProductList = userProducts.get(product.getOwnerId());
            if (userProductList != null) {
                userProductList.remove(productId);
            }
            
            // 从发布列表中移除
            List<String> publishedList = publishedProducts.get("published");
            if (publishedList != null) {
                publishedList.remove(productId);
            }
            saveProductsToFile();
            return true;
        }
        return false;
    }
    
    // 根据商品ID查找商品
    public Product findProductById(String productId) {
        return products.get(productId);
    }
    
    // 根据名称搜索商品 - 修改为只搜索已发布的商品
    public List<Product> searchProductsByName(String name) {
        List<Product> result = new ArrayList<>();
        // 只搜索已发布的商品
        List<String> publishedIds = publishedProducts.get("published");
        if (publishedIds != null) {
            for (String productId : publishedIds) {
                Product product = products.get(productId);
                // 确保商品存在且已发布且未被管理员封禁
                if (product != null && product.isPublished() && !product.isBannedByAdmin() &&
                    product.getName().toLowerCase().contains(name.toLowerCase())) {
                    result.add(product);
                }
            }
        }
        return result;
    }
    
    // 获取用户的所有商品
    public List<Product> getUserProducts(String ownerId) {
        List<Product> result = new ArrayList<>();
        List<String> productIds = userProducts.get(ownerId);
        if (productIds != null) {
            for (String productId : productIds) {
                Product product = products.get(productId);
                if (product != null) {
                    result.add(product);
                }
            }
        }
        return result;
    }
    
    // 获取所有已发布的商品
    public List<Product> getPublishedProducts() {
        List<Product> result = new ArrayList<>();
        List<String> publishedIds = publishedProducts.get("published");
        if (publishedIds != null) {
            for (String productId : publishedIds) {
                Product product = products.get(productId);
                if (product != null && product.isPublished() && !product.isBannedByAdmin()) {
                    result.add(product);
                }
            }
        }
        return result;
    }
    
    // 保存商品数据到文件
    private void saveProductsToFile() {
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            System.err.println("创建资源目录失败: " + e.getMessage());
        }

        try {
            Map<String, Object> data = new HashMap<>();
            data.put("products", products);
            data.put("userProducts", userProducts);
            data.put("publishedProducts", publishedProducts);
            
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(PRODUCT_FILE))) {
                oos.writeObject(data);
            }
        } catch (IOException e) {
            System.err.println("保存商品数据失败: " + e.getMessage());
        }
    }
    
    // 从文件加载商品数据
    private void loadProductsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(PRODUCT_FILE))) {
            Map<String, Object> data = (Map<String, Object>) ois.readObject();
            products = (Map<String, Product>) data.get("products");
            userProducts = (Map<String, List<String>>) data.get("userProducts");
            publishedProducts = (Map<String, List<String>>) data.get("publishedProducts");
        } catch (IOException | ClassNotFoundException e) {
            // 文件不存在或损坏
            System.out.println("商品数据文件不存在或损坏");
        }
    }
}