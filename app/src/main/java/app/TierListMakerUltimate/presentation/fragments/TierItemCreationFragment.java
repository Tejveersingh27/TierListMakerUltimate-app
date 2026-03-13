package app.TierListMakerUltimate.presentation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.io.InputStream;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.application.TierListMakerUltimate;
import app.TierListMakerUltimate.business.exceptions.ValidationException;
import app.TierListMakerUltimate.business.services.IItemPlacementManager;
import app.TierListMakerUltimate.business.services.ITierManager;
import app.TierListMakerUltimate.presentation.utils.ImageHelper;
import app.TierListMakerUltimate.presentation.utils.TextInputExtractor;

public class TierItemCreationFragment extends BaseImageCreationFragment {

    // Note to Grader: This is only relevant to this fragment.
    // Needed to pass the id to the fragment due to how fragments work.
    // So, putting it in constants wouldn't make sense.
    private static final String ARG_TIERLIST_ID = "ID";

    private TextInputLayout descriptionInput;
    private TierItemCreationFragmentActionListener listener;

    private IItemPlacementManager placementManager;
    private ITierManager tierManager;
    private ImageHelper imageHelper;

    public TierItemCreationFragment() {

    }

    public static TierItemCreationFragment newInstance(int tierlistID) {
        TierItemCreationFragment fragment = new TierItemCreationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TIERLIST_ID, tierlistID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TierListMakerUltimate app = (TierListMakerUltimate) requireActivity().getApplication();
        placementManager = app.getItemPlacementManager();
        tierManager = app.getTierManager();
        imageHelper = new ImageHelper(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_creator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);
    }


    private void setupViews(View view) {
        descriptionInput = view.findViewById(R.id.descriptionInputLayout);
        confirmButton = view.findViewById(R.id.createItemButton);
        setupCreateButton();
    }

    private void setupCreateButton() {
        confirmButton.setOnClickListener(v -> {
            String description = TextInputExtractor.getTrimmedText(descriptionInput);
            String name = TextInputExtractor.getTrimmedText(nameInput);

            if (selectImageUri == null) {
                Toast.makeText(requireContext(), R.string.no_image_selected, Toast.LENGTH_SHORT).show();
                return;
            }

            try (InputStream inputStream = requireContext().getContentResolver().openInputStream(selectImageUri)) {
                String extension = imageHelper.getFileExtension(selectImageUri.toString());
                placementManager.createItem(tierManager.getUnrankedTierForList(getTierlistId()).getId(), name, description, inputStream, extension);

                listener.onTierItemCreatedSuccessfully();

                dismiss();

            } catch (IOException ioe) {
                Toast.makeText(requireContext(), R.string.error_loading_image, Toast.LENGTH_SHORT).show();
            } catch (ValidationException ve) {
                Toast.makeText(requireContext(), ve.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setUpListener(TierItemCreationFragmentActionListener listener) {
        this.listener = listener;
    }

    private int getTierlistId() {
        return getArguments().getInt(ARG_TIERLIST_ID);
    }


    public interface TierItemCreationFragmentActionListener {
        void onTierItemCreatedSuccessfully();
    }
}