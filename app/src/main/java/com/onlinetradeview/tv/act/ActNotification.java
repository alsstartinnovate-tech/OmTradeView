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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.adptr.NotificationAdapter;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.PreferenceConnector;
import com.onlinetradeview.tv.cmonulty.customviews.ShowCustomToast;
import com.onlinetradeview.tv.cmonulty.apicalling.WebService;
import com.onlinetradeview.tv.cmonulty.apicalling.WebServiceListener;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;
import com.onlinetradeview.tv.cmonulty.recyclerview.ItemAnimation;
import com.onlinetradeview.tv.mdel.NotificationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActNotification extends AppCompatActivity implements View.OnClickListener, WebServiceListener {
    private RecyclerView rvNotification;
    List<NotificationModel> lstNotifications = new ArrayList<NotificationModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_notification);
        StartApp();

        resumeApp();
    }

    public void resumeApp() {
        rvNotification = (RecyclerView) findViewById(R.id.rv_notification);
        Map<String, String> MyData = new HashMap<String, String>();
        MyData.put("userID", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""));
        callWebService(WebService.GETNOTIFICATION, MyData);
    }

    private Context svContext;
    private ShowCustomToast customToast;

    private void StartApp() {
        svContext = this;
        customToast = new ShowCustomToast(svContext);
        ViewGroup root = findViewById(R.id.headlayout);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }

        hideKeyboard();
        loadToolBar();
    }

    private void loadToolBar() {
        ImageView imgToolBarBack = findViewById(R.id.img_back);
        imgToolBarBack.setOnClickListener(this);

        TextView txtHeading = findViewById(R.id.heading);
        txtHeading.setText(getString(R.string.toolbar_notification));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_back) {
            onBackPressed();
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

    private void callWebService(String[] postUrl, Map<String, String> params) {
        WebService webService = new WebService(svContext, postUrl, params, this, true);
        webService.LoadData();
    }

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);
        if (url.contains(WebService.GETNOTIFICATION[0])) {
//            {"status":1,"message":"success","data":[{"type":"1","message":"Welcome to Online Trade Learn.","created":"12-Sep-2023 12:29:10"}]}
            try {
                lstNotifications = new ArrayList<>();
                JSONObject json = new JSONObject(result);

                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.ToastyError);
                } else {
                    JSONArray data = json.getJSONArray("data");
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        lstNotifications.add(new NotificationModel(data_obj.getString("type"),
//                                data_obj.getString("title"),
                                data_obj.getString("message"),
                                data_obj.getString("created")));
                    }

                    LoadData();
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }
    }

    private void LoadData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(svContext, RecyclerView.VERTICAL, false);
        NotificationAdapter mAdapter = new NotificationAdapter(svContext, lstNotifications, ItemAnimation.NONE);
        rvNotification.setLayoutManager(layoutManager);
        rvNotification.setAdapter(mAdapter);
    }

    @Override
    public void onWebServiceError(String result, String url) {
        customToast.showToast(result, svContext);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}