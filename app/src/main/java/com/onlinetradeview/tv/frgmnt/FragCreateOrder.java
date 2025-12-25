package com.onlinetradeview.tv.frgmnt;

import static com.onlinetradeview.tv.cmonulty.apicalling.WebService.ORDERAUTH;
import static com.onlinetradeview.tv.cmonulty.apicalling.WebService.PRE_URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onlinetradeview.tv.act.ActLogin;
import com.onlinetradeview.tv.act.ActSearchMetal;
import com.onlinetradeview.tv.cmonulty.GlobalData;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.customviews.CustomeProgressDialog;
import com.onlinetradeview.tv.act.ActMain;
import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.mdel.TradeModel;
import com.onlinetradeview.tv.mdel.WatchModel;
import com.onlinetradeview.tv.cmonulty.CheckInternet;
import com.onlinetradeview.tv.cmonulty.PreferenceConnector;
import com.onlinetradeview.tv.cmonulty.apicalling.WebService;
import com.onlinetradeview.tv.cmonulty.apicalling.WebServiceListener;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;
import com.onlinetradeview.tv.cmonulty.customviews.ShowCustomToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class FragCreateOrder extends Fragment implements OnClickListener, WebServiceListener {
    private View aiView = null;
    private TextView txtRateOrder, txtMarketOrder, selectToken, txtRateSl;
    private LinearLayout buyOrder, sellOrder;
    private TextView txtBuy, txtSell;
    private LinearLayout layOrderFuture;
    private EditText tvLotNum, tvWantLotPrice;
    public TextView dataSell, dataBuy, dataLastprice, dataOpenprice, dataCloseprice, dataVolume, dataHighprice, dataLowprice, dataChange, tvMarket;
    public TextView dataBuyers, dataSellers, dataOpenInterest, dataUpperCkt, dataLowerCkt, dataAtp, dataLatBuy, dataLastSell, dataLotSize;

    private final View[] allViewWithClick = {txtRateOrder, txtMarketOrder, layOrderFuture, buyOrder, sellOrder, selectToken, txtRateSl};
    private final int[] allViewWithClickId = {R.id.order, R.id.market, R.id.lay_order_future, R.id.tvbuy, R.id.tvsell, R.id.tvname, R.id.sl};

    private final int[] allCardView = {R.id.cardview_one, R.id.cardview};
    public static int typeBack = 1;
    public static TradeModel createOrderTradeModel = null;

    public FragCreateOrder() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (aiView == null) {
            aiView = inflater.inflate(R.layout.frag_createorder, container, false);
        }
        ((ActMain) getActivity()).changeImage(R.drawable.back);
        return aiView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onCreate(savedInstanceState);
        StartApp();
        resumeApp();
    }

    @Override
    public void onClick(View v) {

    }

    String instrumentName = "", expiryDate = "", catId;

    public void resumeApp() {
        txtBuy = aiView.findViewById(R.id.txt_buy);
        txtSell = aiView.findViewById(R.id.txt_sell);
        if (FragCreateOrder.typeBack == 2) {
            txtBuy.setText("");
            txtSell.setText("");
            selectedToken = createOrderTradeModel.getToken();
            instrumentName = createOrderTradeModel.getName();
            expiryDate = createOrderTradeModel.getStr_date_expiry();
            catId = createOrderTradeModel.getStr_catId();
        } else if (FragCreateOrder.typeBack == 3) {
            txtBuy.setText("");
            txtSell.setText("");
            selectedToken = createOrderTradeModel.getToken();
            instrumentName = createOrderTradeModel.getName();
            expiryDate = createOrderTradeModel.getStr_date_expiry();
            catId = createOrderTradeModel.getStr_catId();
        } else {
            txtBuy.setText(ActSearchMetal.selectedTokenModel.getBuyPrice());
            txtSell.setText(ActSearchMetal.selectedTokenModel.getSellPrice());
            selectedToken = ActSearchMetal.selectedTokenModel.getInstrumentIdentifier();
            instrumentName = ActSearchMetal.selectedTokenModel.getInstrumentName();
            expiryDate = ActSearchMetal.selectedTokenModel.getExpiryDate();
            catId = ActSearchMetal.selectedTokenModel.getGetCatId();
        }

        Log.e("checkdata>>>>1", txtBuy.getText().toString());
        Log.e("checkdata>>>>2", txtSell.getText().toString());
        Log.e("checkdata>>>>3", selectedToken);
        Log.e("checkdata>>>>4",instrumentName);
        Log.e("checkdata>>>>5", expiryDate);
        Log.e("checkdata>>>>6", catId);

        tvLotNum = aiView.findViewById(R.id.tvlotnum);
        dataSell = aiView.findViewById(R.id.data_sell);
        dataBuy = aiView.findViewById(R.id.data_buy);
        dataLastprice = aiView.findViewById(R.id.data_lastprice);
        dataOpenprice = aiView.findViewById(R.id.data_openprice);
        dataCloseprice = aiView.findViewById(R.id.data_closeprice);
        dataVolume = aiView.findViewById(R.id.data_volume);
        dataHighprice = aiView.findViewById(R.id.data_highprice);
        dataLowprice = aiView.findViewById(R.id.data_lowprice);
        dataChange = aiView.findViewById(R.id.data_change);

        RelativeLayout lyt_parent = aiView.findViewById(R.id.lyt_parent);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(svContext.getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setThemeColor(lyt_parent, svContext, font);
        }

        dataBuyers = aiView.findViewById(R.id.data_buyers);
        dataSellers = aiView.findViewById(R.id.data_sellers);
        dataOpenInterest = aiView.findViewById(R.id.data_openinterest);
        dataUpperCkt = aiView.findViewById(R.id.data_upperckt);
        dataLowerCkt = aiView.findViewById(R.id.data_lowerckt);
        dataAtp = aiView.findViewById(R.id.data_atp);
        dataLatBuy = aiView.findViewById(R.id.data_lastbuy);
        dataLastSell = aiView.findViewById(R.id.data_lastsell);
        dataLotSize = aiView.findViewById(R.id.data_lotsize);
        tvMarket = aiView.findViewById(R.id.market);

        GetAllInstrument();

        OnClickCombineDeclare(allViewWithClick);
        ChangeLayoutViewTheme(allCardView);

        tvLotNum.setText("1");
        tvWantLotPrice = aiView.findViewById(R.id.tvbidpricelot);

        tvWantLotPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                txtBuy.setText(s.toString());
                txtSell.setText(s.toString());
            }
        });
    }

    private void ChangeLayoutViewTheme(int[] allView) {
        int drawableColor, tvTextColor;
        if (PreferenceConnector.readInteger(svContext, PreferenceConnector.THEMESELECTED, 0) == 0) {
            drawableColor = R.color.white;
            tvTextColor = R.color.black;
        } else if (PreferenceConnector.readInteger(svContext, PreferenceConnector.THEMESELECTED, 0) == 1) {
            drawableColor = R.color.white;
            tvTextColor = R.color.buttoncolor;
        } else {
            drawableColor = R.color.white;
            tvTextColor = R.color.black;
        }

        for (int i : allView) {
            FontUtils.setCustomThemeColor(aiView.findViewById(i), svContext, tvTextColor);
            ((CardView) aiView.findViewById(i)).setCardBackgroundColor(ContextCompat.getColor(svContext, drawableColor));
        }
        tvMarket.setTextColor(svContext.getResources().getColor(R.color.white));
    }

    private String selectedToken = "";
    private RequestQueue queue;
    private StringRequest stringRequest;
    private String url = "";

    public void GetAllInstrument() {
        if (!stopApi) {
            if (queue == null) {
                queue = Volley.newRequestQueue(svContext);
            }

            url = WebService.LIVEDATAURL + PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, "") + "&token=" + selectedToken;
            stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {
                        Log.e("getAllList", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.has("livedata")) {
                                JSONArray jsonLiveData = json.getJSONArray("livedata");
                                for (int j = 0; j < jsonLiveData.length(); j++) {
                                    JSONObject rows_obj = jsonLiveData.getJSONObject(j);
                                    String InstrumentIdentifier = rows_obj.getString("InstrumentIdentifier");
                                    if (InstrumentIdentifier.equalsIgnoreCase(selectedToken)) {

                                        WatchModel tradeModel = new WatchModel(rows_obj.getString("Exchange"), InstrumentIdentifier, rows_obj.getString("LastTradeTime"),
                                                rows_obj.getString("ServerTime"), rows_obj.getString("AverageTradedPrice"), rows_obj.getString("BuyPrice"),
                                                rows_obj.getString("BuyQty"),
                                                rows_obj.getString("Close"), rows_obj.getString("High"), rows_obj.getString("Low"),
                                                rows_obj.getString("LastTradePrice"), rows_obj.getString("LastTradeQty"), rows_obj.getString("Open"),
                                                rows_obj.getString("OpenInterest"), rows_obj.getString("QuotationLot"), rows_obj.getString("SellPrice"),
                                                rows_obj.getString("SellQty"), rows_obj.getString("TotalQtyTraded"),
                                                rows_obj.getString("Value"), rows_obj.getString("PreOpen"), rows_obj.getString("PriceChange"),
                                                rows_obj.getString("PriceChangePercentage"), rows_obj.getString("OpenInterestChange"), rows_obj.getString("MessageType"),
                                                "", "", true, "", rows_obj.getString("LowerCircuit"), rows_obj.getString("UpperCircuit"));

                                        if (isSetValue) {
                                            txtBuy.setText(rows_obj.getString("SellPrice"));
                                            txtSell.setText(rows_obj.getString("BuyPrice"));
                                        }

                                        dataSell.setText(tradeModel.getSellPrice());
                                        dataBuy.setText(tradeModel.getBuyPrice());
                                        dataLastprice.setText(tradeModel.getLastTradePrice());
                                        dataOpenprice.setText(tradeModel.getOpen());
                                        dataCloseprice.setText(tradeModel.getClose());
                                        dataVolume.setText(tradeModel.getTotalQtyTraded());
                                        dataHighprice.setText(tradeModel.getHigh());
                                        dataLowprice.setText(tradeModel.getLow());
                                        dataChange.setText(tradeModel.getPriceChange());

                                        dataBuyers.setText(tradeModel.getBuyQty());
                                        dataSellers.setText(tradeModel.getSellQty());
                                        dataOpenInterest.setText(tradeModel.getOpenInterest());
                                        dataUpperCkt.setText(tradeModel.getUpperCkt());
                                        dataLowerCkt.setText(tradeModel.getLowerCkt());
                                        dataAtp.setText(tradeModel.getAverageTradedPrice());
                                        dataLatBuy.setText(tradeModel.getBuyQty());
                                        dataLastSell.setText(tradeModel.getSellQty());
                                        dataLotSize.setText(tradeModel.getQuotationLot());
                                    }
                                }
                            } else {
                                if (json.has("message")) {
                                    String message = json.getString("message");
                                    if (message.contains("Session out")) {
                                        PreferenceConnector.cleanPrefrences(svContext);
                                        Intent intent = new Intent(svContext, ActLogin.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        svContext.startActivity(intent);
                                        ((Activity) svContext).finish();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        GetAllInstrument();
                    }, error -> {
                customToast.showCustomToast(svContext, "Network Error", customToast.ToastyError);
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("userID", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""));
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    params.put("Token", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINTOKEN, ""));
                    params.put("Deviceid", GlobalData.getDeviceId(svContext));
                    return params;
                }
            };
            queue.add(stringRequest);
        }
    }


    private String isMarketSelected = "1";

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = aiView.findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(v -> {
                switch (v.getId()) {
                    case R.id.market:
                        stopApi = false;
                        isSetValue = true;
                        isMarketSelected = "1";
                        allViewWithClick[0].setBackground(getResources().getDrawable(R.drawable.back_textviewunselected_center));
                        allViewWithClick[1].setBackground(getResources().getDrawable(R.drawable.back_textviewselected_left));
                        allViewWithClick[6].setBackground(getResources().getDrawable(R.drawable.back_textviewunselected_right));

                        ((TextView) allViewWithClick[0]).setTextColor(getResources().getColor(R.color.colorPrimary));
                        ((TextView) allViewWithClick[1]).setTextColor(getResources().getColor(R.color.white));
                        ((TextView) allViewWithClick[6]).setTextColor(getResources().getColor(R.color.colorPrimary));

                        allViewWithClick[2].setVisibility(View.GONE);
                        break;
                    case R.id.order:
                        stopApi = false;
                        isSetValue = false;
                        isMarketSelected = "2";
                        allViewWithClick[0].setBackground(getResources().getDrawable(R.drawable.back_textviewselected_center));
                        allViewWithClick[1].setBackground(getResources().getDrawable(R.drawable.back_textviewunselected_left));
                        allViewWithClick[6].setBackground(getResources().getDrawable(R.drawable.back_textviewunselected_right));

                        ((TextView) allViewWithClick[0]).setTextColor(getResources().getColor(R.color.white));
                        ((TextView) allViewWithClick[1]).setTextColor(getResources().getColor(R.color.colorPrimary));
                        ((TextView) allViewWithClick[6]).setTextColor(getResources().getColor(R.color.colorPrimary));
                        allViewWithClick[2].setVisibility(View.VISIBLE);
                        break;
                    case R.id.sl:
                        stopApi = false;
                        isSetValue = false;
                        isMarketSelected = "3";
                        allViewWithClick[0].setBackground(getResources().getDrawable(R.drawable.back_textviewunselected_center));
                        allViewWithClick[1].setBackground(getResources().getDrawable(R.drawable.back_textviewunselected_left));
                        allViewWithClick[6].setBackground(getResources().getDrawable(R.drawable.back_textviewselected_right));

                        ((TextView) allViewWithClick[0]).setTextColor(getResources().getColor(R.color.colorPrimary));
                        ((TextView) allViewWithClick[1]).setTextColor(getResources().getColor(R.color.colorPrimary));
                        ((TextView) allViewWithClick[6]).setTextColor(getResources().getColor(R.color.white));

                        allViewWithClick[2].setVisibility(View.VISIBLE);
                        break;
                    case R.id.lay_order_future:

                        break;
                    case R.id.tvbuy:
                        BuySellOrder("1");
                        break;
                    case R.id.tvsell:
                        BuySellOrder("2");
                        break;
                    case R.id.tvname:
//                        Intent intent = new Intent(svContext, ActSearchMetal.class);
//                        intent.putExtra("ischeckshow", "0");
//                        svContext.startActivity(intent);
                        break;
                }
            });
        }
        buyOrder = (LinearLayout) allViewWithClick[3];
        sellOrder = (LinearLayout) allViewWithClick[4];
        selectToken = (TextView) allViewWithClick[5];
        selectToken.setText(instrumentName + "_" + expiryDate);
    }

    @Override
    public void onResume() {
        super.onResume();
        stopApi = false;
    }

    private void BuySellOrder(String orderType) {
        String bidtype = "1";
        if (isMarketSelected.equalsIgnoreCase("1")) {
            bidtype = "1";
            if (tvLotNum.getText().toString().trim().equalsIgnoreCase("Select Option") || selectedToken.equalsIgnoreCase("")) {
                customToast.showCustomToast(svContext, "Please select the trade option", customToast.ToastyError);
            } else if (tvLotNum.getText().toString().trim().isEmpty()) {
                tvLotNum.setError("Please enter lot you want to buy/sell");
            } else {
                String bidPrice = "";
                if (orderType.equals("1")) {
                    bidPrice = txtBuy.getText().toString();
                } else {
                    bidPrice = txtSell.getText().toString();
                }
                SubmitOrder(orderType, bidPrice, bidtype, tvLotNum.getText().toString().trim());
            }
        } else if (isMarketSelected.equalsIgnoreCase("2")) {
            bidtype = "2";
            if (tvLotNum.getText().toString().trim().equalsIgnoreCase("Select Option") || selectedToken.equalsIgnoreCase("")) {
                customToast.showCustomToast(svContext, "Please select the trade option", customToast.ToastyError);
            } else if (tvLotNum.getText().toString().trim().length() == 0) {
                tvLotNum.setError("Please enter lot you want to buy/sell");
            } else if (tvWantLotPrice.getText().toString().trim().length() == 0) {
                tvWantLotPrice.setError("Please enter lot price you want to buy/sell");
            } else {
                SubmitOrder(orderType, tvWantLotPrice.getText().toString().trim(), bidtype, tvLotNum.getText().toString().trim());
            }
        } else {
            bidtype = "3";
            if (tvLotNum.getText().toString().trim().equalsIgnoreCase("Select Option") || selectedToken.equalsIgnoreCase("")) {
                customToast.showCustomToast(svContext, "Please select the trade option", customToast.ToastyError);
            } else if (tvLotNum.getText().toString().trim().length() == 0) {
                tvLotNum.setError("Please enter lot you want to buy/sell");
            } else if (tvWantLotPrice.getText().toString().trim().length() == 0) {
                tvWantLotPrice.setError("Please enter lot price you want to buy/sell");
            } else {
                SubmitOrder(orderType, tvWantLotPrice.getText().toString().trim(), bidtype, tvLotNum.getText().toString().trim());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private boolean isApiCalled = false;
    private RequestQueue requestQueue;

    private void SubmitOrder(String orderType, String bidPrice, String bidtype, String bidLot) {
        if (!isApiCalled) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("userID", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""));
            params.put("catID", catId);
            params.put("exipreDate", expiryDate);
            params.put("orderType", bidtype);
            params.put("bidType", orderType);
            params.put("bidPrice", bidPrice);
            params.put("lotSize", bidLot);
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(svContext);
            }
            CustomeProgressDialog customeProgressDialog = new CustomeProgressDialog(svContext, R.layout.lay_customprogessdialog);
            customeProgressDialog.setCancelable(false);
            customeProgressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, PRE_URL + ORDERAUTH[0], response -> {
                if (customeProgressDialog.isShowing()) {
                    customeProgressDialog.dismiss();
                }
                try {
                    JSONObject json = new JSONObject(response);
                    String str_result = json.getString("status");
                    String str_msg = json.getString("message");
                    if (str_result.equals("1")) {
                        PreferenceConnector.writeString(svContext, PreferenceConnector.WALLETBALANCE, json.getString("wallet_balance"));
                        PreferenceConnector.writeString(svContext, PreferenceConnector.MARGINBALANCE, json.getString("margin_balance"));
                        PreferenceConnector.writeString(svContext, PreferenceConnector.M2MBALANCE, json.getString("m2m_balance"));

                        new MaterialAlertDialogBuilder(svContext)
                                .setTitle("Order Detail")
                                .setMessage(str_msg)
                                .setPositiveButton("OK", (dialog, which) -> {
                                    dialog.dismiss();
                                    switchContent(new FragWatchlist());
                                })
                                .show();
                    } else {
                        ShowDialog(str_msg);
                    }
                    isApiCalled = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, error -> {
                Log.d("error", error.toString());
                if (null != customeProgressDialog && customeProgressDialog.isShowing()) {
                    customeProgressDialog.dismiss();
                }
                error.printStackTrace();
            }) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    params.put("Token", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINTOKEN, ""));
                    params.put("Deviceid", GlobalData.getDeviceId(svContext));
                    return params;
                }
            };
            requestQueue.add(request);
        }

    }

    private void ShowDialog(String showmsg) {
        new MaterialAlertDialogBuilder(svContext)
                .setTitle("Order Detail")
                .setMessage(showmsg)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }


    private Context svContext;
    private ShowCustomToast customToast;
    private CheckInternet checkNetwork;
    private ViewGroup root;

    private void StartApp() {
        svContext = getActivity();
        customToast = new ShowCustomToast(svContext);
        checkNetwork = new CheckInternet(svContext);
        root = (ViewGroup) aiView.findViewById(R.id.mylayout);
    }

    public static void hideFragmentkeyboard(Context meraContext, View meraView) {
        final InputMethodManager imm = (InputMethodManager) meraContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(meraView.getWindowToken(), 0);
    }

    private void callWebService(String[] postUrl, Map<String, String> params) {
        isApiCalled = true;
        WebService webService = new WebService(svContext, postUrl, params, this, true);
        webService.LoadData();
    }

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);
        if (url.contains(ORDERAUTH[0])) {
            try {
                JSONObject json = new JSONObject(result);
                String str_result = json.getString("status");
                String str_msg = json.getString("message");
                if (str_result.equals("1")) {
                    PreferenceConnector.writeString(svContext, PreferenceConnector.WALLETBALANCE, json.getString("wallet_balance"));
                    PreferenceConnector.writeString(svContext, PreferenceConnector.MARGINBALANCE, json.getString("margin_balance"));
                    PreferenceConnector.writeString(svContext, PreferenceConnector.M2MBALANCE, json.getString("m2m_balance"));

                    new MaterialAlertDialogBuilder(svContext)
                            .setTitle("Order Detail")
                            .setMessage(str_msg)
                            .setPositiveButton("OK", (dialog, which) -> {
                                dialog.dismiss();
                                switchContent(new FragWatchlist());
                            })
                            .show();
                } else {
                    ShowDialog(str_msg);
                }
                isApiCalled = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void switchContent(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public void onWebServiceError(String result, String url) {
        customToast.showToast(result, svContext);
    }

    private boolean stopApi = false, isSetValue = true;

    @Override
    public void onStop() {
        super.onStop();
        stopApi = true;
        queue.cancelAll(request -> true);
    }
}