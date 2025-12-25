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
import com.onlinetradeview.tv.mdel.TradeModel;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;

import java.util.List;

public class TradeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TradeModel> items;
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private String lstType;

    public interface OnItemClickListener {
        void onItemClick(View view, String obj, int position);
        void onButtonItemClick(View view, String obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public TradeAdapter(Context context, List<TradeModel> items, String lstType) {
        this.items = items;
        this.ctx = context;
        this.lstType = lstType;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView name, soldtype_lot, soldorde_type, datesold, pricebought, boughttrader;
        public TextView marginused, holding_marginrequest, canOrder;
        public ImageView imgDrawable;
        public CardView cardView;
        public ViewGroup lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.tvname);
            soldtype_lot = v.findViewById(R.id.tradernumb);
            soldorde_type = v.findViewById(R.id.ordertype);
            datesold = v.findViewById(R.id.date);
            pricebought = v.findViewById(R.id.marketrate);
            boughttrader = v.findViewById(R.id.boughttrader);

            marginused = v.findViewById(R.id.marginused);
            holding_marginrequest = v.findViewById(R.id.holding_marginrequest);
            canOrder = v.findViewById(R.id.cancel_order);

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trade, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            TradeModel tradeModel = items.get(position);
            if (lstType.equalsIgnoreCase("1")) {
                if (tradeModel.getStr_order_status().equalsIgnoreCase("4")) {
                    view.canOrder.setText("Cancelled");
                } else {
                    view.canOrder.setText("Cancel Order");
                }
            } else {
                view.canOrder.setText("Close Order");
            }

            view.canOrder.setBackground(ctx.getDrawable(R.drawable.back_button_dark));

            if (tradeModel.getStr_type().equalsIgnoreCase("1")) {
                view.soldtype_lot.setText("Buy" + "X" + tradeModel.getSoldnumber());
                view.boughttrader.setText("Bought By " + tradeModel.getBoughttrader());
                view.soldtype_lot.setBackground(ctx.getResources().getDrawable(R.drawable.back_round_green));
                view.soldtype_lot.setTextColor(ctx.getResources().getColor(R.color.green_400));
                view.pricebought.setBackground(ctx.getResources().getDrawable(R.drawable.back_round_green));
                view.pricebought.setTextColor(ctx.getResources().getColor(R.color.green_400));
            } else {
                view.soldtype_lot.setText("Sell" + "X" + tradeModel.getSoldnumber());
                view.boughttrader.setText("Sold By " + tradeModel.getBoughttrader());
                view.soldtype_lot.setBackground(ctx.getResources().getDrawable(R.drawable.back_round_red));
                view.soldtype_lot.setTextColor(ctx.getResources().getColor(R.color.red_900));
                view.pricebought.setBackground(ctx.getResources().getDrawable(R.drawable.back_round_red));
                view.pricebought.setTextColor(ctx.getResources().getColor(R.color.red_900));
            }

            if (tradeModel.getStr_order_type().equalsIgnoreCase("1")) {
                view.soldorde_type.setBackground(ctx.getResources().getDrawable(R.drawable.back_round_green));
                view.soldorde_type.setTextColor(ctx.getResources().getColor(R.color.green_400));
                view.soldorde_type.setText("Market");
            } else if (tradeModel.getStr_order_type().equalsIgnoreCase("2")) {
                view.soldorde_type.setBackground(ctx.getResources().getDrawable(R.drawable.back_round_red));
                view.soldorde_type.setTextColor(ctx.getResources().getColor(R.color.red_900));
                view.soldorde_type.setText("Order");
            } else if (tradeModel.getStr_order_type().equalsIgnoreCase("3")) {
                view.soldorde_type.setBackground(ctx.getResources().getDrawable(R.drawable.back_round_red));
                view.soldorde_type.setTextColor(ctx.getResources().getColor(R.color.red_900));
                view.soldorde_type.setText("SL");
            } else if (tradeModel.getStr_order_type().equalsIgnoreCase("4")) {
                view.soldorde_type.setBackground(ctx.getResources().getDrawable(R.drawable.back_round_red));
                view.soldorde_type.setTextColor(ctx.getResources().getColor(R.color.red_900));
                view.soldorde_type.setText("Admin");
            }else if (tradeModel.getStr_order_type().equalsIgnoreCase("5")) {
                view.soldorde_type.setBackground(ctx.getResources().getDrawable(R.drawable.back_round_red));
                view.soldorde_type.setTextColor(ctx.getResources().getColor(R.color.red_900));
                view.soldorde_type.setText("Carried Forward");
            }else if (tradeModel.getStr_order_type().equalsIgnoreCase("6")) {
                view.soldorde_type.setBackground(ctx.getResources().getDrawable(R.drawable.back_round_red));
                view.soldorde_type.setTextColor(ctx.getResources().getColor(R.color.red_900));
                view.soldorde_type.setText("I-Fund");
            }else if (tradeModel.getStr_order_type().equalsIgnoreCase("7")) {
                view.soldorde_type.setBackground(ctx.getResources().getDrawable(R.drawable.back_round_red));
                view.soldorde_type.setTextColor(ctx.getResources().getColor(R.color.red_900));
                view.soldorde_type.setText("Month Settlement");
            }

            view.name.setText(tradeModel.getName() + "_" + tradeModel.getStr_date_expiry());
            view.datesold.setText(tradeModel.getDatebought());
            view.pricebought.setText(tradeModel.getPricebought());

            view.marginused.setText("Margin Used: " + tradeModel.getUsedMargin());
            view.holding_marginrequest.setText("Holding Mar. Req.: " + tradeModel.getHoldingMarginReq());

            view.canOrder.setVisibility(View.VISIBLE);
            view.canOrder.setOnClickListener(view1 -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onButtonItemClick(view1, ((TextView) view1).getText().toString(), position);
                }
            });

            view.lyt_parent.setOnClickListener(view1 -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view1, items.get(position).getName(), position);
                }
            });
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}