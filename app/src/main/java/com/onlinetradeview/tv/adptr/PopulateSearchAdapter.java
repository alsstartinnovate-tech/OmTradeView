package com.onlinetradeview.tv.adptr;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onlinetradeview.tv.cmonulty.apicalling.WebService;
import com.onlinetradeview.tv.cmonulty.customviews.CustomeProgressDialog;
import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.mdel.WatchModel;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.PreferenceConnector;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ssridharatiwari on 2021.
 */
public class PopulateSearchAdapter extends RecyclerView.Adapter<PopulateSearchAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private final List<WatchModel> spinnerList;
    private final List<WatchModel> itemsLast;
    private List<WatchModel> spinnerListFiltered;
    private List<WatchModel> filterItemLast;
    private boolean isCheckBoxShow;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvExpiryDate, tvLot, tvLow, tvHigh, tvLastPrice, tvBuy, tvSell, tvOtherPrice;
        public RelativeLayout itemHead;
        public CheckBox cboption;
        public CardView cardBuy, cardSell;
        private LinearLayout layCheckBox;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvname);
            tvExpiryDate = view.findViewById(R.id.tvexpiry_date);
            tvLot = view.findViewById(R.id.tvlot);

            tvBuy = view.findViewById(R.id.txt_buy);
            tvSell = view.findViewById(R.id.txt_sell);
            tvOtherPrice = view.findViewById(R.id.tvotherprice);
            itemHead = view.findViewById(R.id.lyt_parent);
            cboption = (CheckBox) view.findViewById(R.id.checkbox);
            tvLow = view.findViewById(R.id.txt_low);
            tvHigh = view.findViewById(R.id.txt_high);
            tvLastPrice = view.findViewById(R.id.txt_lastprice);
            layCheckBox = view.findViewById(R.id.lay_checkbox);

            cardBuy = view.findViewById(R.id.card_buy);
            cardSell = view.findViewById(R.id.card_sell);

            if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
                Typeface font = Typeface.createFromAsset(context.getAssets(), GlobalVariables.CUSTOMFONTNAME);
                FontUtils.setThemeColor(itemHead, context, font);
            }
        }
    }

    public PopulateSearchAdapter(Context context, List<WatchModel> items, List<WatchModel> itemsLast, boolean isCheckBoxShow) {
        this.context = context;
        this.spinnerList = items;
        this.spinnerListFiltered = items;
        this.filterItemLast = itemsLast;
        this.itemsLast = itemsLast;
        this.isCheckBoxShow = isCheckBoxShow;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (isCheckBoxShow) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_metallivelist, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_metallivelistshow, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final WatchModel model = spinnerListFiltered.get(position);
        if (filterItemLast.size() == 0) {
            filterItemLast = spinnerListFiltered;
        }
        WatchModel modelLast = filterItemLast.get(position);

        holder.cboption.setOnCheckedChangeListener(null);
        holder.cboption.setChecked(model.isSelected());
        holder.cboption.setId(position);
        holder.cboption.setOnCheckedChangeListener((buttonView, isChecked) -> {
            buttonView.setChecked(isChecked);
            if (isChecked) {
                SavePreference("1", buttonView.getId());
            } else {
                SavePreference("0", buttonView.getId());
            }
            (spinnerListFiltered.get(holder.getAdapterPosition())).setSelected(isChecked);
            notifyDataSetChanged();
        });
        holder.tvName.setText(model.getInstrumentName());
        holder.tvExpiryDate.setText(model.getExpiryDate());
        holder.tvLastPrice.setText("Ltp: " + model.getLastTradePrice());
        holder.tvBuy.setText(model.getBuyPrice());
        holder.tvSell.setText(model.getSellPrice());
        holder.tvOtherPrice.setText("Lot Size: " + model.getQuotationLot());
        holder.tvLow.setText("Low: " + model.getLow());
        holder.tvHigh.setText("High: " + model.getHigh());

        isPriceSellIncrease(holder.cardBuy, holder.tvBuy, model.getBuyPrice(), modelLast.getBuyPrice());
        isPriceIncrease(holder.cardSell, holder.tvSell, model.getSellPrice(), modelLast.getSellPrice());
    }

    public void SavePreference(String isChecked, int pos) {
        CustomeProgressDialog customeProgressDialog = new CustomeProgressDialog(context, R.layout.lay_customprogessdialog);
        TextView textView = (TextView) customeProgressDialog.findViewById(R.id.loader_showtext);
        textView.setVisibility(View.VISIBLE);
        textView.setText("Updating");
        customeProgressDialog.setCancelable(false);
        customeProgressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = WebService.PRE_URL + "addUserWatchList?userID=" +
                PreferenceConnector.readString(context, PreferenceConnector.LOGINUSERID, "") +
                "&category_id=" + spinnerListFiltered.get(pos).getGetCatId() +
                "&ischecked=" + isChecked;
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.e("responsechange>>", response);
                    try {
                        JSONObject json = new JSONObject(response);
                        String str_result = json.getString("status");
                        String str_msg = json.getString("message");
                        if (str_result.equals("1")) {
//                            customToast.showCustomToast(context, str_msg, customToast.ToastySuccess);
                        } else {
//                            customToast.showCustomToast(context, str_msg, customToast.ToastyError);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (null != customeProgressDialog && customeProgressDialog.isShowing()) {
                        customeProgressDialog.dismiss();
                    }
                },
                error -> {
                    Log.d("ERROR", "error => " + error.toString());
                    if (null != customeProgressDialog && customeProgressDialog.isShowing()) {
                        customeProgressDialog.dismiss();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Token", PreferenceConnector.readString(context, PreferenceConnector.LOGINTOKEN, ""));
                return params;
            }
        };
        queue.add(getRequest);
    }

    @Override
    public int getItemCount() {
        return spinnerListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filterItemLast = itemsLast;
                    spinnerListFiltered = spinnerList;
                } else {
                    List<WatchModel> filteredList = new ArrayList<>();
                    List<WatchModel> filteredListLast = new ArrayList<>();
                    for (WatchModel row : spinnerList) {
                        if (row.getInstrumentIdentifier().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                            filteredListLast.add(itemsLast.get(spinnerList.indexOf(row)));
                        }
                    }
                    spinnerListFiltered = filteredList;
                    filterItemLast = filteredListLast;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = spinnerListFiltered;
                filterResults.count = spinnerListFiltered.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                spinnerListFiltered = (List<WatchModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private void isPriceIncrease(CardView cv, TextView tv, String currentPrice, String newrice) {
        if (!currentPrice.equals("null") && !newrice.equals("null") && Double.parseDouble(currentPrice) < Double.parseDouble(newrice)) {
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
        } else if (!currentPrice.equals("null") && !newrice.equals("null") && Double.parseDouble(currentPrice) > Double.parseDouble(newrice)) {
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
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            cv.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
//            tv.setTextColor(context.getResources().getColor(R.color.green_400));
        }, 700);
    }

    private void isPriceSellIncrease(CardView cv, TextView tv, String currentPrice, String newrice) {
        if (!currentPrice.equals("null") && !newrice.equals("null") && Double.parseDouble(currentPrice) < Double.parseDouble(newrice)) {
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
        } else if (!currentPrice.equals("null") && !newrice.equals("null") && Double.parseDouble(currentPrice) > Double.parseDouble(newrice)) {
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
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            cv.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
//            tv.setTextColor(context.getResources().getColor(R.color.red_900));
        }, 700);
    }
}