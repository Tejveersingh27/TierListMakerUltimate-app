package app.TierListMakerUltimate.business.services;

import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.Tier;

import java.util.List;

public interface ITierManager {
    Tier createTier(int tierListId, String label, String color) throws ValidationException;

    void removeTier(int tierId) throws ValidationException;

    Tier getTier(int tierId) throws ValidationException;

    void updateTier(Tier updatedTier) throws ValidationException;

    List<Tier> getTiersForList(int tierListId) throws ValidationException;
}
