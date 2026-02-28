package app.TierListMakerUltimate.presentation;

import android.graphics.Color;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.presentation.TierItemAdapter;

public class TierAdapter extends RecyclerView.Adapter<TierAdapter.TierViewHolder>
{
    private List<Tier> tiers;                           // List that contains the tiers to be displayed.
    private Map<Integer, List<TierItem>> tierItemsMap;  // Contains a list of all items in use and which tier they are in.
    private TierActions actions;                        // A class which contains functions we can call when a certain event happens.
                                                        // Separated into another class so we can customize the functions dynamically.
    public TierAdapter(TierActions actions)
    {
        this.tiers = new ArrayList<>();
        this.actions = actions;
    }

    public void setTiers(List<Tier> tiers, Map<Integer, List<TierItem>> tierItemsMap) {
        this.tiers = tiers != null ? tiers : new ArrayList<>();
        this.tierItemsMap = tierItemsMap;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tier_layout, parent, false);
        return new TierViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TierViewHolder holder, int position) {
        Tier tier = tiers.get(position);
        List<TierItem> items = tierItemsMap != null ? tierItemsMap.get(tier.getId()) : new ArrayList<>();
        holder.bind(tier, items, actions);
    }

    @Override
    public int getItemCount() {
        return tiers.size();
    }

    static class TierViewHolder extends RecyclerView.ViewHolder
    {
        private final LinearLayout tierContainer;
        private final TextView tierLabel;
        private final ImageButton settingBtn;
        private final ImageButton upBtn;
        private final ImageButton downBtn;
        private final RecyclerView itemsRecyclerView;
        private final TierItemAdapter itemAdapter;

        public TierViewHolder(@NonNull View itemView) {
            super(itemView);
            tierContainer = itemView.findViewById(R.id.layout0);
            tierLabel = itemView.findViewById(R.id.tierTitle);
            settingBtn = itemView.findViewById(R.id.tierSettings);
            upBtn = itemView.findViewById(R.id.upButton);
            downBtn = itemView.findViewById(R.id.downButton);
            itemsRecyclerView = itemView.findViewById(R.id.itemHolder);

            // Setup horizontal RecyclerView for items
            itemAdapter = new TierItemAdapter();
            itemsRecyclerView.setLayoutManager(
                    new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false)
            );
            itemsRecyclerView.setAdapter(itemAdapter);
        }

        public void bind(Tier tier, List<TierItem> items, TierActions actions) {
            // Set tier label
            tierLabel.setText(tier.getName());

            // Set tier background color
            try {
                tierContainer.setBackgroundColor(Color.parseColor(tier.getColor()));
            } catch (IllegalArgumentException e) {
                tierContainer.setBackgroundColor(Color.parseColor("#BFBFBF")); // Default gray
            }

            // Set items
            itemAdapter.setItems(items);

            // Setup Drag and Drop
            tierContainer.setOnDragListener((v, event) -> {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        return true;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        v.setAlpha(0.7f);
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                    case DragEvent.ACTION_DRAG_ENDED:
                        v.setAlpha(1.0f);
                        return true;
                    case DragEvent.ACTION_DROP:
                        String itemIdStr = event.getClipData().getItemAt(0).getText().toString();
                        int itemId = Integer.parseInt(itemIdStr);
                        if (actions != null) {
                            actions.onItemDropped(itemId, tier.getId());
                        }
                        return true;
                }
                return false;
            });

            // Settings button
            settingBtn.setOnClickListener(v -> {
                if (actions != null) {
                    actions.openTierSettings(tier);
                }
            });

            // Up button
            upBtn.setOnClickListener(v -> {
                if (actions != null) {
                    actions.moveTier(1);
                }
            });

            // Down button
            downBtn.setOnClickListener(v -> {
                if (actions != null) {
                    actions.moveTier(-1);
                }
            });
        }
    }

    public interface TierActions {
        void onDeleteTier(Tier tier);
        void onItemDropped(int itemId, int targetTierId);
        void openTierSettings(Tier tier);
        void moveTier(int direction);   // 1 for up, -1 for down
    }
}
