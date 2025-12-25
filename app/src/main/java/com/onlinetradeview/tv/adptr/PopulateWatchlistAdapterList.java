package com.onlinetradeview.tv.adptr;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.PreferenceConnector;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;
import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.mdel.WatchModel;

import java.util.List;

/**
 * Created by ssridharatiwari on 2021.
 */

public class PopulateWatchlistAdapterList extends RecyclerView.Adapter<PopulateWatchlistAdapterList.MyViewHolder> {
    private final Context context;
    private final List<WatchModel> items, itemsLast;
    private OnItemClickListener mOnItemClickListener;
    private final int drawable;

    public interface OnItemClickListener {
        void onItemClick(View view, int position, WatchModel tokenName);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvOtherPrice;
        public TextView tvBuy, tvSell, tvLow, tvHigh, tvLastPrice, txtTickTime;
        public RelativeLayout itemHead;
        public CardView cardviewMain, cardBuy, cardSell;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvname);
            tvOtherPrice = view.findViewById(R.id.tvotherprice);

            cardBuy = view.findViewById(R.id.card_buy);
            cardSell = view.findViewById(R.id.card_sell);

            tvBuy = view.findViewById(R.id.txt_buy);
            tvSell = view.findViewById(R.id.txt_sell);
            tvLow = view.findViewById(R.id.txt_low);
            tvHigh = view.findViewById(R.id.txt_high);
            tvLastPrice = view.findViewById(R.id.txt_lastprice);
            txtTickTime = view.findViewById(R.id.txt_txttime);
            itemHead = view.findViewById(R.id.lyt_parent);
            cardviewMain = view.findViewById(R.id.cardview);

            if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
                Typeface font = Typeface.createFromAsset(context.getAssets(), GlobalVariables.CUSTOMFONTNAME);
                FontUtils.setThemeColor(itemHead, context, font);
            }
        }
    }

    public PopulateWatchlistAdapterList(Context context, List<WatchModel> items, List<WatchModel> itemsLast, int drawableLayout) {
        this.context = context;
        this.items = items;
        this.drawable = drawableLayout;
        this.itemsLast = itemsLast;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(drawable, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            WatchModel model = items.get(position);
            holder.tvName.setText(model.getInstrumentName());
            holder.tvOtherPrice.setText(model.getExpiryDate());

            holder.tvBuy.setText(model.getBuyPrice());
            holder.tvSell.setText(model.getSellPrice());

            holder.tvLow.setText("Low: " + model.getLow());
            holder.tvHigh.setText("High: " + model.getHigh());
            holder.txtTickTime.setText("Chg: " + model.getPriceChange() + "(" + model.getPriceChangePercentage() + "%)");

            double changePrice = 0.0;
            if (model.getPriceChange() != null && !model.getPriceChange().equalsIgnoreCase("null")) {
                changePrice = Double.parseDouble(model.getPriceChange());
            }
            if (changePrice < 0) {
                holder.txtTickTime.setTextColor(context.getResources().getColor(R.color.red_900));
            } else {
                holder.txtTickTime.setTextColor(context.getResources().getColor(R.color.green_400));
            }

            try {
//                isPriceIncrease(holder.cardBuy, holder.tvBuy, model.getBuyPrice(), itemsLast.get(position).getBuyPrice(), model.getInstrumentName());
                isPriceIncrease(holder.cardBuy, holder.tvBuy, model, itemsLast.get(position), model.getInstrumentName());
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }

            try {
//                isPriceSellIncrease(holder.cardSell, holder.tvSell, model.getSellPrice(), itemsLast.get(position).getSellPrice(), model.getInstrumentName());
                isPriceSellIncrease(holder.cardSell, holder.tvSell, model, itemsLast.get(position), model.getInstrumentName());
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }

            holder.tvLastPrice.setText("Ltp: " + model.getLastTradePrice());
            holder.cardviewMain.setOnClickListener(view -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, position, items.get(position));
                }
            });
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

