package app.TierListMakerUltimate.business.validation;

import static app.TierListMakerUltimate.business.constants.BusinessConstants.*;

import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.TierItem;

public class ItemValidator {

    public void validateCreateItem(String localImagePath, int tierId, String description) throws ValidationException {
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


    public void validateImagePath(String localImagePath) throws ValidationException {
        // TODO: Implement image path validation
    }

    public void validateTierId(int tierId) throws ValidationException {
        if (tierId <= 0) {
            throw new ValidationException(ERROR_TIER_ID_REQUIRED);
        }
    }

    public void validateDescription(String description) throws ValidationException {
        if (description == null) {
            throw new ValidationException(ERROR_DESCRIPTION_NULL);
        }
        if (description.length() > MAX_LENGTH_DESCRIPTION) {
            throw new ValidationException(ERROR_DESCRIPTION_TOO_LONG);
        }
    }

    public void validateMoveItemToTier(int itemId, int targetTierId) throws ValidationException {
        validateItemId(itemId);
        validateTierId(targetTierId);
    }

    public void validateItemId(int itemId) throws ValidationException {
        if (itemId <= 0) {
            throw new ValidationException(ERROR_ITEM_ID_REQUIRED);
        }
    }
}
