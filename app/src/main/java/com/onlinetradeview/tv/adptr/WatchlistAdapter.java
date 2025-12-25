package com.onlinetradeview.tv.adptr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.VH> {
    public interface Callback { void onItemClick(int position, int id); }

    private final List<String> data;
    private final int baseId;
    private final Callback cb;

    public WatchlistAdapter(List<String> data, int baseId, Callback cb) {
        this.data = data;
        this.baseId = baseId;
        this.cb = cb;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView tv = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new VH(tv);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final int id = baseId + position;
        holder.tv.setText(data.get(position).split("#:#")[0]);
        holder.tv.setOnClickListener(v -> {
            if (cb != null) cb.onItemClick(position, id);
        });
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final TextView tv;
        VH(@NonNull View itemView) {
            super(itemView);
            tv = (TextView) itemView;
        }
    }
}

