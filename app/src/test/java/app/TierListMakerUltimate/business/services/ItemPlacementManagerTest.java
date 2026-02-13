package app.TierListMakerUltimate.business.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import app.TierListMakerUltimate.business.validation.ValidationException;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.persistence.TierItemPersistence;
import app.TierListMakerUltimate.persistence.stubs.TierItemPersistenceStub;

public class ItemPlacementManagerTest {
    private TierItemPersistence persistence;
    private ItemPlacementManager manager;

    @BeforeEach
    void setup() {
        persistence = new TierItemPersistenceStub();
        manager = new ItemPlacementManager(persistence);
    }

    @Test
    void testCreateItem() {
        TierItem item = manager.createItem("image.png", 1, "This is a test item");
        assertEquals("This is a test item", item.getDescription());
        assertNotNull(item);
        assertTrue(item.getId() >= 0);
        assertEquals(1, item.getTierId());
    }

    @Test
    void testInvalidImagePathThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.createItem("", 2, "This is an item with no image path");
        });
    }

    @Test
    void testInvalidTierIdThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.createItem("img.png", 0, "This is an item with no image path");
        });
    }

    @Test
    void testMovesItemToTierSuccess() {
        TierItem item = manager.createItem("image.png", 1, "This is a test item");
        assertEquals(1, item.getTierId());
        manager.moveItemToTier(item.getId(), 2);
        item = manager.getItem(item.getId());
        assertEquals(2, item.getTierId()); //confirms that the tierId actually changed after moving
    }

    @Test
    void testMoveItemMoveThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.moveItemToTier(0, 2);
        });
    }

    @Test
    void testRemoveItem() {
        TierItem item = manager.createItem("xyz.png", 1, "Another test item");
        int id = item.getId();
        assertNotNull(persistence.getItem(id));
        manager.removeItem(id); // Now the persistence should be empty
        assertNull(persistence.getItem(id));
    }

    @Test
    void testRemoveInvalidItemThrowsException() {
        TierItem item1 = manager.createItem("itemA.png", 1, "Item A");
        TierItem item2 = manager.createItem("itemB.png", 2, "Item B");
        TierItem item3 = manager.createItem("itemC.png", 3, "Item C");
        assertThrows(ValidationException.class, () -> {
            manager.removeItem(-1); // Doesn't exist
        });
    }

}
