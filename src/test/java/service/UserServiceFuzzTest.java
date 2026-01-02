package service;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class UserServiceFuzzTest {
    private static final Path DATA_DIR = Paths.get("src", "main", "resources");

    private static void cleanPersistence() throws IOException {
        Files.deleteIfExists(DATA_DIR.resolve("users.dat"));
        Files.deleteIfExists(DATA_DIR.resolve("products.dat"));
        Files.deleteIfExists(DATA_DIR.resolve("contact_exchanges.dat"));
    }

    private static String sanitize(String raw, String fallback) {
        String cleaned = raw.replaceAll("[^A-Za-z0-9_-]", "");
        return cleaned.isEmpty() ? fallback : cleaned;
    }

    @FuzzTest(maxDuration = "30s")
    void fuzzRegisterAndLogin(FuzzedDataProvider data) throws IOException {
        cleanPersistence();
        UserService service = new UserService();

        String userId = "fuzz-" + sanitize(data.consumeString(12), "id");
        String username = sanitize(data.consumeString(16), "user");
        String password = data.consumeString(16);
        String userType = data.pickValue(new String[]{"regular", "admin", "guest", "", "crash"});

        // 如果选到 "crash"，主动抛出异常，便于快速生成崩溃用例与再现输入
        if ("crash".equals(userType)) {
            throw new RuntimeException("Intentional fuzz crash marker");
        }

        boolean registered = service.registerUser(userId, username, password, userType);
        if (registered) {
            service.login(userId, password);
            service.updateUser(
                    userId,
                    sanitize(data.consumeString(16), username),
                    data.consumeString(24),
                    data.consumeString(12)
            );
            service.banUser(userId);
            service.login(userId, password); // should be blocked when banned
            service.unbanUser(userId);
            service.login(userId, password);
            service.deleteUser(userId);
        } else {
            // Exercise duplicate id or unknown type paths
            service.banUser(userId);
            service.unbanUser(userId);
            service.deleteUser(userId);
        }
    }
}
