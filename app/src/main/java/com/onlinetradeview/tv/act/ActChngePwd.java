package com.onlinetradeview.tv.act;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.cmonulty.PreferenceConnector;
import com.onlinetradeview.tv.cmonulty.apicalling.WebService;
import com.onlinetradeview.tv.cmonulty.apicalling.WebServiceListener;
import com.onlinetradeview.tv.cmonulty.CheckInternet;
import com.onlinetradeview.tv.cmonulty.CheckValidation;
import com.onlinetradeview.tv.cmonulty.GlobalData;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.errorscreen.NoInternetScreen;
import com.onlinetradeview.tv.cmonulty.customviews.ShowCustomToast;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActChngePwd extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private EditText edExistingPasword, edNewPassword, edConfirmPasword;
    private Button btnCPUpdate;

    private View[] allViewWithClick = {btnCPUpdate};
    private int[] allViewWithClickId = {R.id.btn_update};

    private EditText[] edTexts = {edExistingPasword, edNewPassword, edConfirmPasword};
    private String[] edTextsError = {"Enter existing password", "Enter new password", "Enter confirm password"};
    private int[] editTextsClickId = {R.id.edt_oldpass, R.id.edt_newpass, R.id.edt_newpass_again};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_changepassword);
        StartApp();

        OnClickCombineDeclare(allViewWithClick);
        EditTextDeclare(edTexts);
    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn_update:
                            UpdatePwd();
                            break;
                    }
                }
            });
        }
    }

    private void UpdatePwd() {
        int response = 0;
        response = CheckValidation.emptyEditTextError(edTexts, edTextsError);

        if (!(edNewPassword.getText().toString().trim()).equals(edConfirmPasword.getText().toString().trim())) {
            response++;
            customToast.showCustomToast(svContext, "Password not matching", customToast.ToastyError);
        }

        if (response == 0) {
            Map<String, String> MyData = new HashMap<String, String>();
            MyData.put("userID", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""));
            MyData.put("opw", edExistingPasword.getText().toString().trim());
            MyData.put("npw", edNewPassword.getText().toString().trim());
            callWebService(WebService.CHANGEPWD, MyData);
        }
    }

    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = findViewById(editTextsClickId[j]);
        }
        edExistingPasword = (EditText) editTexts[0];
        edNewPassword = (EditText) editTexts[1];
        edConfirmPasword = (EditText) editTexts[2];
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
        errrorScreen = new NoInternetScreen(svContext, root, ActChngePwd.this);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        hideKeyboard();
        GlobalData.SetLanguage(svContext);

        new SetActionTopBar(root, ActChngePwd.this, "Change password");
        loadToolBar();

    }

    private void loadToolBar() {
        ImageView imgToolBarBack = (ImageView) findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(this);

        TextView txtHeading = (TextView) findViewById(R.id.heading);
        txtHeading.setText("Change Password");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(PreferenceConnector.readString(svContext, PreferenceConnector.ISPWDUPDATED, "0").equalsIgnoreCase("1")) {
           customToast.showCustomToast(svContext, "Please change password for first time login", customToast.SweetAlertFailed);
        }else{
            hideKeyboard();
            finish();
        }
    }


    private void callWebService(String[] postUrl, Map<String, String> params) {
        WebService webService = new WebService(svContext, postUrl, params, this, true);
        webService.LoadData();
    }

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);
        if (url.contains(WebService.CHANGEPWD[0])) {
            try {
                JSONObject json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.SweetAlertFailed);
                } else {
                    customToast.showCustomToast(svContext, str_message, customToast.SweetAlertSuccess);
                    PreferenceConnector.writeString(svContext, PreferenceConnector.ISPWDUPDATED, "0");
                    onBackPressed();
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