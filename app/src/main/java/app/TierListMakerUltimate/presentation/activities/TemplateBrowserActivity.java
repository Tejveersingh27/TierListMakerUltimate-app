package app.TierListMakerUltimate.presentation.activities;

import static app.TierListMakerUltimate.presentation.constants.PresentationConstants.*;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.application.TierListMakerUltimate;
import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.business.services.ITierListCoordinator;
import app.TierListMakerUltimate.business.services.ITierListManager;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.presentation.adapters.TemplateBrowserAdapter;
import app.TierListMakerUltimate.presentation.fragments.TierListCreationFragment;
import app.TierListMakerUltimate.presentation.utils.ImageHelper;

public class TemplateBrowserActivity extends AppCompatActivity implements TemplateBrowserAdapter.TemplateBrowserActionListener, TierListCreationFragment.TierListCreationFragmentActionListener {
    private RecyclerView recyclerView;
    private TemplateBrowserAdapter adapter;
    private TierListCreationFragment fragment;
    private ImageHelper imageHelper;

    private ITierListManager tierListManager;
    private ITierListCoordinator tierListCoordinator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_browser);

        TierListMakerUltimate app = (TierListMakerUltimate) getApplication();
        tierListManager = app.getTierListManager();
        tierListCoordinator = app.getTierListCoordinator();
        imageHelper = new ImageHelper(this);
        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void setupViews() {
        setupRecyclerView();
        setupAddButton();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TemplateBrowserAdapter(tierListManager.getAllTemplates(), imageHelper, this);
        recyclerView.setAdapter(adapter);
    }

    private void refreshList() {
        if (adapter != null) {
            List<TierList> updatedList = tierListManager.getAllTemplates();
            adapter.updateData(updatedList);
        }
    }

    private void showFragment() {
        fragment = new TierListCreationFragment();
        fragment.setUpListener(this);
        fragment.show(getSupportFragmentManager(), "TierListCreationFragment");
    }

    private void setupAddButton() {
        Button createButton = findViewById(R.id.createTierListButton);
        createButton.setOnClickListener(v -> {
            showFragment();
        });
    }

    @Override
    public void onCardClick(TierList tierList) {
        //TODO: hook up to tierlist creation screen
    }

    @Override
    public void onTierListCreate(String name, Uri uri) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
            tierListCoordinator.createTierListWithDefaults(name, false, inputStream, imageHelper.getFileExtension(uri.toString()));
            refreshList();
            fragment.dismiss();
        } catch (IOException ioe) {
            Toast.makeText(this, ERROR_IMAGE_REQUIRED, Toast.LENGTH_SHORT).show();
        } catch (ValidationException ve) {
            Toast.makeText(this, ve.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
