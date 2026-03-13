package app.TierListMakerUltimate.presentation.adapters;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.presentation.utils.ImageHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TierItemAdapter extends RecyclerView.Adapter<TierItemAdapter.TierItemViewHolder> {
    private List<TierItem> items;
    private ImageHelper imageHelper;
    private TierAdapter.TierItemActions actions;

    public TierItemAdapter(ImageHelper imageHelper, TierAdapter.TierItemActions actions) {
        items = new ArrayList<>();
        this.actions = actions;
        this.imageHelper = imageHelper;
    }

    public void setItems(List<TierItem> items) {
        this.items = (items != null ? items : new ArrayList<>());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TierItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tier_image, parent, false);
        return new TierItemViewHolder(view, imageHelper);
    }

    @Override
    public void onBindViewHolder(@NonNull TierItemViewHolder holder, int position) {
        TierItem item = items.get(position);
        holder.bind(item, actions);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class TierItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private ImageHelper imageHelper;


        public TierItemViewHolder(@NonNull View itemView, ImageHelper imageHelper) {
            super(itemView);
            imageView = itemView.findViewById(R.id.tier_image);
            this.imageHelper = imageHelper;
        }

        public void bind(TierItem item, TierAdapter.TierItemActions actions) {
            String image = item.getImagePath();

            imageHelper.loadImage(image, imageView);

            // Set up long click listener to start drag
            itemView.setOnLongClickListener(v -> {
                ClipData data = ClipData.newPlainText("item_id", String.valueOf(item.getId()));
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(data, shadowBuilder, v, 0);
                return true;
            });

            itemView.setOnTouchListener((v, event) -> {
                actions.onItemDblClick(item.getId(), event);
                return false; // Indicate you are handling the touch events
            });
        }
    }
}