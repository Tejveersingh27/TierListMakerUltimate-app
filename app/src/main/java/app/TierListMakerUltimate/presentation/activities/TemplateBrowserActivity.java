package app.TierListMakerUltimate.presentation.activities;

import static app.TierListMakerUltimate.presentation.constants.PresentationConstants.*;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.application.TierListMakerUltimate;
import app.TierListMakerUltimate.business.services.interfaces.ITierListCoordinator;
import app.TierListMakerUltimate.business.services.interfaces.ITierListManager;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.presentation.adapters.TemplateBrowserAdapter;
import app.TierListMakerUltimate.presentation.fragments.TierListCreationFragment;
import app.TierListMakerUltimate.presentation.utils.ImageHelper;

public class TemplateBrowserActivity extends AppCompatActivity implements TierListCreationFragment.TierListCreationFragmentActionListener {

    private RecyclerView recyclerView;
    private TemplateBrowserAdapter adapter;
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

        setupToolbar();
        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarMaterial);
        toolbar.setTitle(R.string.template_browser_title);
        setSupportActionBar(toolbar);
    }

    private void setupViews() {
        setupRecyclerView();
        setupAddButton();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TemplateBrowserAdapter(new ArrayList<>(), imageHelper, tierList -> {
            switchToTierListEditor(tierListCoordinator.deepCopyAsTemplate(tierList.getId(), false));
        });

        recyclerView.setAdapter(adapter);
    }

    private void refreshList() {
        if (adapter != null) {
            List<TierList> updatedList = tierListManager.getAllTemplates();
            adapter.updateData(updatedList);
        }
    }

    private void setupAddButton() {
        Button createButton = findViewById(R.id.createTierListButton);
        createButton.setOnClickListener(v -> showFragment());
    }

    private void showFragment() {
        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TIER_LIST_CREATION) != null) {
            return;
        }

        TierListCreationFragment fragment = new TierListCreationFragment();
        fragment.setUpListener(this);
        fragment.show(getSupportFragmentManager(), FRAGMENT_TIER_LIST_CREATION);
    }

    @Override
    public void onTierListCreatedSuccessfully(TierList newTierList, String tag) {
        refreshList();
        switchToTierListEditor(newTierList);
    }

    private void switchToTierListEditor(TierList tierList) {
        Intent intent = new Intent(TemplateBrowserActivity.this, MainActivity.class);
        intent.putExtra(INTENT_TIER_LIST_ID, tierList.getId());
        intent.putExtra(INTENT_TIER_LIST_NAME, tierList.getName());
        startActivity(intent);
    }
}