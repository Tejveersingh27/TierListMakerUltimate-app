package app.TierListMakerUltimate.presentation.activities;

import static app.TierListMakerUltimate.presentation.constants.PresentationConstants.*;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.business.services.ITierListManager;

import app.TierListMakerUltimate.application.TierListMakerUltimate;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.presentation.MainActivity;
import app.TierListMakerUltimate.presentation.adapters.TierListBrowserAdapter;
import app.TierListMakerUltimate.presentation.fragments.TierListCreationFragment;

public class TierListBrowserActivity extends AppCompatActivity implements TierListBrowserAdapter.TierListBrowserItemActionListener, TierListCreationFragment.TierListCreationFragmentActionListener {
    private RecyclerView recyclerView;
    private TierListBrowserAdapter adapter;

    private ITierListManager tierListManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tierlist_browser);

        TierListMakerUltimate app = (TierListMakerUltimate) getApplication();
        tierListManager = app.getTierListManager();

        setupToolbar();
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

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarMaterial);
        toolbar.setTitle(R.string.tier_list_browser_title);
        setSupportActionBar(toolbar);
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TierListBrowserAdapter(tierListManager.getAllNonTemplateTierLists(), this);
        recyclerView.setAdapter(adapter);
    }

    private void refreshList() {
        if (adapter != null) {
            List<TierList> updatedList = tierListManager.getAllNonTemplateTierLists();
            adapter.updateData(updatedList);
        }
    }

    private void setupAddButton() {
        Button createButton = findViewById(R.id.createTierListButton);
        createButton.setOnClickListener(v -> {
            Intent intent = new Intent(TierListBrowserActivity.this, TemplateBrowserActivity.class);
            startActivity(intent);
        });
    }


    @Override
    public void onEditButtonClick(TierList tierList) {
        Intent intent = new Intent(TierListBrowserActivity.this, MainActivity.class);
        intent.putExtra(INTENT_TIER_LIST_ID, tierList.getId());
        intent.putExtra(INTENT_TIER_LIST_NAME, tierList.getName());
        startActivity(intent);
    }

    @Override
    public void onConfigButtonClick(TierList tierList) {
        TierListCreationFragment fragment = TierListCreationFragment.newInstance(tierList.getId());
        fragment.setUpListener(this);
        fragment.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onDeleteButtonClick(TierList tierList) {
        tierListManager.removeTierList(tierList.getId());
        adapter.updateData(tierListManager.getAllNonTemplateTierLists());
    }

    @Override
    public void onTierListCreatedSuccessfully(TierList newTierList) {
        refreshList();
    }
}
