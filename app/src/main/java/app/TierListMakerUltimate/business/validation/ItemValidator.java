package app.TierListMakerUltimate.business.validation;

import static app.TierListMakerUltimate.business.constants.BusinessConstants.*;

import app.TierListMakerUltimate.business.exceptions.ValidationException;
import app.TierListMakerUltimate.models.TierItem;

public class ItemValidator {

    public void validateCreateItem(int tierId, String name, String description) throws ValidationException {
        validateTierId(tierId);
        validateName(name);
        validateDescription(description);
    }

    public void validateUpdateItem(TierItem updatedItem) throws ValidationException {
        validateItemId(updatedItem.getId());
        validateTierId(updatedItem.getTierId());
        validateDescription(updatedItem.getDescription());
        validateName(updatedItem.getName());
    }

    public void validateRemoveItem(int itemId) throws ValidationException {
        validateItemId(itemId);
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

    public void validateName(String tierName) throws ValidationException {
        if (tierName == null || tierName.trim().isEmpty()) {
            throw new ValidationException(ERROR_NAME_REQUIRED);
        }

        if (tierName.length() > MAX_NAME_LENGTH) {
            throw new ValidationException(ERROR_NAME_TOO_LONG);
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
