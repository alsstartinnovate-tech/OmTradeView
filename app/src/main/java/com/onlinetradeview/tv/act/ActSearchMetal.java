package com.onlinetradeview.tv.act;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.cmonulty.GlobalData;
import com.onlinetradeview.tv.cmonulty.apicalling.WebService;
import com.onlinetradeview.tv.adptr.PopulateSearchAdapter;
import com.onlinetradeview.tv.frgmnt.FragWatchlist;
import com.onlinetradeview.tv.mdel.InstumentDataModel;
import com.onlinetradeview.tv.mdel.WatchModel;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.PreferenceConnector;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;
import com.onlinetradeview.tv.cmonulty.customviews.ShowCustomToast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActSearchMetal extends AppCompatActivity implements View.OnClickListener {
    public static WatchModel selectedTokenModel;
    private ProgressBar progressbarLoad;
    private RecyclerView recyclerView;
    private PopulateSearchAdapter mAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_populatespinner);
        StartApp();
        resumeApp();
    }

    public void resumeApp() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.searchview);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if (queue == null) {
            queue = Volley.newRequestQueue(svContext);
        }

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mAdapter.getFilter().filter(query);
                return true;
            }
        });

        EditText txtSearch = ((EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text));
        txtSearch.setHint("Search & Add");
        int colorTheme = PreferenceConnector.readInteger(svContext, PreferenceConnector.THEMESELECTED, 0);
        if (colorTheme == 0) {
            txtSearch.setHintTextColor(svContext.getResources().getColor(R.color.tv_themethree_light));
            txtSearch.setTextColor(svContext.getResources().getColor(R.color.tv_themethree));
        } else if (colorTheme == 1) {
            txtSearch.setHintTextColor(svContext.getResources().getColor(R.color.tv_themetwo_light));
            txtSearch.setTextColor(svContext.getResources().getColor(R.color.tv_themetwo));
        } else {
            txtSearch.setHintTextColor(svContext.getResources().getColor(R.color.tv_themethree_light));
            txtSearch.setTextColor(svContext.getResources().getColor(R.color.tv_themethree));
        }
        exchangeName = getIntent().getStringExtra("exchangeName");
        GetInstrumentData();
    }

    private Context svContext;
    private ViewGroup root;
    private void StartApp() {
        svContext = this;
        root = findViewById(R.id.headlayout);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setThemeColor(root, svContext, font);
        }

        hideKeyboard();
        loadToolBar();
        progressbarLoad = findViewById(R.id.progressbar_load);
        progressbarLoad.setVisibility(View.VISIBLE);
        SetColorTheme();
    }

    private void SetColorTheme() {
//        if (PreferenceConnector.readInteger(svContext, PreferenceConnector.THEMESELECTED, 0) == 0) {
//            root.setBackgroundResource(R.color.appbackcolor);
//        } else if (PreferenceConnector.readInteger(svContext, PreferenceConnector.THEMESELECTED, 0) == 1) {
//            root.setBackgroundResource(R.color.colorPrimary);
//        } else {
//            root.setBackgroundResource(R.color.colorPrimary);
//        }
    }

    private ImageView imgToolBarBack;

    private void loadToolBar() {
        imgToolBarBack = (ImageView) findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    private String exchangeName = "MCX";

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void hideFragmentkeyboard(Context meraContext, View meraView) {
        final InputMethodManager imm = (InputMethodManager) meraContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(meraView.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragWatchlist.isRefresh = true;
    }

    private void LoadData() {
        if (exchangeName.equalsIgnoreCase("MCX")) {
            mAdapter = new PopulateSearchAdapter(svContext, lstMetalData, lstMetalDataLast, false);
        } else if (exchangeName.equalsIgnoreCase("NFO")){
            mAdapter = new PopulateSearchAdapter(svContext, lstNseData, lstNseDataLast, false);
        }else {
            //show coming soon toast
            ShowCustomToast customToast = new ShowCustomToast(svContext);
            customToast.showCustomToast(svContext, "Coming Soon", customToast.ToastyWarning);
            onBackPressed();
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        progressbarLoad.setVisibility(View.GONE);
    }


    private StringRequest stringRequest;
    private RequestQueue queue;
//    public List<WatchModel> lstMetalAllData = new ArrayList<>();
    public List<WatchModel> lstMetalData = new ArrayList<>();
    public List<WatchModel> lstMetalDataLast = new ArrayList<>();

//    public List<WatchModel> lstNseAllData = new ArrayList<>();
    public List<WatchModel> lstNseData = new ArrayList<>();
    public List<WatchModel> lstNseDataLast = new ArrayList<>();
    public List<InstumentDataModel> mcxinstrmentList = new ArrayList<>();
    private WatchModel watchModel;
    private boolean stopApi = false;

    private void GetInstrumentData() {
        lstMetalDataLast = new ArrayList<>();
        lstNseDataLast = new ArrayList<>();
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
                            for (int data_i = 0; data_i < ((JSONArray) data).length(); data_i++) {
                                JSONObject data_obj = data.getJSONObject(data_i);
                                boolean isActive;
                                if ((data_obj.getString("is_checked")).equalsIgnoreCase("0")) {
                                    isActive = false;
                                } else {
                                    isActive = true;
                                }

                                boolean isCatActive = false;
                                if (data_obj.has("status")) {
                                    if ((data_obj.getString("status")).equalsIgnoreCase("0")) {
                                        isCatActive = false;
                                    } else {
                                        isCatActive = true;
                                    }
                                }


                                String QuotationLot = "";
                                if (data_obj.has("QuotationLot")) {
                                    QuotationLot = data_obj.getString("QuotationLot");
                                }

                                if (isCatActive) {
                                    mcxinstrmentList.add(new InstumentDataModel(data_obj.getString("category_id"), data_obj.getString("instrument_type"),
                                            data_obj.getString("instrument_token"), data_obj.getString("expire_date"), data_obj.getString("title"),
                                            isActive, QuotationLot));
                                }

                                lstMetalDataLast = new ArrayList<>();
                                lstMetalData = new ArrayList<>();
//                                lstMetalAllData = new ArrayList<>();

                                lstNseDataLast = new ArrayList<>();
                                lstNseData = new ArrayList<>();
//                                lstNseAllData = new ArrayList<>();

                                for (int i = 0; i < mcxinstrmentList.size(); i++) {
                                    if (mcxinstrmentList.get(i).getExchangeName().equalsIgnoreCase("MCX")) {
                                        WatchModel model = new WatchModel(mcxinstrmentList.get(i).getExchangeName(), mcxinstrmentList.get(i).getInstrumentIdentifier(),
                                                "NA", "", "0", "0", "0",
                                                "0", "0", "0", "0", "NA", "0", "", "", "0", "0", "",
                                                "", "0", "0", "0", "", "",
                                                mcxinstrmentList.get(i).getExpiryDate(), mcxinstrmentList.get(i).getInstrumentName(),
                                                mcxinstrmentList.get(i).isSelected(), mcxinstrmentList.get(i).getCatId(), "", "");
                                        lstMetalData.add(model);
                                        lstMetalDataLast.add(model);
                                    } else {
                                        WatchModel model = new WatchModel(mcxinstrmentList.get(i).getExchangeName(), mcxinstrmentList.get(i).getInstrumentIdentifier(),
                                                "NA", "", "0", "0", "0",
                                                "0", "0", "0", "0", "NA", "0", "", "", "0", "0", "",
                                                "", "0", "0", "0", "", "",
                                                mcxinstrmentList.get(i).getExpiryDate(), mcxinstrmentList.get(i).getInstrumentName(),
                                                mcxinstrmentList.get(i).isSelected(), mcxinstrmentList.get(i).getCatId(), "", "");
                                        lstNseData.add(model);
                                        lstNseDataLast.add(model);
                                    }

//                                    if (mcxinstrmentList.get(i).getExchangeName().equalsIgnoreCase("MCX")) {
//                                        lstMetalAllData.add(new WatchModel(mcxinstrmentList.get(i).getExchangeName(), mcxinstrmentList.get(i).getInstrumentIdentifier(),
//                                                "NA", "", "0", "0", "0",
//                                                "0", "0", "0", "0", "NA", "0", "", "", "0", "0", "",
//                                                "", "0", "0", "0", "", "",
//                                                mcxinstrmentList.get(i).getExpiryDate(), mcxinstrmentList.get(i).getInstrumentName(),
//                                                mcxinstrmentList.get(i).isSelected(), mcxinstrmentList.get(i).getCatId(), "", ""));
//                                    } else {
//                                        lstNseAllData.add(new WatchModel(mcxinstrmentList.get(i).getExchangeName(), mcxinstrmentList.get(i).getInstrumentIdentifier(),
//                                                "NA", "", "0", "0", "0",
//                                                "0", "0", "0", "0", "NA", "0", "", "", "0", "0", "",
//                                                "", "0", "0", "0", "", "",
//                                                mcxinstrmentList.get(i).getExpiryDate(), mcxinstrmentList.get(i).getInstrumentName(),
//                                                mcxinstrmentList.get(i).isSelected(), mcxinstrmentList.get(i).getCatId(), "", ""));
//                                    }
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
                                    } else {
                                        selectPOs = GetNseIndexByName(InstrumentIdentifier);
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

                                    if (rows_obj.has("QuotationLot")) {
                                        QuotationLot = rows_obj.getString("QuotationLot");
                                    }
                                    if (selectPOs != -1) {
                                        if (Exchange.equalsIgnoreCase("MCX")) {
                                            if (Double.parseDouble(lstMetalData.get(selectPOs).getBuyPrice()) != Double.parseDouble(BuyPrice) ||
                                                    Double.parseDouble(lstMetalData.get(selectPOs).getSellPrice()) != Double.parseDouble(SellPrice)) {
                                                watchModel = new WatchModel(Exchange, InstrumentIdentifier, LastTradeTime, ServerTime, AverageTradedPrice, BuyPrice, BuyQty,
                                                        Close, High, Low, LastTradePrice, LastTradeQty, Open, OpenInterest, QuotationLot, SellPrice, SellQty, TotalQtyTraded,
                                                        Value, PreOpen, PriceChange, PriceChangePercentage, OpenInterestChange, MessageType,
                                                        lstMetalData.get(selectPOs).getExpiryDate(), lstMetalData.get(selectPOs).getInstrumentName(),
                                                        lstMetalData.get(selectPOs).isSelected(), lstMetalData.get(selectPOs).getGetCatId(), "", "");

                                                lstMetalDataLast.set(selectPOs, lstMetalData.get(selectPOs));
                                                lstMetalData.set(selectPOs, watchModel);

                                                mAdapter.notifyItemChanged(selectPOs);
                                            }
                                        } else {
                                            if (Double.parseDouble(lstNseData.get(selectPOs).getBuyPrice()) != Double.parseDouble(BuyPrice) ||
                                                    Double.parseDouble(lstNseData.get(selectPOs).getSellPrice()) != Double.parseDouble(SellPrice)) {
                                                watchModel = new WatchModel(Exchange, InstrumentIdentifier, LastTradeTime, ServerTime, AverageTradedPrice, BuyPrice, BuyQty,
                                                        Close, High, Low, LastTradePrice, LastTradeQty, Open, OpenInterest, QuotationLot, SellPrice, SellQty, TotalQtyTraded,
                                                        Value, PreOpen, PriceChange, PriceChangePercentage, OpenInterestChange, MessageType,
                                                        lstNseData.get(selectPOs).getExpiryDate(), lstNseData.get(selectPOs).getInstrumentName(),
                                                        lstNseData.get(selectPOs).isSelected(), lstNseData.get(selectPOs).getGetCatId(), "", "");

                                                lstNseDataLast.set(selectPOs, lstNseData.get(selectPOs));
                                                lstNseData.set(selectPOs, watchModel);

                                                mAdapter.notifyItemChanged(selectPOs);
                                            }
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

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            GetAllInstrument();
                        }, 500);

                    }, error -> {
//                customToast.showCustomToast(svContext, "Network Error", customToast.ToastyError);
//                GetAllInstrument();
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

    @Override
    public void onStop() {
        super.onStop();
        stopApi = true;
        queue.stop();
        queue.getCache().clear();
        queue.cancelAll(request -> true);
    }

    @Override
    public void onResume() {
        super.onResume();
        stopApi = false;
    }
}