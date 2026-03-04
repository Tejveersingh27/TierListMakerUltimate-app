package app.TierListMakerUltimate.business.validation;

import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.Tier;

public class TierValidator {
    public static final int MAX_LABEL_LENGTH = 60;
    public static final String COLOR_REGEX = "^#[0-9A-Fa-f]{6}$";

    public void validateLabel(String label) throws ValidationException {
        if (label == null || label.trim().isEmpty()) {
            throw new ValidationException("Label is required.");
        }
        if (label.trim().length() > MAX_LABEL_LENGTH) {
            throw new ValidationException("Label must be " + MAX_LABEL_LENGTH + " characters or less.");
        }
    }

    public void validateColor(String color) throws ValidationException {
        if (color == null || !color.matches(COLOR_REGEX)) {
            throw new ValidationException("Color must be a valid hex code.");
        }
    }

    public void validateWritePermission(boolean isUnRanked) throws ValidationException {
        if (isUnRanked) {
            throw new ValidationException("Unranked tiers cannot be edited.");
        }
    }

    public void validateCreateTier(String label, String color) throws ValidationException {
        validateLabel(label);
        validateColor(color);
    }

    public void validateRemoveTier(int tierId) throws ValidationException {
        validateTierId(tierId);
    }

    public void validateUpdateTier(Tier tier) throws ValidationException {
        validateTierId(tier.getId());
        validateLabel(tier.getName());
        validateColor(tier.getColor());
        validateWritePermission(tier.isUnranked());
    }


    public void validateTierId(int tierId) throws ValidationException {
        if (tierId <= 0) {
            throw new ValidationException("Tier Id is required.");
        }
    }

    public void validateTierListId(int tierListId) throws ValidationException {
        if (tierListId <= 0) {
            throw new ValidationException("Tier List Id is required.");
        }
    }
}
