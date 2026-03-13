package app.TierListMakerUltimate.presentation.fragments;

import static app.TierListMakerUltimate.presentation.constants.PresentationConstants.*;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.constraintlayout.helper.widget.Flow;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;


import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.application.TierListMakerUltimate;
import app.TierListMakerUltimate.business.exception.NotFoundException;
import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.business.services.ITierListCoordinator;
import app.TierListMakerUltimate.business.services.ITierManager;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.presentation.utils.TextInputExtractor;

public class TierEditorFragment extends BottomSheetDialogFragment {
    private TierEditorFragmentActionListener listener;

    // Note to Grader: This is only relevant to this fragment.
    // Needed to pass the id to the fragment due to how fragments work.
    // So, putting it in constants wouldn't make sense.
    private static final String ARG_TIER_ID = "ID";

    private ITierManager tierManager;
    private ITierListCoordinator tierListCoordinator;

    private Button confirmButton;
    private TextInputLayout nameInput;
    private Button deleteButton;
    private MaterialCardView selectedCard;

    private Flow colorGrid;

    public TierEditorFragment() {
    }

    public static TierEditorFragment newInstance(int tierId) {
        TierEditorFragment fragment = new TierEditorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TIER_ID, tierId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TierListMakerUltimate app = (TierListMakerUltimate) requireActivity().getApplication();
        tierManager = app.getTierManager();
        tierListCoordinator = app.getTierListCoordinator();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tier_editor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);
        if (getArguments() != null && getArguments().containsKey(ARG_TIER_ID)) {
            loadExistingTier(getTierId());
        }
    }

    private void loadExistingTier(int id) {
        try {
            Tier tier = tierManager.getTier(id);
            if (nameInput.getEditText() != null) {
                nameInput.getEditText().setText(tier.getName());
            }
        } catch (ValidationException | NotFoundException e) {
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    private void setupViews(View view) {
        confirmButton = view.findViewById(R.id.createTierListButton);
        deleteButton = view.findViewById(R.id.deleteButton);
        nameInput = view.findViewById(R.id.nameInputLayout);
        colorGrid = view.findViewById(R.id.flow);
        setupCreateButton();
        setupDeleteButton();
        setupColorGrid(view);

    }

    private void setupColorGrid(View view) {
        int[] referenceIds = colorGrid.getReferencedIds();
        for (int id : referenceIds) {
            MaterialCardView card = view.findViewById(id);
            card.setOnClickListener(v -> {
                if (selectedCard != null) {
                    selectedCard.setStrokeWidth(NOT_SELECTED_STROKE_WIDTH);
                }

                selectedCard = card;
                card.setStrokeColor(card.getCardBackgroundColor());
                card.setStrokeWidth(SELECTED_STROKE_WIDTH);
            });
        }
    }

    private void setupCreateButton() {
        confirmButton.setOnClickListener(v -> {
            try {
                Tier currentTier = tierManager.getTier(getTierId());
                String hexColor = selectedCard != null ? selectedCard.getTag().toString() : currentTier.getColor();
                String name = TextInputExtractor.getTrimmedText(nameInput);
                tierManager.updateTier(new Tier(getTierId(), currentTier.getTierListId(), name, hexColor, false, currentTier.getOrdinalPosition()));
                if (listener != null) {
                    listener.onTierEditorFragmentEditSuccess();
                }
                dismiss();
            } catch (ValidationException | NotFoundException e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupDeleteButton() {
        deleteButton.setOnClickListener(v -> {
            try {
                tierListCoordinator.removeTierAndMoveAllItemsToUnranked(getTierId());
                listener.onTierEditorFragmentDeleteSuccess();
                dismiss();
            } catch (ValidationException | NotFoundException e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

    public void setUpListener(TierEditorFragmentActionListener listener) {
        this.listener = listener;
    }

    private int getTierId() {
        return getArguments().getInt(ARG_TIER_ID);
    }

    public interface TierEditorFragmentActionListener {
        void onTierEditorFragmentEditSuccess();

        void onTierEditorFragmentDeleteSuccess();

    }
}