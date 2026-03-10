package app.TierListMakerUltimate.presentation.fragments;

import static app.TierListMakerUltimate.presentation.constants.PresentationConstants.*;

import android.net.Uri;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.presentation.utils.TextInputExtractor;


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
        selectImageUri = Uri.parse(DEFAULT_TIER_LIST_IMAGE_PATH);
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
            String name = TextInputExtractor.getTrimmedText(nameInput);
            listener.onTierListCreate(name, selectImageUri);
        });
    }


    public void setUpListener(TierListCreationFragmentActionListener listener) {
        this.listener = listener;
    }

    public interface TierListCreationFragmentActionListener {
        void onTierListCreate(String name, Uri uri);
    }
}
