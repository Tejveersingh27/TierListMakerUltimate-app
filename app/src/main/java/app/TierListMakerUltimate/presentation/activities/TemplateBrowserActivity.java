package app.TierListMakerUltimate.presentation.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.application.TierListMakerUltimate;
import app.TierListMakerUltimate.business.services.ITierListCoordinator;
import app.TierListMakerUltimate.business.services.ITierListManager;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.presentation.adapters.TemplateBrowserAdapter;
import app.TierListMakerUltimate.presentation.fragments.TierListCreationFragment;
import app.TierListMakerUltimate.presentation.utils.AppImageLoader;

public class TemplateBrowserActivity extends AppCompatActivity implements TemplateBrowserAdapter.TemplateBrowserActionListener {
    private RecyclerView recyclerView;
    private TemplateBrowserAdapter adapter;
    private AppImageLoader imageLoader;

    private ITierListManager tierListManager;
    private ITierListCoordinator tierListCoordinator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_browser);

        TierListMakerUltimate app = (TierListMakerUltimate) getApplication();
        tierListManager = app.getTierListManager();
        tierListCoordinator = app.getTierListCoordinator();

        imageLoader = new AppImageLoader(this);
        setupRecyclerView();
        setupAddButton();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TemplateBrowserAdapter(tierListManager.getAllTierLists(), imageLoader, this);
        recyclerView.setAdapter(adapter);
    }

    private void showFragment() {
        TierListCreationFragment fragment = new TierListCreationFragment();
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
}
