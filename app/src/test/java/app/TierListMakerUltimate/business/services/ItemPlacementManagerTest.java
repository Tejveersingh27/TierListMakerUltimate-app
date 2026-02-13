package app.TierListMakerUltimate.business.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.business.validation.ItemValidator;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.persistence.TierItemPersistence;
import app.TierListMakerUltimate.persistence.stubs.TierItemPersistenceStub;

public class ItemPlacementManagerTest {
    private TierItemPersistence persistence;
    private ItemValidator validator;
    private ItemPlacementManager manager;

    @BeforeEach
    void setup() {
        persistence = new TierItemPersistenceStub();
        validator = new ItemValidator();
        manager = new ItemPlacementManager(persistence, validator);
    }

    @Test
    void testCreateItemSuccess() {
        TierItem item = manager.createItem("image.png", 1, "This is a test item");
        assertNotNull(item);
        assertEquals("This is a test item", item.getDescription());
        assertTrue(item.getId() > 0);
        assertEquals(1, item.getTierId());
    }

    @Test
    void testCreateItemInvalidImagePathThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.createItem("", 2, "This is an item with no image path");
        });
    }

    @Test
    void testCreateItemInvalidTierIdThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.createItem("img.png", 0, "This is an item with invalid tier id");
        });
    }

    @Test
    void testMoveItemToTierSuccess() {
        TierItem item = manager.createItem("image.png", 1, "This is a test item");
        assertEquals(1, item.getTierId());
        manager.moveItemToTier(item.getId(), 2);
        item = manager.getItem(item.getId());
        assertEquals(2, item.getTierId());
    }

    @Test
    void testMoveItemInvalidIdThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.moveItemToTier(0, 2);
        });
    }

    @Test
    void testRemoveItemSuccess() {
        TierItem item = manager.createItem("xyz.png", 1, "Another test item");
        int id = item.getId();
        assertNotNull(persistence.getItem(id));
        manager.removeItem(id);
        assertNull(persistence.getItem(id));
    }

    @Test
    void testRemoveInvalidItemThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.removeItem(-1);
        });
    }
}
