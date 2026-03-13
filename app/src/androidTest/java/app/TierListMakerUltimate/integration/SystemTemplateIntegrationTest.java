package app.TierListMakerUltimate.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import app.TierListMakerUltimate.business.services.IItemPlacementManager;
import app.TierListMakerUltimate.business.services.ITierListCoordinator;
import app.TierListMakerUltimate.business.services.ITierListManager;
import app.TierListMakerUltimate.business.services.ITierManager;
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
import app.TierListMakerUltimate.persistence.sqlite.AppDBHelper;
import app.TierListMakerUltimate.persistence.sqlite.TierItemPersistenceSQLite;
import app.TierListMakerUltimate.persistence.sqlite.TierListPersistenceSQLite;
import app.TierListMakerUltimate.persistence.sqlite.TierPersistenceSQLite;
import app.TierListMakerUltimate.persistence.system_data.ITierListSeedProvider;

public class SystemTemplateIntegrationTest {
    private SystemTemplateCoordinator templateCoordinator;
    private ITierListCoordinator listCoordinator;
    private ITierManager tierManager;
    private ITierListManager listManager;
    private IItemPlacementManager itemManager;


    private AppDBHelper appDBHelper;
    private TierListPersistence listStorage;
    private TierPersistence tierStorage;
    private TierItemPersistence itemStorage;
    private StubSeedProvider seedProvider;

    // Simple stub provider to give the coordinator fake data to process
    private static class StubSeedProvider implements ITierListSeedProvider {
        List<SystemTemplate> templates = new ArrayList<>();

        @Override
        public List<SystemTemplate> getTemplates() {
            return templates;
        }
    }

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        context.deleteDatabase("TierListMakerUltimate.db");
        
        appDBHelper = new AppDBHelper(context);
        listStorage = new TierListPersistenceSQLite(appDBHelper);
        tierStorage = new TierPersistenceSQLite(appDBHelper);
        itemStorage = new TierItemPersistenceSQLite(appDBHelper);


        ImageFilePersistence imagePersistence = new ImageFilePersistence() {
            @Override
            public String saveImage(InputStream inputStream, String fileName) throws IOException {
                return "test";
            }

            @Override
            public void deleteImage(String fileName) {
            }
        };

        // Initialize the managers
        listManager = new TierListManager(listStorage, imagePersistence, new TierListValidator());
        tierManager = new TierManager(tierStorage, new TierValidator());
        itemManager = new ItemPlacementManager(itemStorage, imagePersistence, new ItemValidator());

        listCoordinator = new TierListCoordinator(tierManager, listManager, itemManager);

        seedProvider = new StubSeedProvider();
        templateCoordinator = new SystemTemplateCoordinator(listCoordinator, tierManager, itemManager, seedProvider);
    }

    @Test
    public void testLoadSystemTemplatesIntegrationFlow() {
        List<SystemTemplateItem> movieItems = new ArrayList<>();

        movieItems.add(new SystemTemplateItem("Star Wars", "Classic Space Opera", "sw.png"));
        movieItems.add(new SystemTemplateItem("Matrix", "Cyberpunk action", "inc.png"));

        //Mock template
        SystemTemplate movieTemplate = new SystemTemplate("Sci Fi Movies", "thumbnailPath", movieItems);
        seedProvider.templates.add(movieTemplate);
        templateCoordinator.loadSystemTemplates();

        // Verifying the TierList was created in the Tier List Persistence
        List<TierList> allLists = listManager.getAllTierLists();
        assertEquals(1, allLists.size());
        TierList createdList = allLists.get(0);
        assertEquals("Sci Fi Movies", createdList.getName());

        List<Tier> tiers = tierManager.getTiersForList(createdList.getId());
        assertTrue(!tiers.isEmpty());

        Tier unranked = tierManager.getUnrankedTierForList(createdList.getId());
        List<TierItem> itemsInUnranked = itemManager.getItemsForTier(unranked.getId());

        assertEquals(2, itemsInUnranked.size());
        // Verify items were placed correctly
        assertTrue(itemsInUnranked.get(0).getName().equals("Star Wars") ||
                itemsInUnranked.get(1).getName().equals("Star Wars"));
    }
}
