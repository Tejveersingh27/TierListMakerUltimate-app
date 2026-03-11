package app.TierListMakerUltimate.business.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import app.TierListMakerUltimate.business.exception.InitializationException;
import app.TierListMakerUltimate.business.exception.NotFoundException;
import app.TierListMakerUltimate.business.exception.PersistenceException;
import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.business.validation.ItemValidator;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.persistence.ImageFilePersistence;
import app.TierListMakerUltimate.persistence.TierItemPersistence;
import app.TierListMakerUltimate.persistence.stubs.TierItemPersistenceStub;

public class ItemPlacementManagerTest {
    private TierItemPersistence persistence;
    private ItemValidator validator;
    private ItemPlacementManager manager;
    private ImageFilePersistence imagePersistence;


    @BeforeEach
    void setup() {

        imagePersistence = new ImageFilePersistence() {
            @Override
            public String saveImage(InputStream inputStream, String fileName) throws IOException {
                return "test";
            }

            @Override
            public void deleteImage(String fileName) throws IOException {
                // Do nothing
            }
        };

        persistence = new TierItemPersistenceStub();
        validator = new ItemValidator();
        manager = new ItemPlacementManager(persistence, imagePersistence, validator);
    }


    @Test
    void testConstructorNullStorageThrowsException() {
        assertThrows(InitializationException.class, () -> {
            new ItemPlacementManager(null, imagePersistence, validator);
        });
    }

    @Test
    void testConstructorNullImagePersistenceThrowsException() {
        assertThrows(InitializationException.class, () -> {
            new ItemPlacementManager(persistence, null, validator);
        });
    }

    @Test
    void testConstructorNullValidatorThrowsException() {
        assertThrows(InitializationException.class, () -> {
            new ItemPlacementManager(persistence, imagePersistence, null);
        });
    }

    @Test
    void testCreateItemSuccess() {
        TierItem item = manager.createItem("path", 1, "This is a test item");
        assertNotNull(item);
        assertEquals("This is a test item", item.getDescription());
        assertTrue(item.getId() > 0);
        assertEquals(1, item.getTierId());
    }

    // Testing Creating items with input stream and image path.
    @Test
    void testCreateItemWithStreamSuccess() {
        // Tests the method that takes an InputStream
        TierItem item = manager.createItem(1, "Stream Item", null, "png");
        assertNotNull(item);
        assertEquals("test", item.getImagePath()); // "test" is returned by our stub in setup()
    }
    
    @Test
    void testCreateItemNullDescriptionThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.createItem("imagePath", 1, null);
        });
    }

    @Test
    void testCreateItemInvalidTierIdThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.createItem("imagePath", 0, "item with invalid tier id");
        });
    }

    @Test
    void testMoveItemToTierSuccess() {
        TierItem item = manager.createItem("path", 1, "This is a test item");
        assertEquals(1, item.getTierId());
        manager.moveItemToTier(item.getId(), 2);
        item = manager.getItem(item.getId());
        assertEquals(2, item.getTierId());
    }

    @Test
    void testMoveItemNotFoundThrowsException() {
        assertThrows(NotFoundException.class, () -> {
            manager.moveItemToTier(1111, 2);
        });
    }

    @Test
    void testMoveItemInvalidIdThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.moveItemToTier(0, 2);
        });
    }

    @Test
    void testUpdateItemSuccess() {
        TierItem item = manager.createItem("path", 1, "Old item");
        TierItem updated = new TierItem(item.getId(), "path", "New item", 1);
        manager.updateItem(updated);
        assertEquals("New item", manager.getItem(item.getId()).getDescription());
    }

    @Test
    void testUpdateItemNotFoundThrowsException() {
        TierItem item = new TierItem(999, "path", "description", 1);
        assertThrows(NotFoundException.class, () -> {
            manager.updateItem(item);
        });
    }

    @Test
    void testUpdateItemWithStreamSuccess() {
        TierItem item = manager.createItem("path", 1, "Old Desc");
        TierItem updated = new TierItem(item.getId(), null, "New Desc", 1);
        manager.updateItem(updated, null, "jpg");
        TierItem result = manager.getItem(item.getId());
        assertEquals("test", result.getImagePath());
    }

    @Test
    void testRemoveItemSuccess() {
        TierItem item = manager.createItem("path", 1, "Another test item");
        int id = item.getId();
        assertNotNull(persistence.getItem(id));
        manager.removeItem(id);
        assertThrows(NotFoundException.class, () -> {
            manager.getItem(id);
        });
    }

    @Test
    void testRemoveItemNotFoundThrowsException() {
        assertThrows(NotFoundException.class, () -> {
            manager.removeItem(8888);
        });
    }

    @Test
    void testRemoveInvalidItemThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.removeItem(-1);
        });
    }

    @Test
    void testGetItemInvalidIdThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.getItem(0);
        });
    }

    @Test
    void testGetItemsForTierSuccess() {
        manager.createItem("p1", 5, "D1");
        assertEquals(1, manager.getItemsForTier(5).size());
    }

    @Test
    void testGetItemsForTierInvalidIdThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.getItemsForTier(0);
        });
    }
}
