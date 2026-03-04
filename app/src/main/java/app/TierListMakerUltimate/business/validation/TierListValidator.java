package app.TierListMakerUltimate.business.validation;

import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.TierList;

public class TierListValidator {


    public void validateUpdateTierList(TierList tierList) {
        validateTierListId(tierList.getId());
        validateTierListName(tierList.getName());
    }

    public void validateCreateTierList(String name) {
        validateTierListName(name);
    }

    public void validateDeleteTierList(int tierListId) {
        validateTierListId(tierListId);
    }

    public void validateTierListName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Valid name is Required");
        }
    }


    public void validateTierListId(int tierListId) {
        if (tierListId <= 0) {
            throw new ValidationException("TierList Id is required");
        }
    }
}
