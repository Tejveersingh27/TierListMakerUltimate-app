package app.TierListMakerUltimate.presentation.fragments;

import static app.TierListMakerUltimate.presentation.constants.PresentationConstants.DEFAULT_TIER_LIST_IMAGE_PATH;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.net.URI;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.presentation.utils.TextInputExtractor;


public class TierListCreationFragment extends BaseCreationFragment {
    private TierListCreationFragmentActionListener listener;

    public TierListCreationFragment() {
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

    @Override
    protected String getDefaultImagePath() {
        return DEFAULT_TIER_LIST_IMAGE_PATH;
    }

    private void setupViews(View view) {
        confirmButton = view.findViewById(R.id.createTierListButton);

        setupCreateButton();
    }

    private void setupCreateButton() {
        confirmButton.setOnClickListener(v -> {
            String name = TextInputExtractor.getTrimmedText(nameInput);
            if (listener != null) {
                listener.onTierListCreate(name, selectImageUri);
            }
        });
    }


    public void setUpListener(TierListCreationFragmentActionListener listener) {
        this.listener = listener;
    }

    public interface TierListCreationFragmentActionListener {
        void onTierListCreate(String name, Uri uri);
    }
}
