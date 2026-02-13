package app.TierListMakerUltimate.persistence.stubs;

import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.persistence.TierItemPersistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TierItemPersistenceStub implements TierItemPersistence {

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
        TierItem copy = new TierItem(id, currentItem.getImagePath(),
                currentItem.getDescription(), tierId);
        items.put(id, copy);
        return id;
    }

    @Override
    public void updateItem(TierItem currentItem) {
        if (items.containsKey(currentItem.getId())) {
            items.put(currentItem.getId(), currentItem);
        }
    }

    @Override
    public void deleteItem(int itemId) {
        items.remove(itemId);
    }
}