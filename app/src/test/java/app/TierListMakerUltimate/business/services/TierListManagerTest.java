package app.TierListMakerUltimate.business.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.business.validation.TierListValidator;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.persistence.TierListPersistence;
import app.TierListMakerUltimate.persistence.stubs.TierListPersistenceStub;

public class TierListManagerTest {
    private TierListPersistence persistence;
    private TierListManager manager;

    @BeforeEach
    void setup() {
        persistence = new TierListPersistenceStub();
        manager = new TierListManager(persistence, new TierListValidator());
    }

    @Test
    void testCreateTierListSuccess() {
        TierList tierListItem = manager.createTierList("Favourite Netflix Series List");
        assertEquals("Favourite Netflix Series List", tierListItem.getName());
        assertTrue(tierListItem.getId() > 0);
    }

    @Test
    void testCreateTierListInvalidNameThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.createTierList("");
        });
    }

    @Test
    void testRemoveTierList() {
        TierList newList = manager.createTierList("Test List");
        int id = newList.getId();
        assertNotNull(persistence.getTierListById(id));
        manager.removeTierList(id);
        assertNull(persistence.getTierListById(id));
    }

    @Test
    void testsRemoveTierListInvalidIdThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.removeTierList(-1);
        });
    }
}
