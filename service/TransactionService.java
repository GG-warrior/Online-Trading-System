package service;

import main.Transaction;
import java.util.*;
import java.io.*;

public class TransactionService implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String TRANSACTION_FILE = "transactions.dat";
    
    // 模拟数据库存储交易记录
    private Map<String, Transaction> transactions = new HashMap<>();
    private Map<String, List<String>> userTransactions = new HashMap<>(); // 用户ID -> 交易ID列表
    
    public TransactionService() {
        // 从文件加载交易数据
        loadTransactionsFromFile();
    }
    
    // 购买商品
    public boolean purchaseProduct(String transactionId, String buyerId, String productId, double amount) {
        if (transactions.containsKey(transactionId)) {
            return false; // 交易ID已存在
        }
        
        Transaction transaction = new Transaction(transactionId, buyerId, productId, amount);
        transactions.put(transactionId, transaction);
        
        // 添加到用户交易列表
        userTransactions.computeIfAbsent(buyerId, k -> new ArrayList<>()).add(transactionId);
        
        // 保存到文件
        saveTransactionsToFile();
        
        System.out.println("用户 " + buyerId + " 成功购买商品 " + productId + "，金额: " + amount);
        return true;
    }
    
    // 获取用户的交易记录
    public List<Transaction> getUserTransactions(String userId) {
        List<Transaction> result = new ArrayList<>();
        List<String> transactionIds = userTransactions.get(userId);
        if (transactionIds != null) {
            for (String transactionId : transactionIds) {
                Transaction transaction = transactions.get(transactionId);
                if (transaction != null) {
                    result.add(transaction);
                }
            }
        }
        return result;
    }
    
    // 根据交易ID查找交易
    public Transaction findTransactionById(String transactionId) {
        return transactions.get(transactionId);
    }
    
    // 获取所有交易记录
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions.values());
    }
    
    // 保存交易数据到文件
    private void saveTransactionsToFile() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("transactions", transactions);
            data.put("userTransactions", userTransactions);
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TRANSACTION_FILE))) {
                oos.writeObject(data);
            }
        } catch (IOException e) {
            System.err.println("保存交易数据失败: " + e.getMessage());
        }
    }
    
    // 从文件加载交易数据
    private void loadTransactionsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TRANSACTION_FILE))) {
            Map<String, Object> data = (Map<String, Object>) ois.readObject();
            transactions = (Map<String, Transaction>) data.get("transactions");
            userTransactions = (Map<String, List<String>>) data.get("userTransactions");
        } catch (IOException | ClassNotFoundException e) {
            // 文件不存在或损坏
            System.out.println("交易数据文件不存在或损坏");
        }
    }
}