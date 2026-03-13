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

import app.TierListMakerUltimate.business.exceptions.InitializationException;
import app.TierListMakerUltimate.business.exceptions.NotFoundException;
import app.TierListMakerUltimate.business.exceptions.ImageException;
import app.TierListMakerUltimate.business.exceptions.ValidationException;
import app.TierListMakerUltimate.business.services.implementations.ItemPlacementManager;
import app.TierListMakerUltimate.business.validation.ItemValidator;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.persistence.interfaces.ImageFilePersistence;
import app.TierListMakerUltimate.persistence.interfaces.TierItemPersistence;
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
            public void deleteImage(String fileName) {
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

        TierItem item = manager.createItem("path", "Item Name", 1, "This is a test item","reasons");
        assertNotNull(item);
        assertEquals("Item Name", item.getName());
        assertEquals("This is a test item", item.getDescription());
        assertEquals("reasons", item.getExplanation());
        assertTrue(item.getId() > 0);
        assertEquals(1, item.getTierId());
    }

    @Test
    void testCreateItemWithStreamSuccess() {
        TierItem item = manager.createItem(1, "Item Name", "Stream Item", "",null, "png");
        assertNotNull(item);
        assertEquals("Item Name", item.getName());
        assertEquals("test", item.getImagePath());
    }
    
    @Test
    void testCreateItemNullDescriptionThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.createItem("imagePath", "Name", 1, null, "");
        });
    }

    @Test
    void testCreateItemInvalidTierIdThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.createItem("imagePath", "Name", -1, "item with invalid tier id", "");
        });
    }

    @Test
    void testMoveItemToTierSuccess() {
        TierItem item = manager.createItem("path", "Name", 1, "This is a test item", "");
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
        TierItem item = manager.createItem("path", "Old Name", 1, "Old item", "Old reason");
        TierItem updated = new TierItem(item.getId(), "path", "New Name", "New item", "New reason", 1);
        manager.updateItem(updated);
        assertEquals("New Name", manager.getItem(item.getId()).getName());
        assertEquals("New item", manager.getItem(item.getId()).getDescription());
        assertEquals("New reason", manager.getItem(item.getId()).getExplanation());
    }

    @Test
    void testUpdateItemNotFoundThrowsException() {
        TierItem item = new TierItem(999, "path", "Name", "description", "explanation",1);
        assertThrows(NotFoundException.class, () -> {
            manager.updateItem(item);
        });
    }

    @Test
    void testUpdateItemWithStreamSuccess() {
        TierItem item = manager.createItem("path", "Old Name", 1, "Old Desc", "Old Expl");
        TierItem updated = new TierItem(item.getId(), null, "New Name", "New Desc", "New Expl", 1);
        manager.updateItem(updated, null, "jpg");
        TierItem result = manager.getItem(item.getId());
        assertEquals("test", result.getImagePath());
        assertEquals("New Name", result.getName());
    }

    @Test
    void testRemoveItemSuccess() {
        TierItem item = manager.createItem("path", "Name", 1, "Another test item", "Some reason");
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
        manager.createItem("p1", "N1", 5, "D1", "E1");
        assertEquals(1, manager.getItemsForTier(5).size());
    }

    @Test
    void testGetItemsForTierInvalidIdThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.getItemsForTier(0);
        });
    }

    @Test
    void testCopyItemSuccess() {
        TierItem item = manager.createItem("path", "Original", 1, "Desc", "Expl");
        TierItem copied = manager.copyItem(item.getId(), 2);

        assertNotNull(copied);
        assertEquals(2, copied.getTierId());
        assertEquals("Original", copied.getName());
        assertEquals("path", copied.getImagePath());
        assertTrue(copied.getId() != item.getId());
    }

    @Test
    void testStoreImageThrowsImageException() {
        ImageFilePersistence failing = new ImageFilePersistence() {
            @Override public String saveImage(InputStream is, String ext) throws IOException { throw new IOException(); }
            @Override public void deleteImage(String name) {}
        };
        ItemPlacementManager failingManager = new ItemPlacementManager(persistence, failing, validator);
        assertThrows(ImageException.class, () -> failingManager.createItem(1, "N", "D", "E",null, "png"));
    }
}
