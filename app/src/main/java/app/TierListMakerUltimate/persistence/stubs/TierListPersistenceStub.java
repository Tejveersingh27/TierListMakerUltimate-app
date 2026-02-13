package app.TierListMakerUltimate.persistence.stubs;

import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.persistence.TierListPersistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TierListPersistenceStub implements TierListPersistence {

    private final Map<Integer, TierList> tierLists = new HashMap<>();
    private int nextId = 1;


    @Override
    public List<TierList> getTierLists() {
        return new ArrayList<>(tierLists.values());
    }

    @Override
    public TierList getTierListById(int tierListId) {
        return tierLists.get(tierListId);
    }

    @Override
    public int insertTierList(TierList currentTierList) {
        int id = nextId++;
        TierList copy = new TierList(id, currentTierList.getName());
        tierLists.put(id, copy);
        return id;
    }

    @Override
    public void updateTierList(TierList currentTierList) {
        if (tierLists.containsKey(currentTierList.getId())) {
            tierLists.put(currentTierList.getId(), currentTierList);
        }
    }

    @Override
    public void deleteTierList(int tierListId) {
        tierLists.remove(tierListId);
    }
}