//    private void isPriceIncrease(CardView cv, TextView tv, String currentPrice, String newrice, String insName) {
//        if (!currentPrice.equals("null") && !newrice.equals("null") && Double.parseDouble(currentPrice) < Double.parseDouble(newrice)) {
//            if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 0) {
//                cv.setCardBackgroundColor(context.getResources().getColor(R.color.red_default));
//                tv.setTextColor(context.getResources().getColor(R.color.black));
//            } else if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 1) {
//                cv.setCardBackgroundColor(context.getResources().getColor(R.color.red_A700));
//                tv.setTextColor(context.getResources().getColor(R.color.buttoncolor));
//            } else {
//                cv.setCardBackgroundColor(context.getResources().getColor(R.color.red_default));
//                tv.setTextColor(context.getResources().getColor(R.color.white));
//            }
//            Log.e("checkdata>>>>>>>>>", currentPrice + ">>" + insName + "--" + newrice);
//        } else if (!currentPrice.equals("null") && !newrice.equals("null") && Double.parseDouble(currentPrice) > Double.parseDouble(newrice)) {
//            Log.e("checkdata>>>>>>>>>", currentPrice + ">>" + insName + "++" + newrice);
//            if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 0) {
//                cv.setCardBackgroundColor(context.getResources().getColor(R.color.green_default));
//                tv.setTextColor(context.getResources().getColor(R.color.black));
//            } else if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 1) {
//                cv.setCardBackgroundColor(context.getResources().getColor(R.color.green_900));
//                tv.setTextColor(context.getResources().getColor(R.color.buttoncolor));
//            } else {
//                cv.setCardBackgroundColor(context.getResources().getColor(R.color.green_default));
//                tv.setTextColor(context.getResources().getColor(R.color.white));
//            }
//        } else {
//            if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 0) {
//                cv.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
//                tv.setTextColor(context.getResources().getColor(R.color.black));
//            } else if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 1) {
//                cv.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
//                tv.setTextColor(context.getResources().getColor(R.color.buttoncolor));
//            } else {
//                cv.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
//                tv.setTextColor(context.getResources().getColor(R.color.white));
//            }
//        }
//
//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            cv.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
//        }, 400);
//    }
//
//    private void isPriceSellIncrease(CardView cv, TextView tv, String currentPrice, String newrice, String insName) {
//        if (!currentPrice.equals("null") && !newrice.equals("null") && Double.parseDouble(currentPrice) < Double.parseDouble(newrice)) {
//            if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 0) {
//                cv.setCardBackgroundColor(context.getResources().getColor(R.color.red_default));
//                tv.setTextColor(context.getResources().getColor(R.color.black));
//            } else if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 1) {
//                cv.setCardBackgroundColor(context.getResources().getColor(R.color.red_A700));
//                tv.setTextColor(context.getResources().getColor(R.color.buttoncolor));
//            } else {
//                cv.setCardBackgroundColor(context.getResources().getColor(R.color.red_default));
//                tv.setTextColor(context.getResources().getColor(R.color.white));
//            }
//        } else if (!currentPrice.equals("null") && !newrice.equals("null") && Double.parseDouble(currentPrice) > Double.parseDouble(newrice)) {
//            if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 0) {
//                cv.setCardBackgroundColor(context.getResources().getColor(R.color.green_default));
//                tv.setTextColor(context.getResources().getColor(R.color.black));
//            } else if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 1) {
//                cv.setCardBackgroundColor(context.getResources().getColor(R.color.green_900));
//                tv.setTextColor(context.getResources().getColor(R.color.buttoncolor));
//            } else {
//                cv.setCardBackgroundColor(context.getResources().getColor(R.color.green_default));
//                tv.setTextColor(context.getResources().getColor(R.color.white));
//            }
//        } else {
//            if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 0) {
//                cv.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
//                tv.setTextColor(context.getResources().getColor(R.color.black));
//            } else if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 1) {
//                cv.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
//                tv.setTextColor(context.getResources().getColor(R.color.buttoncolor));
//            } else {
//                cv.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
//                tv.setTextColor(context.getResources().getColor(R.color.white));
//            }
//        }
//
//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            cv.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
////            tv.setTextColor(context.getResources().getColor(R.color.red_900));
//        }, 400);
//    }

    private void isPriceIncrease(CardView cv, TextView tv, WatchModel currentModel, WatchModel OldPriceModel, String insName) {
        if (!(currentModel.getBuyPrice()).equals("null") && !(OldPriceModel.getBuyPrice()).equals("null")
                && Double.parseDouble(currentModel.getBuyPrice()) < Double.parseDouble((OldPriceModel.getBuyPrice()))
                || !(currentModel.getLastTradePrice()).equals("null") && !(OldPriceModel.getLastTradePrice()).equals("null")
                        && Double.parseDouble(currentModel.getLastTradePrice()) < Double.parseDouble((OldPriceModel.getLastTradePrice()))
        ) {
            if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 0) {
                cv.setCardBackgroundColor(context.getResources().getColor(R.color.red_default));
                tv.setTextColor(context.getResources().getColor(R.color.black));
            } else if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 1) {
                cv.setCardBackgroundColor(context.getResources().getColor(R.color.red_A700));
                tv.setTextColor(context.getResources().getColor(R.color.buttoncolor));
            } else {
                cv.setCardBackgroundColor(context.getResources().getColor(R.color.red_default));
                tv.setTextColor(context.getResources().getColor(R.color.white));
            }
            Log.e("checkdata>>>>>>>>>", (currentModel.getBuyPrice()) + ">>" + insName + "--" + (OldPriceModel.getBuyPrice()));
        } else if (!(currentModel.getBuyPrice()).equals("null") && !(OldPriceModel.getBuyPrice()).equals("null")
                && Double.parseDouble((currentModel.getBuyPrice())) > Double.parseDouble((OldPriceModel.getBuyPrice()))
                || !(currentModel.getLastTradePrice()).equals("null") && !(OldPriceModel.getLastTradePrice()).equals("null")
                        && Double.parseDouble((currentModel.getLastTradePrice())) > Double.parseDouble((OldPriceModel.getLastTradePrice()))
        ) {
            Log.e("checkdata>>>>>>>>>", (currentModel.getLastTradePrice()) + ">>" + insName + "++" + (OldPriceModel.getLastTradePrice()));
            if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 0) {
                cv.setCardBackgroundColor(context.getResources().getColor(R.color.green_default));
                tv.setTextColor(context.getResources().getColor(R.color.black));
            } else if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 1) {
                cv.setCardBackgroundColor(context.getResources().getColor(R.color.green_900));
                tv.setTextColor(context.getResources().getColor(R.color.buttoncolor));
            } else {
                cv.setCardBackgroundColor(context.getResources().getColor(R.color.green_default));
                tv.setTextColor(context.getResources().getColor(R.color.white));
            }
        } else {
            if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 0) {
                cv.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
                tv.setTextColor(context.getResources().getColor(R.color.black));
            } else if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 1) {
                cv.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
                tv.setTextColor(context.getResources().getColor(R.color.buttoncolor));
            } else {
                cv.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
                tv.setTextColor(context.getResources().getColor(R.color.white));
            }
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            cv.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
        }, 400);
    }

    private void isPriceSellIncrease(CardView cv, TextView tv,  WatchModel currentModel, WatchModel OldPriceModel, String insName) {
        if (!(currentModel.getSellPrice()).equals("null") && !(OldPriceModel.getSellPrice()).equals("null")
                && Double.parseDouble((currentModel.getSellPrice())) < Double.parseDouble((OldPriceModel.getSellPrice()))
                || !(currentModel.getLastTradePrice()).equals("null") && !(OldPriceModel.getLastTradePrice()).equals("null")
                        && Double.parseDouble((currentModel.getLastTradePrice())) < Double.parseDouble((OldPriceModel.getLastTradePrice()))
        ) {
            if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 0) {
                cv.setCardBackgroundColor(context.getResources().getColor(R.color.red_default));
                tv.setTextColor(context.getResources().getColor(R.color.black));
            } else if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 1) {
                cv.setCardBackgroundColor(context.getResources().getColor(R.color.red_A700));
                tv.setTextColor(context.getResources().getColor(R.color.buttoncolor));
            } else {
                cv.setCardBackgroundColor(context.getResources().getColor(R.color.red_default));
                tv.setTextColor(context.getResources().getColor(R.color.white));
            }
        } else if (!(currentModel.getSellPrice()).equals("null") && !(OldPriceModel.getSellPrice()).equals("null")
                && Double.parseDouble((currentModel.getSellPrice())) > Double.parseDouble((OldPriceModel.getSellPrice()))
                || !(currentModel.getLastTradePrice()).equals("null") && !(OldPriceModel.getLastTradePrice()).equals("null")
                        && Double.parseDouble((currentModel.getLastTradePrice())) > Double.parseDouble((OldPriceModel.getLastTradePrice()))
        ) {
            if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 0) {
                cv.setCardBackgroundColor(context.getResources().getColor(R.color.green_default));
                tv.setTextColor(context.getResources().getColor(R.color.black));
            } else if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 1) {
                cv.setCardBackgroundColor(context.getResources().getColor(R.color.green_900));
                tv.setTextColor(context.getResources().getColor(R.color.buttoncolor));
            } else {
                cv.setCardBackgroundColor(context.getResources().getColor(R.color.green_default));
                tv.setTextColor(context.getResources().getColor(R.color.white));
            }
        } else {
            if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 0) {
                cv.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
                tv.setTextColor(context.getResources().getColor(R.color.black));
            } else if (PreferenceConnector.readInteger(context, PreferenceConnector.THEMESELECTED, 0) == 1) {
                cv.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
                tv.setTextColor(context.getResources().getColor(R.color.buttoncolor));
            } else {
                cv.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
                tv.setTextColor(context.getResources().getColor(R.color.white));
            }
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            cv.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
//            tv.setTextColor(context.getResources().getColor(R.color.red_900));
        }, 400);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
