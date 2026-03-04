package app.TierListMakerUltimate.persistence;

import app.TierListMakerUltimate.models.Tier;

import java.util.List;


public interface TierPersistence {
    List<Tier> getTiersForList(int tierListId);

    Tier getTier(int tierId);

    Tier insertTier(int tierListId, Tier currentTier); // Returns ID

    Tier updateTier(Tier currentTier);

    void deleteTier(int tierId);
}