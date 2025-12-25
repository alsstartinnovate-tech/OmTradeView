package com.onlinetradeview.tv.frgmnt;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.adptr.McxAdapter;
import com.onlinetradeview.tv.mdel.McxModel;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.apicalling.WebService;
import com.onlinetradeview.tv.cmonulty.apicalling.WebServiceListener;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;
import com.onlinetradeview.tv.cmonulty.customviews.ShowCustomToast;
import com.onlinetradeview.tv.cmonulty.recyclerview.ItemAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragMcxFuture extends Fragment implements OnClickListener, WebServiceListener {
    private View aiView = null;
    private boolean mAlreadyLoaded = false;
    private RecyclerView rvlotterylist,rvtradenames;

    private View[] allViewWithClick = {};
    private int[] allViewWithClickId = {};

    private EditText edsearchandadd;
    private ImageButton search;

    private EditText[] edTexts = {};
    private String[] edTextsError = {};
    private int[] editTextsClickId = {};
    private List<McxModel> lsttrades = new ArrayList<>();
    public FragMcxFuture() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (aiView == null) {
            aiView = inflater.inflate(R.layout.frag_mcxfuture, container, false);
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
        resumeApp();
    }

    @Override
    public void onClick(View v) {

    }

    public void resumeApp() {
        edsearchandadd = (EditText) aiView.findViewById(R.id.ed_search);
        edsearchandadd.setVisibility(View.GONE);

        search = (ImageButton) aiView.findViewById(R.id.searchwatchlist);
        search.setOnClickListener(view -> {
            if (edsearchandadd.getVisibility() == View.VISIBLE) {
                edsearchandadd.setVisibility(View.GONE);
            } else {
                edsearchandadd.setVisibility(View.VISIBLE);
            }
        });

        rvlotterylist = (RecyclerView) aiView.findViewById(R.id.rv_mcxlist);
        rvtradenames = (RecyclerView) aiView.findViewById(R.id.rv_tradenames);
        for (int j = 0; j < 10; j++) {
            lsttrades.add(new McxModel("","","","","","","",
                    ""));
        }
        rvlotterylist = (RecyclerView) aiView.findViewById(R.id.rv_mcxlist);

        LinearLayoutManager layoutManager = new LinearLayoutManager(svContext, RecyclerView.VERTICAL,true);
        rvlotterylist.setLayoutManager(layoutManager);
        rvlotterylist.setHasFixedSize(true);

        int animation_type = ItemAnimation.NONE;
        McxAdapter mAdapter = new McxAdapter(svContext, lsttrades, animation_type);
        rvlotterylist.setAdapter(mAdapter);

    }

    private void EditTextDeclare(EditText[] editTexts) {
        for (int j = 0; j < editTexts.length; j++) {
            editTexts[j] = aiView.findViewById(editTextsClickId[j]);
        }
    }

    private void OnClickCombineDeclare(View[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            allViewWithClick[j] = aiView.findViewById(allViewWithClickId[j]);
            allViewWithClick[j].setOnClickListener(v -> {

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
            FontUtils.setThemeColor(root, svContext, font);
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
    }

    @Override
    public void onWebServiceError(String result, String url) {
        customToast.showToast(result, svContext);
    }
}