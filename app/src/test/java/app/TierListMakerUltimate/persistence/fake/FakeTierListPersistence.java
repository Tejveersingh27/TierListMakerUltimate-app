// FAKE PERSISTENCE CREATED BY CHATGPT JUST FOR TESTING
package app.TierListMakerUltimate.persistence.fake;

import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.persistence.TierListPersistence;

import java.util.*;

public class FakeTierListPersistence implements TierListPersistence {

    private final Map<Integer, TierList> lists = new HashMap<>();
    private int nextId = 1;

    @Override
    public List<TierList> getTierLists() {
        return new ArrayList<>(lists.values());
    }

    @Override
    public TierList getTierListById(int tierListId) {
        return lists.get(tierListId);
    }

    @Override
    public int insertTierList(TierList currentTierList) {
        int id = nextId++;
        currentTierList.setId(id);
        lists.put(id, currentTierList);
        return id;
    }

    @Override
    public void updateTierList(TierList currentTierList) {
        lists.put(currentTierList.getId(), currentTierList);
    }

    @Override
    public void deleteTierList(int tierListId) {
        lists.remove(tierListId);
    }
}
