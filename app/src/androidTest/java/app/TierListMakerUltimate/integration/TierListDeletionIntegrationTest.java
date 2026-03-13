//package app.TierListMakerUltimate.business.integration;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.List;
//
//import app.TierListMakerUltimate.business.exception.NotFoundException;
//import app.TierListMakerUltimate.business.services.ItemPlacementManager;
//import app.TierListMakerUltimate.business.services.TierListCoordinator;
//import app.TierListMakerUltimate.business.services.TierListManager;
//import app.TierListMakerUltimate.business.services.TierManager;
//import app.TierListMakerUltimate.business.validation.ItemValidator;
//import app.TierListMakerUltimate.business.validation.TierListValidator;
//import app.TierListMakerUltimate.business.validation.TierValidator;
//import app.TierListMakerUltimate.models.Tier;
//import app.TierListMakerUltimate.models.TierList;
//import app.TierListMakerUltimate.persistence.ImageFilePersistence;
//import app.TierListMakerUltimate.persistence.TierItemPersistence;
//import app.TierListMakerUltimate.persistence.TierListPersistence;
//import app.TierListMakerUltimate.persistence.TierPersistence;
//import app.TierListMakerUltimate.persistence.stubs.TierItemPersistenceStub;
//import app.TierListMakerUltimate.persistence.stubs.TierListPersistenceStub;
//import app.TierListMakerUltimate.persistence.stubs.TierPersistenceStub;
//
/// **
// * Integration Test to verify that deleting a TierList correctly cleans up
// * all associated Tiers and Items across the different managers.
// */
//
//public class TierListDeletionIntegrationTest {
//    private TierListCoordinator listCoordinator;
//    private TierListManager listManager;
//    private TierManager tierManager;
//    private ItemPlacementManager itemManager;
//
//    private TierListPersistence listStorage;
//    private TierPersistence tierStorage;
//    private TierItemPersistence itemStorage;
//
//    @BeforeEach
//    void setup() {
//        listStorage = new TierListPersistenceStub();
//        tierStorage = new TierPersistenceStub();
//        itemStorage = new TierItemPersistenceStub();
//
//        ImageFilePersistence imagePersistence = new ImageFilePersistence() {
//            @Override public String saveImage(InputStream is, String ext) throws IOException {
//                return "path";
//            }
//            @Override public void deleteImage(String name) {
//
//            }
//        };
//
//        listManager = new TierListManager(listStorage, imagePersistence, new TierListValidator());
//        tierManager = new TierManager(tierStorage, new TierValidator());
//        itemManager = new ItemPlacementManager(itemStorage, imagePersistence, new ItemValidator());
//
//
//        listCoordinator = new TierListCoordinator(tierManager, listManager, itemManager);
//    }
//
//    @Test
//    void testDeleteTierListCleansUpEverything() {
//        TierList list = listManager.createTierList("list", "thumbnailPath", false);
//        int listId = list.getId();
//
//        Tier sTier = tierManager.createTier(listId, "s-Tier", "#FF0000");
//        int sTierId = sTier.getId();
//
//        itemManager.createItem("item1.png", "Item 1", sTierId, "description 1");
//        itemManager.createItem("item2.png", "Item 2", sTierId, "description 2");
//
//        assertEquals(1, listManager.getAllTierLists().size());
//        assertEquals(1, tierManager.getTiersForList(listId).size());
//        assertEquals(2, itemManager.getItemsForTier(sTierId).size());
//
//        // This call to the Coordinator should trigger deletions in List, Tier, and Item managers
//        listCoordinator.removeTierList(listId);
//
//        // List should be gone
//        assertThrows(NotFoundException.class, () -> listManager.getTierList(listId));
//
//        // Tiers shoulf be empty
//        List<Tier> tiers = tierManager.getTiersForList(listId);
//        assertTrue(tiers.isEmpty());
//
//
//        assertThrows(NotFoundException.class, () -> {
//            itemManager.getItem(1);
//        });
//
//    }
//}
