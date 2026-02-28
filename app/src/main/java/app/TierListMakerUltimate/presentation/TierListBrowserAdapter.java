package app.TierListMakerUltimate.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.TierListMakerUltimate.R;
import app.TierListMakerUltimate.models.TierList;

public class TierListBrowserAdapter extends RecyclerView.Adapter<TierListBrowserAdapter.TierListBrowserViewHolder> {
    private final List<TierList> tierLists;

    public TierListBrowserAdapter(List<TierList> tierLists) {
        this.tierLists = tierLists;
    }

    @NonNull
    @Override
    public TierListBrowserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tier_list, parent, false);
        return new TierListBrowserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TierListBrowserViewHolder holder, int position) {
        TierList tierList = tierLists.get(position);
        holder.tierListName.setText(tierList.getName());
    }

    @Override
    public int getItemCount() {
        return tierLists.size();
    }

    public static class TierListBrowserViewHolder extends RecyclerView.ViewHolder {
        TextView tierListName;
        ImageButton editButton;
        ImageButton deleteButton;

        public TierListBrowserViewHolder(@NonNull View itemView) {
            super(itemView);
            tierListName = itemView.findViewById(R.id.textTitle);
            editButton = itemView.findViewById(R.id.buttonEdit);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
