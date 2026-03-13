package app.TierListMakerUltimate.business.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import app.TierListMakerUltimate.business.services.ItemPlacementManager;
import app.TierListMakerUltimate.business.services.SystemTemplateCoordinator;
import app.TierListMakerUltimate.business.services.TierListCoordinator;
import app.TierListMakerUltimate.business.services.TierListManager;
import app.TierListMakerUltimate.business.services.TierManager;
import app.TierListMakerUltimate.business.validation.ItemValidator;
import app.TierListMakerUltimate.business.validation.TierListValidator;
import app.TierListMakerUltimate.business.validation.TierValidator;
import app.TierListMakerUltimate.models.SystemTemplate;
import app.TierListMakerUltimate.models.SystemTemplateItem;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.persistence.ImageFilePersistence;
import app.TierListMakerUltimate.persistence.TierItemPersistence;
import app.TierListMakerUltimate.persistence.TierListPersistence;
import app.TierListMakerUltimate.persistence.TierPersistence;
import app.TierListMakerUltimate.persistence.stubs.TierItemPersistenceStub;
import app.TierListMakerUltimate.persistence.stubs.TierListPersistenceStub;
import app.TierListMakerUltimate.persistence.stubs.TierPersistenceStub;
import app.TierListMakerUltimate.persistence.system_data.ITierListSeedProvider;

/**
 * Integration Test ensuring that the SystemTemplateCoordinator correctly 
 * orchestrates TierListCoordinator and ItemPlacementManager to load system data.
 */
public class SystemTemplateIntegrationTest {
    private SystemTemplateCoordinator templateCoordinator;
    private TierListCoordinator listCoordinator;
    private TierManager tierManager;
    private TierListManager listManager;
    private ItemPlacementManager itemManager;
    
    private TierListPersistence listStorage;
    private TierPersistence tierStorage;
    private TierItemPersistence itemStorage;
    private StubSeedProvider seedProvider;

    // Simple stub provider to give the coordinator fake data to process
    private static class StubSeedProvider implements ITierListSeedProvider {
        List<SystemTemplate> templates = new ArrayList<>();
        @Override public List<SystemTemplate> getTemplates() {
            return templates;
        }
    }

    @BeforeEach
    void setup() {
        // Use interfaces for storage to keep it clean for future SQLite swap
        listStorage = new TierListPersistenceStub();
        tierStorage = new TierPersistenceStub();
        itemStorage = new TierItemPersistenceStub();

        ImageFilePersistence imagePersistence = new ImageFilePersistence() {
            @Override public String saveImage(InputStream inputStream, String fileName) throws IOException {
                return "test";
            }
            @Override public void deleteImage(String fileName) throws IOException {}
        };

        // Initialize the managers
        listManager = new TierListManager(listStorage, imagePersistence, new TierListValidator());
        tierManager = new TierManager(tierStorage, new TierValidator());
        listCoordinator = new TierListCoordinator(tierManager, listManager);
        itemManager = new ItemPlacementManager(itemStorage, imagePersistence, new ItemValidator());
        seedProvider = new StubSeedProvider();
        templateCoordinator = new SystemTemplateCoordinator(listCoordinator, itemManager, seedProvider);
    }

    @Test
    void testLoadSystemTemplatesIntegrationFlow() {
        List<SystemTemplateItem> movieItems = new ArrayList<>();
        movieItems.add(new SystemTemplateItem("Star Wars", "sw.png"));
        movieItems.add(new SystemTemplateItem("Matrix", "inc.png"));

        //Mock template
        SystemTemplate movieTemplate = new SystemTemplate("Sci Fi Movies", "thumbnailPath", movieItems);
        seedProvider.templates.add(movieTemplate);
        templateCoordinator.loadSystemTemplates();

        // Verifying the TierList was created in the Tier List Persistence
        List<TierList> allLists = listManager.getAllTierLists();
        assertEquals(1, allLists.size());
        TierList createdList = allLists.get(0);
        assertEquals("Sci Fi Movies", createdList.getName());
        assertTrue(createdList.isTemplate());


        List<Tier> tiers = tierManager.getTiersForList(createdList.getId());
        assertTrue(!tiers.isEmpty());

        //Verify items were placed in the Unranked tier
        Tier unranked = listCoordinator.getUrankedTier(createdList.getId());
        List<TierItem> itemsInUnranked = itemManager.getItemsForTier(unranked.getId());
        
        assertEquals(2, itemsInUnranked.size());
        assertTrue(itemsInUnranked.get(0).getDescription().contains("Star Wars") || 
                   itemsInUnranked.get(1).getDescription().contains("Star Wars"));
    }
}
