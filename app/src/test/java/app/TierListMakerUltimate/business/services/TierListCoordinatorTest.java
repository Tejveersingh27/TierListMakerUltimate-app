package app.TierListMakerUltimate.business.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import app.TierListMakerUltimate.business.validation.TierListValidator;
import app.TierListMakerUltimate.business.validation.TierValidator;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.persistence.ImageFilePersistence;
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
        ImageFilePersistence imagePersistence = new ImageFilePersistence() {
            @Override
            public String saveImage(InputStream inputStream, String fileName) throws IOException {
                return "test";
            }

            @Override
            public void deleteImage(String fileName) throws IOException {
                // Do nothing
            }
        };

        tierManager = new TierManager(tierStorage, new TierValidator());

        tierListManager = new TierListManager(tierListStorage, imagePersistence, new TierListValidator());

        tierListCoordinator = new TierListCoordinator(tierManager, tierListManager);
    }

    @Test
    void addTierListCreatesListAndDefaultTiers() {
        TierList list = tierListCoordinator.createTierListWithDefaults("My List", "Thumbnail", false);

        // Verify list exists
        assertNotNull(list);

        // Verify default tiers were added
        List<Tier> tiers = tierManager.getTiersForList(list.getId());
        assertEquals(7, tiers.size());

    }


    @Test
    void removeTierListDeletesTierList() { // TODO: Test for tier and item deletion
        TierList tierList = tierListCoordinator.createTierListWithDefaults("To Delete", "Thumbnail", false);
        int listId = tierList.getId();
        assertEquals(1, tierListManager.getAllTierLists().size());

        tierListCoordinator.removeTierList(listId);
        assertEquals(0, tierListManager.getAllTierLists().size());
    }
}
