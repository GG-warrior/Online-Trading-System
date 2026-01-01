package src.main.java.main;
import javax.swing.*;
import java.awt.*;
import java.util.List;

import src.main.java.service.ContactExchangeService;
import src.main.java.service.ProductService;
import src.main.java.service.UserService;

public class TradingSystemGUI extends JFrame {
    private UserService userService = new UserService();
    private ProductService productService = new ProductService();
    private ContactExchangeService contactExchangeService = new ContactExchangeService();
    
    private User currentUser = null;
    
    // 主面板和卡片布局
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    // 登录面板组件
    private JTextField loginUserIdField;
    private JPasswordField loginPasswordField;
    
    // 注册面板组件
    private JTextField registerUserIdField;
    private JTextField registerUsernameField;
    private JPasswordField registerPasswordField;
    private JComboBox<String> userTypeComboBox;
    
    // 输出区域
    private JTextArea outputArea;
    private JScrollPane outputScrollPane;
    
    public TradingSystemGUI() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setTitle("在线交易系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void initializeComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // 初始化登录面板组件
        loginUserIdField = new JTextField(15);
        loginPasswordField = new JPasswordField(15);
        
        // 初始化注册面板组件
        registerUserIdField = new JTextField(15);
        registerUsernameField = new JTextField(15);
        registerPasswordField = new JPasswordField(15);
        userTypeComboBox = new JComboBox<>(new String[]{"regular", "admin"});
        
        // 初始化输出区域
        outputArea = new JTextArea(15, 50);
        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        outputScrollPane = new JScrollPane(outputArea);
    }
    
