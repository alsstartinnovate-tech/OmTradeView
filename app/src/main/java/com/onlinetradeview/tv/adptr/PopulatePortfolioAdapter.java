package com.onlinetradeview.tv.adptr;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.mdel.WatchModel;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ssridharatiwari on 2021.
 */

public class PopulatePortfolioAdapter extends RecyclerView.Adapter<PopulatePortfolioAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<WatchModel> spinnerList;

    private McxAdapter.OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(final McxAdapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvBuy, tvSell, tvLatPrice, tvOpen, tvHigh, tvLow, tvClose;
        public RelativeLayout itemHead;
        public CardView cardviewMain;

        public MyViewHolder(View view) {
            super(view);
            tvBuy = view.findViewById(R.id.data_buy);
            tvSell = view.findViewById(R.id.data_sell);
            tvLatPrice = view.findViewById(R.id.data_lastprice);
            tvOpen = view.findViewById(R.id.data_openprice);
            tvHigh = view.findViewById(R.id.data_highprice);
            tvLow = view.findViewById(R.id.data_lowprice);
            tvClose = view.findViewById(R.id.data_closeprice);
            itemHead = view.findViewById(R.id.lyt_parent);
            cardviewMain = view.findViewById(R.id.cardview);

            if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
                Typeface font = Typeface.createFromAsset(context.getAssets(), GlobalVariables.CUSTOMFONTNAME);
                FontUtils.setFont(itemHead, font);
            }
        }
    }

    public PopulatePortfolioAdapter(Context context, List<WatchModel> contactList) {
        spinnerList = new ArrayList<>();
        this.context = context;
        this.spinnerList = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bidhistory, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final WatchModel model = spinnerList.get(position);

        holder.tvBuy.setText(model.getBuyPrice());
        holder.tvSell.setText(model.getSellPrice());
        holder.tvLatPrice.setText(model.getLastTradePrice());
        holder.tvOpen.setText(model.getOpen());
        holder.tvClose.setText(model.getClose());
        holder.tvHigh.setText(model.getHigh());
        holder.tvLow.setText(model.getLow());

        if (position != 0) {
            if (Double.parseDouble(model.getLastTradePrice()) > Double.parseDouble(spinnerList.get(position - 1).getLastTradePrice())) {
                holder.cardviewMain.setCardBackgroundColor(context.getResources().getColor(R.color.green_900));
            } else if (Double.parseDouble(model.getLastTradePrice()) == Double.parseDouble(spinnerList.get(position - 1).getLastTradePrice())) {
                holder.cardviewMain.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            } else {
                holder.cardviewMain.setCardBackgroundColor(context.getResources().getColor(R.color.red_900));
            }
        }

        holder.itemView.setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return spinnerList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    spinnerList = spinnerList;
                } else {
                    List<WatchModel> filteredList = new ArrayList<>();
                    for (WatchModel row : spinnerList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getInstrumentIdentifier().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    spinnerList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = spinnerList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                spinnerList = (ArrayList<WatchModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void UpdateListData(List<WatchModel> list) {
        spinnerList = list;
        spinnerList = list;
        notifyDataSetChanged();
    }

}