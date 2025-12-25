package com.onlinetradeview.tv.cmonulty.apicalling;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onlinetradeview.tv.cmonulty.CheckInternet;
import com.onlinetradeview.tv.cmonulty.GlobalData;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.PreferenceConnector;
import com.onlinetradeview.tv.cmonulty.customviews.CustomeProgressDialog;
import com.onlinetradeview.tv.cmonulty.errorscreen.NoInternetScreen;
import com.onlinetradeview.tv.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WebService {
    public static String LIVEDATAURL = "https://www.onlinetradelearn.com/mcx/authController/getLiveRate?userID=";
    public static String PRE_URL_DOMAIN = "https://www.onlinetradelearn.com/";
    public static String PRE_URL = PRE_URL_DOMAIN + "mcx/authController/";

    public static final String[] LOGINAUTH = {"loginAuth", "Loading Data..."};
    public static final String[] UPDATEFCM = {"userDetail", "Loading Data..."};

    public static final String[] GETMCXORDERLIST = {"getTradeList", "Loading Data..."};
    public static final String[] ORDERAUTH = {"orderAuth", "Loading Data..."};
    public static final String[] GETPORTFOLIO = {"getPortfolioList", "Loading Data..."};
    public static final String[] GETPORTFOLIOCLOSE = {"getClosePortfolioList", "Loading Data..."};
    public static final String[] CLOSECANCELTRADE = {"closeCancelTrade", "Loading Data..."};

    public static final String[] GETWALLETHISTORY = {"getWalletHistory", "Loading Data..."};
    public static final String[] CHANGEPWD = {"changePassword", "Loading Data..."};
    public static final String[] GETPROFILEDATA = {"getProfileData", "Loading Data..."};

    public static final String[] GETNOTIFICATION = {"getNotificationList", "Loading Data..."};

    private Context context;
    private String[] ApiUrl;
    private WebServiceListener listener;
    private CustomeProgressDialog customeProgressDialog;
    private Map<String, String> params;
    private boolean isShowText = false;
    private boolean isDialogShow;
    private CheckInternet checkNetwork;
    private NoInternetScreen errorScreen;

    public WebService(Context context, String[] postUrl, Map<String, String> params, WebServiceListener listener, View root, Activity act, boolean isDialogShow) {
        this.context = context;
        this.ApiUrl = postUrl;
        this.params = params;
        this.listener = listener;
        this.isDialogShow = isDialogShow;
        this.checkNetwork = new CheckInternet(context);
        this.errorScreen = new NoInternetScreen(context, root, act);
    }

    public WebService(Context context, String[] postUrl, Map<String, String> params, WebServiceListener listener, boolean isDialogShow) {
        this.context = context;
        this.ApiUrl = postUrl;
        this.params = params;
        this.listener = listener;
        this.isDialogShow = isDialogShow;
        this.checkNetwork = new CheckInternet(context);
    }

    private RequestQueue requestQueue;
    public void LoadData() {
        if (GlobalVariables.ISTESTING) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println(key + "=====>" + value);
            }
        }

        if (isDialogShow) {
            customeProgressDialog = new CustomeProgressDialog(context, R.layout.lay_customprogessdialog);
            TextView textView = (TextView) customeProgressDialog.findViewById(R.id.loader_showtext);
            if (isShowText) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(ApiUrl[1]);
            } else {
                textView.setVisibility(View.GONE);
            }
            customeProgressDialog.setCancelable(false);
            customeProgressDialog.show();
        }

        if (checkNetwork.isConnectingToInternet()) {
            if (errorScreen != null) {
                errorScreen.hideError();
            }
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(context);
            }

            StringRequest request = new StringRequest(Request.Method.POST, PRE_URL + ApiUrl[0], response -> {
                Log.d(ApiUrl[0] + ">>>", response);
                if (null != customeProgressDialog && customeProgressDialog.isShowing()) {
                    customeProgressDialog.dismiss();
                }
                listener.onWebServiceActionComplete(response, ApiUrl[0]);
            }, error -> {
                Log.d("error", error.toString());
                if (null != customeProgressDialog && customeProgressDialog.isShowing()) {
                    customeProgressDialog.dismiss();
                }
                listener.onWebServiceError(error.toString(), ApiUrl[0]);
            }) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    params.put("Token", PreferenceConnector.readString(context, PreferenceConnector.LOGINTOKEN, ""));
                    params.put("Deviceid", GlobalData.getDeviceId(context));
                    return params;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.getCache().clear();
            requestQueue.add(request);
        }else {
            if (null != customeProgressDialog && customeProgressDialog.isShowing()) {
                customeProgressDialog.dismiss();
            }
            listener.onWebServiceError("Internet not available", ApiUrl[0]);
            if (errorScreen != null) {
                errorScreen.showError();
            }
        }
    }

    private void CancelAllQueue(){
        requestQueue.cancelAll(request -> true);
    }

    public static String getStrFromObject(JSONObject obj, String name) throws JSONException {
        String strData = "";
        if (obj.has(name)) {
            strData = obj.getString(name);
        }

        if (strData.equals("") || strData.equals("null")) {
            strData = "";
        }
        return strData;
    }
}


