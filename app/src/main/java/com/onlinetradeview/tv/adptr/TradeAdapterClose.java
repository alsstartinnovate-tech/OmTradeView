package com.onlinetradeview.tv.adptr;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onlinetradeview.tv.cmonulty.GetFormattedDateTime;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;
import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.mdel.TradeModel;

import java.util.List;

public class TradeAdapterClose extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TradeModel> items;
    private final Context ctx;

    public interface OnItemClickListener {
        void onItemClick(View view, String obj, int position);
    }

    // Add an item to the adapter
    public void addItem(TradeModel item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    // Add a list of items to the adapter
    public void addItems(List<TradeModel> items) {
        int startPosition = items.size();
        items.addAll(items);
        notifyItemRangeInserted(startPosition, items.size());
    }

    public TradeAdapterClose(Context context, List<TradeModel> items) {
        this.items = items;
        this.ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView name, datesold, pricebought, boughttrader, tvexpirydate;
        public TextView marketQty, soldtrader, date_bought, ordertype_bought, ordertype_sold, profitloss;
        public TextView marginused, holding_marginrequest;
        public ViewGroup lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.tvname);
            pricebought = v.findViewById(R.id.marketrate);
            boughttrader = v.findViewById(R.id.boughttrader);
            marketQty = v.findViewById(R.id.marketquantity);
            soldtrader = v.findViewById(R.id.soldtrader);
            datesold = v.findViewById(R.id.date);
            date_bought = v.findViewById(R.id.date_bought);
            ordertype_bought = v.findViewById(R.id.ordertype_bought);
            ordertype_sold = v.findViewById(R.id.ordertype_sold);
            profitloss = v.findViewById(R.id.profitloss);
            tvexpirydate = v.findViewById(R.id.tvexpirydate);
            marginused = v.findViewById(R.id.marginused);
            holding_marginrequest = v.findViewById(R.id.holding_marginrequest);
            lyt_parent = v.findViewById(R.id.lyt_parent);
            if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
                Typeface font = Typeface.createFromAsset(ctx.getAssets(), GlobalVariables.CUSTOMFONTNAME);
                FontUtils.setThemeColor(lyt_parent, ctx, font);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trade_close, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            TradeModel tradeModel = items.get(position);
            view.boughttrader.setText(tradeModel.getStr_type().equalsIgnoreCase("1") ? tradeModel.getPricebought() : tradeModel.getSoldPrice());
            view.soldtrader.setText(tradeModel.getStr_type().equalsIgnoreCase("1") ? tradeModel.getSoldPrice() : tradeModel.getPricebought());
            view.datesold.setText(GetFormatedDateTime(tradeModel.getStr_type().equalsIgnoreCase("1") ? tradeModel.getDatebought() : tradeModel.getDatesold()));
            view.date_bought.setText(GetFormatedDateTime(tradeModel.getStr_type().equalsIgnoreCase("1") ? tradeModel.getDatesold() : tradeModel.getDatebought()));
            view.pricebought.setBackground(tradeModel.getPriceBoughtBg());
            view.pricebought.setTextColor(tradeModel.getPriceBoughtTc());
            view.profitloss.setBackground(tradeModel.getBgProfitLossMrktQty());
            view.profitloss.setTextColor(tradeModel.getTcProfitLossMrktQty());
            view.marketQty.setBackground(tradeModel.getBgProfitLossMrktQty());
            view.marketQty.setTextColor(tradeModel.getTcProfitLossMrktQty());
            view.tvexpirydate.setText(tradeModel.getStr_date_expiry());
            view.marketQty.setText("Qty: " + tradeModel.getQtybought());
            view.ordertype_bought.setText(tradeModel.getOrdertype_bought());
            view.profitloss.setText(tradeModel.getProfitloss() + "/-" + tradeModel.getBrokerage());
            view.name.setText(tradeModel.getName());
            view.pricebought.setText(tradeModel.getPricebought());
            view.marginused.setText("Margin Used: " + tradeModel.getUsedMargin());
            view.holding_marginrequest.setText("Holding Mar. Req.: " + tradeModel.getHoldingMarginReq());
            view.ordertype_bought.setBackground(tradeModel.getOrderTypeBoughtBg());
            view.ordertype_bought.setTextColor(tradeModel.getOrderTypeBoughtTc());
            view.ordertype_bought.setText(tradeModel.getOrderTypeBoughtText());
            view.ordertype_sold.setBackground(tradeModel.getOrderTypeSoldBg());
            view.ordertype_sold.setTextColor(tradeModel.getOrderTypeSoldTc());
            view.ordertype_sold.setText(tradeModel.getOrderTypeSoldText());
        }
    }

    private String GetFormatedDateTime(String date) {
        String[] strDateTime = date.split(" ");
        String[] strDateTimeArray = strDateTime[0].split("-");

        String[] strTimeArray = strDateTime[1].split(":");
        return strDateTimeArray[0] + " " + GetFormattedDateTime.returnMonthAlphabet(Integer.parseInt(strDateTimeArray[1])) + " " + strTimeArray[0] + ":" + strTimeArray[1];
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
