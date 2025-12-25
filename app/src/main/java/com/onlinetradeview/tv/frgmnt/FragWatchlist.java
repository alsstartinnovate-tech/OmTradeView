package com.onlinetradeview.tv.frgmnt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onesignal.OneSignal;
import com.onesignal.notifications.IPermissionObserver;
import com.onlinetradeview.tv.act.ActLogin;
import com.onlinetradeview.tv.act.ActSearchMetal;
import com.onlinetradeview.tv.cmonulty.GlobalData;
import com.onlinetradeview.tv.cmonulty.PreferenceConnector;
import com.onlinetradeview.tv.cmonulty.apicalling.WebService;
import com.onlinetradeview.tv.adptr.PopulateWatchlistAdapterList;
import com.onlinetradeview.tv.act.ActMain;
import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.mdel.InstumentDataModel;
import com.onlinetradeview.tv.mdel.WatchModel;
import com.onlinetradeview.tv.cmonulty.FragmentTAG;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;
import com.onlinetradeview.tv.cmonulty.customviews.ShowCustomToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragWatchlist extends Fragment implements OnClickListener, IPermissionObserver {
    private View aiView = null;
    private RecyclerView rvlotterylist;
    private TextView txtComingSoon;
    private View viewTabMcx, viewTabNse, viewOthers;

    private final int[] allViewWithClickId = {R.id.lay_tabmcxfuture, R.id.lay_tabnsefuture, R.id.lay_tabother, R.id.searchwatchlist, R.id.btn_add};

    public List<WatchModel> lstMetalData = new ArrayList<>();
    public List<WatchModel> lstMetalDataLast = new ArrayList<>();

    public List<WatchModel> lstNseData = new ArrayList<>();
    public List<WatchModel> lstNseDataLast = new ArrayList<>();

    public List<WatchModel> lstAllData = new ArrayList<>();
    public List<WatchModel> lstAllDataLast = new ArrayList<>();

    private WatchModel watchModel;
    private StringRequest stringRequest;
    public List<InstumentDataModel> mcxinstrmentList = new ArrayList<>();
    private RequestQueue queue;
    private boolean stopApi = false;

    public FragWatchlist() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (aiView == null) {
            aiView = inflater.inflate(R.layout.frag_watchlist, container, false);
        }
        return aiView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onCreate(savedInstanceState);
        aiView = getView();
        StartApp();
        OnClickCombineDeclare(allViewWithClickId);
        resumeApp();
        ((ActMain) svContext).defaultImage();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        isRefresh = true;
        stopApi = true;
        queue.cancelAll(request -> true);
    }

    @Override
    public void onResume() {
        super.onResume();
        stopApi = false;
        if (isRefresh) {
            isRefresh = false;
            resumeApp();
        }
        ((ActMain) svContext).defaultImage();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
    }

    public void resumeApp() {
        rvlotterylist = aiView.findViewById(R.id.rv_lotterylist);
        txtComingSoon = aiView.findViewById(R.id.txt_comingsoon);

        ShowComingSoon((getResources().getText(R.string.label_loading)).toString(), false);

        viewTabMcx = aiView.findViewById(R.id.indicator_mcxfuture);
        viewTabNse = aiView.findViewById(R.id.indicator_nsefutures);
        viewOthers = aiView.findViewById(R.id.indicator_other);

        rvlotterylist.setVisibility(View.VISIBLE);

        GetInstrumentData();
    }

    private void GetInstrumentData() {
        lstMetalDataLast = new ArrayList<>();
        lstNseDataLast = new ArrayList<>();
        lstAllDataLast = new ArrayList<>();

        if (queue == null) {
            queue = Volley.newRequestQueue(svContext);
        }
        String url = WebService.PRE_URL + "getMcxCategoryList?userID=" +
                PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, "");
        ShowCustomToast customToast = new ShowCustomToast(svContext);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.e("getMcxCategoryList", response);
                    try {
                        mcxinstrmentList = new ArrayList<>();
                        JSONObject json = new JSONObject(response);

                        String str_message = json.getString("message");
                        String str_status = json.getString("status");

                        if (str_status.equalsIgnoreCase("0")) {
                            customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                        } else {
                            JSONArray data = json.getJSONArray("data");
                            for (int data_i = 0; data_i < data.length(); data_i++) {
                                JSONObject data_obj = data.getJSONObject(data_i);
                                boolean isActive = false;
                                isActive = !(data_obj.getString("is_checked")).equalsIgnoreCase("0");
                                String QuotationLot = "";
                                if (data_obj.has("QuotationLot")) {
                                    QuotationLot = data_obj.getString("QuotationLot");
                                }
                                boolean isCatActive = false;
                                if (data_obj.has("status")) {
                                    isCatActive = !(data_obj.getString("status")).equalsIgnoreCase("0");
                                }

                                if (isCatActive) {
                                    mcxinstrmentList.add(new InstumentDataModel(data_obj.getString("category_id"), data_obj.getString("instrument_type"),
                                            data_obj.getString("instrument_token"), data_obj.getString("expire_date"), data_obj.getString("title"),
                                            isActive, QuotationLot));
                                }

                                lstMetalDataLast = new ArrayList<>();
                                lstMetalData = new ArrayList<>();

                                lstNseDataLast = new ArrayList<>();
                                lstNseData = new ArrayList<>();

                                lstAllDataLast = new ArrayList<>();
                                lstAllData = new ArrayList<>();

                                for (int i = 0; i < mcxinstrmentList.size(); i++) {
                                    if (mcxinstrmentList.get(i).isSelected()) {
                                        if (mcxinstrmentList.get(i).getExchangeName().equalsIgnoreCase("MCX")) {
                                            WatchModel model = new WatchModel(mcxinstrmentList.get(i).getExchangeName(), mcxinstrmentList.get(i).getInstrumentIdentifier(),
                                                    "NA", "", "0", "0", "0",
                                                    "0", "0", "0", "0", "NA", "0", "", "", "0", "0", "",
                                                    "", "0", "0", "0", "", "",
                                                    mcxinstrmentList.get(i).getExpiryDate(), mcxinstrmentList.get(i).getInstrumentName(),
                                                    mcxinstrmentList.get(i).isSelected(), mcxinstrmentList.get(i).getCatId(), "", "");
                                            lstMetalData.add(model);
                                            lstMetalDataLast.add(model);
                                        } else if (mcxinstrmentList.get(i).getExchangeName().equalsIgnoreCase("NSE")) {
                                            WatchModel model = new WatchModel(mcxinstrmentList.get(i).getExchangeName(), mcxinstrmentList.get(i).getInstrumentIdentifier(),
                                                    "NA", "", "0", "0", "0",
                                                    "0", "0", "0", "0", "NA", "0", "", "", "0", "0", "",
                                                    "", "0", "0", "0", "", "",
                                                    mcxinstrmentList.get(i).getExpiryDate(), mcxinstrmentList.get(i).getInstrumentName(),
                                                    mcxinstrmentList.get(i).isSelected(), mcxinstrmentList.get(i).getCatId(), "", "");
                                            lstNseData.add(model);
                                            lstNseDataLast.add(model);
                                        } else if (mcxinstrmentList.get(i).getExchangeName().equalsIgnoreCase("ALL")) {
                                            WatchModel model = new WatchModel(mcxinstrmentList.get(i).getExchangeName(), mcxinstrmentList.get(i).getInstrumentIdentifier(),
                                                    "NA", "", "0", "0", "0",
                                                    "0", "0", "0", "0", "NA", "0", "", "", "0", "0", "",
                                                    "", "0", "0", "0", "", "",
                                                    mcxinstrmentList.get(i).getExpiryDate(), mcxinstrmentList.get(i).getInstrumentName(),
                                                    mcxinstrmentList.get(i).isSelected(), mcxinstrmentList.get(i).getCatId(), "", "");
                                            lstAllData.add(model);
                                            lstAllDataLast.add(model);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    LoadData();
                    GetAllInstrument();
                }, error -> customToast.showCustomToast(svContext, "Network Error", customToast.ToastyError)) {
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

    public void GetAllInstrument() {
        if (!stopApi) {
            if (queue == null) {
                queue = Volley.newRequestQueue(svContext);
            }
            stringRequest = new StringRequest(Request.Method.GET, WebService.LIVEDATAURL +
                    PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""),
                    response -> {
                        Log.e("getAllList", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.has("livedata")) {
                                JSONArray jsonLiveData = json.getJSONArray("livedata");
                                for (int j = 0; j < jsonLiveData.length(); j++) {
                                    JSONObject rows_obj = jsonLiveData.getJSONObject(j);
                                    String InstrumentIdentifier = rows_obj.getString("InstrumentIdentifier");
                                    String Exchange = rows_obj.getString("Exchange");

                                    int selectPOs = -1;
                                    if (Exchange.equalsIgnoreCase("MCX")) {
                                        selectPOs = GetIndexByName(InstrumentIdentifier);
                                    } else if (Exchange.equalsIgnoreCase("NSE")) {
                                        selectPOs = GetNseIndexByName(InstrumentIdentifier);
                                    } else if (Exchange.equalsIgnoreCase("ALL")) {
                                        selectPOs = GetAllIndexByName(InstrumentIdentifier);
                                    }

                                    String BuyPrice = rows_obj.getString("BuyPrice");
                                    String High = rows_obj.getString("High");
                                    String Low = rows_obj.getString("Low");
                                    String LastTradePrice = rows_obj.getString("LastTradePrice");
                                    String SellPrice = rows_obj.getString("SellPrice");
                                    String PriceChange = rows_obj.getString("PriceChange");
                                    String PriceChangePercentage = rows_obj.getString("PriceChangePercentage");

                                    String LastTradeTime = "", ServerTime = "", BuyQty = "", Close = "", LastTradeQty = "", Open = "",
                                            OpenInterest = "", QuotationLot = "", SellQty = "", TotalQtyTraded = "", Value = "", PreOpen = "",
                                            OpenInterestChange = "", MessageType = "", AverageTradedPrice = "";
                                    if (selectPOs != -1) {
                                        if (Exchange.equalsIgnoreCase("MCX")) {
//                                            if (Double.parseDouble(lstMetalData.get(selectPOs).getBuyPrice()) != Double.parseDouble(BuyPrice) ||
//                                                    Double.parseDouble(lstMetalData.get(selectPOs).getSellPrice()) != Double.parseDouble(SellPrice)) {
                                            watchModel = new WatchModel(Exchange, InstrumentIdentifier, LastTradeTime, ServerTime, AverageTradedPrice, BuyPrice, BuyQty,
                                                    Close, High, Low, LastTradePrice, LastTradeQty, Open, OpenInterest, QuotationLot, SellPrice, SellQty, TotalQtyTraded,
                                                    Value, PreOpen, PriceChange, PriceChangePercentage, OpenInterestChange, MessageType,
                                                    lstMetalData.get(selectPOs).getExpiryDate(), lstMetalData.get(selectPOs).getInstrumentName(),
                                                    true, lstMetalData.get(selectPOs).getGetCatId(), "", "");

                                            lstMetalDataLast.set(selectPOs, lstMetalData.get(selectPOs));
                                            lstMetalData.set(selectPOs, watchModel);
                                            mAdapter.notifyItemChanged(selectPOs);
//                                            }else {
//
//                                            }
                                        } else if (Exchange.equalsIgnoreCase("NFO")) {
                                            watchModel = new WatchModel(Exchange, InstrumentIdentifier, LastTradeTime, ServerTime, AverageTradedPrice, BuyPrice, BuyQty,
                                                    Close, High, Low, LastTradePrice, LastTradeQty, Open, OpenInterest, QuotationLot, SellPrice, SellQty, TotalQtyTraded,
                                                    Value, PreOpen, PriceChange, PriceChangePercentage, OpenInterestChange, MessageType,
                                                    lstNseData.get(selectPOs).getExpiryDate(), lstNseData.get(selectPOs).getInstrumentName(),
                                                    true, lstNseData.get(selectPOs).getGetCatId(), "", "");

                                            lstNseDataLast.set(selectPOs, lstNseData.get(selectPOs));
                                            lstNseData.set(selectPOs, watchModel);
                                            mAdapter.notifyItemChanged(selectPOs);
                                        } else if (Exchange.equalsIgnoreCase("ALL")) {
                                            watchModel = new WatchModel(Exchange, InstrumentIdentifier, LastTradeTime, ServerTime, AverageTradedPrice, BuyPrice, BuyQty,
                                                    Close, High, Low, LastTradePrice, LastTradeQty, Open, OpenInterest, QuotationLot, SellPrice, SellQty, TotalQtyTraded,
                                                    Value, PreOpen, PriceChange, PriceChangePercentage, OpenInterestChange, MessageType,
                                                    lstAllData.get(selectPOs).getExpiryDate(), lstAllData.get(selectPOs).getInstrumentName(),
                                                    true, lstAllData.get(selectPOs).getGetCatId(), "", "");

                                            lstAllDataLast.set(selectPOs, lstAllData.get(selectPOs));
                                            lstAllData.set(selectPOs, watchModel);
                                            mAdapter.notifyItemChanged(selectPOs);
                                        }
                                    } else {
                                        queue.cancelAll(request -> true);
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

                        new Handler(Looper.getMainLooper()).postDelayed(this::GetAllInstrument, 500);
                    }, // add Header here
                    error -> {
                        Log.e("error", error.toString());
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

    private PopulateWatchlistAdapterList mAdapter;
    private String exchangeName = "MCX";

    private void LoadData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(svContext, RecyclerView.VERTICAL, false);
        rvlotterylist.setLayoutManager(layoutManager);
        rvlotterylist.setHasFixedSize(true);

        HideComingSoon("");
        rvlotterylist.setItemAnimator(null);

        if (exchangeName.equalsIgnoreCase("MCX")) {
            if (PreferenceConnector.readInteger(svContext, PreferenceConnector.THEMESELECTED, 0) == 0) {
                mAdapter = new PopulateWatchlistAdapterList(svContext, lstMetalData, lstMetalDataLast, R.layout.item_watchlist);
            } else if (PreferenceConnector.readInteger(svContext, PreferenceConnector.THEMESELECTED, 0) == 1) {
                mAdapter = new PopulateWatchlistAdapterList(svContext, lstMetalData, lstMetalDataLast, R.layout.item_watchlist_dark);
            } else {
                mAdapter = new PopulateWatchlistAdapterList(svContext, lstMetalData, lstMetalDataLast, R.layout.item_watchlist_vip);
            }
        } else if (exchangeName.equalsIgnoreCase("NSE")) {
            if (PreferenceConnector.readInteger(svContext, PreferenceConnector.THEMESELECTED, 0) == 0) {
                mAdapter = new PopulateWatchlistAdapterList(svContext, lstNseData, lstNseDataLast, R.layout.item_watchlist);
            } else if (PreferenceConnector.readInteger(svContext, PreferenceConnector.THEMESELECTED, 0) == 1) {
                mAdapter = new PopulateWatchlistAdapterList(svContext, lstNseData, lstNseDataLast, R.layout.item_watchlist_dark);
            } else {
                mAdapter = new PopulateWatchlistAdapterList(svContext, lstNseData, lstNseDataLast, R.layout.item_watchlist_vip);
            }
        } else if (exchangeName.equalsIgnoreCase("ALL")) {
            lstAllData.addAll(lstNseData);
            lstAllData.addAll(lstMetalData);

            lstAllDataLast.addAll(lstNseDataLast);
            lstAllDataLast.addAll(lstMetalDataLast);
            if (PreferenceConnector.readInteger(svContext, PreferenceConnector.THEMESELECTED, 0) == 0) {
                mAdapter = new PopulateWatchlistAdapterList(svContext, lstAllData, lstAllDataLast, R.layout.item_watchlist);
            } else if (PreferenceConnector.readInteger(svContext, PreferenceConnector.THEMESELECTED, 0) == 1) {
                mAdapter = new PopulateWatchlistAdapterList(svContext, lstAllData, lstAllDataLast, R.layout.item_watchlist_dark);
            } else {
                mAdapter = new PopulateWatchlistAdapterList(svContext, lstAllData, lstAllDataLast, R.layout.item_watchlist_vip);
            }
        }

        rvlotterylist.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((view, position, model) -> {
            stopApi = true;
            ActSearchMetal.selectedTokenModel = model;
            FragCreateOrder.typeBack = 1;
            switchContent(new FragCreateOrder(), FragmentTAG.FragCreateOrder);
        });

    }

    public void switchContent(Fragment fragment, String fragTag) {
        hideFragmentkeyboard(svContext, getView());
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(fragTag)
                .commit();
    }

    private void OnClickCombineDeclare(int[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            aiView.findViewById(allViewWithClickId[j]).setOnClickListener(v -> {
                switch (v.getId()) {
                    case R.id.lay_tabmcxfuture:
                        LoadRv(0);
                        exchangeName = "MCX";
                        LoadData();
                        HideComingSoon((getResources().getText(R.string.label_comingsoon)).toString());
                        break;
                    case R.id.lay_tabnsefuture:
                        LoadRv(1);
                        exchangeName = "NSE";
                        LoadData();
                        HideComingSoon((getResources().getText(R.string.label_comingsoon)).toString());
                        break;
                    case R.id.lay_tabother:
                        LoadRv(2);
                        exchangeName = "ALL";
                        LoadData();
                        ShowComingSoon((getResources().getText(R.string.label_comingsoon)).toString(), false);
                        break;
                    case R.id.searchwatchlist:
                    case R.id.btn_add:
                        showWatchlistBottomSheet();
                        break;
                }
            });
        }
    }

    public static boolean isRefresh = false;

    private void ShowComingSoon(String txtValue, boolean isAdd) {
        rvlotterylist.setVisibility(View.GONE);
        txtComingSoon.setText(txtValue);
        txtComingSoon.setVisibility(View.VISIBLE);
    }

    private void HideComingSoon(String txtValue) {
        new Handler(Looper.getMainLooper()).post(() -> {
            rvlotterylist.setVisibility(View.VISIBLE);
            txtComingSoon.setText(txtValue);
            txtComingSoon.setVisibility(View.GONE);
        });
    }

    private void LoadRv(int type) {
        if (type == 0) {
            viewTabMcx.setVisibility(View.VISIBLE);
            viewTabNse.setVisibility(View.INVISIBLE);
            viewOthers.setVisibility(View.INVISIBLE);
        } else if (type == 1) {
            viewTabMcx.setVisibility(View.INVISIBLE);
            viewTabNse.setVisibility(View.VISIBLE);
            viewOthers.setVisibility(View.INVISIBLE);
        } else if (type == 2) {
            viewTabMcx.setVisibility(View.INVISIBLE);
            viewTabNse.setVisibility(View.INVISIBLE);
            viewOthers.setVisibility(View.VISIBLE);
        }
    }

    private Context svContext;

    private void StartApp() {
        aiView = getView();
        svContext = getActivity();
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setThemeColor(aiView.findViewById(R.id.mylayout), svContext, font);
        }
        int colorTheme = PreferenceConnector.readInteger(svContext, PreferenceConnector.THEMESELECTED, 0);
        if (colorTheme == 0) {
            (aiView.findViewById(R.id.searchwatchlist)).setBackgroundColor(svContext.getResources().getColor(R.color.tv_themethree_light));
        } else if (colorTheme == 1) {
            (aiView.findViewById(R.id.searchwatchlist)).setBackgroundColor(svContext.getResources().getColor(R.color.grey_80));
        } else {
            (aiView.findViewById(R.id.searchwatchlist)).setBackgroundColor(svContext.getResources().getColor(R.color.grey_80));
        }
    }

    public static void hideFragmentkeyboard(Context meraContext, View meraView) {
        final InputMethodManager imm = (InputMethodManager) meraContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(meraView.getWindowToken(), 0);
    }

    public int GetIndexByName(String instIdentifier) {
        int pos = -1;
        for (int i = 0; i < lstMetalData.size(); i++) {
            if (lstMetalData.get(i).getInstrumentIdentifier().equalsIgnoreCase(instIdentifier)) {
                pos = i;
            }
        }
        return pos;
    }

    public int GetNseIndexByName(String instIdentifier) {
        int pos = -1;
        for (int i = 0; i < lstNseData.size(); i++) {
            if (lstNseData.get(i).getInstrumentIdentifier().equalsIgnoreCase(instIdentifier)) {
                pos = i;
            }
        }
        return pos;
    }

    public int GetAllIndexByName(String instIdentifier) {
        int pos = -1;
        for (int i = 0; i < lstAllData.size(); i++) {
            if (lstAllData.get(i).getInstrumentIdentifier().equalsIgnoreCase(instIdentifier)) {
                pos = i;
            }
        }
        return pos;
    }

    @Override
    public void onNotificationPermissionChange(boolean permission) {
        if (permission) {
            PreferenceConnector.writeString(svContext, PreferenceConnector.FCMID, OneSignal.getUser().getPushSubscription().getId());
        }
    }

    public void showWatchlistBottomSheet() {
        WatchlistBottomSheet.newInstance(1, 1).show(getParentFragmentManager(), "watchlist_root");
    }
}
