package app.TierListMakerUltimate.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import app.TierListMakerUltimate.business.constants.DefaultTiers;
import app.TierListMakerUltimate.business.exception.NotFoundException;
import app.TierListMakerUltimate.business.services.IItemPlacementManager;
import app.TierListMakerUltimate.business.services.ITierListCoordinator;
import app.TierListMakerUltimate.business.services.ITierListManager;
import app.TierListMakerUltimate.business.services.ITierManager;
import app.TierListMakerUltimate.business.services.ItemPlacementManager;
import app.TierListMakerUltimate.business.services.TierListCoordinator;
import app.TierListMakerUltimate.business.services.TierListManager;
import app.TierListMakerUltimate.business.services.TierManager;
import app.TierListMakerUltimate.business.validation.ItemValidator;
import app.TierListMakerUltimate.business.validation.TierListValidator;
import app.TierListMakerUltimate.business.validation.TierValidator;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.persistence.ImageFilePersistence;
import app.TierListMakerUltimate.persistence.TierItemPersistence;
import app.TierListMakerUltimate.persistence.TierListPersistence;
import app.TierListMakerUltimate.persistence.TierPersistence;
import app.TierListMakerUltimate.persistence.sqlite.AppDBHelper;
import app.TierListMakerUltimate.persistence.sqlite.TierItemPersistenceSQLite;
import app.TierListMakerUltimate.persistence.sqlite.TierListPersistenceSQLite;
import app.TierListMakerUltimate.persistence.sqlite.TierPersistenceSQLite;

public class TierListCoordinatorIntegrationTest {
    private ITierListCoordinator listCoordinator;
    private ITierListManager listManager;
    private ITierManager tierManager;
    private IItemPlacementManager itemManager;

    private AppDBHelper appDBHelper;
    private TierListPersistence listStorage;
    private TierPersistence tierStorage;
    private TierItemPersistence itemStorage;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        context.deleteDatabase("TierListMakerUltimate.db");

        appDBHelper = new AppDBHelper(context, null, false);

        listStorage = new TierListPersistenceSQLite(appDBHelper);
        tierStorage = new TierPersistenceSQLite(appDBHelper);
        itemStorage = new TierItemPersistenceSQLite(appDBHelper);

        ImageFilePersistence imagePersistence = new ImageFilePersistence() {
            @Override
            public String saveImage(InputStream is, String ext) throws IOException {
                return "path";
            }

            @Override
            public void deleteImage(String name) {
            }
        };

        listManager = new TierListManager(listStorage, imagePersistence, new TierListValidator());
        tierManager = new TierManager(tierStorage, new TierValidator());
        itemManager = new ItemPlacementManager(itemStorage, imagePersistence, new ItemValidator());

