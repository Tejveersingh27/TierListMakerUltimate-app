package app.TierListMakerUltimate.presentation.utils;

import com.google.android.material.textfield.TextInputLayout;

public class TextInputExtractor {
    private TextInputExtractor() {
    }

    public static String getTrimmedText(TextInputLayout layout) {
        if (layout != null && layout.getEditText() != null) {
            return layout.getEditText().getText().toString().trim();
        }
        return "";
    }
}
