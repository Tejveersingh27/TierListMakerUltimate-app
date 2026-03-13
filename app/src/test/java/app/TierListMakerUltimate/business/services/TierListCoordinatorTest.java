package app.TierListMakerUltimate.business.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import app.TierListMakerUltimate.business.exception.InitializationException;
import app.TierListMakerUltimate.business.exception.NotFoundException;
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
            }
        };

        tierManager = new TierManager(tierStorage, new TierValidator());

        tierListManager = new TierListManager(tierListStorage, imagePersistence, new TierListValidator());

        tierListCoordinator = new TierListCoordinator(tierManager, tierListManager);
    }

    @Test
    void testConstructorNullDependencies() {
        assertThrows(InitializationException.class, () -> new TierListCoordinator(null, tierListManager));
        assertThrows(InitializationException.class, () -> new TierListCoordinator(tierManager, null));
    }

    @Test
    void testAddTierListCreatesListAndDefaultTiers() {
        TierList list = tierListCoordinator.createTierListWithDefaults("My List", "Thumbnail", false);

        // Verify list exists
        assertNotNull(list);
        List<Tier> tiers = tierManager.getTiersForList(list.getId());
        assertEquals(7, tiers.size());
    }

    @Test
    void testAddTierListStreamCreatesListAndTiers() {
        TierList list = tierListCoordinator.createTierListWithDefaults("Stream List", false, null, "png");
        
        assertNotNull(list);
        assertEquals("test", list.getThumbnailPath());
        List<Tier> tiers = tierManager.getTiersForList(list.getId());
        assertEquals(7, tiers.size());
    }

    @Test
    void testGetUnrankedTierSuccess() {
        TierList list = tierListCoordinator.createTierListWithDefaults("Unranked", "thumbnailPath", false);
        
        Tier unranked = tierListCoordinator.getUrankedTier(list.getId());
        
        assertNotNull(unranked);
        assertTrue(unranked.isUnranked());
        assertEquals("Unranked", unranked.getName());
    }

    @Test
    void testGetUnrankedTierNotFoundThrowsException() {
        assertThrows(NotFoundException.class, () ->  {
            tierListCoordinator.getUrankedTier(9999);
        });
    }

    @Test
    void removeTierListDeletesTierList() {
        TierList tierList = tierListCoordinator.createTierListWithDefaults("To Delete", "Thumbnail", false);
        int listId = tierList.getId();
        assertEquals(1, tierListManager.getAllTierLists().size());

        tierListCoordinator.removeTierList(listId);
        assertEquals(0, tierListManager.getAllTierLists().size());
    }

    @Test
    void removeTierListNotFoundThrowsException() {
        assertThrows(NotFoundException.class, () -> {
            tierListCoordinator.removeTierList(8888);
        });
    }
}
