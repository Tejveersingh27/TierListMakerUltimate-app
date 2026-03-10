package app.TierListMakerUltimate.presentation.fragments;

import android.net.Uri;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.io.InputStream;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.presentation.constants.PresentationConstants;


public class TierListCreationFragment extends BottomSheetDialogFragment {
    private ActivityResultLauncher<PickVisualMediaRequest> imagePicker;
    private ImageButton selectImageButton;
    private Button createListButton;
    private TextInputLayout nameInput;
    private Uri selectImageUri;
    private TierListCreationFragmentActionListener listener;

    public TierListCreationFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Needs to be registered here, or app will crash
        imagePicker = registerForActivityResult(new PickVisualMedia(), uri -> {
            if (uri != null) {
                selectImageUri = uri;
                selectImageButton.setPadding(10, 10, 10, 10);
                selectImageButton.setImageURI(uri);
                selectImageButton.setBackground(null);
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tierlist_creator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);
    }

    private void setupViews(View view) {
        nameInput = view.findViewById(R.id.textInputLayout);
        selectImageButton = view.findViewById(R.id.selectImageButton);
        createListButton = view.findViewById(R.id.createTierListButton);

        setupCreateButton();
        setupSelectImageButton();
    }

    private void setupSelectImageButton() {
        selectImageButton.setOnClickListener(v -> {
            imagePicker.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
    }

    private void setupCreateButton() {
        createListButton.setOnClickListener(v -> {
            String name = (nameInput.getEditText() != null)
                    ? nameInput.getEditText().getText().toString().trim()
                    : "";

            if (validateInputs(name)) {
                createTierList(name);
            }
        });
    }

    private boolean validateInputs(String name) {
        boolean isValid = true;

        if (name.isEmpty()) {
            nameInput.setError(PresentationConstants.ERROR_NAME_REQUIRED);
            isValid = false;
        } else {
            nameInput.setError(null);
        }

        if (selectImageUri == null) {
            Toast.makeText(getContext(), PresentationConstants.ERROR_IMAGE_REQUIRED, Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    private void createTierList(String name) {
        if (listener != null) {
            try (InputStream inputStream = requireContext().getContentResolver().openInputStream(selectImageUri)) {
                String extension = getFileExtension();
                listener.onTierListCreate(name, inputStream, extension);
                dismiss();
            } catch (IOException ioe) {
                Toast.makeText(getContext(), PresentationConstants.ERROR_LOADING_IMAGE, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getFileExtension() {
        String mimeType = requireContext().getContentResolver().getType(selectImageUri);
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
    }

    public void setActionListener(TierListCreationFragmentActionListener listener) {
        this.listener = listener;
    }

    public interface TierListCreationFragmentActionListener {
        void onTierListCreate(String name, InputStream imageFile, String extension);
    }
}
