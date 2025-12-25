package com.onlinetradeview.tv.adptr;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.onlinetradeview.tv.cmonulty.GlobalData;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;
import com.onlinetradeview.tv.cmonulty.recyclerview.ItemAnimation;
import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.mdel.TradeModel;

import java.util.List;

public class PotfolioCloseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TradeModel> items;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private final int animation_type;

    public interface OnItemClickListener {
        void onItemClick(View view, String obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public PotfolioCloseAdapter(Context context, List<TradeModel> items, int animation_type) {
        this.items = items;
        this.ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView name, profitLoss, bougthCalc, cmp, marginused, txtExpiryData, holdinmarginused, txtBoughtSell, tvAvgBuy, tvAvgSell, tvBrokerage;
        public ImageView imgDrawable;
        public CardView cardView;
        public ViewGroup lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.txt_metal_name);
            profitLoss = v.findViewById(R.id.profitLoss);
            cmp = v.findViewById(R.id.datemarkettwo);
            bougthCalc = v.findViewById(R.id.boughtqty);
            marginused = v.findViewById(R.id.marginused);
            holdinmarginused = v.findViewById(R.id.holdin_margin);
            txtBoughtSell = v.findViewById(R.id.boughtorsell);
            tvAvgBuy = v.findViewById(R.id.avg_buy);
            tvAvgSell = v.findViewById(R.id.avg_sell);
            tvBrokerage = v.findViewById(R.id.brokerage);

            txtExpiryData = v.findViewById(R.id.txt_expirydate);

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_portfolio_close, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OriginalViewHolder) {
            position = holder.getAdapterPosition();
            OriginalViewHolder view = (OriginalViewHolder) holder;

            TradeModel tradeModel = items.get(position);

            if (tradeModel.getStr_type().equalsIgnoreCase("1")) {
                view.txtBoughtSell.setTextColor(ctx.getResources().getColor(R.color.green_400));
                view.cmp.setText(tradeModel.getStrCmp());
            } else {
                view.txtBoughtSell.setTextColor(ctx.getResources().getColor(R.color.red_400));
                view.cmp.setText(tradeModel.getStrCmp());
            }
            view.txtExpiryData.setText(tradeModel.getName() + "_" + tradeModel.getStr_date_expiry());
            view.name.setText(tradeModel.getName());

            Double pl = Double.parseDouble(tradeModel.getStr_plbalanceClose() + "");
            if (pl < 0) {
                view.profitLoss.setTextColor(ctx.getResources().getColor(R.color.red_400));
                view.profitLoss.setText(roundup(tradeModel.getStr_plbalanceClose()));
            } else {
                view.profitLoss.setTextColor(ctx.getResources().getColor(R.color.green_400));
                view.profitLoss.setText("+" + roundup(tradeModel.getStr_plbalanceClose()));
            }
            view.tvBrokerage.setText(GlobalData.roundUp(Double.parseDouble(tradeModel.getBrokerage()), 2) + "");

            view.bougthCalc.setText(tradeModel.getQtybought());
            view.marginused.setText(tradeModel.getUsedMargin());
            view.holdinmarginused.setText(tradeModel.getHoldingMarginReq());

            view.tvAvgBuy.setText(tradeModel.getAvgBuyPrice());
            view.tvAvgSell.setText(tradeModel.getAvgSellPrice());
            view.tvBrokerage.setTextColor(ctx.getResources().getColor(R.color.red_400));
            int finalPosition = position;
            view.lyt_parent.setOnClickListener(view1 -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view1, tradeModel.getName(), finalPosition);
                }
            });
            setAnimation(view.itemView, position);
        }
    }

    private String roundup(String value) {
        double rounded = GlobalData.roundUp(Double.parseDouble(value), 2);
        return String.format("%,.2f", rounded);  // Format with thousands separator and 2 decimal places
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