package app.TierListMakerUltimate.business.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.business.validation.TierValidator;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.persistence.TierPersistence;
import app.TierListMakerUltimate.persistence.stubs.TierPersistenceStub;

class TierManagerTest {

    private TierPersistence tierStorage;
    private TierManager tierManager;

    @BeforeEach
    void setup() {
        tierStorage = new TierPersistenceStub();
        tierManager = new TierManager(tierStorage, new TierValidator());
    }

    @Test
    void createTier_assignsIdAndStoresCorrectly() {
        Tier created = tierManager.createTier(1, "S Tier", "#FFFFFF");

        assertNotNull(created);
        assertTrue(created.getId() > 0);
        assertEquals("S Tier", created.getName());
        assertEquals("#FFFFFF", created.getColor());
    }

    @Test
    void getTier_returnsExistingTier() {
        Tier created = tierManager.createTier(1, "S Tier", "#FFFFFF");
        Tier retrieved = tierManager.getTier(created.getId());

        assertNotNull(retrieved);
        assertEquals("S Tier", retrieved.getName());
    }

    @Test
    void renameTier_updatesName() {
        Tier created = tierManager.createTier(1, "Old", "#FFFFFF");
        tierManager.renameTier(created.getId(), "New");

        assertEquals("New", tierManager.getTier(created.getId()).getName());
    }

    @Test
    void changeTierColor_updatesColor() {
        Tier created = tierManager.createTier(1, "S Tier", "#FFFFFF");
        tierManager.changeTierColor(created.getId(), "#000000");

        assertEquals("#000000", tierManager.getTier(created.getId()).getColor());
    }

    @Test
    void removeTier_deletesTier() {
        Tier created = tierManager.createTier(1, "S Tier", "#FFFFFF");
        tierManager.removeTier(created.getId());

        assertNull(tierManager.getTier(created.getId()));
    }

    @Test
    void getTiersForList_returnsTiersForList() {
        tierManager.createTier(1, "S Tier", "#FFFFFF");
        tierManager.createTier(1, "A Tier", "#FFFFFF");
        tierManager.createTier(2, "B Tier", "#FFFFFF");

        List<Tier> list = tierManager.getTiersForList(1);
        assertEquals(2, list.size());
    }

    @Test
    void createTier_rejectsEmptyLabel() {
        assertThrows(ValidationException.class,
                () -> tierManager.createTier(1, "", "#FFFFFF"));
    }

    @Test
    void createTier_rejectsInvalidColor() {
        assertThrows(ValidationException.class,
                () -> tierManager.createTier(1, "S Tier", "invalid"));
    }

    @Test
    void renameTier_rejectsInvalidLabel() {
        Tier created = tierManager.createTier(1, "Original", "#FFFFFF");

        assertThrows(ValidationException.class,
                () -> tierManager.renameTier(created.getId(), ""));

        assertEquals("Original", tierManager.getTier(created.getId()).getName());
    }
}
