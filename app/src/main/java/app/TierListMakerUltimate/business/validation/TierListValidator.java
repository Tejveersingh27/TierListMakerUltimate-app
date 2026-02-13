package app.TierListMakerUltimate.business.validation;

import app.TierListMakerUltimate.business.exception.ValidationException;

public class TierListValidator {

    public void validateCreateTierList(String name) {
        validateName(name);
    }

    public void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Valid name is Required");
        }
    }

    public void validateRemoveTierList(int tierListId) {
        validateTierListId(tierListId);
    }

    public void validateTierListId(int tierListId) {
        if (tierListId <= 0) {
            throw new ValidationException("TierList Id is required");
        }
    }
}
