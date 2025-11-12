package main;

import service.UserService;
import service.ProductService;
import service.TransactionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    private static UserService userService = new UserService();
    private static ProductService productService = new ProductService();
    private static TransactionService transactionService = new TransactionService();
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;
    
    public static void main(String[] args) {
        System.out.println("欢迎使用在线交易系统！");
        
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }
    
    private static void showLoginMenu() {
        System.out.println("\n=== 登录菜单 ===");
        System.out.println("1. 登录");
        System.out.println("2. 注册");
        System.out.println("3. 退出");
        System.out.print("请选择操作: ");
        
        int choice = -1;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // 消费换行符
        } catch (InputMismatchException e) {
            scanner.nextLine(); // 清除无效输入
            System.out.println("输入无效，请输入数字选项");
            return;
        }
        
        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 3:
                System.out.println("感谢使用在线交易系统！");
                System.exit(0);
                break;
            default:
                System.out.println("无效选择，请重新输入");
        }
    }
    
    private static void showMainMenu() {
        System.out.println("\n=== 主菜单 ===");
        System.out.println("当前用户: " + currentUser.getUsername());
        System.out.println("1. 查看个人信息");
        System.out.println("2. 更新个人信息");
        
        if (currentUser instanceof RegularUser) {
            showRegularUserMenu();
        } else if (currentUser instanceof AdminUser) {
            showAdminUserMenu();
        }
        
        System.out.println("0. 退出登录");
        System.out.print("请选择操作: ");
        
        int choice = -1;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // 消费换行符
        } catch (InputMismatchException e) {
            scanner.nextLine(); // 清除无效输入
            System.out.println("输入无效，请输入数字选项");
            return;
        }
        
        handleMainMenuChoice(choice);
    }
    
    private static void showRegularUserMenu() {
        System.out.println("3. 创建商品");
        System.out.println("4. 发布商品");
        System.out.println("5. 查看我的商品");
        System.out.println("6. 搜索商品");
        System.out.println("7. 获取卖家联系方式");
        System.out.println("8. 搜索用户");
        System.out.println("9. 查看交易记录");
    }
    
    private static void showAdminUserMenu() {
        System.out.println("3. 管理用户");
        System.out.println("4. 管理商品");
        System.out.println("5. 查看系统报告");
        System.out.println("6. 查看所有用户");
        System.out.println("7. 查看所有商品");
    }
    
    private static void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 0:
                currentUser = null;
                System.out.println("已退出登录");
                break;
            case 1:
                viewProfile();
                break;
            case 2:
                updateProfile();
                break;
            default:
                if (currentUser instanceof RegularUser) {
                    handleRegularUserChoice(choice);
                } else if (currentUser instanceof AdminUser) {
                    handleAdminUserChoice(choice);
                }
        }
    }
    
    private static void handleRegularUserChoice(int choice) {
        switch (choice) {
            case 3:
                createProduct();
                break;
            case 4:
                publishProduct();
                break;
            case 5:
                viewMyProducts();
                break;
            case 6:
                searchProducts();
                break;
            case 7:
                getSellerContactInfo();
                break;
            case 8:
                searchUsers();
                break;
            case 9:
                viewTransactionHistory();
                break;
            default:
                System.out.println("无效选择");
        }
    }
    
    private static void handleAdminUserChoice(int choice) {
        AdminUser admin = (AdminUser) currentUser;
        switch (choice) {
            case 3:
                System.out.print("请输入用户ID: ");
                String userId = scanner.nextLine();
                System.out.print("请输入操作 (manage/delete/ban/unban): ");
                String action = scanner.nextLine();
                
                switch (action.toLowerCase()) {
                    case "manage":
                        admin.manageUser(userId, action);
                        break;
                    case "delete":
                        if (userService.deleteUser(userId)) {
                            System.out.println("用户删除成功");
                        } else {
                            System.out.println("用户删除失败，用户不存在");
                        }
                        break;
                    case "ban":
                        if (userService.banUser(userId)) {
                            System.out.println("用户封禁成功");
                        } else {
                            System.out.println("用户封禁失败，用户不存在");
                        }
                        break;
                    case "unban":
                        if (userService.unbanUser(userId)) {
                            System.out.println("用户解封成功");
                        } else {
                            System.out.println("用户解封失败，用户不存在");
                        }
                        break;
                    default:
                        System.out.println("无效操作");
                }
                break;
            case 4:
                System.out.print("请输入商品ID: ");
                String productId = scanner.nextLine();
                System.out.print("请输入操作 (manage/delete): ");
                String productAction = scanner.nextLine();
                admin.manageProduct(productId, productAction);
                break;
            case 5:
                admin.viewSystemReport();
                break;
            case 6:
                viewAllUsers();
                break;
            case 7:
                viewAllProducts();
                break;
            default:
                System.out.println("无效选择");
        }
    }
    
    private static void login() {
        System.out.print("请输入用户ID: ");
        String userId = scanner.nextLine();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();
        
        currentUser = userService.login(userId, password);
        if (currentUser != null) {
            System.out.println("登录成功！欢迎 " + currentUser.getUsername());
        } else {
            System.out.println("登录失败，请检查用户ID和密码");
        }
    }
    
    private static void register() {
        System.out.print("请输入用户ID: ");
        String userId = scanner.nextLine();
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();
        System.out.print("请输入用户类型 (regular/admin): ");
        String userType = scanner.nextLine();
        
        if (userService.registerUser(userId, username, password, userType)) {
            System.out.println("注册成功！");
        } else {
            System.out.println("注册失败，用户ID可能已存在");
        }
    }
    
    private static void viewProfile() {
        System.out.println("用户信息:");
        System.out.println(currentUser.toString());
    }
    
    private static void updateProfile() {
        System.out.print("请输入新用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入新邮箱: ");
        String email = scanner.nextLine();
        System.out.print("请输入新手机号: ");
        String phone = scanner.nextLine();
        
        if (userService.updateUser(currentUser.getUserId(), username, email, phone)) {
            currentUser.setUsername(username);
            currentUser.setEmail(email);
            currentUser.setPhoneNumber(phone);
            System.out.println("个人信息更新成功！");
        } else {
            System.out.println("更新失败");
        }
    }
    
    private static void createProduct() {
        System.out.print("请输入商品ID: ");
        String productId = scanner.nextLine();
        System.out.print("请输入商品名称: ");
        String name = scanner.nextLine();
        System.out.print("请输入商品描述: ");
        String description = scanner.nextLine();
        System.out.print("请输入商品价格: ");
        
        double price = 0;
        try {
            price = scanner.nextDouble();
            scanner.nextLine(); // 消费换行符
        } catch (InputMismatchException e) {
            scanner.nextLine(); // 清除无效输入
            System.out.println("价格输入无效，请输入数字");
            return;
        }
        
        RegularUser user = (RegularUser) currentUser;
        if (productService.createProduct(productId, name, description, price, user.getUserId())) {
            System.out.println("商品创建成功！");
        } else {
            System.out.println("商品创建失败，商品ID可能已存在");
        }
    }
    
    private static void publishProduct() {
        System.out.print("请输入要发布的商品ID: ");
        String productId = scanner.nextLine();
        
        if (productService.publishProduct(productId)) {
            System.out.println("商品发布成功！");
        } else {
            System.out.println("商品发布失败，请检查商品ID");
        }
    }
    
    private static void viewMyProducts() {
        RegularUser user = (RegularUser) currentUser;
        List<Product> products = productService.getUserProducts(user.getUserId());
        
        if (products.isEmpty()) {
            System.out.println("您还没有创建任何商品");
        } else {
            System.out.println("您的商品列表:");
            for (Product product : products) {
                System.out.println(product.toString());
            }
        }
    }
    
    private static void searchProducts() {
        System.out.print("请输入商品名称关键词: ");
        String keyword = scanner.nextLine();
        
        List<Product> products = productService.searchProductsByName(keyword);
        if (products.isEmpty()) {
            System.out.println("未找到相关商品");
        } else {
            System.out.println("搜索结果:");
            for (Product product : products) {
                System.out.println(product.toString());
            }
        }
    }
    
    private static void viewTransactionHistory() {
        List<Transaction> transactions = transactionService.getUserTransactions(currentUser.getUserId());
        
        if (transactions.isEmpty()) {
            System.out.println("您还没有任何交易记录");
        } else {
            System.out.println("您的交易记录:");
            for (Transaction transaction : transactions) {
                System.out.println(transaction.toString());
            }
        }
    }

    private static void getSellerContactInfo() {
        System.out.print("请输入商品ID: ");
        String productId = scanner.nextLine();
        
        Product product = productService.findProductById(productId);
        // 检查商品是否存在且已发布
        if (product != null && product.isPublished()) {
            User seller = userService.findUserById(product.getOwnerId());
            if (seller != null) {
                System.out.println("商品信息: " + product.getName());
                System.out.println("卖家联系方式: " + seller.getContactInfo());
            } else {
                System.out.println("未找到卖家信息");
            }
        } else {
            System.out.println("商品不存在或未发布");
        }
    }

    private static void searchUsers() {
        System.out.print("请输入用户名关键词: ");
        String keyword = scanner.nextLine();
        
        List<User> users = userService.getAllUsers();
        List<User> matchedUsers = new ArrayList<>();
        
        for (User user : users) {
            if (user.getUsername().toLowerCase().contains(keyword.toLowerCase())) {
                matchedUsers.add(user);
            }
        }
        
        if (matchedUsers.isEmpty()) {
            System.out.println("未找到匹配的用户");
        } else {
            System.out.println("搜索结果:");
            for (User user : matchedUsers) {
                System.out.println("用户ID: " + user.getUserId() + ", 用户名: " + user.getUsername());
            }
        }
    }

    // 新增方法：查看所有用户
    private static void viewAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("系统中没有用户");
        } else {
            System.out.println("=== 所有用户 ===");
            for (User user : users) {
                System.out.println(user.toString());
            }
        }
    }

    // 新增方法：查看所有商品
    private static void viewAllProducts() {
        // 获取所有商品（包括已发布和未发布的）
        List<Product> allProducts = new ArrayList<>();
        // 通过反射获取所有商品
        try {
            // 这里我们通过ProductService获取所有商品
            List<Product> publishedProducts = productService.getPublishedProducts();
            allProducts.addAll(publishedProducts);
            
            // 获取所有用户创建的商品
            List<User> users = userService.getAllUsers();
            for (User user : users) {
                if (user instanceof RegularUser) {
                    List<Product> userProducts = productService.getUserProducts(user.getUserId());
                    for (Product product : userProducts) {
                        // 避免重复添加已发布的商品
                        if (!allProducts.contains(product)) {
                            allProducts.add(product);
                        }
                    }
                }
            }
            
            if (allProducts.isEmpty()) {
                System.out.println("系统中没有商品");
            } else {
                System.out.println("=== 所有商品 ===");
                for (Product product : allProducts) {
                    System.out.println(product.toString());
                }
            }
        } catch (Exception e) {
            System.out.println("获取商品列表时出错: " + e.getMessage());
        }
    }
}