package app.TierListMakerUltimate.persistence;

import app.TierListMakerUltimate.models.Tier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;

public class TierStub implements TierPersistence {
    private static final Map<Integer, Tier> tiers = new HashMap<>();
    private static final Map<Integer, Set<Integer>> relatedTierLists = new HashMap<>();

    public TierStub() {
        // Init the tiers
        tiers.put(1, new Tier("S", "FF6600"));
        tiers.put(2, new Tier("A", "33FF00"));
        tiers.put(3, new Tier("B", "00FFFF"));
        tiers.put(4, new Tier("C", "FFFF33"));

        // Init tier lists that use tiers
        relatedTierLists.put(1, new HashSet<>());
        relatedTierLists.get(1).add(1);
        relatedTierLists.get(1).add(2);
        relatedTierLists.get(1).add(3);

        relatedTierLists.put(2, new HashSet<>());
        relatedTierLists.get(2).add(1);
        relatedTierLists.get(2).add(2);
        relatedTierLists.get(2).add(3);

        relatedTierLists.put(3, new HashSet<>());
        relatedTierLists.get(3).add(1);
        relatedTierLists.get(3).add(2);

        relatedTierLists.put(4, new HashSet<>());
        relatedTierLists.get(4).add(1);
        relatedTierLists.get(4).add(2);
    }

    @Override
    public List<Tier> getTiersForList(int tierListId) {
        List<Tier> result = new ArrayList<>();

        for (Integer tierID: relatedTierLists.keySet()) {
            Set<Integer> tierListIDs = relatedTierLists.get(tierID);
            if (tierListIDs.contains(tierListId)) {
                result.add(tiers.get(tierID));
            }
        }

        return result;
    }

    @Override
    public Tier getTier(int tierId) {
        return tiers.get(tierId);
    }

    @Override // TODO: add methods for changing related tier lists
    public int insertTier(int tierListId, Tier currentTier) {
        int newID = tiers.size() + 1;
        currentTier.setId(newID);
        tiers.put(newID, currentTier);

        return newID;
    }

    @Override
    public void updateTier(Tier currentTier){
        tiers.remove(currentTier.getId());
        tiers.put(currentTier.getId(), currentTier);
    }

    @Override
    public void deleteTier(int tierId){
        tiers.remove(tierId);
    }
}