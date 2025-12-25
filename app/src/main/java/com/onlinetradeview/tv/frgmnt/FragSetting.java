package com.onlinetradeview.tv.frgmnt;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.act.ActMain;
import com.onlinetradeview.tv.cmonulty.PreferenceConnector;
import com.onlinetradeview.tv.cmonulty.GlobalData;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.apicalling.WebService;
import com.onlinetradeview.tv.cmonulty.customviews.ShowCustomToast;
import com.onlinetradeview.tv.cmonulty.apicalling.WebServiceListener;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragSetting extends Fragment implements View.OnClickListener, WebServiceListener {
    private View aiView = null;
    private boolean mAlreadyLoaded = false;

    private final int[] allViewWithClickId = {R.id.lay_addfund, R.id.lay_wallethistory, R.id.lay_profile, R.id.lay_changepwd, R.id.lay_notification,
            R.id.lay_sharerate, R.id.lay_about, R.id.lay_logout, R.id.lay_profile_two};

    private TextView marqueeText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (aiView == null) {
            aiView = inflater.inflate(R.layout.frag_setting, container, false);
        }
        return aiView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onCreate(savedInstanceState);
        StartApp();
        if (savedInstanceState == null && !mAlreadyLoaded) {
            mAlreadyLoaded = true;
            aiView = getView();
        }
        ((ActMain) svContext).defaultImage();
        OnClickCombineDeclare(allViewWithClickId);
        resumeApp();
    }

    public void resumeApp() {
        ((TextView) aiView.findViewById(R.id.username)).setText("Username: " + PreferenceConnector.readString(svContext, PreferenceConnector.USERNAME, "NA"));
        ((TextView) aiView.findViewById(R.id.userId)).setText("MemberId: " + PreferenceConnector.readString(svContext, PreferenceConnector.UNIQUEID, "NA"));

        marqueeText = aiView.findViewById(R.id.marqueeText);

        Map<String, String> MyData = new HashMap<String, String>();
        MyData.put("userID", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""));
        callWebServiceWithoutLoader(WebService.GETNOTIFICATION, MyData);
    }

    private void callWebServiceWithoutLoader(String[] postUrl, Map<String, String> params) {
        WebService webService = new WebService(svContext, postUrl, params, this, false);
        webService.LoadData();
    }

    private void OnClickCombineDeclare(int[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            aiView.findViewById(allViewWithClickId[j]).setOnClickListener(v -> {
                switch (v.getId()) {
                    case R.id.lay_addfund:
                        ActMain.onDrawerItemClick("Add Fund", svContext);
                        break;
                    case R.id.lay_wallethistory:
                        ActMain.onDrawerItemClick("Funds", svContext);
                        break;
                    case R.id.lay_profile:
                        ActMain.onDrawerItemClick("Profile", svContext);
                        break;
                    case R.id.lay_profile_two:;
                        ActMain.onDrawerItemClick("Profile Two", svContext);
                        break;
                    case R.id.lay_changepwd:
                        ActMain.onDrawerItemClick("Change password", svContext);
                        break;
                    case R.id.lay_notification:
                        ActMain.onDrawerItemClick("Notification", svContext);
                        break;
                    case R.id.lay_sharerate:
                        ActMain.onDrawerItemClick("Share & Rate", svContext);
                        break;
                    case R.id.lay_about:
                        ActMain.onDrawerItemClick("Privacy Policy", svContext);
                        break;
                    case R.id.lay_logout:
                        ActMain.onDrawerItemClick("Logout", svContext);
                        break;
                }
            });
        }
    }

    private Context svContext;
    private ShowCustomToast customToast;

    private void StartApp() {
        svContext = getActivity();
        customToast = new ShowCustomToast(svContext);
        ViewGroup root = (ViewGroup) aiView.findViewById(R.id.mylayout);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        GlobalData.SetLanguage(svContext);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    public static void hideFragmentkeyboard(Context meraContext, View meraView) {
        final InputMethodManager imm = (InputMethodManager) meraContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(meraView.getWindowToken(), 0);
    }

    private String strMarqueeMessage = "";
    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);
        if (url.contains(WebService.GETNOTIFICATION[0])) {
            try {
                JSONObject json = new JSONObject(result);

                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    JSONArray data = json.getJSONArray("data");
                    if (data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject data_obj = data.getJSONObject(i);
                            if (data_obj.has("message")) {
                                strMarqueeMessage += data_obj.getString("message") + "   ";
                            }
                        }
                        // Clear and set new marquee text
                        marqueeText.setText("");
                        marqueeText.setSelected(false);
                        marqueeText.setText(strMarqueeMessage);

                        // Ensure marquee functionality
                        marqueeText.setSelected(true);
                        marqueeText.setFocusable(true);
                        marqueeText.setFocusableInTouchMode(true);
                        new Handler().postDelayed(() -> {
                            marqueeText.setSelected(true);
                            marqueeText.requestFocus();
                        }, 100); // Adjust the delay as needed
                    }
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onWebServiceError(String result, String url) {
        customToast.showCustomToast(svContext, result, customToast.ToastyError);
    }
}