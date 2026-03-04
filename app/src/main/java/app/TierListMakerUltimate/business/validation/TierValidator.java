package app.TierListMakerUltimate.business.validation;

import static app.TierListMakerUltimate.business.constants.BusinessConstants.*;

import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.Tier;

public class TierValidator {

    public void validateLabel(String label) throws ValidationException {
        if (label == null || label.trim().isEmpty()) {
            throw new ValidationException(ERROR_LABEL_REQUIRED);
        }
        if (label.trim().length() > MAX_LABEL_LENGTH) {
            throw new ValidationException(ERROR_LABEL_TOO_LONG);
        }
    }

    public void validateColor(String color) throws ValidationException {
        if (color == null || !color.matches(COLOR_REGEX)) {
            throw new ValidationException(ERROR_COLOR_INVALID);
        }
    }

    public void validateWritePermission(boolean isUnRanked) throws ValidationException {
        if (isUnRanked) {
            throw new ValidationException(ERROR_UNRANKED_EDIT);
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
            throw new ValidationException(ERROR_TIER_ID_REQUIRED);
        }
    }

    public void validateTierListId(int tierListId) throws ValidationException {
        if (tierListId <= 0) {
            throw new ValidationException(ERROR_TIER_LIST_ID_REQUIRED);
        }
    }
}
