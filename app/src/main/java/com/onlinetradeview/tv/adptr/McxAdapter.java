package com.onlinetradeview.tv.adptr;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;
import com.onlinetradeview.tv.mdel.McxModel;
import com.onlinetradeview.tv.cmonulty.recyclerview.ItemAnimation;

import java.util.ArrayList;
import java.util.List;

public class McxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<McxModel> items;
    private Context ctx;
    private int animation_type = 0;

    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public McxAdapter(Context context, List<McxModel> items, int animation_type) {
        this.items = items;
        this.ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView name,date,tvchg,hnumber,mcxno,ltpone,mcxtwo,
                ltptwo;
        public ImageView imgDrawable;
        public CardView cardView;
        public ViewGroup lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.tvname);
            date = v.findViewById(R.id.mcxdate);
            tvchg = v.findViewById(R.id.chgno);
            hnumber = v.findViewById(R.id.hnumber);

            mcxno = v.findViewById(R.id.mcxnumber);
            ltpone = v.findViewById(R.id.ltpnumb);
            mcxtwo = v.findViewById(R.id.mcxnumbertwo);
            ltptwo = v.findViewById(R.id.ltpnotwo);

            cardView = v.findViewById(R.id.cardview);
            lyt_parent = v.findViewById(R.id.lyt_parent);
            if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
                Typeface font = Typeface.createFromAsset(ctx.getAssets(), GlobalVariables.CUSTOMFONTNAME);
                FontUtils.setThemeColor(lyt_parent, ctx, font);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mcxfuture, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OriginalViewHolder) {
            position = holder.getAdapterPosition();
            OriginalViewHolder view = (OriginalViewHolder) holder;

            int finalPosition = position;
            view.lyt_parent.setOnClickListener(view1 -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view1, finalPosition);
                }
            });
            setAnimation(view.itemView, position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
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