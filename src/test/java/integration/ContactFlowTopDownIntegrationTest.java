package integration;

import main.ContactExchangeRecord;
import main.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ContactExchangeService;
import service.ProductService;
import service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ContactFlowTopDownIntegrationTest {
    private static final Path USERS_FILE = Paths.get("src", "main", "resources", "users.dat");
    private static final Path PRODUCTS_FILE = Paths.get("src", "main", "resources", "products.dat");
    private static final Path EXCHANGES_FILE = Paths.get("src", "main", "resources", "contact_exchanges.dat");

    private UserService userService;
    private ProductService productService;
    private ContactExchangeService contactExchangeService;

    @BeforeEach
    void setUp() throws IOException {
        cleanupFiles();
        userService = new UserService();
        productService = new ProductService();
        contactExchangeService = new ContactExchangeService();
    }

    @AfterEach
    void tearDown() throws IOException {
        cleanupFiles();
    }

    @Test
    void buyerRequestsContactAfterPublish() {
        String sellerId = "seller-" + UUID.randomUUID();
        String buyerId = "buyer-" + UUID.randomUUID();
        String productId = "product-" + UUID.randomUUID();
        String recordId = "record-" + UUID.randomUUID();

        assertTrue(userService.registerUser(sellerId, "seller", "pwd", "regular"));
        assertTrue(userService.registerUser(buyerId, "buyer", "pwd", "regular"));

        assertTrue(productService.createProduct(productId, "Gaming Laptop", "RTX inside", 9999.0, sellerId));
        assertTrue(productService.publishProduct(productId, sellerId));

        assertTrue(contactExchangeService.recordContactExchange(recordId, buyerId, productId, sellerId));
        List<ContactExchangeRecord> buyerRecords = contactExchangeService.getUserExchangeRecords(buyerId);
        List<ContactExchangeRecord> sellerRecords = contactExchangeService.getUserExchangeRecords(sellerId);

        assertEquals(1, buyerRecords.size());
        assertEquals(1, sellerRecords.size());
        ContactExchangeRecord stored = contactExchangeService.findExchangeRecordById(recordId);
        assertNotNull(stored);
        assertEquals(buyerId, stored.getRequesterId());
        assertEquals(productId, stored.getProductId());
        assertEquals(sellerId, stored.getOwnerId());

        assertFalse(contactExchangeService.recordContactExchange(recordId, buyerId, productId, sellerId));
    }

    private void cleanupFiles() throws IOException {
        Files.deleteIfExists(USERS_FILE);
        Files.deleteIfExists(PRODUCTS_FILE);
        Files.deleteIfExists(EXCHANGES_FILE);
    }
}
