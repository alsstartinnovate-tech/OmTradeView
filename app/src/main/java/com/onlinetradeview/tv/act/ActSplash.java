package com.onlinetradeview.tv.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.app.AppCompatActivity;

import com.onesignal.OneSignal;
import com.onesignal.notifications.IPermissionObserver;
import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.cmonulty.CheckInternet;
import com.onlinetradeview.tv.cmonulty.GlobalData;
import com.onlinetradeview.tv.cmonulty.errorscreen.NoInternetScreen;
import com.onlinetradeview.tv.cmonulty.PreferenceConnector;
import com.onlinetradeview.tv.cmonulty.customviews.ShowCustomToast;
import com.onlinetradeview.tv.cmonulty.apicalling.WebService;
import com.onlinetradeview.tv.cmonulty.apicalling.WebServiceListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.onlinetradeview.tv.cmonulty.GlobalVariables.ISMOBILELOGINONLY;

public class ActSplash extends AppCompatActivity implements WebServiceListener, IPermissionObserver {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalData.Fullscreen(ActSplash.this);
        setContentView(R.layout.act_splash);
        StartApp();
        if (PreferenceConnector.readInteger(this, PreferenceConnector.THEMESELECTED, -1) == -1) {
            PreferenceConnector.writeInteger(this, PreferenceConnector.THEMESELECTED, 0);
        }
        PreferenceConnector.writeInteger(this, PreferenceConnector.THEMESELECTED, 0);
        resumeApp();
    }

    public void resumeApp() {
        PreferenceConnector.writeString(svContext, PreferenceConnector.FCMID, OneSignal.getUser().getPushSubscription().getId());
        if (checkNetwork.isConnectingToInternet()) {
            errrorScreen.hideError();
            if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.AUTOLOGIN, false)) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userID", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""));
                params.put("fcm_id", PreferenceConnector.readString(svContext, PreferenceConnector.FCMID, OneSignal.getUser().getPushSubscription().getId()));
                params.put("deviceName", GetDeviceName());
                callWebService(WebService.UPDATEFCM, params);
            } else {
                new Handler(Looper.getMainLooper()).postDelayed(this::ShowLoginScreen, 3000);
            }
        } else {
            errrorScreen.showInternetError();
        }
    }

    private String GetDeviceName() {
        return Build.MANUFACTURER + " " + Build.MODEL;
    }

    private void ShowLoginScreen() {
        Intent svIntent;
        if (ISMOBILELOGINONLY) {
            svIntent = new Intent(svContext, ActLogin.class);
        } else {
            svIntent = new Intent(svContext, ActLogin.class);
        }
        startActivity(svIntent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
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
        errrorScreen = new NoInternetScreen(svContext, root, ActSplash.this);
        hideKeyboard();
    }

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

    private void callWebService(String[] postUrl, Map<String, String> params) {
        WebService webService = new WebService(svContext, postUrl, params, this, true);
        webService.LoadData();
    }

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);
        if (url.contains(WebService.UPDATEFCM[0])) {
            if (ActSplash.LoginAttempt(result, svContext, customToast)) {
            } else {
                ShowLoginScreen();
            }
        }
    }

    public static final String TAG_MSG = "message";

    public static boolean LoginAttempt(String result, Context svContext, ShowCustomToast customToast) {
        try {
            JSONObject json = new JSONObject(result);
            String str_result = json.getString("status");
            if (str_result.equals("1")) {
                if (json.has("user_data")) {
                    JSONObject logindetail_obj = json.getJSONObject("user_data");
                    if (logindetail_obj.has("user_id")) {
                        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINUSERID, logindetail_obj.getString("user_id"));
                    }
                    PreferenceConnector.writeString(svContext, PreferenceConnector.USERNAME, logindetail_obj.getString("name"));
                    PreferenceConnector.writeString(svContext, PreferenceConnector.UNIQUEID, logindetail_obj.getString("unique_id"));
                    PreferenceConnector.writeString(svContext, PreferenceConnector.WALLETBALANCE, logindetail_obj.getString("wallet_balance"));
                    PreferenceConnector.writeString(svContext, PreferenceConnector.MARGINBALANCE, logindetail_obj.getString("margin_balance"));
                    PreferenceConnector.writeString(svContext, PreferenceConnector.M2MBALANCE, logindetail_obj.getString("m2m_balance"));
                    PreferenceConnector.writeString(svContext, PreferenceConnector.USERPHONE, logindetail_obj.getString("mobile"));
                    PreferenceConnector.writeString(svContext, PreferenceConnector.USERIMAGE, logindetail_obj.getString("profile_photo"));
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.AUTOLOGIN, true);

                    if (logindetail_obj.has("is_first_time_login")) {
                        PreferenceConnector.writeString(svContext, PreferenceConnector.ISPWDUPDATED, logindetail_obj.getString("is_first_time_login"));
                    }

                    if (logindetail_obj.has("token")) {
                        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINTOKEN, logindetail_obj.getString("token"));
                    }

                    if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.ISPASSCODESET, false)) {
                        Intent svIntent = new Intent(svContext, ActVerificationPasscode.class);
                        svContext.startActivity(svIntent);
                        ((Activity) svContext).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        ((Activity) svContext).finish();
                    } else {
                        Intent svIntent = new Intent(svContext, ActMain.class);
                        svContext.startActivity(svIntent);
                        ((Activity) svContext).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        ((Activity) svContext).finish();
                    }
                    return true;
                }
            } else {
                customToast.showCustomToast(svContext, json.getString(TAG_MSG), customToast.ToastyError);
                LogoutAttempt(svContext);
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean UpdateData(String result, Context svContext, ShowCustomToast customToast) {
        try {
            JSONObject json = new JSONObject(result);
            String str_result = json.getString("status");
            if (str_result.equals("1")) {
                if (json.has("user_data")) {
                    JSONObject logindetail_obj = json.getJSONObject("user_data");
                    if (logindetail_obj.has("user_id")) {
                        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINUSERID, logindetail_obj.getString("user_id"));
                    }
                    PreferenceConnector.writeString(svContext, PreferenceConnector.USERNAME, logindetail_obj.getString("name"));
                    PreferenceConnector.writeString(svContext, PreferenceConnector.UNIQUEID, logindetail_obj.getString("unique_id"));
                    PreferenceConnector.writeString(svContext, PreferenceConnector.WALLETBALANCE, logindetail_obj.getString("wallet_balance"));
                    PreferenceConnector.writeString(svContext, PreferenceConnector.MARGINBALANCE, logindetail_obj.getString("margin_balance"));
                    PreferenceConnector.writeString(svContext, PreferenceConnector.M2MBALANCE, logindetail_obj.getString("m2m_balance"));
                    PreferenceConnector.writeString(svContext, PreferenceConnector.USERPHONE, logindetail_obj.getString("mobile"));
                    PreferenceConnector.writeString(svContext, PreferenceConnector.USERIMAGE, logindetail_obj.getString("profile_photo"));

                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.AUTOLOGIN, true);

                    if (logindetail_obj.has("token")) {
                        PreferenceConnector.writeString(svContext, PreferenceConnector.LOGINTOKEN, logindetail_obj.getString("token"));
                    }
                    return true;
                }
            } else {
                customToast.showCustomToast(svContext, json.getString(TAG_MSG), customToast.ToastyError);
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void LogoutAttempt(Context svContext) {
        PreferenceConnector.cleanPrefrences(svContext);
    }

    @Override
    public void onWebServiceError(String result, String url) {
        customToast.showToast(result, svContext);
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    @Override
    public void onNotificationPermissionChange(boolean permission) {
        if (permission) {
            PreferenceConnector.writeString(svContext, PreferenceConnector.FCMID, OneSignal.getUser().getPushSubscription().getId());
        }
    }
}