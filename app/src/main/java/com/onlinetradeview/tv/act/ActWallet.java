package com.onlinetradeview.tv.act;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.cmonulty.apicalling.WebService;
import com.onlinetradeview.tv.cmonulty.apicalling.WebServiceListener;
import com.onlinetradeview.tv.adptr.WalletHistoryAdapter;
import com.onlinetradeview.tv.mdel.WalletHistoryModel;
import com.onlinetradeview.tv.cmonulty.CheckInternet;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.PreferenceConnector;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;
import com.onlinetradeview.tv.cmonulty.customviews.ShowCustomToast;
import com.onlinetradeview.tv.cmonulty.errorscreen.NoInternetScreen;
import com.onlinetradeview.tv.cmonulty.recyclerview.ItemAnimation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActWallet extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private RecyclerView wallethistoryrv;
    private TextView txtWalletbal;
    private CardView cardShowBalance;
    private boolean isFirstLoad = true;
    private RecyclerView rvPagination;
    private NestedScrollView layNestedScroll;
    private int pageNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_wallet);
        StartApp();
        resumeApp();
    }

    public void resumeApp() {
        layNestedScroll = (NestedScrollView) findViewById(R.id.lay_nestedscroll);
        wallethistoryrv = (RecyclerView) findViewById(R.id.wallethistory_rv);
        rvPagination = (RecyclerView) findViewById(R.id.rv_pagination);
        txtWalletbal = (TextView) findViewById(R.id.walletbal);
        txtWalletbal.setVisibility(View.INVISIBLE);
        cardShowBalance = (CardView) findViewById(R.id.card_wallbal);
        cardShowBalance.setVisibility(View.INVISIBLE);
        isFirstLoad = true;

        LoadWalletHistory();

        txtWalletbal.setText(PreferenceConnector.readString(svContext, PreferenceConnector.WALLETBALANCE, "0"));
    }

    private void LoadWalletHistory() {
        Map<String, String> MyData = new HashMap<String, String>();
        MyData.put("userID", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""));
        callWebService(WebService.GETWALLETHISTORY, MyData);
    }


    private Context svContext;
    private ShowCustomToast customToast;
    private CheckInternet checkNetwork;
    private NoInternetScreen errrorScreen;

    private void StartApp() {
        svContext = this;
        customToast = new ShowCustomToast(svContext);
        checkNetwork = new CheckInternet(svContext);
        ViewGroup root = (ViewGroup) findViewById(R.id.headlayout);
        errrorScreen = new NoInternetScreen(svContext, root, ActWallet.this);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        hideKeyboard();
        loadToolBar();
    }

    private ImageView imgToolBarBack;

    private void loadToolBar() {
        imgToolBarBack = (ImageView) findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView txtHeadingName = (TextView) findViewById(R.id.heading);
        txtHeadingName.setText("Wallet History");
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

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        // check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void hideFragmentkeyboard(Context meraContext, View meraView) {
        final InputMethodManager imm = (InputMethodManager) meraContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(meraView.getWindowToken(), 0);
    }

    private List<WalletHistoryModel> lstItems = new ArrayList<>();

//    void LoadPaginationView(int paginationSize) {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        rvPagination.setLayoutManager(layoutManager);
//        rvPagination.setHasFixedSize(true);
//        int animation_type = ItemAnimation.LEFT_RIGHT;
//        PaginationAdapter mAdapter = new PaginationAdapter(this, paginationSize, animation_type);
//        rvPagination.setNestedScrollingEnabled(false);
//        rvPagination.setAdapter(mAdapter);
//        mAdapter.setOnItemClickListener(new PaginationAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, String obj, int position) {
//                pageNumber = Integer.parseInt(obj);
//                LoadWalletHistory();
//            }
//        });
//        if (paginationSize <= 1) {
//            rvPagination.setVisibility(View.GONE);
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void callWebService(String[] postUrl, Map<String, String> params) {
        WebService webService = new WebService(svContext, postUrl, params, this, true);
        webService.LoadData();
    }

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);
        if (url.contains(WebService.GETWALLETHISTORY[0])) {
            try {
                lstItems = new ArrayList<>();
                JSONObject json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    JSONArray data = json.getJSONArray("data");
                    for (int data_i = 0; data_i < ((JSONArray) data).length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
//                        String str_user_name = data_obj.getString("user");
                        String str_amount = data_obj.getString("affect_point");
                        String str_datetime = data_obj.getString("created");
                        String str_description = data_obj.getString("description");
                        String str_type = data_obj.getString("type");

                        String str_bp = data_obj.getString("before_point");
                        String str_ap = data_obj.getString("after_point");

                        lstItems.add(new WalletHistoryModel("str_user_name", str_amount, str_datetime, str_description, str_type, str_bp, str_ap));
                    }
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }

            cardShowBalance.setVisibility(View.GONE);

            txtWalletbal.setVisibility(View.VISIBLE);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            wallethistoryrv.setLayoutManager(layoutManager);
            wallethistoryrv.setHasFixedSize(true);
            int animation_type = ItemAnimation.LEFT_RIGHT;
            WalletHistoryAdapter mAdapter = new WalletHistoryAdapter(this, lstItems, animation_type);
            wallethistoryrv.setNestedScrollingEnabled(false);
            wallethistoryrv.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener((view, obj, position) -> {

            });
            layNestedScroll.scrollTo(0, 0);
            if (isFirstLoad) {
                isFirstLoad = false;
//                LoadPaginationView(strPageCount);
            }
        }
    }

    @Override
    public void onWebServiceError(String result, String url) {
        customToast.showToast(result, svContext);
    }

}