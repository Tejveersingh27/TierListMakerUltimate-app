package app.TierListMakerUltimate.business.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import app.TierListMakerUltimate.business.exception.NotFoundException;
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
        Tier created = tierManager.createTier(1, "S Tier", "#FFFFFF");

        assertNotNull(created);
        assertFalse(created.isUnranked(), "Normally created tiers should not be unranked");
    }


    @Test
    void createTierAssignsIdAndStoresCorrectly() {
        Tier created = tierManager.createTier(1, "S Tier", "#FFFFFF");

        assertTrue(created.getId() > 0);
        assertNotNull(created);
        assertEquals("S Tier", created.getName());
        assertEquals("#FFFFFF", created.getColor());
    }

    @Test
    void getTierReturnsExistingTier() {
        Tier created = tierManager.createTier(1, "S Tier", "#FFFFFF");

        assertNotNull(created);
        assertEquals("S Tier", created.getName());
    }

    @Test
    void updateTierUpdatesName() {
        Tier created = tierManager.createTier(1, "Old", "#FFFFFF");
        Tier updated = new Tier(created.getId(), created.getTierListId(), "New", created.getColor(), created.isUnranked());

        tierManager.updateTier(updated);

        assertEquals("New", tierManager.getTier(created.getId()).getName());
    }

    @Test
    void updateTierUpdatesColor() {
        Tier created = tierManager.createTier(1, "S Tier", "#FFFFFF");
        Tier updated = new Tier(created.getId(), created.getTierListId(), created.getName(), "#000000", created.isUnranked());

        tierManager.updateTier(updated);

        assertEquals("#000000", tierManager.getTier(created.getId()).getColor());
    }

    @Test
    void removeTierDeletesTier() {
        int id = tierManager.createTier(1, "S Tier", "#FFFFFF").getId();
        tierManager.removeTier(id);

        assertThrows(NotFoundException.class, () -> tierManager.getTier(id));
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
    void updateTierRejectsInvalidLabel() {
        Tier created = tierManager.createTier(1, "Original", "#FFFFFF");
        Tier invalid = new Tier(created.getId(), created.getTierListId(), "", created.getColor(), created.isUnranked());

        assertThrows(ValidationException.class,
                () -> tierManager.updateTier(invalid));

        assertEquals("Original", tierManager.getTier(created.getId()).getName());
    }

    @Test
    void updateTierThrowsExceptionIfNotFound() {
        Tier nonExistent = new Tier(999, 1, "Ghost", "#000000", false);

        assertThrows(NotFoundException.class, () -> tierManager.updateTier(nonExistent));
    }
}
