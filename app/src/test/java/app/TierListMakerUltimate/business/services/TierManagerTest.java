package app.TierListMakerUltimate.business.services;

import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.persistence.TierPersistence;
import app.TierListMakerUltimate.persistence.TierStub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TierManagerTest {

    private TierPersistence tierStorage;
    private TierManager tierManager;

    @BeforeEach
    void setup() {
        tierStorage = new TierStub(); // TODO: Replace with actual storage implementation
        tierManager = new TierManager(tierStorage);
    }

    // Happy Paths :)

    @Test
    void createTier_assignsIdAndStoresCorrectly() {
        Tier created = tierManager.createTier(1, "S Tier", "#FFFFFF");

        assertNotNull(created);
        assertTrue(created.getId() > 0);
        assertEquals("S Tier", created.getName());
        assertEquals("#FF0000", created.getColor());
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
        Tier created = tierManager.createTier(1, "Old", "##FFFFFF");
        tierManager.renameTier(created.getId(), "New");

        assertEquals("New", tierManager.getTier(created.getId()).getName());
    }

    @Test
    void changeTierColor_updatesColor() {
        Tier created = tierManager.createTier(1, "S Tier", "#FFFFFF");
        tierManager.changeTierColor(created.getId(), "#FFFFFF");

        assertEquals("#FFFFFF", tierManager.getTier(created.getId()).getColor());
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

    // Edge cases :(

    @Test
    void createTier_rejectsEmptyLabel() {
        assertThrows(IllegalArgumentException.class,
                () -> tierManager.createTier(1, "", "#FFFFFF"));
    }

    @Test
    void createTier_rejectsInvalidColor() {
        assertThrows(IllegalArgumentException.class,
                () -> tierManager.createTier(1, "S Tier", "invalid"));
    }

    @Test
    void renameTier_rejectsInvalidLabel() {
        Tier created = tierManager.createTier(1, "Original", "#FFFFFF");

        assertThrows(IllegalArgumentException.class,
                () -> tierManager.renameTier(created.getId(), ""));

        assertEquals("Original", tierManager.getTier(created.getId()).getName());
    }
}