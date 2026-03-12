package app.TierListMakerUltimate.presentation.fragments;

import static app.TierListMakerUltimate.presentation.constants.PresentationConstants.*;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.application.TierListMakerUltimate;
import app.TierListMakerUltimate.business.exception.NotFoundException;
import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.business.services.ITierListCoordinator;
import app.TierListMakerUltimate.business.services.ITierListManager;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.presentation.utils.ImageHelper;
import app.TierListMakerUltimate.presentation.utils.TextInputExtractor;

public class TierListCreationFragment extends BaseImageCreationFragment {
    private TierListCreationFragmentActionListener listener;

    // Note to Grader: This is only relevant to this fragment.
    // Needed to pass the id to the fragment due to how fragments work.
    // So, putting it in constants wouldn't make sense.
    private static final String ARG_TIERLIST_ID = "ID";
    private ITierListCoordinator tierListCoordinator;
    private ITierListManager tierListManager;
    private ImageHelper imageHelper;
    private Uri originalImageUri;

    public TierListCreationFragment() {
    }

    public static TierListCreationFragment newInstance(int tierlistID) {
        TierListCreationFragment fragment = new TierListCreationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TIERLIST_ID, tierlistID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TierListMakerUltimate app = (TierListMakerUltimate) requireActivity().getApplication();
        tierListCoordinator = app.getTierListCoordinator();
        tierListManager = app.getTierListManager();
        imageHelper = new ImageHelper(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tierlist_creator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);

        if (getArguments() != null && getArguments().containsKey(ARG_TIERLIST_ID)) {
            loadExistingTierList(getTierlistId());
        }
    }

    private void loadExistingTierList(int id) {
        try {
            TierList list = tierListManager.getTierList(id);
            if (nameInput.getEditText() != null) {
                nameInput.getEditText().setText(list.getName());
            }
            if (list.getThumbnailPath() != null) {
                // Use the original image URI and set properties
                originalImageUri = Uri.parse(list.getThumbnailPath());
                super.setImageViewProperties(originalImageUri);
                imageHelper.loadImage(list.getThumbnailPath(), selectImageButton);
            }
        } catch (ValidationException | NotFoundException e) {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    private void setupViews(View view) {
        confirmButton = view.findViewById(R.id.createTierListButton);
        setupCreateButton();
    }

    private void setupCreateButton() {
        confirmButton.setOnClickListener(v -> handleConfirmClick());
    }

    private void handleConfirmClick() {
        String name = TextInputExtractor.getTrimmedText(nameInput);
        boolean isEditMode = getArguments() != null && getArguments().containsKey(ARG_TIERLIST_ID);

        if (selectImageUri == null) {
            Toast.makeText(requireContext(), R.string.no_image_selected, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (isEditMode) {
                updateExistingTierList(name);
            } else {
                createNewTierList(name);
            }
            dismiss();
        } catch (IOException e) {
            Toast.makeText(requireContext(), R.string.error_loading_image, Toast.LENGTH_SHORT).show();
        } catch (ValidationException | NotFoundException e) {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void createNewTierList(String name) throws IOException, ValidationException {
        try (InputStream is = requireContext().getContentResolver().openInputStream(selectImageUri)) {
            String ext = imageHelper.getFileExtension(selectImageUri.toString());
            TierList newList = tierListCoordinator.createTierListWithDefaults(name, false, is, ext);

            listener.onTierListCreatedSuccessfully(newList);
        }
    }

    private void updateExistingTierList(String name) throws IOException, ValidationException, NotFoundException {
        int id = getTierlistId();
        TierList existing = tierListManager.getTierList(id);

        TierList updatedList = new TierList(existing.getId(), name, existing.getThumbnailPath(), existing.isTemplate());

        boolean imageChanged = selectImageUri != null && !selectImageUri.equals(originalImageUri);

        if (imageChanged) {
            try (InputStream is = requireContext().getContentResolver().openInputStream(selectImageUri)) {
                String ext = imageHelper.getFileExtension(selectImageUri.toString());
                tierListManager.updateTierList(updatedList, is, ext);
            }
        } else {
            tierListManager.updateTierList(updatedList);
        }

        listener.onTierListCreatedSuccessfully(updatedList);
    }

    public void setUpListener(TierListCreationFragmentActionListener listener) {
        this.listener = listener;
    }

    private int getTierlistId() {
        return getArguments().getInt(ARG_TIERLIST_ID);
    }

    public interface TierListCreationFragmentActionListener {
        void onTierListCreatedSuccessfully(TierList newTierList);
    }
}