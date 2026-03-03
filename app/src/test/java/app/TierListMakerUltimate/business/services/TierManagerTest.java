package app.TierListMakerUltimate.business.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    void createTierSetsIsUnrankedToFalse() {
        int id = tierManager.createTier(1, "S Tier", "#FFFFFF");
        Tier created = tierManager.getTier(id);

        assertNotNull(created);
        assertFalse(created.isUnranked(), "Normally created tiers should not be unranked");
    }

    @Test
    void createUnrankedTierSetsIsUnrankedToTrue() {
        int id = tierManager.createUnrankedTier(1, "Unranked", "#808080");
        Tier created = tierManager.getTier(id);

        assertNotNull(created);
        assertTrue(created.isUnranked(), "Unranked tiers must have the unranked flag set to true");
    }

    @Test
    void createTierAssignsIdAndStoresCorrectly() {
        int id = tierManager.createTier(1, "S Tier", "#FFFFFF");

        assertTrue(id > 0);
        Tier created = tierManager.getTier(id);
        assertNotNull(created);
        assertEquals("S Tier", created.getName());
        assertEquals("#FFFFFF", created.getColor());
    }

    @Test
    void getTierReturnsExistingTier() {
        int id = tierManager.createTier(1, "S Tier", "#FFFFFF");
        Tier retrieved = tierManager.getTier(id);

        assertNotNull(retrieved);
        assertEquals("S Tier", retrieved.getName());
    }

    @Test
    void renameTierUpdatesName() {
        int id = tierManager.createTier(1, "Old", "#FFFFFF");
        tierManager.renameTier(id, "New");

        assertEquals("New", tierManager.getTier(id).getName());
    }

    @Test
    void changeTierColorUpdatesColor() {
        int id = tierManager.createTier(1, "S Tier", "#FFFFFF");
        tierManager.changeTierColor(id, "#000000");

        assertEquals("#000000", tierManager.getTier(id).getColor());
    }

    @Test
    void removeTierDeletesTier() {
        int id = tierManager.createTier(1, "S Tier", "#FFFFFF");
        tierManager.removeTier(id);

        assertNull(tierManager.getTier(id));
    }

    @Test
    void getTiersForListReturnsTiersForList() {
        tierManager.createTier(1, "S Tier", "#FFFFFF");
        tierManager.createTier(1, "A Tier", "#FFFFFF");
        tierManager.createTier(2, "B Tier", "#FFFFFF");

        List<Tier> list = tierManager.getTiersForList(1);
        assertEquals(2, list.size());
    }

    @Test
    void createTierRejectsEmptyLabel() {
        assertThrows(ValidationException.class,
                () -> tierManager.createTier(1, "", "#FFFFFF"));
    }

    @Test
    void createTierRejectsInvalidColor() {
        assertThrows(ValidationException.class,
                () -> tierManager.createTier(1, "S Tier", "invalid"));
    }

    @Test
    void renameTierRejectsInvalidLabel() {
        int id = tierManager.createTier(1, "Original", "#FFFFFF");

        assertThrows(ValidationException.class,
                () -> tierManager.renameTier(id, ""));

        assertEquals("Original", tierManager.getTier(id).getName());
    }
}
