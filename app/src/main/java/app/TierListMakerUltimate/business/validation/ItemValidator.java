package app.TierListMakerUltimate.business.validation;

import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.TierItem;

public class ItemValidator {
    private static final int MAX_LENGTH_DESCRIPTION = 100; // TODO user constants

    public void validateCreateItem(int localImagePath, int tierId, String description) throws ValidationException {
        validateImagePath(localImagePath);
        validateTierId(tierId);
        validateDescription(description);
    }

    public void validateUpdateItem(TierItem updatedItem) throws ValidationException {
        validateItemId(updatedItem.getId());
        validateTierId(updatedItem.getTierId());
        validateImagePath(updatedItem.getImagePath());
        validateDescription(updatedItem.getDescription());
    }

    public void validateRemoveItem(int itemId) throws ValidationException {
        validateItemId(itemId);
    }


    public void validateImagePath(int localImagePath) throws ValidationException {
        // TODO: Implement image path validation
        //if (localImagePath >= 0) {
        //   throw new ValidationException("A valid image path is required");
        //}
    }

    public void validateTierId(int tierId) throws ValidationException {
        if (tierId <= 0) {
            throw new ValidationException("Tier Id is required");
        }
    }

    public void validateDescription(String description) throws ValidationException {
        if (description == null) {
            throw new ValidationException("Description cannot be null");
        }
        if (description.length() > MAX_LENGTH_DESCRIPTION) {
            throw new ValidationException("Description should be less than " + MAX_LENGTH_DESCRIPTION + " characters");
        }
    }

    public void validateMoveItemToTier(int itemId, int targetTierId) throws ValidationException {
        validateItemId(itemId);
        validateTierId(targetTierId);
    }

    public void validateItemId(int itemId) throws ValidationException {
        if (itemId <= 0) {
            throw new ValidationException("Item Id is required.");
        }
    }
}
