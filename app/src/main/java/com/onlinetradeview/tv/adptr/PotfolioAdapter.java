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
import com.onlinetradeview.tv.mdel.TradeModel;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;
import com.onlinetradeview.tv.cmonulty.recyclerview.ItemAnimation;

import java.util.List;

public class PotfolioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<TradeModel> items;
    private final Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;

    public interface OnItemClickListener {
        void onItemClick(View view, String obj, int position);
        void onButtonItemClick(View view, String obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public PotfolioAdapter(Context context, List<TradeModel> items, int animation_type) {
        this.items = items;
        this.ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView name, profitLoss, bougthCalc, cmp, marginused, txtExpiryData, holdinmarginused, txtBoughtSell;
        public TextView canOrder;
        public ImageView imgDrawable;
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
            canOrder = v.findViewById(R.id.cancel_order);
            txtExpiryData = v.findViewById(R.id.txt_expirydate);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_portfolio_scriptlist, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            TradeModel tradeModel = items.get(position);

            view.canOrder.setText("Close Order");
            view.canOrder.setOnClickListener(view1 -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onButtonItemClick(view1, ((TextView)view1).getText().toString(), position);
                }
            });
            view.lyt_parent.setOnClickListener(view1 -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view1, items.get(position).getName(), position);
                }
            });
            if (tradeModel.getStr_type().equalsIgnoreCase("1")) {
                view.txtBoughtSell.setText("Bought : ");
                view.txtBoughtSell.setTextColor(ctx.getResources().getColor(R.color.green_400));
                view.cmp.setText(tradeModel.getStrCmp());
            }else {
                view.txtBoughtSell.setText("Sold : ");
                view.txtBoughtSell.setTextColor(ctx.getResources().getColor(R.color.red_400));
                view.cmp.setText(tradeModel.getStrCmp());
            }
            view.txtExpiryData.setText(tradeModel.getName()+"_"+tradeModel.getStr_date_expiry());
            view.name.setText(tradeModel.getName());
            Double pl = Double.parseDouble(tradeModel.getStrPlPRice());

            if (pl<0) {
                view.profitLoss.setTextColor(ctx.getResources().getColor(R.color.red_400));
                view.profitLoss.setText(tradeModel.getStrPlPRice());
            }else {
                view.profitLoss.setTextColor(ctx.getResources().getColor(R.color.green_400));
                view.profitLoss.setText("+"+tradeModel.getStrPlPRice());
            }
            view.bougthCalc.setText(tradeModel.getQtybought()+"@"+ tradeModel.getStr_avg_bidprice());
            view.marginused.setText(tradeModel.getUsedMargin());
            view.holdinmarginused.setText(tradeModel.getHoldingMarginReq());
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