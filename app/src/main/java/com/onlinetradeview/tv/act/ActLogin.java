package com.onlinetradeview.tv.act;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.onesignal.OneSignal;
import com.onesignal.notifications.IPermissionObserver;
import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.cmonulty.CheckInternet;
import com.onlinetradeview.tv.cmonulty.CheckValidation;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.PreferenceConnector;
import com.onlinetradeview.tv.cmonulty.apicalling.WebService;
import com.onlinetradeview.tv.cmonulty.apicalling.WebServiceListener;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;
import com.onlinetradeview.tv.cmonulty.customviews.ShowCustomToast;
import com.onlinetradeview.tv.cmonulty.errorscreen.NoInternetScreen;

import java.util.HashMap;
import java.util.Map;

public class ActLogin extends AppCompatActivity implements WebServiceListener, IPermissionObserver {
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 200;
    private EditText edUsername, edPassword;
    private Button btnLogin;
    private ImageView imgFbLogin, imgGpLogin;
    private Button txtRegister;

    private EditText[] edTexts = {edUsername, edPassword};
    private int[] editTextsClickId = {R.id.ed_username, R.id.ed_password};

    private View[] allViewWithClick = {btnLogin};
    private int[] allViewWithClickId = {R.id.btn_login};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        StartApp();

        EditTextDeclare(edTexts);
        OnClickCombineDeclare(allViewWithClick);

        resumeApp();

    }

    public void resumeApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (!OneSignal.getNotifications().getPermission()) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.POST_NOTIFICATIONS},
                            REQUEST_CODE_POST_NOTIFICATIONS);
                }
//            }
        }

        PreferenceConnector.writeString(svContext, PreferenceConnector.FCMID, OneSignal.getUser().getPushSubscription().getId());
    }

    // This is the callback for the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }
    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(v -> {
                if (v.getId() == R.id.btn_login) {
                    LoginStart();
                }
            });
        }
    }

    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickId[j]);
        }
        edUsername = editTexts[0];
        edPassword = editTexts[1];
    }

    private void LoginStart() {
        int response = 0;
        response = CheckValidation.emptyEditTextError(
                edTexts,
                new String[]{"enter username", "enter password"});

        if (response == 0) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", edUsername.getText().toString().trim());
            params.put("password", edPassword.getText().toString().trim());
            params.put("fcm_id", PreferenceConnector.readString(svContext, PreferenceConnector.FCMID, OneSignal.getUser().getPushSubscription().getId()));
            callWebService(WebService.LOGINAUTH, params);
        }
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
        root = findViewById(R.id.headlayout);
        errrorScreen = new NoInternetScreen(svContext, root, ActLogin.this);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        hideKeyboard();
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

    private void callWebService(String[] postUrl, Map<String, String> params) {
        WebService webService = new WebService(svContext, postUrl, params, this, true);
        webService.LoadData();
    }

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);
        if (url.contains(WebService.LOGINAUTH[0])) {
            if (ActSplash.LoginAttempt(result, svContext, customToast)) {
                Intent svIntent = new Intent(svContext, ActMain.class);
                svContext.startActivity(svIntent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
            }
        }
    }

    @Override
    public void onWebServiceError(String result, String url) {
        customToast.showToast(result, svContext);
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