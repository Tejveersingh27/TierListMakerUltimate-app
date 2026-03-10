package app.TierListMakerUltimate.presentation.fragments;

import static app.TierListMakerUltimate.presentation.constants.PresentationConstants.DEFAULT_TIER_LIST_IMAGE_PATH;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.presentation.utils.TextInputExtractor;


public class TierItemCreationFragment extends BottomSheetDialogFragment {
    private ActivityResultLauncher<PickVisualMediaRequest> imagePicker;
    private ImageButton selectImageButton;
    private Button createListButton;
    private TextInputLayout descriptionInput;
    private TextInputLayout nameInput;
    private Uri selectImageUri;
    private TierItemCreationFragmentActionListener listener;

    public TierItemCreationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Needs to be registered here, or app will crash
        imagePicker = registerForActivityResult(new PickVisualMedia(), uri -> {
            selectImageUri = uri;
            int padding = getResources().getDimensionPixelSize(R.dimen.image_padding);
            selectImageButton.setPadding(padding, padding, padding, padding);
            selectImageButton.setImageURI(uri);
            selectImageButton.setBackground(null);
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_creator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        selectImageUri = Uri.parse(DEFAULT_TIER_LIST_IMAGE_PATH);
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);
    }

    private void setupViews(View view) {
        nameInput = view.findViewById(R.id.textInputLayout);
        descriptionInput = view.findViewById(R.id.descriptionInputLayout);
        selectImageButton = view.findViewById(R.id.selectImageButton);
        createListButton = view.findViewById(R.id.createItemButton);

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
            String description = TextInputExtractor.getTrimmedText(descriptionInput);
            listener.onTierItemCreate(name, description, selectImageUri);
        });
    }


    public void setUpListener(TierItemCreationFragmentActionListener listener) {
        this.listener = listener;
    }

    public interface TierItemCreationFragmentActionListener {
        void onTierItemCreate(String name, String description, Uri uri);
    }
}
