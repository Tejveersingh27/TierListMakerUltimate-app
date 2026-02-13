package app.TierListMakerUltimate.persistence;

import app.TierListMakerUltimate.models.TierItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TierItemStub implements TierItemPersistence {
    private static final Map<Integer, TierItem> tierItems = new HashMap<>();
    private static final Map<Integer, Integer> relatedTiers = new HashMap<>();
    private static final Map<Integer, Integer> relatedTierLists = new HashMap<>();

    public TierItemStub() {
        // Init some items
        tierItems.put(1, new TierItem(1, "ImagePath1", "Description1", 1));
        relatedTierLists.put(1, 1);
        relatedTiers.put(1, 1);

        tierItems.put(2, new TierItem(2, "ImagePath2", "Description2", 2));
        relatedTierLists.put(2, 1);
        relatedTiers.put(2, 2);

        tierItems.put(3, new TierItem(3, "ImagePath3", "Description3", 3));
        relatedTierLists.put(3, 1);
        relatedTiers.put(3, 3);

        tierItems.put(4, new TierItem(4, "ImagePath4", "Description4", 4));
        relatedTierLists.put(4, 2);
        relatedTiers.put(4, 1);

        tierItems.put(5, new TierItem(5, "ImagePath5", "Description5", 5));
        relatedTierLists.put(5, 2);
        relatedTiers.put(5, 2);

        tierItems.put(6, new TierItem(6, "ImagePath6", "Description6", 6));
        relatedTierLists.put(6, 3);
        relatedTiers.put(6, 1);

        tierItems.put(7, new TierItem(7, "ImagePath7", "Description7", 7));
        relatedTierLists.put(7, 4);
        relatedTiers.put(7, 1);
    }

    @Override
    public List<TierItem> getItemsForTier(int tierId) {
        List<TierItem> result = new ArrayList<TierItem>();

        for (Integer itemID: tierItems.keySet()) {
            if (relatedTiers.get(itemID).equals(tierId)) {
                result.add(tierItems.get(itemID));
            }
        }

        return result;
    }

    @Override
    public TierItem getItem(int itemId) {
        return tierItems.get(itemId);
    }

    @Override // TODO: add methods for changing related tiers and tier lists
    public int insertItem(int tierId, TierItem currentItem) {   // Returns ID
        int newID = tierItems.size() + 1;
        tierItems.put(newID, currentItem);

        return newID;
    }

    @Override
    public void updateItem(TierItem currentItem) {
        tierItems.remove(currentItem.getId());
        tierItems.put(currentItem.getId(), currentItem);
    }

    @Override
    public void deleteItem(int itemId) {
        tierItems.remove(itemId);
        relatedTiers.remove(itemId);
        relatedTierLists.remove(itemId);
    }
}