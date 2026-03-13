package app.TierListMakerUltimate.business.validation;

import static app.TierListMakerUltimate.business.constants.BusinessConstants.*;

import app.TierListMakerUltimate.business.exceptions.ValidationException;
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
        checkString(name, ERROR_NAME_REQUIRED);
        if (name.length() > MAX_NAME_LENGTH) {
            throw new ValidationException(ERROR_NAME_TOO_LONG);
        }
    }


    public void validateTierListId(int tierListId) throws ValidationException {
        if (tierListId <= 0) {
            throw new ValidationException(ERROR_TIER_LIST_ID_REQUIRED);
        }
    }

    public void validateTierListThumbnail(String thumbnailPath) throws ValidationException {
        checkString(thumbnailPath, ERROR_THUMBNAIL_PATH_REQUIRED);
    }

    private void checkString(String string, String errorMessage) throws ValidationException {
        if (string == null || string.trim().isEmpty()) {
            throw new ValidationException(errorMessage);
        }
    }
}
