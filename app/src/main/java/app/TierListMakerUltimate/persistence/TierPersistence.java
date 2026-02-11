package app.TierListMakerUltimate.persistence;

import app.TierListMakerUltimate.models.Tier;

import java.util.List;


public interface TierPersistence {
    List<Tier> getTiersForList(int tierListId);

    Tier getTier(int tierId);

    int insertTier(int tierListId, Tier currentTier); // Returns ID

    void updateTier(Tier currentTier);

    void deleteTier(int tierId);
}