    private void setupLayout() {
        // 登录面板
        JPanel loginPanel = createLoginPanel();
        
        // 注册面板
        JPanel registerPanel = createRegisterPanel();
        
        // 主菜单面板
        JPanel mainMenuPanel = createMainMenuPanel();
        
        // 添加面板到主面板
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(registerPanel, "REGISTER");
        mainPanel.add(mainMenuPanel, "MAIN_MENU");
        
        add(mainPanel);
        
        // 初始显示登录面板
        cardLayout.show(mainPanel, "LOGIN");
    }
    
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(new JLabel("欢迎使用在线交易系统"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(new JLabel("用户ID:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(loginUserIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("密码:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(loginPasswordField, gbc);
        
        JButton loginButton = new JButton("登录");
        JButton goToRegisterButton = new JButton("注册新账户");
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(loginButton, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(goToRegisterButton, gbc);
        
        // 添加事件监听器
        loginButton.addActionListener(e -> performLogin());
        goToRegisterButton.addActionListener(e -> cardLayout.show(mainPanel, "REGISTER"));
        
        return panel;
    }
    
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(new JLabel("用户注册"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(new JLabel("用户ID:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(registerUserIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("用户名:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(registerUsernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("密码:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(registerPasswordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("用户类型:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(userTypeComboBox, gbc);
        
        JButton registerButton = new JButton("注册");
        JButton backButton = new JButton("返回登录");
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(registerButton, gbc);
        
        gbc.gridx = 1; gbc.gridy = 5;
        panel.add(backButton, gbc);
        
        // 添加事件监听器
        registerButton.addActionListener(e -> performRegister());
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
        
        return panel;
    }
    
    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // 顶部面板 - 显示当前用户信息
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel userInfoLabel = new JLabel("当前用户: ");
        topPanel.add(userInfoLabel);
        
        JButton logoutButton = new JButton("退出登录");
        topPanel.add(logoutButton);
        
        // 中间面板 - 功能按钮（将根据用户类型动态添加）
        JPanel centerPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        centerPanel.setName("centerPanel"); // 为面板设置名称以便后续访问
        
        // 底部面板 - 输出区域
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(new JLabel("系统输出:"), BorderLayout.NORTH);
        bottomPanel.add(outputScrollPane, BorderLayout.CENTER);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        // 添加事件监听器
        logoutButton.addActionListener(e -> performLogout());
        
        return panel;
    }
    
    private void setupEventHandlers() {
        // 登录框按回车键触发登录
        loginPasswordField.addActionListener(e -> performLogin());
        
        // 注册框按回车键触发注册
        registerPasswordField.addActionListener(e -> performRegister());
    }
    
    private void performLogin() {
        String userId = loginUserIdField.getText().trim();
        String password = new String(loginPasswordField.getPassword());
        
        if (userId.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写完整的登录信息", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        User user = userService.login(userId, password);
        if (user != null) {
            currentUser = user;
            outputArea.append("登录成功！欢迎 " + user.getUsername() + "\n");
            
            // 更新界面显示
            cardLayout.show(mainPanel, "MAIN_MENU");
            updateMainMenuUI();
        } else {
            JOptionPane.showMessageDialog(this, "登录失败，请检查用户ID和密码", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void performRegister() {
        String userId = registerUserIdField.getText().trim();
        String username = registerUsernameField.getText().trim();
        String password = new String(registerPasswordField.getPassword());
        String userType = (String) userTypeComboBox.getSelectedItem();
        
        if (userId.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写完整的注册信息", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean success = userService.registerUser(userId, username, password, userType);
        if (success) {
            JOptionPane.showMessageDialog(this, "注册成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(mainPanel, "LOGIN");
            // 清空注册表单
            registerUserIdField.setText("");
            registerUsernameField.setText("");
            registerPasswordField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "注册失败，用户ID已存在", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void performLogout() {
        currentUser = null;
        outputArea.append("已退出登录\n");
        cardLayout.show(mainPanel, "LOGIN");
        // 清空登录表单
        loginUserIdField.setText("");
        loginPasswordField.setText("");
    }
    
    private void updateMainMenuUI() {
        if (currentUser == null) return;
        
        // 获取主菜单面板和中心面板
        JPanel mainMenuPanel = (JPanel) mainPanel.getComponent(2); // MAIN_MENU面板是第三个组件
        JPanel centerPanel = null;
        for (Component comp : mainMenuPanel.getComponents()) {
            if (comp instanceof JPanel && "centerPanel".equals(((JPanel) comp).getName())) {
                centerPanel = (JPanel) comp;
                break;
            }
        }
        
        if (centerPanel == null) return;
        
        // 清空之前的按钮
        centerPanel.removeAll();
        
        // 添加通用按钮
        JButton viewProfileButton = new JButton("查看个人信息");
        JButton updateProfileButton = new JButton("更新个人信息");
        JButton searchProductsButton = new JButton("搜索商品");
        JButton searchUsersButton = new JButton("搜索用户");
        JButton viewContactHistoryButton = new JButton("查看联系方式交换记录");
        
        viewProfileButton.addActionListener(e -> viewProfile());
        updateProfileButton.addActionListener(e -> showUpdateProfileDialog());
        searchProductsButton.addActionListener(e -> showSearchProductsDialog());
        searchUsersButton.addActionListener(e -> showSearchUsersDialog());
        viewContactHistoryButton.addActionListener(e -> viewContactExchangeHistory());
        
        centerPanel.add(viewProfileButton);
        centerPanel.add(updateProfileButton);
        centerPanel.add(searchProductsButton);
        centerPanel.add(searchUsersButton);
        centerPanel.add(viewContactHistoryButton);
        
        // 根据用户类型添加特定按钮
        if (currentUser instanceof RegularUser) {
            // 普通用户按钮
            JButton createProductButton = new JButton("创建商品");
            JButton publishProductButton = new JButton("发布商品");
            JButton unpublishProductButton = new JButton("下架商品");
            JButton viewMyProductsButton = new JButton("查看我的商品");
            JButton getSellerContactButton = new JButton("获取卖家联系方式");
            
            createProductButton.addActionListener(e -> showCreateProductDialog());
            publishProductButton.addActionListener(e -> showPublishProductDialog());
            unpublishProductButton.addActionListener(e -> showUnpublishProductDialog());
            viewMyProductsButton.addActionListener(e -> viewMyProducts());
            getSellerContactButton.addActionListener(e -> showGetSellerContactDialog());
            
            centerPanel.add(createProductButton);
            centerPanel.add(publishProductButton);
            centerPanel.add(unpublishProductButton);
            centerPanel.add(viewMyProductsButton);
            centerPanel.add(getSellerContactButton);
        } else if (currentUser instanceof AdminUser) {
            // 管理员按钮
            JButton manageUsersButton = new JButton("管理用户");
            JButton manageProductsButton = new JButton("管理商品");
            JButton viewAllUsersButton = new JButton("查看所有用户");
            JButton viewAllProductsButton = new JButton("查看所有商品");
            
            manageUsersButton.addActionListener(e -> showManageUsersDialog());
            manageProductsButton.addActionListener(e -> showManageProductsDialog());
            viewAllUsersButton.addActionListener(e -> viewAllUsers());
            viewAllProductsButton.addActionListener(e -> viewAllProducts());
            
            centerPanel.add(manageUsersButton);
            centerPanel.add(manageProductsButton);
            centerPanel.add(viewAllUsersButton);
            centerPanel.add(viewAllProductsButton);
        }
        
        // 更新顶部用户信息标签
        for (Component comp : mainMenuPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel topPanel = (JPanel) comp;
                for (Component topComp : topPanel.getComponents()) {
                    if (topComp instanceof JLabel) {
                        JLabel label = (JLabel) topComp;
                        if (label.getText().startsWith("当前用户:")) {
                            String userType = currentUser instanceof AdminUser ? "管理员" : "普通用户";
                            label.setText("当前用户: " + currentUser.getUsername() + " (" + userType + ")");
                            break;
                        }
                    }
                }
                break;
            }
        }
        
        // 重新验证和重绘面板
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    
    private void viewProfile() {
        if (currentUser == null) return;
        
        outputArea.append("=== 个人信息 ===\n");
        outputArea.append(currentUser.toString() + "\n");
    }
    
    private void showUpdateProfileDialog() {
        if (currentUser == null) return;
        
        JTextField emailField = new JTextField(currentUser.getEmail() != null ? currentUser.getEmail() : "");
        JTextField phoneField = new JTextField(currentUser.getPhoneNumber() != null ? currentUser.getPhoneNumber() : "");
        
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("邮箱:"));
        panel.add(emailField);
        panel.add(new JLabel("电话:"));
        panel.add(phoneField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "更新个人信息", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            
            boolean success = userService.updateUser(currentUser.getUserId(), currentUser.getUsername(), email, phone);
            if (success) {
                currentUser.setEmail(email);
                currentUser.setPhoneNumber(phone);
                outputArea.append("个人信息更新成功\n");
            } else {
                outputArea.append("个人信息更新失败\n");
            }
        }
    }
    
    private void showCreateProductDialog() {
        if (!(currentUser instanceof RegularUser)) {
            outputArea.append("只有普通用户可以创建商品\n");
            return;
        }
        
        JTextField productIdField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField priceField = new JTextField();
        
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("商品ID:"));
        panel.add(productIdField);
        panel.add(new JLabel("商品名称:"));
        panel.add(nameField);
        panel.add(new JLabel("描述:"));
        panel.add(descriptionField);
        panel.add(new JLabel("价格:"));
        panel.add(priceField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "创建商品", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String productId = productIdField.getText().trim();
            String name = nameField.getText().trim();
            String description = descriptionField.getText().trim();
            String priceStr = priceField.getText().trim();
            
            if (productId.isEmpty() || name.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写完整的商品信息", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                double price = Double.parseDouble(priceStr);
                boolean success = productService.createProduct(productId, name, description, price, currentUser.getUserId());
                if (success) {
                    outputArea.append("商品创建成功\n");
                } else {
                    outputArea.append("商品创建失败，商品ID已存在\n");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "价格必须是有效的数字", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showPublishProductDialog() {
        if (!(currentUser instanceof RegularUser)) {
            outputArea.append("只有普通用户可以发布商品\n");
            return;
        }
        
        JTextField productIdField = new JTextField();
        
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("请输入要发布的商品ID:"));
        panel.add(productIdField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "发布商品", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String productId = productIdField.getText().trim();
            
            if (productId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入商品ID", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean success = productService.publishProduct(productId, currentUser.getUserId());
            if (success) {
                outputArea.append("商品发布成功\n");
            } else {
                outputArea.append("商品发布失败，可能原因：商品不存在、不是您的商品、已被管理员封禁或已发布\n");
            }
        }
    }
    
    private void showUnpublishProductDialog() {
        if (!(currentUser instanceof RegularUser)) {
            outputArea.append("只有普通用户可以下架商品\n");
            return;
        }
        
        JTextField productIdField = new JTextField();
        
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("请输入要下架的商品ID:"));
        panel.add(productIdField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "下架商品", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String productId = productIdField.getText().trim();
            
            if (productId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入商品ID", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean success = productService.unpublishProduct(productId, currentUser.getUserId());
            if (success) {
                outputArea.append("商品下架成功\n");
            } else {
                outputArea.append("商品下架失败，可能原因：商品不存在、不是您的商品或未发布\n");
            }
        }
    }
    
    private void viewMyProducts() {
        if (!(currentUser instanceof RegularUser)) {
            outputArea.append("只有普通用户可以查看自己的商品\n");
            return;
        }
        
        List<Product> products = productService.getUserProducts(currentUser.getUserId());
        outputArea.append("=== 我的商品 ===\n");
        if (products.isEmpty()) {
            outputArea.append("暂无商品\n");
        } else {
            for (Product product : products) {
                outputArea.append(product.toString() + "\n");
            }
        }
    }
    
    private void showSearchProductsDialog() {
        JTextField keywordField = new JTextField();
        
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("请输入搜索关键字:"));
        panel.add(keywordField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "搜索商品", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String keyword = keywordField.getText().trim();
            
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入搜索关键字", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<Product> products = productService.searchProductsByName(keyword);
            outputArea.append("=== 搜索结果 ===\n");
            if (products.isEmpty()) {
                outputArea.append("未找到匹配的商品\n");
            } else {
                for (Product product : products) {
                    outputArea.append(product.toString() + "\n");
                }
            }
        }
    }
    
    private void showGetSellerContactDialog() {
        JTextField productIdField = new JTextField();
        
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("请输入商品ID以获取卖家联系方式:"));
        panel.add(productIdField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "获取卖家联系方式", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String productId = productIdField.getText().trim();
            
            if (productId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入商品ID", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Product product = productService.findProductById(productId);
            if (product != null && product.isPublished() && !product.isBannedByAdmin()) {
                User seller = userService.findUserById(product.getOwnerId());
                if (seller != null) {
                    // 记录联系方式查看
                    String recordId = "record_" + System.currentTimeMillis();
                    contactExchangeService.recordContactExchange(recordId, currentUser.getUserId(), productId, product.getOwnerId());
                    
                    outputArea.append("=== 卖家联系方式 ===\n");
                    outputArea.append("用户名: " + seller.getUsername() + "\n");
                    outputArea.append("邮箱: " + seller.getEmail() + "\n");
                    outputArea.append("电话: " + seller.getPhoneNumber() + "\n");
                } else {
                    outputArea.append("未找到卖家信息\n");
                }
            } else {
                outputArea.append("无法获取联系方式，商品可能不存在、未发布或已被管理员封禁\n");
            }
        }
    }
    
    private void showSearchUsersDialog() {
        JTextField keywordField = new JTextField();
        
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("请输入搜索关键字(用户名):"));
        panel.add(keywordField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "搜索用户", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String keyword = keywordField.getText().trim();
            
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入搜索关键字", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<User> users = userService.getAllUsers();
            outputArea.append("=== 搜索结果 ===\n");
            boolean found = false;
            for (User user : users) {
                if (user.getUsername().toLowerCase().contains(keyword.toLowerCase())) {
                    outputArea.append(user.toString() + "\n");
                    found = true;
                }
            }
            if (!found) {
                outputArea.append("未找到匹配的用户\n");
            }
        }
    }
    
    private void viewContactExchangeHistory() {
        List<ContactExchangeRecord> records = contactExchangeService.getUserExchangeRecords(currentUser.getUserId());
        outputArea.append("=== 联系方式交换记录 ===\n");
        if (records.isEmpty()) {
            outputArea.append("暂无记录\n");
        } else {
            for (ContactExchangeRecord record : records) {
                outputArea.append(record.toString() + "\n");
            }
        }
    }
    
    private void showManageUsersDialog() {
        if (!(currentUser instanceof AdminUser)) {
            outputArea.append("只有管理员可以管理用户\n");
            return;
        }
        
        JTextField userIdField = new JTextField();
        JTextField actionField = new JTextField();
        
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("用户ID:"));
        panel.add(userIdField);
        panel.add(new JLabel("操作(ban/unban):"));
        panel.add(actionField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "管理用户", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String userId = userIdField.getText().trim();
            String action = actionField.getText().trim().toLowerCase();
            
            if (userId.isEmpty() || action.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写完整的用户管理信息", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean success = false;
            switch (action) {
                case "ban":
                    success = userService.banUser(userId);
                    if (success) {
                        outputArea.append("用户封禁成功\n");
                    } else {
                        outputArea.append("用户封禁失败，用户不存在\n");
                    }
                    break;
                case "unban":
                    success = userService.unbanUser(userId);
                    if (success) {
                        outputArea.append("用户解封成功\n");
                    } else {
                        outputArea.append("用户解封失败，用户不存在\n");
                    }
                    break;
                default:
                    outputArea.append("无效操作，请输入ban或unban\n");
            }
        }
    }
    
    private void showManageProductsDialog() {
        if (!(currentUser instanceof AdminUser)) {
            outputArea.append("只有管理员可以管理商品\n");
            return;
        }
        
        JTextField productIdField = new JTextField();
        JTextField actionField = new JTextField();
        
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("商品ID:"));
        panel.add(productIdField);
        panel.add(new JLabel("操作(ban/unban):"));
        panel.add(actionField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "管理商品", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String productId = productIdField.getText().trim();
            String action = actionField.getText().trim().toLowerCase();
            
            if (productId.isEmpty() || action.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写完整的商品管理信息", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean success = false;
            switch (action) {
                case "ban":
                    success = productService.banProductByAdmin(productId);
                    if (success) {
                        outputArea.append("商品封禁成功\n");
                    } else {
                        outputArea.append("商品封禁失败，商品不存在\n");
                    }
                    break;
                case "unban":
                    success = productService.unbanProductByAdmin(productId);
                    if (success) {
                        outputArea.append("商品解封成功\n");
                    } else {
                        outputArea.append("商品解封失败，商品不存在或未被封禁\n");
                    }
                    break;
                default:
                    outputArea.append("无效操作，请输入ban或unban\n");
            }
        }
    }
    
    private void viewAllUsers() {
        if (!(currentUser instanceof AdminUser)) {
            outputArea.append("只有管理员可以查看所有用户\n");
            return;
        }
        
        List<User> users = userService.getAllUsers();
        outputArea.append("=== 所有用户 ===\n");
        for (User user : users) {
            outputArea.append(user.toString() + "\n");
        }
    }
    
    private void viewAllProducts() {
        List<Product> products = productService.getPublishedProducts();
        outputArea.append("=== 所有已发布的商品 ===\n");
        if (products.isEmpty()) {
            outputArea.append("暂无已发布的商品\n");
        } else {
            for (Product product : products) {
                outputArea.append(product.toString() + "\n");
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new TradingSystemGUI();
        });
    }
}