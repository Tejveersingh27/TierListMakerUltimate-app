package app.TierListMakerUltimate.business.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import app.TierListMakerUltimate.business.exception.InitializationException;
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
    void testConstructorNullDependenciesThrowsException() {
        assertThrows(InitializationException.class, () ->
            new TierManager(null, new TierValidator()));

        assertThrows(InitializationException.class, () ->
                    new TierManager(tierStorage, null));
    }

    @Test
    void testCreateTierIsInitiallyRanked() {
        Tier created = tierManager.createTier(1, "S Tier", "#FFD700");
        assertNotNull(created);
        assertFalse(created.isUnranked());
    }


    @Test
    void testCreateTierStoresCorrectData() {
        Tier created = tierManager.createTier(1, "God Tier", "#FFFFFF");

        assertTrue(created.getId() > 0);
        assertNotNull(created);
        assertEquals("God Tier", created.getName());
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
        Tier updated = new Tier(created.getId(), created.getTierListId(), "New", created.getColor(), created.isUnranked(), created.getPosition());

        tierManager.updateTier(updated);

        assertEquals("New", tierManager.getTier(created.getId()).getName());
    }

    @Test
    void testUpdateTierUpdatesColor() {
        Tier created = tierManager.createTier(1, "S Tier", "#FFFFFF");
        Tier updated = new Tier(created.getId(), created.getTierListId(), created.getName(), "#000000", created.isUnranked(), created.getPosition());

        tierManager.updateTier(updated);

        assertEquals("#000000", tierManager.getTier(created.getId()).getColor());
    }

    @Test
    void updateTierUpdatesPosition() {
        Tier created = tierManager.createTier(1, "S Tier", "#FFFFFF");
        Tier updated = new Tier(created.getId(), created.getTierListId(), created.getName(), created.getColor(), created.isUnranked(), 1);

        tierManager.updateTier(updated);

        assertEquals(1, tierManager.getTier(created.getId()).getPosition());
    }

    @Test
    void testRemoveTierDeletesTier() {
        int id = tierManager.createTier(1, "S Tier", "#FFFFFF").getId();
        tierManager.removeTier(id);

        assertThrows(NotFoundException.class, () -> {tierManager.getTier(id);
        });
    }

    @Test
    void testGetTiersForListFiltersByListId() {
        // Tiers for List 1
        tierManager.createTier(1, "Tier 1A", "#FF0000");
        tierManager.createTier(1, "Tier 1B", "#00FF00");

        tierManager.createTier(2, "Tier 2A", "#0000FF");

        List<Tier> list1Tiers = tierManager.getTiersForList(1);
        assertEquals(2, list1Tiers.size());
    }

    @Test
    void testCreateTierRejectsInvalidInput() {
        assertThrows(ValidationException.class, () -> {
            tierManager.createTier(1, "", "#FFFFFF");
        });
        assertThrows(ValidationException.class, () -> {
            tierManager.createTier(1, "S Tier", "not a color");
        });
    }

    @Test
    void updateTierRejectsInvalidLabel() {
        Tier created = tierManager.createTier(1, "Original", "#FFFFFF");
        Tier invalid = new Tier(created.getId(), created.getTierListId(), "", created.getColor(), created.isUnranked(), created.getPosition());

        assertThrows(ValidationException.class, () -> {
            tierManager.updateTier(invalid);
        });

        assertEquals("Original", tierManager.getTier(created.getId()).getName());
    }

    @Test
    void updateTierNotFoundThrowsException() {
        Tier tier = new Tier(888, 1, "Ehhhh", "#000000", false, 0);

        assertThrows(NotFoundException.class, () -> {
            tierManager.updateTier(tier);
        });
    }

    @Test
    void testGetTierIfNotFoundThrowsException() {
        assertThrows(NotFoundException.class, () -> {
            tierManager.getTier(1234);
        });
    }

    @Test
    void getTiersForListInvalidIdThrowsException() {
        assertThrows(ValidationException.class, () -> tierManager.getTiersForList(-1));
    }

    @Test
    void removeTierNotFoundThrowsException() {
        assertThrows(NotFoundException.class, () -> tierManager.removeTier(6666));
    }
}
