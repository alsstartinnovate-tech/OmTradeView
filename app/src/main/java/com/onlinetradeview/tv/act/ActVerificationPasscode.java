package com.onlinetradeview.tv.act;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.cmonulty.CheckInternet;
import com.onlinetradeview.tv.cmonulty.GlobalData;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.errorscreen.NoInternetScreen;
import com.onlinetradeview.tv.cmonulty.PreferenceConnector;
import com.onlinetradeview.tv.cmonulty.customviews.ShowCustomToast;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;

public class ActVerificationPasscode extends AppCompatActivity {
    private EditText et_otpOne, et_otptwo, et_otpthree, et_otpfour;
    private String passCode = "";
    private AppCompatButton btnContinue;
    private TextView txtSkipPasscode;
    private String tempPasscode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_passcode);

        StartApp();

        et_otpOne   = findViewById(R.id.et_otpone);
        et_otptwo   = findViewById(R.id.et_otptwo);
        et_otpthree = findViewById(R.id.et_otpthree);
        et_otpfour  = findViewById(R.id.et_otpfour);

        txtSkipPasscode = (TextView)findViewById(R.id.skipPascode);
        btnContinue = (AppCompatButton) findViewById(R.id.btn_continue);
        txtSkipPasscode.setVisibility(View.INVISIBLE);

        passCode = PreferenceConnector.readString(this, PreferenceConnector.PASSCODE, "1234");
//        passCodeEnable = PreferenceConnector.readBoolean(this, PreferenceConnector.PASSCODESET, false);

        if (PreferenceConnector.readString(this, PreferenceConnector.PASSCODE, "").equals("")){
            btnContinue.setText("Next");
        }else {

        }

        tempPasscode = "";
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputOtp = et_otpOne.getText().toString().trim() + et_otptwo.getText().toString().trim() + et_otpthree.getText().toString().trim() +
                        et_otpfour.getText().toString().trim();

                if ((btnContinue.getText().toString().trim()).equalsIgnoreCase("Next")) {
                    if (inputOtp.length() == 4) {
                        tempPasscode = inputOtp;

                        et_otpOne.setText("");
                        et_otptwo.setText("");
                        et_otpthree.setText("");
                        et_otpfour.setText("");

                        et_otpOne.requestFocus();

                        btnContinue.setText("Set Passcode");
                    }else {
                        customToast.showCustomToast(svContext, "Enter Passcode", customToast.ToastyError);
                    }
                }else if ((btnContinue.getText().toString().trim()).equalsIgnoreCase("Set Passcode")) {
                    if (inputOtp.length() == 4) {
                        if (tempPasscode.equalsIgnoreCase(inputOtp)) {
                            PreferenceConnector.writeString(ActVerificationPasscode.this, PreferenceConnector.PASSCODE, inputOtp);
//                            PreferenceConnector.writeBoolean(VerificationPasscode.this, PreferenceConnector.PASSCODESET, true);
                            btnContinue.setText("Verify");

                            Intent svIntent = new Intent(ActVerificationPasscode.this, ActMain.class);
                            startActivity(svIntent);
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            finish();
                        }else {
                            customToast.showCustomToast(svContext,"Passcode not matching", customToast.ToastyError);
                        }
                    }else {
                        customToast.showCustomToast(svContext, "Enter Passcode", customToast.ToastyError);
                    }
                }else if ((btnContinue.getText().toString().trim()).equalsIgnoreCase("Verify")) {
                    if (passCode.equals(inputOtp)) {
                        Intent svIntent = new Intent(ActVerificationPasscode.this, ActMain.class);
                        startActivity(svIntent);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        finish();
                    }else {
                        customToast.showCustomToast(svContext, "Enter Correct Passcode", customToast.ToastyError);
                    }
                }
            }
        });

        txtSkipPasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        et_otpOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null) {
                    if (editable.length() == 1) {
                        et_otptwo.requestFocus();
                    } else {

                    }
                }
            }
        });

        et_otptwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null) {
                    if (editable.length() == 1) {
                        et_otpthree.requestFocus();
                    } else {
                        et_otpOne.requestFocus();
                    }
                }
            }
        });

        et_otpthree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null) {
                    if (editable.length() == 1) {
                        et_otpfour.requestFocus();
                    } else {
                        et_otptwo.requestFocus();
                    }
                }

            }
        });

        et_otpfour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null) {
                    if (editable.length() == 1) {

                    } else {
                        et_otpthree.requestFocus();
                    }
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
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
        errrorScreen = new NoInternetScreen(svContext, root, ActVerificationPasscode.this);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        hideKeyboard();
        GlobalData.SetLanguage(svContext);
        if (checkNetwork.isConnectingToInternet()) {
            errrorScreen.hideError();
        } else {
            errrorScreen.showInternetError();
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

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }
}
