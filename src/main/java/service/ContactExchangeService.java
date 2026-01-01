package src.main.java.service;

import java.util.*;
import java.nio.file.*;

import src.main.java.main.ContactExchangeRecord;

import java.io.*;

public class ContactExchangeService implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Path DATA_DIR = Paths.get("src", "main", "resources");
    private static final Path EXCHANGE_RECORD_FILE = DATA_DIR.resolve("contact_exchanges.dat");
    
    // 存储联系方式交换记录
    private Map<String, ContactExchangeRecord> exchangeRecords = new HashMap<>();
    private Map<String, List<String>> userExchangeRecords = new HashMap<>(); // 用户ID -> 记录ID列表
    
    public ContactExchangeService() {
        // 从文件加载交换记录数据
        loadExchangeRecordsFromFile();
    }
    
    // 记录联系方式查看
    public boolean recordContactExchange(String recordId, String requesterId, String productId, String ownerId) {
        if (exchangeRecords.containsKey(recordId)) {
            return false; // 记录ID已存在
        }
        
        ContactExchangeRecord record = new ContactExchangeRecord(recordId, requesterId, productId, ownerId);
        exchangeRecords.put(recordId, record);
        
        // 添加到请求者记录列表
        userExchangeRecords.computeIfAbsent(requesterId, k -> new ArrayList<>()).add(recordId);
        
        // 也添加到商品所有者的记录列表
        userExchangeRecords.computeIfAbsent(ownerId, k -> new ArrayList<>()).add(recordId);
        
        // 保存到文件
        saveExchangeRecordsToFile();
        
        System.out.println("用户 " + requesterId + " 查看了商品 " + productId + " 的联系方式");
        return true;
    }
    
    // 获取用户的联系方式交换记录
    public List<ContactExchangeRecord> getUserExchangeRecords(String userId) {
        List<ContactExchangeRecord> result = new ArrayList<>();
        List<String> recordIds = userExchangeRecords.get(userId);
        if (recordIds != null) {
            for (String recordId : recordIds) {
                ContactExchangeRecord record = exchangeRecords.get(recordId);
                if (record != null) {
                    result.add(record);
                }
            }
        }
        return result;
    }
    
    // 根据记录ID查找交换记录
    public ContactExchangeRecord findExchangeRecordById(String recordId) {
        return exchangeRecords.get(recordId);
    }
    
    // 获取所有交换记录
    public List<ContactExchangeRecord> getAllExchangeRecords() {
        return new ArrayList<>(exchangeRecords.values());
    }
    
    // 保存交换记录数据到文件
    private void saveExchangeRecordsToFile() {
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            System.err.println("创建资源目录失败: " + e.getMessage());
        }

        try {
            Map<String, Object> data = new HashMap<>();
            data.put("exchangeRecords", exchangeRecords);
            data.put("userExchangeRecords", userExchangeRecords);
            
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(EXCHANGE_RECORD_FILE))) {
                oos.writeObject(data);
            }
        } catch (IOException e) {
            System.err.println("保存联系方式交换记录失败: " + e.getMessage());
        }
    }
    
    // 从文件加载交换记录数据
    private void loadExchangeRecordsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(EXCHANGE_RECORD_FILE))) {
            Map<String, Object> data = (Map<String, Object>) ois.readObject();
            exchangeRecords = (Map<String, ContactExchangeRecord>) data.get("exchangeRecords");
            userExchangeRecords = (Map<String, List<String>>) data.get("userExchangeRecords");
        } catch (IOException | ClassNotFoundException e) {
            // 文件不存在或损坏
            System.out.println("联系方式交换记录文件不存在或损坏");
        }
    }
}