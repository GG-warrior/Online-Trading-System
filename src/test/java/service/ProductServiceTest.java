package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {
    private ProductService productService;
    private static final Path DATA_DIR = Paths.get("src", "main", "resources");

    @BeforeEach
    void setUp() throws IOException {
        // Clean persisted files so each test starts from a known empty state
        Files.deleteIfExists(DATA_DIR.resolve("products.dat"));
        Files.deleteIfExists(DATA_DIR.resolve("users.dat"));
        Files.deleteIfExists(DATA_DIR.resolve("contact_exchanges.dat"));
        productService = new ProductService();
    }

    @Test
    void createProductSucceedsAndPersistsToUserList() {
        boolean created = productService.createProduct("p1", "Phone", "Nice phone", 199.0, "u1");
        List<Product> userProducts = productService.getUserProducts("u1");

        assertTrue(created);
        assertEquals(1, userProducts.size());
        assertEquals("p1", userProducts.get(0).getProductId());
    }

    @Test
    void createProductFailsWhenIdExists() {
        productService.createProduct("p1", "Phone", "Nice phone", 199.0, "u1");
        boolean createdAgain = productService.createProduct("p1", "Phone2", "Another", 99.0, "u1");

        assertFalse(createdAgain);
    }

    @Test
    void publishProductSucceedsOnlyForOwner() {
        productService.createProduct("p1", "Phone", "", 100.0, "owner1");

        assertTrue(productService.publishProduct("p1", "owner1"));
        assertTrue(productService.findProductById("p1").isPublished());
        assertFalse(productService.publishProduct("p1", "other"));
    }

    @Test
    void publishProductFailsWhenBannedByAdmin() {
        productService.createProduct("p1", "Phone", "", 100.0, "owner1");
        assertTrue(productService.banProductByAdmin("p1"));

        assertFalse(productService.publishProduct("p1", "owner1"));
    }

    @Test
    void publishProductFailsWhenAlreadyPublished() {
        productService.createProduct("p1", "Phone", "", 100.0, "owner1");
        assertTrue(productService.publishProduct("p1", "owner1"));

        assertFalse(productService.publishProduct("p1", "owner1"));
    }

    @Test
    void unpublishProductSucceedsForOwner() {
        productService.createProduct("p1", "Phone", "", 100.0, "owner1");
        productService.publishProduct("p1", "owner1");

        assertTrue(productService.unpublishProduct("p1", "owner1"));
        assertFalse(productService.findProductById("p1").isPublished());
    }

    @Test
    void unpublishProductFailsForNonOwnerOrUnpublished() {
        productService.createProduct("p1", "Phone", "", 100.0, "owner1");

        assertFalse(productService.unpublishProduct("p1", "owner2"));
        assertFalse(productService.unpublishProduct("p1", "owner1"));
    }

    @Test
    void banProductByAdminMarksBannedAndRemovesFromPublishedList() {
        productService.createProduct("p1", "Phone", "", 100.0, "owner1");
        productService.publishProduct("p1", "owner1");

        assertTrue(productService.banProductByAdmin("p1"));
        Product product = productService.findProductById("p1");
        assertTrue(product.isBannedByAdmin());
        assertFalse(product.isPublished());
        assertTrue(productService.getPublishedProducts().isEmpty());
    }

    @Test
    void unbanProductByAdminOnlyWorksWhenBanned() {
        productService.createProduct("p1", "Phone", "", 100.0, "owner1");
        assertFalse(productService.unbanProductByAdmin("p1"));

        productService.banProductByAdmin("p1");
        assertTrue(productService.unbanProductByAdmin("p1"));
        assertFalse(productService.findProductById("p1").isBannedByAdmin());
    }

    @Test
    void searchProductsByNameReturnsOnlyPublishedAndNotBanned() {
        productService.createProduct("p1", "Phone", "", 100.0, "o1");
        productService.createProduct("p2", "Phone Case", "", 10.0, "o2");
        productService.createProduct("p3", "PhoneX", "", 200.0, "o3");

        productService.publishProduct("p1", "o1");
        productService.publishProduct("p3", "o3");
        productService.banProductByAdmin("p3");

        List<Product> results = productService.searchProductsByName("phone");

        assertEquals(1, results.size());
        assertEquals("p1", results.get(0).getProductId());
    }
}
