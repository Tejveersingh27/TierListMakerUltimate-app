package app.TierListMakerUltimate.business.validation;

import app.TierListMakerUltimate.business.exception.ValidationException;

public class ItemValidator {
    private static final int MAX_LENGTH_DESCRIPTION = 100;

    public void validateCreateItem(String localImagePath, int tierId, String description) {
        validateImagePath(localImagePath);
        validateTierId(tierId);
        validateDescription(description);
    }

    public void validateImagePath(String localImagePath) {
        if (localImagePath == null || localImagePath.trim().isEmpty()) {
            throw new ValidationException("Image path is required");
        }
    }

    public void validateTierId(int tierId) {
        if (tierId <= 0) {
            throw new ValidationException("Tier Id is required");
        }
    }

    public void validateDescription(String description) {
        if (description == null) {
            throw new ValidationException("Description cannot be null");
        }
        if (description.length() > MAX_LENGTH_DESCRIPTION) {
            throw new ValidationException("Description should be less than " + MAX_LENGTH_DESCRIPTION + " characters");
        }
    }

    public void validateMoveItemToTier(int itemId, int targetTierId) {
        validateItemId(itemId);
        validateTierId(targetTierId);
    }

    public void validateItemId(int itemId) {
        if (itemId <= 0) {
            throw new ValidationException("Item Id is required.");
        }
    }
}
