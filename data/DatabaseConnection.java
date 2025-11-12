package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:online_trading.db";
    private static Connection connection = null;
    
    // 获取数据库连接
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL);
                System.out.println("数据库连接成功");
            } catch (SQLException e) {
                System.err.println("数据库连接失败: " + e.getMessage());
            }
        }
        return connection;
    }
    
    // 关闭数据库连接
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("数据库连接已关闭");
            } catch (SQLException e) {
                System.err.println("关闭数据库连接时出错: " + e.getMessage());
            }
        }
    }
}