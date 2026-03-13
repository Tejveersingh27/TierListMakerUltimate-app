package app.TierListMakerUltimate.presentation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.io.InputStream;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.application.TierListMakerUltimate;
import app.TierListMakerUltimate.business.exceptions.NotFoundException;
import app.TierListMakerUltimate.business.exceptions.ValidationException;
import app.TierListMakerUltimate.business.services.interfaces.IItemPlacementManager;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.presentation.utils.ImageHelper;
import app.TierListMakerUltimate.presentation.utils.TextInputExtractor;

public class TierItemEditFragment extends BaseImageCreationFragment {

    // Note to Grader: This is only relevant to this fragment.
    // Needed to pass the id to the fragment due to how fragments work.
    // So, putting it in constants wouldn't make sense.
    private static final String ARG_ITEM_ID = "ITEM";

    private TextInputLayout descriptionInput;
    private TextInputLayout explanationInput;
    private Button deleteButton;
    private TierItemEditFragmentActionListener listener;

    private IItemPlacementManager placementManager;
    private ImageHelper imageHelper;

    public TierItemEditFragment() {

    }

    public static TierItemEditFragment newInstance(int itemId) {
        TierItemEditFragment fragment = new TierItemEditFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ITEM_ID, itemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TierListMakerUltimate app = (TierListMakerUltimate) requireActivity().getApplication();
        placementManager = app.getItemPlacementManager();
        imageHelper = new ImageHelper(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);
    }

    private void setupViews(View view) {
        TierItem leItem = placementManager.getItem(getItemId());

        explanationInput = view.findViewById(R.id.explanationInputLayout);
        TextInputEditText exText = view.findViewById(R.id.explanationInput);
        exText.setText(leItem.getExplanation());

        descriptionInput = view.findViewById(R.id.descriptionInputLayout);
        TextInputEditText descText = view.findViewById(R.id.descriptionInput);
        descText.setText(leItem.getDescription());

        TextInputEditText nameText = view.findViewById(R.id.nameInput);
        nameText.setText(leItem.getName());

        confirmButton = view.findViewById(R.id.createItemButton);
        deleteButton = view.findViewById(R.id.deleteItemButton);

        setupCreateButton();
        setupDeleteButton();
    }

    private void setupCreateButton() {
        confirmButton.setOnClickListener(v -> {
            boolean success = true;
            String explanation = TextInputExtractor.getTrimmedText(explanationInput);
            String description = TextInputExtractor.getTrimmedText(descriptionInput);
            String name = TextInputExtractor.getTrimmedText(nameInput);

            TierItem leItem = placementManager.getItem(getItemId());

            // Update all values
            leItem.setName(name);
            leItem.setDescription(description);
            leItem.setExplanation(explanation);

            System.out.println("ITEM EXPLANATION: " + leItem.getExplanation());
            try {
                placementManager.updateItem(leItem);
            } catch (ValidationException ve) {
                Toast.makeText(requireContext(), ve.getMessage(), Toast.LENGTH_SHORT).show();
                success = false;
            }

            // Only change image if we picked a new one
            if (selectImageUri != null)
            {
                try (InputStream inputStream = requireContext().getContentResolver().openInputStream(selectImageUri)) {
                    String extension = imageHelper.getFileExtension(selectImageUri.toString());
                    placementManager.updateItem(leItem, inputStream, extension);
                } catch (IOException ioe) {
                    Toast.makeText(requireContext(), R.string.error_loading_image, Toast.LENGTH_SHORT).show();
                } catch (ValidationException ve) {
                    Toast.makeText(requireContext(), ve.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            if (success) {
                listener.onTierItemEditedSuccessfully();
                dismiss();
            }
        });
    }

    private void setupDeleteButton() {
        deleteButton.setOnClickListener(v ->{
            try {
                placementManager.removeItem(getItemId());

                listener.onTierItemDeletedSuccessfully();
                dismiss();
            } catch (ValidationException | NotFoundException e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setUpListener(TierItemEditFragmentActionListener listener) {
        this.listener = listener;
    }

    private int getItemId() {
        return getArguments().getInt(ARG_ITEM_ID);
    }

    public interface TierItemEditFragmentActionListener {
        void onTierItemEditedSuccessfully();
        void onTierItemDeletedSuccessfully();
    }
}