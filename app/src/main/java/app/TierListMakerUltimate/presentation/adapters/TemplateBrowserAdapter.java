package app.TierListMakerUltimate.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.presentation.utils.AppImageLoader;

public class TemplateBrowserAdapter extends RecyclerView.Adapter<TemplateBrowserAdapter.TemplateBrowserViewHolder> {
    private final List<TierList> tierLists;
    private final TemplateBrowserActionListener listener;
    private final AppImageLoader imageLoader;


    public TemplateBrowserAdapter(List<TierList> tierLists, AppImageLoader imageLoader, TemplateBrowserActionListener listener) {
        this.tierLists = tierLists;
        this.imageLoader = imageLoader;
        this.listener = listener;
    }


    @NonNull
    @Override
    public TemplateBrowserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_template_list, parent, false);
        return new TemplateBrowserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateBrowserViewHolder holder, int position) {
        TierList tierList = tierLists.get(position);
        holder.tierListName.setText(tierList.getName());
        imageLoader.loadImage(tierList.getThumbnailPath(), holder.cardThumbnail);
        holder.cardButton.setOnClickListener(v -> listener.onCardClick(tierList));
    }

    @Override
    public int getItemCount() {
        return tierLists.size();
    }

    public static class TemplateBrowserViewHolder extends RecyclerView.ViewHolder {
        TextView tierListName;
        MaterialCardView cardButton;
        ImageView cardThumbnail;

        public TemplateBrowserViewHolder(@NonNull View itemView) {
            super(itemView);
            tierListName = itemView.findViewById(R.id.tierListTitle);
            cardButton = itemView.findViewById(R.id.templateCard);
            cardThumbnail = itemView.findViewById(R.id.tierListThumbnail);
        }
    }

    public interface TemplateBrowserActionListener {
        void onCardClick(TierList tierList);
    }
}
