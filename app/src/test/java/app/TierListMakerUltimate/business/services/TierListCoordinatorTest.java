package app.TierListMakerUltimate.business.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import app.TierListMakerUltimate.business.validation.TierListValidator;
import app.TierListMakerUltimate.business.validation.TierValidator;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.persistence.stubs.TierListPersistenceStub;
import app.TierListMakerUltimate.persistence.stubs.TierPersistenceStub;

class TierListCoordinatorTest {

    private TierListCoordinator tierListCoordinator;
    private TierManager tierManager;
    private TierListManager tierListManager;

    @BeforeEach
    void setup() {
        TierPersistenceStub tierStorage = new TierPersistenceStub();
        TierListPersistenceStub tierListStorage = new TierListPersistenceStub();

        tierManager = new TierManager(tierStorage, new TierValidator());
        tierListManager = new TierListManager(tierListStorage, new TierListValidator());

        tierListCoordinator = new TierListCoordinator(tierManager, tierListManager);
    }

    @Test
    void addTierListCreatesListAndUnrankedTier() {
        TierList tierList = tierListCoordinator.addTierList("My List");
        int listId = tierList.getId();

        // Verify list exists
        assertNotNull(tierListManager.getTierList(listId));

        // Verify unranked tier was created
        Tier unranked = tierListCoordinator.getUrankedTier(listId);
        assertNotNull(unranked);
        assertTrue(unranked.isUnranked());
        assertEquals("unranked", unranked.getName());
        assertEquals(listId, unranked.getTierListId());
    }

    @Test
    void creatingTierListWithoutCoordinatorThrowsException() {
        TierList list = tierListManager.createTierList("Empty List");

        assertThrows(RuntimeException.class, () -> {
            tierListCoordinator.getUrankedTier(list.getId());
        });
    }

    @Test
    void removeTierListDeletesTierList() { // TODO: Test for tier and item deletion
        TierList tierList = tierListCoordinator.addTierList("To Delete");
        int listId = tierList.getId();
        assertEquals(1, tierListManager.getAllTierLists().size());

        tierListCoordinator.removeTierList(listId);
        assertEquals(0, tierListManager.getAllTierLists().size());
    }
}