        listCoordinator = new TierListCoordinator(tierManager, listManager, itemManager);
    }

    @Test
    public void testRemoveTierAndMoveAllItemsToUnranked() {
        // Setup: Create list with an unranked tier and a new tier
        TierList list = listCoordinator.createTierListWithDefaults("Coordinator Test", "thumb", false);
        int listId = list.getId();

        Tier unranked = tierManager.getUnrankedTierForList(listId);
        Tier sTier = tierManager.createTier(listId, "S-Tier", "#FF0000");

        // Add items to the new tier
        TierItem item1 = itemManager.createItem("img1.png", "Item 1", sTier.getId(), "desc 1");
        TierItem item2 = itemManager.createItem("img2.png", "Item 2", sTier.getId(), "desc 2");

        // Verify initial state
        assertEquals(2, itemManager.getItemsForTier(sTier.getId()).size());
        int initialUnrankedCount = itemManager.getItemsForTier(unranked.getId()).size();

        // Remove the tier using the Coordinator
        listCoordinator.removeTierAndMoveAllItemsToUnranked(sTier.getId());

        // Assert Tier is gone
        assertThrows(NotFoundException.class, () -> tierManager.getTier(sTier.getId()));

        // Assert Items are now in unranked
        List<TierItem> unrankedItems = itemManager.getItemsForTier(unranked.getId());
        assertEquals(initialUnrankedCount + 2, unrankedItems.size());

        boolean foundItem1 = false;
        boolean foundItem2 = false;
        for (TierItem item : unrankedItems) {
            if (item.getId() == item1.getId()) foundItem1 = true;
            if (item.getId() == item2.getId()) foundItem2 = true;
        }
        assertTrue("Item 1 should be in unranked tier", foundItem1);
        assertTrue("Item 2 should be in unranked tier", foundItem2);
    }

    @Test
    public void testCreateTierListWithDefaults() {
        TierList list = listCoordinator.createTierListWithDefaults("Default Test", "thumb", false);
        assertNotNull(list);
        assertTrue(list.getId() > 0);

        List<Tier> tiers = tierManager.getTiersForList(list.getId());

        // Verify we have the correct number of tiers based on the enum
        assertEquals(DefaultTiers.values().length, tiers.size());

        // Verify some specific tiers exist
        boolean hasS = false;
        boolean hasUnranked = false;
        for (Tier t : tiers) {
            if ("S".equals(t.getName())) hasS = true;
            if (t.isUnranked()) hasUnranked = true;
        }
        assertTrue("Should have an S tier", hasS);
        assertTrue("Should have an unranked tier", hasUnranked);
    }

    @Test
    public void testDeepCopyAsTemplate() {
        // Setup list
        TierList sourceList = listCoordinator.createTierListWithDefaults("Source", "thumb", false);
        Tier sTier = null;
        for (Tier t : tierManager.getTiersForList(sourceList.getId())) {
            if ("S".equals(t.getName())) {
                sTier = t;
                break;
            }
        }
        assertNotNull(sTier);

        TierItem item = itemManager.createItem("src.png", "Original Item", sTier.getId(), "desc");

        // Deep copy list as template
        TierList copiedList = listCoordinator.deepCopyAsTemplate(sourceList.getId(), true);

        // Assert New list is different but has same name
        assertNotEquals(sourceList.getId(), copiedList.getId());
        assertEquals(sourceList.getName(), copiedList.getName());
        assertTrue(copiedList.isTemplate());

        // Assert: Tiers are copied
        List<Tier> copiedTiers = tierManager.getTiersForList(copiedList.getId());
        assertEquals(DefaultTiers.values().length, copiedTiers.size());

        // Assert: Item is copied and moved to unranked in the new list
        Tier newUnranked = tierManager.getUnrankedTierForList(copiedList.getId());
        List<TierItem> unrankedItems = itemManager.getItemsForTier(newUnranked.getId());

        assertEquals(1, unrankedItems.size());
        assertEquals("Original Item", unrankedItems.get(0).getName());
        assertNotEquals(item.getId(), unrankedItems.get(0).getId());
    }

    @Test
    public void testDeleteTierListCleansUpEverything() {
        // Setup  Tier list with items and tiers
        TierList list = listCoordinator.createTierListWithDefaults("Deletion Test", "thumb", false);
        int listId = list.getId();

        Tier sTier = null;
        for (Tier t : tierManager.getTiersForList(listId)) {
            if ("S".equals(t.getName())) {
                sTier = t;
                break;
            }
        }
        assertNotNull(sTier);

        TierItem item = itemManager.createItem("item.png", "To Be Deleted", sTier.getId(), "desc");
        int itemId = item.getId();

        // Delete entire list
        listCoordinator.removeTierList(listId);

        // Assert everything is delete
        // List is gone
        assertThrows(NotFoundException.class, () -> listManager.getTierList(listId));
        // Tiers for that list are gone
        assertTrue(tierManager.getTiersForList(listId).isEmpty());
        // Item is gone
        assertThrows(NotFoundException.class, () -> itemManager.getItem(itemId));
    }
}
