package app.TierListMakerUltimate.business.validation;

public class ItemPlacementValidator {
    private static final int MAX_LENGTH_DESCRIPTION = 100;
    public void validateCreateItem(String localImagePath, int tierId, String description)
    {
        if(localImagePath == null || localImagePath.trim().isEmpty()) { // The image path cant be blank or null There should be something
            throw new ValidationException("Image path is required");
        }
        if (tierId <= 0) {
            throw new ValidationException("Tier Id is required");
        }
        if(description.length() > MAX_LENGTH_DESCRIPTION){
            throw new ValidationException("Description should be less than 100 characters");
        }
    }
    public void validateMoveItemToTier(int itemId, int targetTierId){
        validateItemId(itemId);
        if(targetTierId <= 0)
        {
            throw new ValidationException("Tier Id is required");
        }
    }
    public void validateItemId(int itemId){
        if(itemId <= 0)
        {
            throw new ValidationException("Item Id is required.");
        }
    }





}
