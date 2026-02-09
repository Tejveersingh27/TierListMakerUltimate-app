package com.lameault.sample_project.business.services;

import com.lameault.sample_project.models.Tier;
import com.lameault.sample_project.persistence.TierPersistence;
import com.lameault.sample_project.persistence.stubs.StubTierPersistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TierManagerTest {

    private TierPersistence tierStorage;
    private TierManager tierManager;

    @BeforeEach
    void setup() {
        tierStorage = new StubTierPersistence();
        tierManager = new TierManager(tierStorage);
    }

    // Happy Paths :)

    @Test
    void createTier_assignsIdAndStoresCorrectly() {
        Tier created = tierManager.createTier(1, "S Tier", "#FF0000");

        assertNotNull(created);
        assertTrue(created.getId() > 0);
        assertEquals("S Tier", created.getName());
        assertEquals("#FF0000", created.getColor());
    }

    @Test
    void getTier_returnsExistingTier() {
        Tier created = tierManager.createTier(1, "S Tier", "#FF0000");
        Tier retrieved = tierManager.getTier(created.getId());

        assertNotNull(retrieved);
        assertEquals("S Tier", retrieved.getName());
    }

    @Test
    void renameTier_updatesName() {
        Tier created = tierManager.createTier(1, "Old", "#FF0000");
        tierManager.renameTier(created.getId(), "New");

        assertEquals("New", tierManager.getTier(created.getId()).getName());
    }

    @Test
    void changeTierColor_updatesColor() {
        Tier created = tierManager.createTier(1, "S Tier", "#FF0000");
        tierManager.changeTierColor(created.getId(), "#00FF00");

        assertEquals("#00FF00", tierManager.getTier(created.getId()).getColor());
    }

    @Test
    void removeTier_deletesTier() {
        Tier created = tierManager.createTier(1, "S Tier", "#FF0000");
        tierManager.removeTier(created.getId());

        assertNull(tierManager.getTier(created.getId()));
    }

    @Test
    void getTiersForList_returnsOnlyTiersForThatList() {
        tierManager.createTier(1, "S Tier", "#FF0000");
        tierManager.createTier(1, "A Tier", "#00FF00");
        tierManager.createTier(2, "B Tier", "#0000FF");

        List<Tier> list = tierManager.getTiersForList(1);
        assertEquals(2, list.size());
    }

    // Edge cases :(

    @Test
    void createTier_rejectsEmptyLabel() {
        assertThrows(IllegalArgumentException.class,
                () -> tierManager.createTier(1, "", "#FF0000"));
    }

    @Test
    void createTier_rejectsInvalidColor() {
        assertThrows(IllegalArgumentException.class,
                () -> tierManager.createTier(1, "S Tier", "invalid"));
    }

    @Test
    void renameTier_rejectsInvalidLabel() {
        Tier created = tierManager.createTier(1, "Original", "#FF0000");

        assertThrows(IllegalArgumentException.class,
                () -> tierManager.renameTier(created.getId(), ""));

        assertEquals("Original", tierManager.getTier(created.getId()).getName());
    }
}