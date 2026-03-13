package app.TierListMakerUltimate.presentation;

import static app.TierListMakerUltimate.presentation.constants.PresentationConstants.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.application.TierListMakerUltimate;
import app.TierListMakerUltimate.business.exception.BusinessException;
import app.TierListMakerUltimate.business.services.IItemPlacementManager;
import app.TierListMakerUltimate.business.services.ITierListCoordinator;
import app.TierListMakerUltimate.business.services.ITierManager;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.presentation.activities.TierListBrowserActivity;
import app.TierListMakerUltimate.presentation.controllers.TierItemDragController;
import app.TierListMakerUltimate.presentation.fragments.TierEditorFragment;
import app.TierListMakerUltimate.presentation.fragments.TierItemCreationFragment;
import app.TierListMakerUltimate.presentation.fragments.TierListCreationFragment;
import app.TierListMakerUltimate.presentation.utils.ImageHelper;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements TierItemCreationFragment.TierItemCreationFragmentActionListener, TierEditorFragment.TierEditorFragmentActionListener, TierItemDragController.DragDropListener, TierAdapter.TierActions, TierListCreationFragment.TierListCreationFragmentActionListener {
    private int tierlistID;
    private String tierlistName;

    // Instance Variables
    private ITierManager tierManager;
    private IItemPlacementManager placementManager;
    private ITierListCoordinator tierListCoordinator;
    private TierAdapter tierAdapter;
    private ImageHelper imageHelper;

    private TierItemAdapter unrankedAdapter;

    // Views
    TextView tierListTitle;
    ImageButton addTierItemButton;
    ImageButton tierConfigButton;
    RecyclerView tierRecycler;
    RecyclerView unrankedItemsRecycler;
    ImageButton addTierButton;

    ImageButton shareTemplateButton;

    ImageButton backToMyTierListsButton;
    View tierListEditButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageHelper = new ImageHelper(this);
        TierListMakerUltimate app = (TierListMakerUltimate) getApplication();

        tierManager = app.getTierManager();
        placementManager = app.getItemPlacementManager();
        tierListCoordinator = app.getTierListCoordinator();


        Intent intent = getIntent();
        tierlistID = intent.getIntExtra(INTENT_TIER_LIST_ID, 0);
        tierlistName = intent.getStringExtra(INTENT_TIER_LIST_NAME);
        bindViews();
        tierListTitle.setText(tierlistName);

        setupTiersRecycler();
        setupUnrankedRecycler();
        setupAllButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    // Puts any relevant menu items in a hashmap for easy access.
    private void bindViews() {
        tierListTitle = findViewById(R.id.tierListTitle);
        addTierItemButton = findViewById(R.id.plusIconItem);
        addTierButton = findViewById(R.id.plusIcon);
        tierConfigButton = findViewById(R.id.tierSettings);
        tierRecycler = findViewById(R.id.tierContainer);
        unrankedItemsRecycler = findViewById(R.id.itemHolderUnranked);
        shareTemplateButton = findViewById(R.id.shareTemplate);
        backToMyTierListsButton = findViewById(R.id.backButton);
        tierListEditButton = findViewById(R.id.titleEditArea);
    }

    private void setupAllButtons() {
        setupAddItemButton();
        setupAddTierButton();
        setupEditTierListNameButton();
        setupBackToMyTierListsButton();
        setupShareTemplateButton();
    }

    private void setupTiersRecycler() {
        tierAdapter = new TierAdapter(this, imageHelper);
        tierRecycler.setLayoutManager(new LinearLayoutManager(this));
        tierRecycler.setAdapter(tierAdapter);

    }

    private void setupUnrankedRecycler() {
        unrankedAdapter = new TierItemAdapter(imageHelper);

        TierItemDragController unrankedDragController = new TierItemDragController(this, tierManager.getUnrankedTierForList(tierlistID).getId());

        unrankedItemsRecycler.setOnDragListener(unrankedDragController);
        unrankedItemsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        unrankedItemsRecycler.setAdapter(unrankedAdapter);
    }

    // Implement these later

    // Opens the tier settings menu
    private void openTierEditor(Tier tier) {
        TierEditorFragment fragment = TierEditorFragment.newInstance(tier.getId());
        fragment.setUpListener(this);
        showFragment(fragment, FRAGMENT_TIER_EDITOR);
    }

    private void openTierCreator(String tag) {
        TierListCreationFragment fragment = TierListCreationFragment.newInstance(tierlistID);
        fragment.setUpListener(this);
        showFragment(fragment, tag);
    }


    // Open tier item creation fragment
    private void setupAddItemButton() {
        addTierItemButton.setOnClickListener(v -> {
            TierItemCreationFragment fragment = TierItemCreationFragment.newInstance(tierlistID);
            fragment.setUpListener(this);
            showFragment(fragment, FRAGMENT_TIER_ITEM_CREATION);
        });
    }

    private void showFragment(androidx.fragment.app.DialogFragment fragment, String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
            return;
        }
        fragment.show(getSupportFragmentManager(), tag);
    }

    // Add new default tier
    private void setupAddTierButton() {
        addTierButton.setOnClickListener(v -> {
            tierManager.createDefaultTier(tierlistID);
            refreshList();
        });
    }

    private void setupEditTierListNameButton() {
        tierListEditButton.setOnClickListener(v -> {
            openTierCreator(FRAGMENT_TIER_LIST_CREATION);
        });
    }

    private void setupShareTemplateButton() {
        shareTemplateButton.setOnClickListener(v -> {
            showAlert(R.string.share_tier_list_as_template_title, R.string.share_tier_list_as_template_message, R.string.publish, R.string.cancel);
        });
    }

    private void showAlert(int title, int message, int positiveButton, int negativeButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(positiveButton, (dialog, which) -> {
            openTierCreator(FRAGMENT_TEMPLATE_CREATION);
        });

        builder.setNegativeButton(negativeButton, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setupBackToMyTierListsButton() {
        backToMyTierListsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TierListBrowserActivity.class);
            startActivity(intent);
        });
    }

    // Moves an item to a target tier
    private void moveItem(int itemId, int targetTierId) {
        try {
            placementManager.moveItemToTier(itemId, targetTierId);
            refreshList();
        } catch (BusinessException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    // This function should make the tier physically move up or down on the list
    // based on the direction parameter
    private void shiftTier(int direction) {
        Toast.makeText(this, NOT_IMPLEMENTED, Toast.LENGTH_LONG).show();
    }

    // Refreshes the tierlist to reflect item movements.
    private void refreshList() {
        // Set items for unranked
        int unrankedId = tierManager.getUnrankedTierForList(tierlistID).getId();
        unrankedAdapter.setItems(placementManager.getItemsForTier(unrankedId));

        // Set items for non-unranked
        List<Tier> rankedTiers = tierManager.getRankedTiersForList(tierlistID);
        Map<Integer, List<TierItem>> tierItemsMap = new HashMap<>();
        for (Tier tier : rankedTiers) {
            tierItemsMap.put(tier.getId(), placementManager.getItemsForTier(tier.getId()));
        }

        tierAdapter.setTiers(rankedTiers, tierItemsMap);

    }


    // Tier List Creation Fragment Overrides
    @Override
    public void onTierListCreatedSuccessfully(TierList newTierList, String tag) {
        if (tag.equals(FRAGMENT_TIER_LIST_CREATION)) {
            tierListTitle.setText(newTierList.getName());
        } else {
            tierListCoordinator.deepCopyAsTemplate(newTierList.getId(), true);
            Toast.makeText(this, R.string.template_created, Toast.LENGTH_SHORT).show();
        }
    }


    // Tier Adapter Overrides
    @Override
    public void openTierSettings(Tier tier) {
        openTierEditor(tier);
    }


    @Override
    public void onItemDroppedTierOnTier(int itemId, int targetTierId) {
        moveItem(itemId, targetTierId);
    }

    @Override
    public void moveTier(int direction) {
        shiftTier(direction);
    }

    // Tier Item Creation Fragment Overrides
    @Override
    public void onTierItemCreatedSuccessfully() {
        refreshList();
    }


    // Tier Editor Fragment Overrides
    @Override
    public void onTierEditorFragmentEditSuccess() {
        refreshList();
    }

    @Override
    public void onTierEditorFragmentDeleteSuccess() {
        refreshList();
    }

    // Drag Controller Overrides
    @Override
    public void onItemDropped(int itemId, int targetTierId) {
        moveItem(itemId, targetTierId);
    }

}
