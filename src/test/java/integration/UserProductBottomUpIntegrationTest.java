package integration;

import main.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ProductService;
import service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserProductBottomUpIntegrationTest {
    private static final Path USERS_FILE = Paths.get("src", "main", "resources", "users.dat");
    private static final Path PRODUCTS_FILE = Paths.get("src", "main", "resources", "products.dat");

    private UserService userService;
    private ProductService productService;

    @BeforeEach
    void setUp() throws IOException {
        cleanupFiles();
        userService = new UserService();
        productService = new ProductService();
    }

    @AfterEach
    void tearDown() throws IOException {
        cleanupFiles();
    }

    @Test
    void createPublishAndDeleteProductFlow() {
        String userId = "regular-" + UUID.randomUUID();
        String productId = "product-" + UUID.randomUUID();

        assertTrue(userService.registerUser(userId, "buyer", "pwd", "regular"));

        assertTrue(productService.createProduct(productId, "Road Bike", "A fast bike", 1999.0, userId));
        assertTrue(productService.publishProduct(productId, userId));

        List<Product> searchResult = productService.searchProductsByName("bike");
        assertEquals(1, searchResult.size());
        Product found = searchResult.get(0);
        assertEquals(productId, found.getProductId());
        assertTrue(found.isPublished());

        assertTrue(productService.deleteProduct(productId, userId));
        assertTrue(productService.getPublishedProducts().isEmpty());
        assertTrue(productService.getUserProducts(userId).isEmpty());
    }

    private void cleanupFiles() throws IOException {
        Files.deleteIfExists(USERS_FILE);
        Files.deleteIfExists(PRODUCTS_FILE);
    }
}
