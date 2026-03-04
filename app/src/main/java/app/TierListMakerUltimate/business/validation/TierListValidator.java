package app.TierListMakerUltimate.business.validation;

import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.models.TierList;

public class TierListValidator {


    public void validateUpdateTierList(TierList tierList) throws ValidationException {
        validateTierListId(tierList.getId());
        validateTierListName(tierList.getName());
    }

    public void validateCreateTierList(String name) throws ValidationException {
        validateTierListName(name);
    }

    public void validateDeleteTierList(int tierListId) throws ValidationException {
        validateTierListId(tierListId);
    }

    public void validateTierListName(String name) throws ValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Valid name is Required");
        }
    }


    public void validateTierListId(int tierListId) throws ValidationException {
        if (tierListId <= 0) {
            throw new ValidationException("TierList Id is required");
        }
    }
}
