package app.TierListMakerUltimate.business.validation;

import app.TierListMakerUltimate.business.exception.ValidationException;

public class TierValidator {
    public static final int MAX_LABEL_LENGTH = 60;
    public static final String COLOR_REGEX = "^#[0-9A-Fa-f]{6}$";

    public static void validateLabel(String label) {
        if (label == null || label.trim().isEmpty()) {
            throw new ValidationException("Label is required.");
        }
        if (label.trim().length() > MAX_LABEL_LENGTH) {
            throw new ValidationException("Label must be " + MAX_LABEL_LENGTH + " characters or less.");
        }
    }

    public static void validateColor(String color) {
        if (color == null || !color.matches(COLOR_REGEX)) {
            throw new ValidationException("Color must be a valid hex code.");
        }
    }

    public static void validateTier(String label, String color) {
        validateLabel(label);
        validateColor(color);
    }
}
