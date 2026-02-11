package app.TierListMakerUltimate.persistence;

import app.TierListMakerUltimate.models.TierItem;

import java.util.List;


public interface TierItemPersistence {
    List<TierItem> getItemsForTier(int tierId);

    TierItem getItem(int itemId);

    int insertItem(int tierId, TierItem currentItem); // Returns ID

    void updateItem(TierItem currentItem);

    void deleteItem(int itemId);
}