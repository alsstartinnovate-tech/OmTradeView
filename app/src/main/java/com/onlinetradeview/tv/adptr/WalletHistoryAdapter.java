package com.onlinetradeview.tv.adptr;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.mdel.WalletHistoryModel;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.recyclerview.ItemAnimation;

import java.util.ArrayList;
import java.util.List;

import me.grantland.widget.AutofitHelper;

public class WalletHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<WalletHistoryModel> items = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;
    private String[] strColors = {"#388E3C", "#D32F2F"};

    public interface OnItemClickListener {
        void onItemClick(View view, String obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public WalletHistoryAdapter(Context context, List<WalletHistoryModel> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView username, amountRecharge, recharegDate, desc, afterPoint, beforePoint;
        public CardView cardView;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            username = (TextView) v.findViewById(R.id.username);
            amountRecharge = (TextView) v.findViewById(R.id.vm_memberid);
            recharegDate = (TextView) v.findViewById(R.id.vm_name);
            desc = (TextView) v.findViewById(R.id.rechargedesc);
            afterPoint = (TextView) v.findViewById(R.id.afterpopint);
            beforePoint = (TextView) v.findViewById(R.id.beforepoint);

            cardView = (CardView) v.findViewById(R.id.cardview);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallethistory, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OriginalViewHolder) {
            position = holder.getAdapterPosition();
            OriginalViewHolder view = (OriginalViewHolder) holder;
            view.username.setText(items.get(position).getUsername());
            view.amountRecharge.setText(items.get(position).getStr_amount());
            view.recharegDate.setText(items.get(position).getStr_datetime().split(" ")[0]);
            view.desc.setText(items.get(position).getStr_description());

            view.afterPoint.setText("After Point: " + GlobalVariables.CURRENCYSYMBOL + items.get(position).getStr_ap());
            view.beforePoint.setText("Before Point: " + GlobalVariables.CURRENCYSYMBOL + items.get(position).getStr_bp());

            AutofitHelper.create(view.desc);

            if ((items.get(position).getStr_type()).equalsIgnoreCase("CR")) {
                view.amountRecharge.setTextColor(Color.parseColor(strColors[0]));
            } else {
                view.amountRecharge.setTextColor(Color.parseColor(strColors[1]));
            }

            int finalPosition = position;
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(finalPosition).getStr_datetime(), finalPosition);
                    }
                }
            });
            setAnimation(view.itemView, position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private int lastPosition = -1;
    private boolean on_attach = true;

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, on_attach ? position : -1, animation_type);
            lastPosition = position;
        }
    }
}