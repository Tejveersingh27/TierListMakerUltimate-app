package app.TierListMakerUltimate.business.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import app.TierListMakerUltimate.business.services.ItemPlacementManager;
import app.TierListMakerUltimate.business.services.TierListManager;
import app.TierListMakerUltimate.business.services.TierManager;
import app.TierListMakerUltimate.business.validation.ItemValidator;
import app.TierListMakerUltimate.business.validation.TierListValidator;
import app.TierListMakerUltimate.business.validation.TierValidator;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.persistence.ImageFilePersistence;
import app.TierListMakerUltimate.persistence.stubs.TierItemPersistenceStub;
import app.TierListMakerUltimate.persistence.stubs.TierListPersistenceStub;
import app.TierListMakerUltimate.persistence.stubs.TierPersistenceStub;

public class TierListFLowIntegrationTest {
    private TierListManager listManager;
    private TierManager tierManager;
    private ItemPlacementManager itemManager;
    
    private TierListPersistenceStub listStorage;
    private TierPersistenceStub tierStorage;
    private TierItemPersistenceStub itemStorage;

    @BeforeEach
    void setup() {
        listStorage = new TierListPersistenceStub();
        tierStorage = new TierPersistenceStub();
        itemStorage = new TierItemPersistenceStub();

        //Initialize a Real Image Saver
        ImageFilePersistence imagePersistence = new ImageFilePersistence() {
            @Override
            public String saveImage(InputStream inputStream, String fileName) throws IOException {
                return "integration_test_path";
            }
            @Override
            public void deleteImage(String fileName) throws IOException {}
        };

        listManager = new TierListManager(listStorage, imagePersistence, new TierListValidator());
        tierManager = new TierManager(tierStorage, new TierValidator());
        itemManager = new ItemPlacementManager(itemStorage, imagePersistence, new ItemValidator());
    }

    @Test
    void testFullTierListCreationAndManipulationFlow() {
        TierList list = listManager.createTierList("Vacation Plans!", "thumbnailPath", false);
        assertNotNull(list);
        int listId = list.getId();
        assertTrue(listId > 0);

        // Add Tier s and a using TierManager
        Tier sTier = tierManager.createTier(listId, "Must Visit", "#FF0000");
        Tier aTier = tierManager.createTier(listId, "Maybe Visit", "#FF7700");
        
        assertNotNull(sTier);
        assertEquals(listId, sTier.getTierListId());
        

        List<Tier> tiers = tierManager.getTiersForList(listId);
        assertEquals(2, tiers.size());

       // Adding item to s tier
        TierItem item = itemManager.createItem("japan.png", sTier.getId(), "Japan Trip");
        assertNotNull(item);
        assertEquals(sTier.getId(), item.getTierId());

        //Verify the item is retrievable via the manager
        List<TierItem> mustVisitItems = itemManager.getItemsForTier(sTier.getId());
        assertEquals(1, mustVisitItems.size());
        assertEquals("Japan Trip", mustVisitItems.get(0).getDescription());

        itemManager.moveItemToTier(item.getId(), aTier.getId());
        
        // Check "Must Visit" is empty and "Maybe Visit" has the trip
        assertTrue(itemManager.getItemsForTier(sTier.getId()).isEmpty());
        assertEquals(1, itemManager.getItemsForTier(aTier.getId()).size());
        assertEquals("Japan Trip", itemManager.getItemsForTier(aTier.getId()).get(0).getDescription());

        //Removing the item
        itemManager.removeItem(item.getId());
        assertTrue(itemManager.getItemsForTier(aTier.getId()).isEmpty());
    }
}
