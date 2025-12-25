package com.onlinetradeview.tv.adptr;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.recyclerview.ItemAnimation;
import com.onlinetradeview.tv.mdel.NotificationModel;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NotificationModel> items;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;
    private String[] strColors = {"#e91e63", "#9c27b0", "#673ab7", "#E53935", "#5677fc", "#689F38", "#03a9f4", "#00bcd4",
            "#009688", "#259b24", "#ff5722", "#795548", "#607d8b", "#ff9800"};

    public interface OnItemClickListener {
        void onItemClick(View view, String obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public NotificationAdapter(Context context, List<NotificationModel> items, int animation_type) {
        this.items = items;
        this.ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView title, message, date;
        public ImageView imgDrawable;
        public ViewGroup lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.tv_title);
            message = v.findViewById(R.id.tv_msg);
            date = v.findViewById(R.id.tvdate);
            
            lyt_parent = v.findViewById(R.id.lyt_parent);
            if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
                Typeface font = Typeface.createFromAsset(ctx.getAssets(), GlobalVariables.CUSTOMFONTNAME);
//                FontUtils.setThemeColor(lyt_parent, ctx, font);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            NotificationModel tradeModel = items.get(position);

            view.title.setText(tradeModel.getTitle());
            view.message.setText(tradeModel.getMessage());
            view.date.setText(tradeModel.getCreated());

            view.lyt_parent.setOnClickListener(view1 -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view1, items.get(0).getMessage(), position);
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