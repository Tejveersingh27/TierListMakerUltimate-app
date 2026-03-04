package app.TierListMakerUltimate.business.services;

import app.TierListMakerUltimate.business.exception.NotFoundException;
import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.Tier;

import java.util.List;

public interface ITierManager {
    Tier createTier(int tierListId, String label, String color) throws ValidationException;

    // Added this overload so the Coordinator can use it via the interface
    Tier createTier(int tierListId, String label, String color, boolean isUnranked) throws ValidationException;

    void removeTier(int tierId) throws ValidationException, NotFoundException;

    Tier getTier(int tierId) throws ValidationException, NotFoundException;

    void updateTier(Tier updatedTier) throws ValidationException, NotFoundException;

    List<Tier> getTiersForList(int tierListId) throws ValidationException;
}
