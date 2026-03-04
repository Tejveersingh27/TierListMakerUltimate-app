package app.TierListMakerUltimate.business.services;

import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.TierItem;

import java.util.List;

public interface IItemPlacementManager {
    TierItem createItem(int localImagePath, int tierId, String description) throws ValidationException;

    TierItem moveItemToTier(int itemId, int targetTierId) throws ValidationException;

    void updateItem(TierItem updatedItem) throws ValidationException;

    void removeItem(int itemId) throws ValidationException;

    TierItem getItem(int itemId) throws ValidationException;

    List<TierItem> getItemsForTier(int tierId) throws ValidationException;
}
