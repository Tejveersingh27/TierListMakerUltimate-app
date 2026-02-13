// FAKE PERSISTENCE CREATED BY CHATGPT JUST FOR TESTING
package app.TierListMakerUltimate.persistence.fake;

import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.persistence.TierItemPersistence;

import java.util.*;

public class FakeTierItemPersistence implements TierItemPersistence {

    private final Map<Integer, TierItem> items = new HashMap<>();
    private int nextId = 1;

    @Override
    public List<TierItem> getItemsForTier(int tierId) {
        List<TierItem> result = new ArrayList<>();
        for (TierItem item : items.values()) {
            if (item.getTierId() == tierId) {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public TierItem getItem(int itemId) {
        return items.get(itemId);
    }

    @Override
    public int insertItem(int tierId, TierItem currentItem) {
        int id = nextId++;
        currentItem.setId(id);
        items.put(id, currentItem);
        return id;
    }

    @Override
    public void updateItem(TierItem currentItem) {
        items.put(currentItem.getId(), currentItem);
    }

    @Override
    public void deleteItem(int itemId) {
        items.remove(itemId);
    }
}
