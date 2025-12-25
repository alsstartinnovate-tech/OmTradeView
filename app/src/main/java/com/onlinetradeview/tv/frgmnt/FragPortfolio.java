package com.onlinetradeview.tv.frgmnt;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onlinetradeview.tv.cmonulty.FragmentTAG;
import com.onlinetradeview.tv.cmonulty.GlobalData;
import com.onlinetradeview.tv.act.ActMain;
import com.onlinetradeview.tv.act.ActSplash;
import com.onlinetradeview.tv.adptr.PotfolioAdapter;
import com.onlinetradeview.tv.adptr.PotfolioCloseAdapter;
import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.mdel.TradeModel;
import com.onlinetradeview.tv.cmonulty.CheckInternet;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.PreferenceConnector;
import com.onlinetradeview.tv.cmonulty.apicalling.WebService;
import com.onlinetradeview.tv.cmonulty.apicalling.WebServiceListener;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;
import com.onlinetradeview.tv.cmonulty.customviews.ShowCustomToast;
import com.onlinetradeview.tv.cmonulty.recyclerview.ItemAnimation;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FragPortfolio extends Fragment implements OnClickListener, WebServiceListener {
    private View aiView = null;
    private boolean mAlreadyLoaded = false;
    private RecyclerView history_rv;
    private CardView cardActiveData, cardCloseData;
    private Parcelable recyclerViewState;

    private final int[] allViewWithClickId = {R.id.lay_tabmcxfuture, R.id.lay_tabnsefuture};

    private final int[] chanegLayoutTheme = {R.id.lay_top, R.id.lay_close};
    private final int[] chanegLayoutViewTheme = {R.id.view_one, R.id.view_two, R.id.view_three, R.id.view_four, R.id.view_five, R.id.view_six, R.id.view_ten};
    private final int[] chanegLayoutCardViewTheme = {R.id.cardview, R.id.cardview_close};

    public FragPortfolio() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (aiView == null) {
            aiView = inflater.inflate(R.layout.frag_portfolio, container, false);
        }
        return aiView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onCreate(savedInstanceState);
        StartApp();
        OnClickCombineDeclare(allViewWithClickId);
        if (savedInstanceState == null && !mAlreadyLoaded) {
            mAlreadyLoaded = true;
            aiView = getView();
        }
        resumeApp();
        ((ActMain) svContext).defaultImage();
    }

    private int animation_type = ItemAnimation.NONE;
    @Override
    public void onClick(View v) {

    }

    private SwipeRefreshLayout layrefrsh;
    private TextView txtLedBal, txtMergin, txtM2m, txtPl, txtReqHolMargin, txtPlClose, txtLedBalClose, txtBrokerageClose, txtTotalPlClose;
    public void resumeApp() {
        history_rv = aiView.findViewById(R.id.rv_lotterylist);
        txtLedBal = aiView.findViewById(R.id.txt_led_bal);
        txtMergin = aiView.findViewById(R.id.txt_margin_avail);
        txtM2m = aiView.findViewById(R.id.txt_m2m);
        txtPl = aiView.findViewById(R.id.txt_active_pl);
        txtReqHolMargin = aiView.findViewById(R.id.txt_req_holding_margin);

        txtPlClose = aiView.findViewById(R.id.txt_active_pl_close);
        txtLedBalClose = aiView.findViewById(R.id.txt_led_bal_close);
        txtBrokerageClose = aiView.findViewById(R.id.txt_brokerage_close);
        txtTotalPlClose = aiView.findViewById(R.id.txt_pl_close);

        cardActiveData = aiView.findViewById(R.id.cardview);
        cardCloseData = aiView.findViewById(R.id.cardview_close);

        viewTabPending = aiView.findViewById(R.id.indicator_mcxfuture);
        viewTabActive = aiView.findViewById(R.id.indicator_nsefutures);

        layrefrsh = aiView.findViewById(R.id.layrefrsh);
        layrefrsh.setOnRefreshListener(() -> {
            layrefrsh.setRefreshing(false);
            strType = 1;
            LoadPortfolio(strType);
        });
        LoadHeaderData();
        layrefrsh.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

        strType = 1;
        LoadPortfolio(strType);

        ChangeLayoutTheme(chanegLayoutTheme);
        ChangeLayoutViewTheme(chanegLayoutViewTheme);
        ChangeLayoutCardViewTheme(chanegLayoutCardViewTheme);
    }

    private void ChangeLayoutCardViewTheme(int[] allView) {
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
            FontUtils.setCustomThemeColor(((CardView) aiView.findViewById(i)), svContext, tvTextColor);
            ((CardView) aiView.findViewById(i)).setCardBackgroundColor(ContextCompat.getColor(svContext, drawableColor));
        }
    }


    private void LoadHeaderData() {
        txtLedBal.setText(roundup(PreferenceConnector.readString(svContext, PreferenceConnector.WALLETBALANCE, "0")));
        txtMergin.setText(roundup(PreferenceConnector.readString(svContext, PreferenceConnector.MARGINBALANCE, "0")));
        txtM2m.setText(roundup(PreferenceConnector.readString(svContext, PreferenceConnector.M2MBALANCE, "0")));

        Double pl = GlobalData.roundUp(Double.parseDouble(PreferenceConnector.readString(svContext, PreferenceConnector.PLBALANCE, "0")), 2);
        txtPl.setText(pl + "");
        if (pl < 0) {
            txtPl.setTextColor(svContext.getResources().getColor(R.color.red_400));
        } else {
            txtPl.setTextColor(svContext.getResources().getColor(R.color.green_400));
        }
    }

    private String roundup(String value) {
        double rounded = GlobalData.roundUp(Double.parseDouble(value), 2);
        return String.format("%,.2f", rounded);  // Format with thousands separator and 2 decimal places
    }



    private Map<String, String> params;
    private void LoadPortfolio(int type) {
        if (type == 1) {
            cardActiveData.setVisibility(View.VISIBLE);
            cardCloseData.setVisibility(View.GONE);
        } else {
            cardActiveData.setVisibility(View.GONE);
            cardCloseData.setVisibility(View.VISIBLE);
        }

        if (!stopApi) {
            if (type == 1) {
                LoadDataSync(type);
            } else {
                params = new HashMap<>();
                params.put("userID", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""));
                params.put("type", type + "");
                callWebServiceWithoutLoader(WebService.GETPORTFOLIOCLOSE, params);
            }
        }
    }

    private RequestQueue queue;
    private StringRequest stringRequest;
    private void LoadDataSync(int type) {
        if (!stopApi) {
            if (queue == null) {
                queue = Volley.newRequestQueue(svContext);
            }
            stringRequest = new StringRequest(Request.Method.POST, WebService.PRE_URL + WebService.GETPORTFOLIO[0],
                    response -> {
                        Log.e("getAllList", response);
                        try {
                            strReqHoldMargin = "0";
                            lstMcxtrades = new ArrayList<>();
                            JSONObject json = new JSONObject(response);
                            String str_result = json.getString("status");
                            String str_msg = json.getString("message");

                            if (str_result.equals("1")) {
                                PreferenceConnector.writeString(svContext, PreferenceConnector.MARGINBALANCE, getStringKey(json, "totalMarginBalance"));
                                PreferenceConnector.writeString(svContext, PreferenceConnector.M2MBALANCE, getStringKey(json, "totalM2MBalance"));
                                PreferenceConnector.writeString(svContext, PreferenceConnector.WALLETBALANCE, getStringKey(json, "totalLedgerBalance"));
                                PreferenceConnector.writeString(svContext, PreferenceConnector.PLBALANCE, getStringKey(json, "totalPLBalance"));

                                txtLedBal.setText(roundup(PreferenceConnector.readString(svContext, PreferenceConnector.WALLETBALANCE, "0")));
                                txtMergin.setText(roundup(PreferenceConnector.readString(svContext, PreferenceConnector.MARGINBALANCE, "0")));
                                txtM2m.setText((PreferenceConnector.readString(svContext, PreferenceConnector.M2MBALANCE, "0")));
                                txtPl.setText(roundup(PreferenceConnector.readString(svContext, PreferenceConnector.PLBALANCE, "0")));

                                JSONArray data = json.getJSONArray(TAG_DATA);
                                for (int data_i = 0; data_i < data.length(); data_i++) {
                                    JSONObject data_obj = data.getJSONObject(data_i);
                                    String strInstName = data_obj.getString("instrument_token");
                                    String str_lot = data_obj.getString("lot");
                                    String str_catId = data_obj.getString("category_id");
                                    String str_date = data_obj.getString("orderDateTime");
                                    String str_category = data_obj.getString("category_name");
                                    String str_order_type = data_obj.getString("order_type");
                                    String str_bid_price = data_obj.getString("bid_price");
                                    String str_type = data_obj.getString("bid_type");
                                    String str_intraday = data_obj.getString("intradayMargin");
                                    String str_holding = data_obj.getString("holdingMargin");
                                    String str_avg_bidprice = data_obj.getString("avrageBidPrice");

                                    String str_pl = data_obj.getString("pl_balance");
                                    String str_cmp = data_obj.getString("cmprate");

                                    strReqHoldMargin = (roundup(getStringKey(data_obj, "totalReqHoldingMargin")));

                                    String str_expiry_date = "";
                                    if (data_obj.has("expire_date")) {
                                        str_expiry_date = data_obj.getString("expire_date");
                                    }
                                    String str_id = "";
                                    if (data_obj.has("orderID")) {
                                        str_id = data_obj.getString("orderID");
                                    }

                                    TradeModel tradeModel = new TradeModel();
                                    tradeModel.setId(str_id);
                                    tradeModel.setName(str_category);
                                    tradeModel.setSoldtrader(svContext.getResources().getString(R.string.app_name));
                                    tradeModel.setSoldnumber(str_lot);
                                    tradeModel.setDatesold(str_date);
                                    tradeModel.setDatesoldmarket(str_date);
                                    tradeModel.setPricebought(str_bid_price);
                                    tradeModel.setQtybought(str_lot);
                                    tradeModel.setBoughtradernumber("");
                                    tradeModel.setDatebought(str_date);
                                    tradeModel.setDateboughtmarket(str_date);
                                    tradeModel.setStr_avg_bidprice(str_avg_bidprice);
                                    tradeModel.setStr_order_type(str_order_type);
                                    tradeModel.setStr_type(str_type);
                                    tradeModel.setHoldingMarginReq(str_holding);
                                    tradeModel.setUsedMargin(str_intraday);
                                    tradeModel.setStr_date_expiry(str_expiry_date);
                                    tradeModel.setStrPlPRice(str_pl);
                                    tradeModel.setStrCmp(str_cmp);
                                    tradeModel.setStr_catId(str_catId);
                                    tradeModel.setToken(strInstName);
                                    lstMcxtrades.add(tradeModel);
                                }
                                txtReqHolMargin.setText(strReqHoldMargin);
                                LoadHeaderData();
                                LoadData(strType);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (strType == 1) {
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                LoadPortfolio(strType);
                            }, 500);
                        }
                    }, error -> customToast.showCustomToast(svContext, "Network Error", customToast.ToastyError)) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("userID", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""));
                    params.put("type", type + "");
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

    private void OnClickCombineDeclare(int[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            (aiView.findViewById(allViewWithClickId[j])).setOnClickListener(v -> {
                switch (v.getId()) {
                    case R.id.lay_tabmcxfuture:
                        stopApi = false;
                        strType = 1;
                        LoadRv(strType);
                        LoadPortfolio(strType);
                        break;
                    case R.id.lay_tabnsefuture:
                        strType = 2;
                        LoadRv(strType);
                        LoadPortfolio(strType);
                        break;
                }
            });
        }
    }

    private void ChangeLayoutTheme(int[] allView) {
        int drawableBack;
        if (PreferenceConnector.readInteger(svContext, PreferenceConnector.THEMESELECTED, 0) == 0) {
            drawableBack = R.drawable.back_dash_black;
        } else if (PreferenceConnector.readInteger(svContext, PreferenceConnector.THEMESELECTED, 0) == 1) {
            drawableBack = R.drawable.back_dash_golden;
        } else {
            drawableBack = R.drawable.back_dash_card;
        }
        for (int j = 0; j < allView.length; j++) {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                aiView.findViewById(allView[j]).setBackgroundDrawable(ContextCompat.getDrawable(svContext, drawableBack));
            } else {
                aiView.findViewById(allView[j]).setBackground(ContextCompat.getDrawable(svContext, drawableBack));
            }
        }
    }

    private void ChangeLayoutViewTheme(int[] allView) {
        int drawableColor;
        if (PreferenceConnector.readInteger(svContext, PreferenceConnector.THEMESELECTED, 0) == 0) {
            drawableColor = R.color.black;
        } else if (PreferenceConnector.readInteger(svContext, PreferenceConnector.THEMESELECTED, 0) == 1) {
            drawableColor = R.color.golden;
        } else {
            drawableColor = R.color.black;
        }
        for (int i : allView) {
            aiView.findViewById(i).setBackgroundColor(ContextCompat.getColor(svContext, drawableColor));
        }
    }


    private View viewTabPending, viewTabActive;

    private void LoadRv(int type) {
        if (type == 1) {
            viewTabPending.setVisibility(View.VISIBLE);
            viewTabActive.setVisibility(View.INVISIBLE);
        } else if (type == 2) {
            viewTabPending.setVisibility(View.INVISIBLE);
            viewTabActive.setVisibility(View.VISIBLE);
        } else {
            viewTabPending.setVisibility(View.INVISIBLE);
            viewTabActive.setVisibility(View.INVISIBLE);
        }
    }

    public static final String TAG_DATA = "data";
    private List<TradeModel> lstMcxtrades = new ArrayList<>();
    private List<TradeModel> lstMcxtradesClose = new ArrayList<>();
    private void LoadData(int type) {
        recyclerViewState = Objects.requireNonNull(history_rv.getLayoutManager()).onSaveInstanceState();
        if (type == 1) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(svContext, RecyclerView.VERTICAL, false);
            PotfolioAdapter mAdapter = new PotfolioAdapter(svContext, lstMcxtrades, animation_type);
            history_rv.setLayoutManager(layoutManager);
            history_rv.setAdapter(mAdapter);
            history_rv.getLayoutManager().onRestoreInstanceState(recyclerViewState);

            mAdapter.setOnItemClickListener(new PotfolioAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, String obj, int position) {
                    FragCreateOrder.createOrderTradeModel = lstMcxtrades.get(position);
                    FragCreateOrder.typeBack = 3;
                    switchContent(new FragCreateOrder(), FragmentTAG.FragCreateOrder);
                }

                @Override
                public void onButtonItemClick(View view, String obj, int position) {
                    ConfirmDialog(svContext, "Close Trade", "Confirm Close Trade",
                            "Confirm and close the selected trade", position);
                }
            });
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(svContext, RecyclerView.VERTICAL, false);
            PotfolioCloseAdapter mAdapter = new PotfolioCloseAdapter(svContext, lstMcxtradesClose, animation_type);
            history_rv.setLayoutManager(layoutManager);
            history_rv.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener((view, obj, position) -> {

            });
        }
    }

    public void switchContent(Fragment fragment, String fragTag) {
        hideFragmentkeyboard(svContext, getView());
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(fragTag)
                .commit();
    }

    Dialog dialogCancel;
    public void ConfirmDialog(Context svContext, String head, String strTitle, String strDesc, int position) {
        dialogCancel = new Dialog(svContext);
        dialogCancel.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCancel.setCancelable(false);
        dialogCancel.setContentView(R.layout.dialog_header_twobutton);

        TextView textTitle = (TextView) dialogCancel.findViewById(R.id.dialog_title);
        textTitle.setText(strTitle);
        TextView textDesc = (TextView) dialogCancel.findViewById(R.id.dialog_desc);
        textDesc.setText(strDesc);
        TextView textHead = (TextView) dialogCancel.findViewById(R.id.dialog_head);
        textHead.setText(head);

        Button declineDialogButton = (Button) dialogCancel.findViewById(R.id.bt_decline);
        declineDialogButton.setOnClickListener(v -> dialogCancel.dismiss());

        Button confirmDialogButton = (Button) dialogCancel.findViewById(R.id.bt_confirm);
        confirmDialogButton.setOnClickListener(v -> {
            Map<String, String> params = new HashMap<String, String>();
            params.put("userID", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""));
            params.put("orderID", lstMcxtrades.get(position).getStr_catId());
            params.put("type", "3");
            callWebService(WebService.CLOSECANCELTRADE, params);
        });
        dialogCancel.show();
    }


    private Context svContext;
    private ShowCustomToast customToast;
    private CheckInternet checkNetwork;
    private ViewGroup root;

    private void StartApp() {
        svContext = getActivity();
        customToast = new ShowCustomToast(svContext);
        checkNetwork = new CheckInternet(svContext);
        root = aiView.findViewById(R.id.mylayout);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setThemeColor(root, svContext, font);
        }
    }

    public static void hideFragmentkeyboard(Context meraContext, View meraView) {
        final InputMethodManager imm = (InputMethodManager) meraContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(meraView.getWindowToken(), 0);
    }

    private void callWebServiceWithoutLoader(String[] postUrl, Map<String, String> params) {
        WebService webService = new WebService(svContext, postUrl, params, this, false);
        webService.LoadData();
    }

    private void callWebService(String[] postUrl, Map<String, String> params) {
        WebService webService = new WebService(svContext, postUrl, params, this, true);
        webService.LoadData();
    }

    private String getStringKey(JSONObject json, String key) throws JSONException {
        String strKey = json.getString(key);
        if (strKey.equals("null")) {
            strKey = "0";
        }
        return strKey;
    }


    String strReqHoldMargin = "0";
    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);
        if (url.contains(WebService.GETPORTFOLIO[0])) {
            try {
                strReqHoldMargin = "0";
                lstMcxtrades = new ArrayList<>();
                JSONObject json = new JSONObject(result);
                String str_result = json.getString("status");
                String str_msg = json.getString("message");

                PreferenceConnector.writeString(svContext, PreferenceConnector.MARGINBALANCE, getStringKey(json, "totalMarginBalance"));
                PreferenceConnector.writeString(svContext, PreferenceConnector.M2MBALANCE, getStringKey(json, "totalM2MBalance"));
                PreferenceConnector.writeString(svContext, PreferenceConnector.WALLETBALANCE, getStringKey(json, "totalLedgerBalance"));
                PreferenceConnector.writeString(svContext, PreferenceConnector.PLBALANCE, getStringKey(json, "totalPLBalance"));

                txtLedBal.setText(roundup(PreferenceConnector.readString(svContext, PreferenceConnector.WALLETBALANCE, "0")));
                txtMergin.setText(roundup(PreferenceConnector.readString(svContext, PreferenceConnector.MARGINBALANCE, "0")));
                txtM2m.setText((PreferenceConnector.readString(svContext, PreferenceConnector.M2MBALANCE, "0")));
                txtPl.setText(roundup(PreferenceConnector.readString(svContext, PreferenceConnector.PLBALANCE, "0")));

                if (str_result.equals("1")) {
                    JSONArray data = json.getJSONArray(TAG_DATA);
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String strInstName = data_obj.getString("instrument_token");
                        String str_lot = data_obj.getString("lot");
                        String str_catId = data_obj.getString("category_id");
                        String str_date = data_obj.getString("orderDateTime");
                        String str_category = data_obj.getString("category_name");
                        String str_order_type = data_obj.getString("order_type");
                        String str_bid_price = data_obj.getString("bid_price");
                        String str_type = data_obj.getString("bid_type");
                        String str_intraday = data_obj.getString("intradayMargin");
                        String str_holding = data_obj.getString("holdingMargin");
                        String str_avg_bidprice = data_obj.getString("avrageBidPrice");
                        String str_pl = data_obj.getString("pl_balance");
                        String str_cmp = data_obj.getString("cmprate");
                        strReqHoldMargin = (roundup(getStringKey(data_obj, "totalReqHoldingMargin")));

                        String str_expiry_date = "";
                        if (data_obj.has("expire_date")) {
                            str_expiry_date = data_obj.getString("expire_date");
                        }
                        String str_id = "";
                        if (data_obj.has("orderID")) {
                            str_id = data_obj.getString("orderID");
                        }

                        TradeModel tradeModel = new TradeModel();
                        tradeModel.setId(str_id);
                        tradeModel.setName(str_category);
                        tradeModel.setSoldtrader(svContext.getResources().getString(R.string.app_name));
                        tradeModel.setSoldnumber(str_lot);
                        tradeModel.setDatesold(str_date);
                        tradeModel.setToken(strInstName);
                        tradeModel.setDatesoldmarket(str_date);
                        tradeModel.setPricebought(str_bid_price);
                        tradeModel.setQtybought(str_lot);
                        tradeModel.setBoughtradernumber("");
                        tradeModel.setDatebought(str_date);
                        tradeModel.setDateboughtmarket(str_date);
                        tradeModel.setStr_avg_bidprice(str_avg_bidprice);
                        tradeModel.setStr_order_type(str_order_type);
                        tradeModel.setStr_type(str_type);
                        tradeModel.setHoldingMarginReq(str_holding);
                        tradeModel.setUsedMargin(str_intraday);
                        tradeModel.setStr_date_expiry(str_expiry_date);
                        tradeModel.setStrPlPRice(str_pl);
                        tradeModel.setStrCmp(str_cmp);
                        tradeModel.setStr_catId(str_catId);
                        lstMcxtrades.add(tradeModel);
                    }

                    txtReqHolMargin.setText(strReqHoldMargin);
                    LoadHeaderData();
                    LoadData(strType);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strType == 1) {
                LoadPortfolio(strType);
            }
        } else if (url.contains(WebService.GETPORTFOLIOCLOSE[0])) {
            try {
                lstMcxtradesClose = new ArrayList<>();
                JSONObject json = new JSONObject(result);
                String str_result = json.getString("status");
                String str_msg = json.getString("message");

                PreferenceConnector.writeString(svContext, PreferenceConnector.LEDGERBALANCECLOSE, json.getString("totalLedgerBalance"));
                PreferenceConnector.writeString(svContext, PreferenceConnector.PLBALANCECLOSE, json.getString("totalPLBalance"));

                txtPlClose.setText(roundup(PreferenceConnector.readString(svContext, PreferenceConnector.PLBALANCECLOSE, "0")));
                txtLedBalClose.setText(roundup(PreferenceConnector.readString(svContext, PreferenceConnector.LEDGERBALANCECLOSE, "0")));
                txtBrokerageClose.setText(roundup(json.getString("totalBrokerage")));
                txtTotalPlClose.setText(roundup(json.getString("netProfitLoss")));

                if (str_result.equals("1")) {
                    JSONArray data = json.getJSONArray(TAG_DATA);
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        String str_id = "";
                        if (data_obj.has("orderID")) {
                            str_id = data_obj.getString("orderID");
                        }
                        String str_catId = data_obj.getString("category_id");
                        String str_category = data_obj.getString("category_name");
                        String strInstName = data_obj.getString("instrument_token");
                        String str_expiry_date = "";
                        if (data_obj.has("expire_date")) {
                            str_expiry_date = data_obj.getString("expire_date");
                        }
                        String str_order_type = data_obj.getString("order_type");
                        String str_lot = data_obj.getString("lot");
                        String str_type = data_obj.getString("bid_type");
                        String str_date = data_obj.getString("orderDateTime");
                        String str_bid_price = data_obj.getString("bid_price");
                        String str_intraday = data_obj.getString("intradayMargin");
                        String str_holding = data_obj.getString("holdingMargin");
                        String str_avg_buyprice = data_obj.getString("avgBuyPrice");
                        String str_avg_sellprice = data_obj.getString("avgSellPrice");
                        String str_brokerage = data_obj.getString("brokerage");
                        String str_plbalance = data_obj.getString("pl_balance");

                        TradeModel tradeModel = new TradeModel();
                        tradeModel.setId(str_id);
                        tradeModel.setName(str_category);
                        tradeModel.setSoldtrader(svContext.getResources().getString(R.string.app_name));
                        tradeModel.setSoldnumber(str_lot);
                        tradeModel.setDatesold(str_date);
                        tradeModel.setDatesoldmarket(str_date);
                        tradeModel.setPricebought(str_bid_price);
                        tradeModel.setQtybought(str_lot);
                        tradeModel.setBoughtradernumber("");
                        tradeModel.setDatebought(str_date);
                        tradeModel.setDateboughtmarket(str_date);
                        tradeModel.setStr_order_type(str_order_type);
                        tradeModel.setStr_type(str_type);
                        tradeModel.setHoldingMarginReq(str_holding);
                        tradeModel.setUsedMargin(str_intraday);
                        tradeModel.setStr_date_expiry(str_expiry_date);
                        tradeModel.setStr_catId(str_catId);
                        tradeModel.setAvgBuyPrice(str_avg_buyprice);
                        tradeModel.setAvgSellPrice(str_avg_sellprice);
                        tradeModel.setBrokerage(str_brokerage);
                        tradeModel.setStr_plbalanceClose(str_plbalance);
                        tradeModel.setToken(strInstName);
                        lstMcxtradesClose.add(tradeModel);
                    }
                    LoadData(strType);
                }
                stopApi = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (url.contains(WebService.CLOSECANCELTRADE[0])) {
            try {
                JSONObject json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("0")) {
                    dialogCancel.dismiss();
                    customToast.showCustomToast(svContext, str_message, customToast.SweetAlertFailed);
                } else {
                    dialogCancel.dismiss();
                    customToast.showCustomToast(svContext, str_message, customToast.SweetAlertSuccess);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        } else if (url.contains(WebService.UPDATEFCM[0])) {
            if (ActSplash.UpdateData(result, svContext, customToast)) {
                txtLedBal.setText(PreferenceConnector.readString(svContext, PreferenceConnector.WALLETBALANCE, "0"));
                txtMergin.setText(PreferenceConnector.readString(svContext, PreferenceConnector.MARGINBALANCE, "0"));
                txtM2m.setText(PreferenceConnector.readString(svContext, PreferenceConnector.M2MBALANCE, "0"));
            }
        }
    }

    private int strType = 1;

    @Override
    public void onWebServiceError(String result, String url) {
        customToast.showToast(result, svContext);
    }

    private boolean stopApi = false;

    @Override
    public void onStop() {
        super.onStop();
        stopApi = true;
        queue.cancelAll(request -> true);
    }

    @Override
    public void onResume() {
        super.onResume();
        stopApi = false;
    }
}