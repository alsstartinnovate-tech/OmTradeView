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

import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.cmonulty.CheckInternet;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.PreferenceConnector;
import com.onlinetradeview.tv.cmonulty.apicalling.WebService;
import com.onlinetradeview.tv.cmonulty.apicalling.WebServiceListener;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;
import com.onlinetradeview.tv.cmonulty.customviews.ShowCustomToast;
import com.onlinetradeview.tv.cmonulty.errorscreen.NoInternetScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActViewProfile extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    public ActViewProfile() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_viewprofile);
        StartApp();
        resumeApp();
    }

    private void loadToolBar() {
        ImageView imgToolBarBack = (ImageView) findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(this);

        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText(getString(R.string.toolbar_editprofile));
    }

    public void resumeApp() {
        Map<String, String> MyData = new HashMap<String, String>();
        MyData.put("userID", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""));
        callWebService(WebService.GETPROFILEDATA, MyData);
    }

    private Context svContext;
    private ShowCustomToast customToast;
    private CheckInternet checkNetwork;
    private NoInternetScreen errrorScreen;
    private ViewGroup root;

    private void StartApp() {
        svContext = this;
        customToast = new ShowCustomToast(svContext);
        checkNetwork = new CheckInternet(svContext);
        root = (ViewGroup) findViewById(R.id.headlayout);
        errrorScreen = new NoInternetScreen(svContext, root, ActViewProfile.this);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        hideKeyboard();
        loadToolBar();
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

    public void onWebServiceActionComplete(String result) {
        try {
            JSONObject json = new JSONObject(result);
            String str_result = json.getString("result");
            String str_msg = json.getString("msg");
            if (str_result.equals("true")) {
                customToast.showCustomToast(svContext, str_msg, customToast.ToastySuccess);
            } else {
                customToast.showCustomToast(svContext, str_msg, customToast.ToastyError);
            }
        } catch (JSONException e) {
            customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }


    private void callWebService(String[] postUrl, Map<String, String> params) {
        WebService webService = new WebService(svContext, postUrl, params, this, true);
        webService.LoadData();
    }


    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);
        if (url.contains(WebService.GETPROFILEDATA[0])) {
            try {
                JSONObject json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.SweetAlertFailed);
                } else {
                    String nse_trading = json.getString("nse_trading");
                    String mcx_trading = json.getString("mcx_trading");
                    String nse_brokerage = json.getString("nse_brokerage");
                    String nse_intraday_margin = json.getString("nse_intraday_margin");
                    String nse_holding_margin = json.getString("nse_holding_margin");

                    String mcx_brokerage_type = json.getString("mcx_brokerage_type");
                    String mcx_brokerage;
                    if (mcx_brokerage_type.equalsIgnoreCase("Per Crore")) {
                        mcx_brokerage = json.getString("mcx_brokerage");
                    } else {
                        mcx_brokerage = json.getString("mcx_brokerage_per_lot");
                    }
                    JSONArray dataIntradayMargin = json.getJSONArray("mcx_intraday");
                    JSONObject jsoArrayIntraday = new JSONObject();
                    for (int data_i = 0; data_i < dataIntradayMargin.length(); data_i++) {
                        JSONObject data_obj = dataIntradayMargin.getJSONObject(data_i);
                        jsoArrayIntraday.put(data_obj.getString("title"), data_obj.getString("margin"));
                    }

                    JSONArray dataHoldingMargin = json.getJSONArray("mcx_holding");
                    JSONObject jsoArrayHolding = new JSONObject();
                    for (int data_i = 0; data_i < dataHoldingMargin.length(); data_i++) {
                        JSONObject data_obj = dataHoldingMargin.getJSONObject(data_i);
                        jsoArrayHolding.put(data_obj.getString("title"), data_obj.getString("margin"));
                    }

                    ((TextView) findViewById(R.id.txt_brokerage)).setText(nse_brokerage + " " + mcx_brokerage_type);
                    ((TextView) findViewById(R.id.txt_margin_intraday)).setText("Turnover / " + nse_intraday_margin);
                    ((TextView) findViewById(R.id.txt_margin_holding)).setText("Turnover / " + nse_holding_margin);

                    ((TextView) findViewById(R.id.txt_brokerage_two)).setText(mcx_brokerage + " " + mcx_brokerage_type);

                    ((TextView) findViewById(R.id.txt_margin_intraday_rule)).setText(jsoArrayIntraday.toString());
                    ((TextView) findViewById(R.id.txt_margin_holding_rule)).setText(jsoArrayHolding.toString());

                    String nseStatus = "Trading Disabled", mcxStatus = "Trading Disabled";
                    if (nse_trading.equalsIgnoreCase("1")) {
                        nseStatus = "Trading Enabled";
                    }
                    if (mcx_trading.equalsIgnoreCase("1")) {
                        mcxStatus = "Trading Enabled";
                    }
                    ((TextView) findViewById(R.id.nse_status)).setText(nseStatus);
                    ((TextView) findViewById(R.id.mcx_status)).setText(mcxStatus);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onWebServiceError(String result, String url) {
        customToast.showToast(result, svContext);
    }
}