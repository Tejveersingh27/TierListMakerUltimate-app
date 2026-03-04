package app.TierListMakerUltimate.business.services;

import app.TierListMakerUltimate.business.exception.NotFoundException;
import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.TierItem;

import java.util.List;

public interface IItemPlacementManager {
    TierItem createItem(String localImagePath, int tierId, String description) throws ValidationException;

    TierItem moveItemToTier(int itemId, int targetTierId) throws ValidationException, NotFoundException;

    void updateItem(TierItem updatedItem) throws ValidationException, NotFoundException;

    void removeItem(int itemId) throws ValidationException, NotFoundException;

    TierItem getItem(int itemId) throws ValidationException, NotFoundException;

    List<TierItem> getItemsForTier(int tierId) throws ValidationException;
}
