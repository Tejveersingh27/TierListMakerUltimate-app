package app.TierListMakerUltimate.presentation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.business.services.TierListManager;

import app.TierListMakerUltimate.application.TierListMakerUltimate;
import app.TierListMakerUltimate.models.TierList;

public class TierListBrowserActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TierListBrowserAdapter adapter;

    private TierListManager tierListManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tierlist_browser);

        TierListMakerUltimate app = (TierListMakerUltimate) getApplication();
        tierListManager = app.getTierListManager();

        // Sample tier lists to test layout
        List<TierList> tierLists = tierListManager.getAllTierLists();
        if (tierLists.isEmpty()) {
            List<String> names = Arrays.asList(
                    "Tier List 1",
                    "Tier List 2",
                    "Tier List 3",
                    "Tier List 1",
                    "Tier List 2",
                    "Tier List 3",
                    "Tier List 1",
                    "Tier List 2",
                    "Tier List 3"
            );

            for (String name : names) {
                tierListManager.createTierList(name);
            }
        }
        adapter = new TierListBrowserAdapter(tierLists);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TierListBrowserAdapter(tierListManager.getAllTierLists());
        recyclerView.setAdapter(adapter);
    }


}
