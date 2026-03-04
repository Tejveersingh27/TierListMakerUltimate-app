package app.TierListMakerUltimate.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.application.TierListMakerUltimate;
import app.TierListMakerUltimate.business.services.ITierListCoordinator;
import app.TierListMakerUltimate.business.services.ItemPlacementManager;
import app.TierListMakerUltimate.business.services.ITierListManager;
import app.TierListMakerUltimate.models.TierList;

public class TemplateBrowserActivity extends AppCompatActivity implements TemplateBrowserAdapter.TemplateBrowserActionListener {
    private RecyclerView recyclerView;
    private TemplateBrowserAdapter adapter;

    private ITierListManager tierListManager;
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

        adapter = new TemplateBrowserAdapter(tierListManager.getAllTierLists(), this);
        recyclerView.setAdapter(adapter);
    }

    private void setupAddButton() {
        Button createButton = findViewById(R.id.createTierListButton);
        createButton.setOnClickListener(v -> {
            // TODO: hook up to tierlist creation screen
        });
    }


    @Override
    public void onCardClick(TierList tierList) {
        //TODO: hook up to tierlist creation screen
    }
}
