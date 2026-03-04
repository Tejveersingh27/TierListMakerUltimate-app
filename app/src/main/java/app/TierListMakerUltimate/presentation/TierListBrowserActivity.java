package app.TierListMakerUltimate.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.business.services.ITierListCoordinator;
import app.TierListMakerUltimate.business.services.TierListManager;

import app.TierListMakerUltimate.application.TierListMakerUltimate;
import app.TierListMakerUltimate.models.TierList;

public class TierListBrowserActivity extends AppCompatActivity implements TierListBrowserAdapter.TierListBrowserActionListener {
    private RecyclerView recyclerView;
    private TierListBrowserAdapter adapter;

    private TierListManager tierListManager;
    private ITierListCoordinator tierListCoordinator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tierlist_browser);

        TierListMakerUltimate app = (TierListMakerUltimate) getApplication();
        tierListManager = app.getTierListManager();
        tierListCoordinator = app.getTierListCoordinator();

        setupRecyclerView();
        setupAddButton();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TierListBrowserAdapter(tierListManager.getAllTierLists(), this);
        recyclerView.setAdapter(adapter);
    }

    private void setupAddButton() {
        Button createButton = findViewById(R.id.createTierListButton);
        createButton.setOnClickListener(v -> {
            tierListCoordinator.addTierList("New Tier List");
            TierList createdTierList = tierListCoordinator.addTierList("Untitled");
            adapter.addItem(createdTierList);
        });
        // TODO: hook up to tierlist creation screen
    }


    @Override
    public void onEditButtonClick(TierList tierList) {
        // TODO: hook up to editor with actual tier list
        Intent intent = new Intent(TierListBrowserActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDeleteButtonClick(int position, TierList tierList) {
        tierListManager.removeTierList(tierList.getId());
        adapter.removeItem(position);
    }
}
