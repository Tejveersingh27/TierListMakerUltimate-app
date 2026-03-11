package app.TierListMakerUltimate.presentation.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.presentation.utils.TextInputExtractor;


public class TierItemCreationFragment extends BaseCreationFragment {
    private TextInputLayout descriptionInput;
    private TierItemCreationFragmentActionListener listener;

    public TierItemCreationFragment() {
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

    @Override
    protected String getDefaultImagePath() {
        return null;
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
            if (listener != null) {
                listener.onTierItemCreate(name, description, selectImageUri);
            }
        });
    }

    public void setUpListener(TierItemCreationFragmentActionListener listener) {
        this.listener = listener;
    }

    public interface TierItemCreationFragmentActionListener {
        void onTierItemCreate(String name, String description, Uri uri);
    }
}
