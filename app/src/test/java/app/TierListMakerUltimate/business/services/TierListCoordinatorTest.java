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

import app.TierListMakerUltimate.business.exceptions.InitializationException;
import app.TierListMakerUltimate.business.exceptions.NotFoundException;
import app.TierListMakerUltimate.business.services.implementations.ItemPlacementManager;
import app.TierListMakerUltimate.business.services.implementations.TierListCoordinator;
import app.TierListMakerUltimate.business.services.implementations.TierListManager;
import app.TierListMakerUltimate.business.services.implementations.TierManager;
import app.TierListMakerUltimate.business.validation.ItemValidator;
import app.TierListMakerUltimate.business.validation.TierListValidator;
import app.TierListMakerUltimate.business.validation.TierValidator;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.persistence.interfaces.ImageFilePersistence;
import app.TierListMakerUltimate.persistence.fake.TierItemPersistenceFake;
import app.TierListMakerUltimate.persistence.fake.TierListPersistenceFake;
import app.TierListMakerUltimate.persistence.fake.TierPersistenceFake;

class TierListCoordinatorTest {

    private TierListCoordinator tierListCoordinator;
    private TierManager tierManager;
    private TierListManager tierListManager;
    private ItemPlacementManager itemPlacementManager;

    @BeforeEach
    void setup() {
        TierPersistenceFake tierStorage = new TierPersistenceFake();
        TierListPersistenceFake tierListStorage = new TierListPersistenceFake();
        ImageFilePersistence imagePersistence = new ImageFilePersistence() {
            @Override
            public String saveImage(InputStream inputStream, String fileName) throws IOException {
                return "test";
            }

            @Override
            public void deleteImage(String fileName) {
                // Do nothing
            }
        };

        tierManager = new TierManager(tierStorage, new TierValidator());
        tierListManager = new TierListManager(tierListStorage, imagePersistence, new TierListValidator());
        itemPlacementManager = new ItemPlacementManager(new TierItemPersistenceFake(), imagePersistence, new ItemValidator());

        tierListCoordinator = new TierListCoordinator(tierManager, tierListManager, itemPlacementManager);
    }

    @Test
    void testConstructorNullDependencies() {
        assertThrows(InitializationException.class, () -> new TierListCoordinator(null, tierListManager, itemPlacementManager));
        assertThrows(InitializationException.class, () -> new TierListCoordinator(tierManager, null, itemPlacementManager));
        assertThrows(InitializationException.class, () -> new TierListCoordinator(tierManager, tierListManager, null));
    }

    @Test
    void testAddTierListCreatesListAndDefaultTiers() {
        TierList list = tierListCoordinator.createTierListWithDefaults("My List", "Thumbnail", false);

        assertNotNull(list);
        List<Tier> tiers = tierManager.getTiersForList(list.getId());
        assertEquals(8, tiers.size());
    }

    @Test
    void testAddTierListStreamCreatesListAndTiers() {
        TierList list = tierListCoordinator.createTierListWithDefaults("Stream List", false, null, "png");

        assertNotNull(list);
        assertEquals("test", list.getThumbnailPath());
        List<Tier> tiers = tierManager.getTiersForList(list.getId());
        assertEquals(8, tiers.size());
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
    void testDeepCopyAsTemplate() {
        TierList original = tierListCoordinator.createTierListWithDefaults("Original", "thumb", false);
        Tier unranked = tierManager.getUnrankedTierForList(original.getId());
        itemPlacementManager.createItem("path", "Item", unranked.getId(), "Desc", "");

        TierList copy = tierListCoordinator.deepCopyAsTemplate(original.getId(), true);
        assertNotNull(copy);
        assertTrue(copy.isTemplate());
        assertEquals(original.getName(), copy.getName());

        List<Tier> copyTiers = tierManager.getTiersForList(copy.getId());
        assertEquals(8, copyTiers.size());

        Tier copyUnranked = tierManager.getUnrankedTierForList(copy.getId());
        List<TierItem> items = itemPlacementManager.getItemsForTier(copyUnranked.getId());
        assertEquals(1, items.size());
        assertEquals("Item", items.get(0).getName());
    }

    @Test
    void testRemoveTierAndMoveAllItemsToUnranked() {
        TierList list = tierListCoordinator.createTierListWithDefaults("List", "thumb", false);
        List<Tier> rankedTiers = tierManager.getRankedTiersForList(list.getId());
        Tier tierToRemove = rankedTiers.get(0);
        Tier unrankedTier = tierManager.getUnrankedTierForList(list.getId());

        itemPlacementManager.createItem("path", "Item", tierToRemove.getId(), "Desc", "");
        assertEquals(1, itemPlacementManager.getItemsForTier(tierToRemove.getId()).size());

        tierListCoordinator.removeTierAndMoveAllItemsToUnranked(tierToRemove.getId());

        assertThrows(NotFoundException.class, () -> tierManager.getTier(tierToRemove.getId()));
        List<TierItem> unrankedItems = itemPlacementManager.getItemsForTier(unrankedTier.getId());
        assertEquals(1, unrankedItems.size());
        assertEquals("Item", unrankedItems.get(0).getName());
    }

    @Test
    void removeTierListNotFoundThrowsException() {
        assertThrows(NotFoundException.class, () -> {
            tierListCoordinator.removeTierList(8888);
        });
    }
}
