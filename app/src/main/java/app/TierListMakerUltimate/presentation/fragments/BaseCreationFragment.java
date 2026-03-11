package app.TierListMakerUltimate.presentation.fragments;

import static app.TierListMakerUltimate.presentation.constants.PresentationConstants.DEFAULT_TIER_LIST_IMAGE_PATH;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.net.URI;

import app.TierListMakerUltimate.R;

public abstract class BaseCreationFragment extends BottomSheetDialogFragment {
    protected ActivityResultLauncher<PickVisualMediaRequest> imagePicker;
    protected ImageButton selectImageButton;
    protected Button confirmButton;
    protected TextInputLayout nameInput;
    protected Uri selectImageUri;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Needs to be registered here, or app will crash (Runs on image selected)
        imagePicker = registerForActivityResult(new PickVisualMedia(), uri -> {
            if (uri != null) {
                setImageViewProperties(uri);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getDefaultImagePath() != null) {
            selectImageUri = Uri.parse(getDefaultImagePath());
        }
        nameInput = view.findViewById(R.id.textInputLayout);
        selectImageButton = view.findViewById(R.id.selectImageButton);
        setupSelectImageButton();
    }

    private void setupSelectImageButton() {
        selectImageButton.setOnClickListener(v -> {
            imagePicker.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
    }

    protected abstract String getDefaultImagePath();


    private void setImageViewProperties(Uri uri) {
        selectImageUri = uri;
        int padding = getResources().getDimensionPixelSize(R.dimen.image_padding);
        selectImageButton.setPadding(padding, padding, padding, padding);
        selectImageButton.setImageURI(uri);
        selectImageButton.setBackground(null);
    }
}
