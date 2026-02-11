package app.TierListMakerUltimate.business.services;
import app.TierListMakerUltimate.business.validation.ValidationException;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.persistence.TierItemPersistence;
import app.TierListMakerUltimate.business.validation.ItemPlacementValidator;
public class ItemPlacementManager{
    private final TierItemPersistence itemStorage;
    private final ItemPlacementValidator validator;
    public ItemPlacementManager(TierItemPersistence itemStorage) // SHOULD I PASS VALIDATOR AS INPUT HERE?
    {
        this.itemStorage = itemStorage;
        this.validator = new ItemPlacementValidator();
    }
    public ItemPlacementManager(TierItemPersistence itemStorage, ItemPlacementValidator validator)
    {
        this.itemStorage = itemStorage;
        this.validator = validator;
    }
    public TierItem createItem(String localImagePath, int tierId, String description)
    {
        validator.validateCreateItem(localImagePath, tierId, description);
        TierItem newTierItem = new TierItem(localImagePath, description, tierId);
        int tierItemId = itemStorage.insertItem(tierId, newTierItem);
        newTierItem.setId(tierItemId);
        return newTierItem;
    }
    // Create DSO > add to database > set DSO ID
    public void moveItemToTier(int itemId, int targetTierId)
    {
        validator.validateMoveItemToTier(itemId, targetTierId);
        TierItem targetItem = itemStorage.getItem(itemId); // Get the item with matching id from the database
        if(targetItem == null)
        {
            throw new ValidationException("Tier Item not Found");
        }
        targetItem.setTierId(targetTierId); // setting the tier Id of the item as the targeted one
        itemStorage.updateItem(targetItem);
    }
    public void removeItem(int itemId)// Remove from Database > Remove image file
    {
        validator.validateItemId(itemId);
        itemStorage.deleteItem(itemId);
    }
    public TierItem getItem(int itemId)
    {
        validator.validateItemId(itemId);
        return itemStorage.getItem(itemId);
    }


}
