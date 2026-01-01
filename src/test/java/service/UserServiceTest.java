package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.AdminUser;
import main.RegularUser;
import main.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService userService;
    private static final Path DATA_DIR = Paths.get("src", "main", "resources");

    @BeforeEach
    void setUp() throws IOException {
        // Clean persisted files so each test starts from a known empty state
        Files.deleteIfExists(DATA_DIR.resolve("users.dat"));
        Files.deleteIfExists(DATA_DIR.resolve("products.dat"));
        Files.deleteIfExists(DATA_DIR.resolve("contact_exchanges.dat"));
        userService = new UserService();
    }

    @Test
    void registerUserCreatesRegularUser() {
        boolean registered = userService.registerUser("u100", "alice", "pwd", "regular");
        User user = userService.findUserById("u100");

        assertTrue(registered);
        assertTrue(user instanceof RegularUser);
        assertEquals("alice", user.getUsername());
    }

    @Test
    void registerUserCreatesAdminUser() {
        boolean registered = userService.registerUser("a100", "admin", "pwd", "admin");
        User user = userService.findUserById("a100");

        assertTrue(registered);
        assertTrue(user instanceof AdminUser);
    }

    @Test
    void registerUserFailsWhenIdExists() {
        userService.registerUser("u100", "alice", "pwd", "regular");
        boolean second = userService.registerUser("u100", "bob", "pwd2", "regular");

        assertFalse(second);
    }

    @Test
    void registerUserFailsForUnknownType() {
        boolean registered = userService.registerUser("u101", "ghost", "pwd", "guest");
        assertFalse(registered);
    }

    @Test
    void loginSucceedsForValidCredentials() {
        User user = userService.login("user001", "password123");
        assertNotNull(user);
        assertEquals("regularUser", user.getUsername());
    }

    @Test
    void loginFailsForWrongPassword() {
        User user = userService.login("user001", "wrong");
        assertNull(user);
    }

    @Test
    void loginFailsWhenUserIsBanned() {
        userService.registerUser("u200", "tom", "pwd", "regular");
        assertTrue(userService.banUser("u200"));

        assertNull(userService.login("u200", "pwd"));
    }

    @Test
    void updateUserChangesContactInfo() {
        userService.registerUser("u300", "eve", "pwd", "regular");
        boolean updated = userService.updateUser("u300", "eva", "eva@mail.com", "123456");
        User user = userService.findUserById("u300");

        assertTrue(updated);
        assertEquals("eva", user.getUsername());
        assertEquals("eva@mail.com", user.getEmail());
        assertEquals("123456", user.getPhoneNumber());
    }

    @Test
    void banAndUnbanUserToggleFlag() {
        userService.registerUser("u400", "nick", "pwd", "regular");

        assertTrue(userService.banUser("u400"));
        assertTrue(userService.findUserById("u400").isBanned());

        assertTrue(userService.unbanUser("u400"));
        assertFalse(userService.findUserById("u400").isBanned());
    }

    @Test
    void deleteUserRemovesFromAllCollections() {
        userService.registerUser("u500", "kate", "pwd", "regular");
        assertTrue(userService.deleteUser("u500"));
        assertNull(userService.findUserById("u500"));
    }

    @Test
    void getAllUsersIncludesDefaultsAndNewOnes() {
        int baseSize = userService.getAllUsers().size();
        userService.registerUser("u600", "mia", "pwd", "regular");
        List<User> users = userService.getAllUsers();

        assertEquals(baseSize + 1, users.size());
        assertTrue(users.stream().anyMatch(u -> "u600".equals(u.getUserId())));
    }
}
