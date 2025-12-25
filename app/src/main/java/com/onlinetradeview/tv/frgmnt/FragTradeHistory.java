package com.onlinetradeview.tv.frgmnt;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.onlinetradeview.tv.adptr.TradeAdapterClose;
import com.onlinetradeview.tv.act.ActMain;
import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.adptr.TradeAdapter;
import com.onlinetradeview.tv.cmonulty.FragmentTAG;
import com.onlinetradeview.tv.mdel.TradeModel;
import com.onlinetradeview.tv.cmonulty.CheckInternet;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.PreferenceConnector;
import com.onlinetradeview.tv.cmonulty.apicalling.WebService;
import com.onlinetradeview.tv.cmonulty.apicalling.WebServiceListener;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;
import com.onlinetradeview.tv.cmonulty.customviews.ShowCustomToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragTradeHistory extends Fragment implements OnClickListener, WebServiceListener {
    private View aiView = null;
    private boolean mAlreadyLoaded = false;
    private RecyclerView rvtrade;
    private List<TradeModel> lstMcxtrades = new ArrayList<>();
    private NestedScrollView scrollMain;
    private TextView txtComingSoon;
    private View viewTabPending, viewTabActive, viewTabClose;
    private final int[] allViewWithClickId = {R.id.lay_tabmcxfuture, R.id.lay_tabnsefuture, R.id.lay_tabother, R.id.cancelall_mcx, R.id.cancelall_nse, R.id.cancelall_mcxpending, R.id.cancelall_nsepending};
    private SwipeRefreshLayout layrefrsh;
    private LinearLayout layCancelOrder, layCancelPendingOrder;

    public FragTradeHistory() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (aiView == null) {
            aiView = inflater.inflate(R.layout.frag_markettrade, container, false);
        }
        return aiView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null && !mAlreadyLoaded) {
            mAlreadyLoaded = true;
        }
        StartApp();
        OnClickCombineDeclare(allViewWithClickId);
        resumeApp();
        ((ActMain) svContext).defaultImage();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:

                break;
            default:
                break;
        }
    }

    public void resumeApp() {
        rvtrade = (RecyclerView) aiView.findViewById(R.id.rv_markettrade);
        viewTabPending = aiView.findViewById(R.id.indicator_mcxfuture);
        viewTabActive = aiView.findViewById(R.id.indicator_nsefutures);
        viewTabClose = aiView.findViewById(R.id.indicator_other);
        txtComingSoon = aiView.findViewById(R.id.txt_showerror);
        scrollMain = aiView.findViewById(R.id.nestedscrollview);
        layCancelOrder = aiView.findViewById(R.id.lay_cancelorder);
        layCancelPendingOrder = aiView.findViewById(R.id.lay_cancelpendingorder);

        layCancelOrder.setVisibility(View.GONE);
        layCancelPendingOrder.setVisibility(View.VISIBLE);

        layrefrsh = aiView.findViewById(R.id.layrefrsh);
        layrefrsh.setOnRefreshListener(() -> {
            layrefrsh.setRefreshing(false);
            LoadData(lstType);
        });
        layrefrsh.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

        lstType = "1";
        LoadData(lstType);
    }

    private static String lstType = "1";
    private void LoadData(String type) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userID", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""));
        params.put("status", type);
        callWebService(WebService.GETMCXORDERLIST, params);
    }

    private void LoadDataWithoutLoader(String type) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userID", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""));
        params.put("status", type);
        callWebServiceWithoutLoader(WebService.GETMCXORDERLIST, params);
    }

    private void ShowError(String txtValue) {
        rvtrade.setVisibility(View.GONE);
        txtComingSoon.setText(txtValue);
        txtComingSoon.setVisibility(View.VISIBLE);
    }

    private void HideError(String txtValue) {
        rvtrade.setVisibility(View.VISIBLE);
        txtComingSoon.setText(txtValue);
        txtComingSoon.setVisibility(View.GONE);
    }

    public static final String TAG_DATA = "data";

    @Override
    public void onWebServiceActionComplete(String result, String url) {
        System.out.println(result + ".........jsonresponse....." + url);
        if (url.contains(WebService.GETMCXORDERLIST[0])) {
            lstMcxtrades = new ArrayList<>();
            try {
                JSONObject json = new JSONObject(result);
                String str_result = json.getString("status");
                String str_msg = json.getString("message");
                if (str_result.equals("1")) {
                    JSONArray data = json.getJSONArray(TAG_DATA);
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);
                        TradeModel tradeModel = new TradeModel();
                        if (data_obj.has("orderID")) {
                            tradeModel.setId(data_obj.optString("orderID", ""));
                        }
                        tradeModel.setName(data_obj.optString("category_name", ""));
                        tradeModel.setSoldtrader(svContext.getResources().getString(R.string.app_name));
                        tradeModel.setSoldnumber(data_obj.optString("lot", ""));
                        tradeModel.setDatesoldmarket(data_obj.optString("orderDateTime", ""));
                        tradeModel.setPricebought(data_obj.optString("bid_price", ""));
                        tradeModel.setQtybought(data_obj.optString("lot", ""));
//                        if (data_obj.has("by_user")) {
                            tradeModel.setBoughttrader(data_obj.optString("by_user", "Trader"));
//                        }
                        tradeModel.setDateboughtmarket(data_obj.optString("orderDateTime", ""));
                        tradeModel.setStr_type(data_obj.optString("bid_type", ""));

                        tradeModel.setHoldingMarginReq(data_obj.optString("holdingMargin", ""));
                        tradeModel.setUsedMargin(data_obj.optString("intradayMargin", ""));
                        if (data_obj.has("expire_date")) {
                            tradeModel.setStr_date_expiry(data_obj.optString("expire_date", ""));
                        }
                        tradeModel.setProfitloss(data_obj.optString("total_profit", ""));
                        tradeModel.setToken(data_obj.optString("token", ""));
                        tradeModel.setDatesold(data_obj.optString("orderUpdateDateTime", ""));
                        tradeModel.setSoldPrice(data_obj.optString("sell_price", ""));
                        tradeModel.setStr_order_status(data_obj.optString("status", ""));
                        tradeModel.setBrokerage(data_obj.optString("brokerage", ""));
                        tradeModel.setBoughtradernumber("");

                        if (lstType.equalsIgnoreCase("3")/* && str_type.equals("2")*/) {
                            tradeModel.setOrdertype_bought(data_obj.getString("order_type"));
                            tradeModel.setStr_order_type(data_obj.getString("close_order_type"));
                            tradeModel.setDatesold(data_obj.getString("orderDateTime"));
                            tradeModel.setDatebought(data_obj.getString("orderUpdateDateTime"));

                            LoadDataRv(tradeModel, data_obj.optString("bid_type", ""),
                                    data_obj.getString("close_order_type"),
                                    data_obj.getString("order_type"),
                                    data_obj.optString("total_profit", ""));
                        } else {
                            tradeModel.setOrdertype_bought(data_obj.getString("close_order_type"));
                            tradeModel.setStr_order_type(data_obj.getString("order_type"));
                            tradeModel.setDatesold(data_obj.getString("orderUpdateDateTime"));
                            tradeModel.setDatebought(data_obj.getString("orderDateTime"));

                            LoadDataRv(tradeModel, data_obj.optString("bid_type", ""),
                                    data_obj.getString("order_type"),
                                    data_obj.getString("close_order_type"),
                                    data_obj.optString("total_profit", ""));
                        }
                        tradeModel.setStr_catId(data_obj.optString("category_id", ""));
                        lstMcxtrades.add(tradeModel);
                    }

                    LoadRv();
                } else {
                    ShowError((svContext.getResources().getText(R.string.label_nodata)).toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (url.contains(WebService.CLOSECANCELTRADE[0])) {
            try {
                JSONObject json = new JSONObject(result);
                String str_message = json.getString("message");
                String str_status = json.getString("status");
                if (str_status.equalsIgnoreCase("0")) {
                    customToast.showCustomToast(svContext, str_message, customToast.SweetAlertFailed);
                } else {
                    customToast.showCustomToast(svContext, str_message, customToast.SweetAlertSuccess);
                    LoadDataWithoutLoader(lstType);
                }
            } catch (JSONException e) {
                customToast.showCustomToast(svContext, "Some error occured", customToast.ToastyError);
                e.printStackTrace();
            }
        }
    }

    private void LoadDataRv(TradeModel tradeModelRv, String strType, String strOrderType, String strOrderTypeBought, String totalProfit) {
        if (strType.equalsIgnoreCase("1")) {
            tradeModelRv.setSoldTypeLotBg(getResources().getDrawable(R.drawable.back_round_green));
            tradeModelRv.setSoldTypeLotTc(getResources().getColor(R.color.green_400));
            tradeModelRv.setPriceBoughtBg(getResources().getDrawable(R.drawable.back_round_green));
            tradeModelRv.setPriceBoughtTc(getResources().getColor(R.color.green_400));
            if (strOrderTypeBought.equalsIgnoreCase("1")) {
                tradeModelRv.setOrderTypeBoughtBg(getResources().getDrawable(R.drawable.back_round_green));
                tradeModelRv.setOrderTypeBoughtTc(getResources().getColor(R.color.green_400));
                tradeModelRv.setOrderTypeBoughtText("Market");
            } else if (strOrderTypeBought.equalsIgnoreCase("2")) {
                tradeModelRv.setOrderTypeBoughtBg(getResources().getDrawable(R.drawable.back_round_red));
                tradeModelRv.setOrderTypeBoughtTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeBoughtText("Order");
            } else if (strOrderTypeBought.equalsIgnoreCase("3")) {
                tradeModelRv.setOrderTypeBoughtBg(getResources().getDrawable(R.drawable.back_round_red));
                tradeModelRv.setOrderTypeBoughtTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeBoughtText("SL");
            } else if (strOrderTypeBought.equalsIgnoreCase("4")) {
                tradeModelRv.setOrderTypeBoughtBg(getResources().getDrawable(R.drawable.back_round_red));
                tradeModelRv.setOrderTypeBoughtTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeBoughtText("Admin");
            } else if (strOrderTypeBought.equalsIgnoreCase("5")) {
                tradeModelRv.setOrderTypeBoughtBg(getResources().getDrawable(R.drawable.back_round_red));
                tradeModelRv.setOrderTypeBoughtTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeBoughtText("Carried Forward");
            } else if (strOrderTypeBought.equalsIgnoreCase("6")) {
                tradeModelRv.setOrderTypeBoughtBg(getResources().getDrawable(R.drawable.back_round_red));
                tradeModelRv.setOrderTypeBoughtTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeBoughtText("I-Fund");
            } else if (strOrderTypeBought.equalsIgnoreCase("7")) {
                tradeModelRv.setOrderTypeBoughtBg(getResources().getDrawable(R.drawable.back_round_red));
                tradeModelRv.setOrderTypeBoughtTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeBoughtText("Month Settlement");
            }
            if (strOrderType.equalsIgnoreCase("1")) {
                tradeModelRv.setOrderTypeSoldBg(getResources().getDrawable(R.drawable.back_round_green));
                tradeModelRv.setOrderTypeSoldTc(getResources().getColor(R.color.green_400));
                tradeModelRv.setOrderTypeSoldText("Market");
            } else if (strOrderType.equalsIgnoreCase("2")) {
                tradeModelRv.setOrderTypeSoldBg(getResources().getDrawable(R.drawable.back_round_red));
                tradeModelRv.setOrderTypeSoldTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeSoldText("Order");
            } else if (strOrderType.equalsIgnoreCase("3")) {
                tradeModelRv.setOrderTypeSoldBg(getResources().getDrawable(R.drawable.back_round_red));
                tradeModelRv.setOrderTypeSoldTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeSoldText("SL");
            } else if (strOrderType.equalsIgnoreCase("4")) {
                tradeModelRv.setOrderTypeSoldBg(getResources().getDrawable(R.drawable.back_round_red));
                tradeModelRv.setOrderTypeSoldTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeSoldText("Admin");
            } else if (strOrderType.equalsIgnoreCase("5")) {
                tradeModelRv.setOrderTypeSoldBg(getResources().getDrawable(R.drawable.back_round_red));
                tradeModelRv.setOrderTypeSoldTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeSoldText("Carried Forward");
            } else if (strOrderType.equalsIgnoreCase("6")) {
                tradeModelRv.setOrderTypeSoldBg(getResources().getDrawable(R.drawable.back_round_red));
                tradeModelRv.setOrderTypeSoldTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeSoldText("I-Fund");
            } else if (strOrderType.equalsIgnoreCase("7")) {
                tradeModelRv.setOrderTypeSoldBg(getResources().getDrawable(R.drawable.back_round_red));
                tradeModelRv.setOrderTypeSoldTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeSoldText("Month Settlement");
            }
        } else {
            tradeModelRv.setSoldTypeLotBg(getResources().getDrawable(R.drawable.back_round_red));
            tradeModelRv.setSoldTypeLotTc(getResources().getColor(R.color.red_900));
            tradeModelRv.setPriceBoughtBg(getResources().getDrawable(R.drawable.back_round_red));
            tradeModelRv.setPriceBoughtTc(getResources().getColor(R.color.red_900));

            if (strOrderType.equalsIgnoreCase("1")) {
                tradeModelRv.setOrderTypeBoughtBg(getResources().getDrawable(R.drawable.back_round_green));
                tradeModelRv.setOrderTypeBoughtTc(getResources().getColor(R.color.green_400));
                tradeModelRv.setOrderTypeBoughtText("Market");
            } else if (strOrderType.equalsIgnoreCase("2")) {
                tradeModelRv.setOrderTypeBoughtBg(getResources().getDrawable(R.drawable.back_round_red));
                tradeModelRv.setOrderTypeBoughtTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeBoughtText("Order");
            } else if (strOrderType.equalsIgnoreCase("3")) {
                tradeModelRv.setOrderTypeBoughtBg(getResources().getDrawable(R.drawable.back_round_red));
                tradeModelRv.setOrderTypeBoughtTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeBoughtText("SL");
            } else if (strOrderType.equalsIgnoreCase("4")) {
                tradeModelRv.setOrderTypeBoughtBg(getResources().getDrawable(R.drawable.back_round_red));
                tradeModelRv.setOrderTypeBoughtTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeBoughtText("Admin");
            } else if (strOrderType.equalsIgnoreCase("5")) {
                tradeModelRv.setOrderTypeBoughtBg(getResources().getDrawable(R.drawable.back_round_red));
                tradeModelRv.setOrderTypeBoughtTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeBoughtText("Carried Forward");
            } else if (strOrderType.equalsIgnoreCase("6")) {
                tradeModelRv.setOrderTypeBoughtBg(getResources().getDrawable(R.drawable.back_round_red));
                tradeModelRv.setOrderTypeBoughtTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeBoughtText("I-Fund");
            } else if (strOrderType.equalsIgnoreCase("7")) {
                tradeModelRv.setOrderTypeBoughtBg(getResources().getDrawable(R.drawable.back_round_red));
                tradeModelRv.setOrderTypeBoughtTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeBoughtText("Month Settlement");
            }
            if (strOrderTypeBought.equalsIgnoreCase("1")) {
                tradeModelRv.setOrderTypeSoldBg(getResources().getDrawable(R.drawable.back_round_green));
                tradeModelRv.setOrderTypeSoldTc(getResources().getColor(R.color.green_400));
                tradeModelRv.setOrderTypeSoldText("Market");
            } else if (strOrderTypeBought.equalsIgnoreCase("2")) {
                tradeModelRv.setOrderTypeSoldBg(getResources().getDrawable(R.drawable.back_round_green));
                tradeModelRv.setOrderTypeSoldTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeSoldText("Order");
            } else if (strOrderTypeBought.equalsIgnoreCase("3")) {
                tradeModelRv.setOrderTypeSoldBg(getResources().getDrawable(R.drawable.back_round_green));
                tradeModelRv.setOrderTypeSoldTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeSoldText("SL");
            } else if (strOrderTypeBought.equalsIgnoreCase("4")) {
                tradeModelRv.setOrderTypeSoldBg(getResources().getDrawable(R.drawable.back_round_green));
                tradeModelRv.setOrderTypeSoldTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeSoldText("Admin");
            } else if (strOrderTypeBought.equalsIgnoreCase("5")) {
                tradeModelRv.setOrderTypeSoldBg(getResources().getDrawable(R.drawable.back_round_green));
                tradeModelRv.setOrderTypeSoldTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeSoldText("Carried Forward");
            } else if (strOrderTypeBought.equalsIgnoreCase("6")) {
                tradeModelRv.setOrderTypeSoldBg(getResources().getDrawable(R.drawable.back_round_green));
                tradeModelRv.setOrderTypeSoldTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeSoldText("I-Fund");
            } else if (strOrderTypeBought.equalsIgnoreCase("7")) {
                tradeModelRv.setOrderTypeSoldBg(getResources().getDrawable(R.drawable.back_round_green));
                tradeModelRv.setOrderTypeSoldTc(getResources().getColor(R.color.red_900));
                tradeModelRv.setOrderTypeSoldText("Month Settlement");
            }
        }
        if (lstType.equalsIgnoreCase("3")) {
            double changePrice = Double.parseDouble(totalProfit);
            if (changePrice > 0) {
                tradeModelRv.setBgProfitLossMrktQty(getResources().getDrawable(R.drawable.back_round_green));
                tradeModelRv.setTcProfitLossMrktQty(getResources().getColor(R.color.green_400));
            } else {
                tradeModelRv.setBgProfitLossMrktQty(getResources().getDrawable(R.drawable.back_round_red));
                tradeModelRv.setTcProfitLossMrktQty(getResources().getColor(R.color.red_900));
            }
        }
    }


    private void LoadRv() {
        if (lstType.equalsIgnoreCase("1") || lstType.equalsIgnoreCase("2")) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(svContext, RecyclerView.VERTICAL, false);
            TradeAdapter mAdapter = new TradeAdapter(svContext, lstMcxtrades, lstType);
            rvtrade.setLayoutManager(layoutManager);
            rvtrade.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(new TradeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, String obj, int position) {
                    FragCreateOrder.createOrderTradeModel = lstMcxtrades.get(position);
                    FragCreateOrder.typeBack = 2;
                    switchContent(new FragCreateOrder(), FragmentTAG.FragCreateOrder);
                }

                @Override
                public void onButtonItemClick(View view, String obj, int position) {
                    if (obj.equalsIgnoreCase("Cancel Order")) {
                        ConfirmCancelApplication("1", position, svContext, "Cancel Order", "Are you sure you want to cancel order?", "Cancel Order");
                    } else if (obj.equalsIgnoreCase("Close Order")) {
                        ConfirmCancelApplication("2", position, svContext, "Close Order", "Are you sure you want to close order?", "Close Order");
                    } else if (obj.equalsIgnoreCase("Cancelled")) {
                        customToast.showCustomToast(svContext, "Order already Cancelled", customToast.SweetAlertFailed);
                    } else {
                        customToast.showCustomToast(svContext, obj + "", customToast.ToastySuccess);
                    }
                }
            });
        } else if (lstType.equalsIgnoreCase("3")) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(svContext, RecyclerView.VERTICAL, false);
            TradeAdapterClose mAdapter = new TradeAdapterClose(svContext, lstMcxtrades);
            rvtrade.setLayoutManager(layoutManager);
            rvtrade.setAdapter(mAdapter);
        }

        rvtrade.setVisibility(View.VISIBLE);
        if (lstMcxtrades.isEmpty()) {
            ShowError((getActivity().getResources().getText(R.string.label_nodata)).toString());
            if (lstType.equalsIgnoreCase("1")) {
                layCancelOrder.setVisibility(View.GONE);
                layCancelPendingOrder.setVisibility(View.GONE);
            } else {
                layCancelOrder.setVisibility(View.GONE);
                layCancelPendingOrder.setVisibility(View.GONE);
            }
        } else {
            HideError((getResources().getText(R.string.label_nodata)).toString());
        }
        scrollMain.fullScroll(NestedScrollView.FOCUS_UP);
    }


    public void ConfirmCancelApplication(String strType, int position, Context svContext, String head, String strTitle, String strDesc) {
        final Dialog dialog = new Dialog(svContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_header_twobutton);

        TextView textTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        textTitle.setText(strTitle);
        TextView textDesc = (TextView) dialog.findViewById(R.id.dialog_desc);
        textDesc.setText(strDesc);
        textDesc.setVisibility(View.GONE);
        TextView textHead = (TextView) dialog.findViewById(R.id.dialog_head);
        textHead.setText(head);

        Button declineDialogButton = (Button) dialog.findViewById(R.id.bt_decline);
        declineDialogButton.setOnClickListener(v -> dialog.dismiss());

        Button confirmDialogButton = (Button) dialog.findViewById(R.id.bt_confirm);
        confirmDialogButton.setOnClickListener(v -> {
            dialog.dismiss();
            Map<String, String> params;
                if (strType.equalsIgnoreCase("1")) {
                    params = new HashMap<String, String>();
                    params.put("userID", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""));
                    params.put("orderID", lstMcxtrades.get(position).getId());
                    params.put("type", "1");
                    callWebService(WebService.CLOSECANCELTRADE, params);
                } else if (strType.equalsIgnoreCase("2")) {
                    params = new HashMap<String, String>();
                    params.put("userID", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""));
                    params.put("orderID", lstMcxtrades.get(position).getId());
                    params.put("type", "2");
                    callWebService(WebService.CLOSECANCELTRADE, params);
                }
        });
        dialog.show();
    }

    public void switchContent(Fragment fragment, String fragTag) {
        hideFragmentkeyboard(svContext, getView());
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(fragTag)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lstType.equalsIgnoreCase("1")) {
            layCancelOrder.setVisibility(View.GONE);
            layCancelPendingOrder.setVisibility(View.VISIBLE);
            lstType = "1";
            LoadRv(lstType);
            LoadData(lstType);
        } else if (lstType.equalsIgnoreCase("2")) {
            layCancelOrder.setVisibility(View.VISIBLE);
            layCancelPendingOrder.setVisibility(View.GONE);
            lstType = "2";
            LoadRv(lstType);
            LoadData(lstType);
        } else {
            layCancelOrder.setVisibility(View.GONE);
            layCancelPendingOrder.setVisibility(View.GONE);
            lstType = "3";
            LoadRv(lstType);
            LoadData(lstType);
        }
    }

    public static void hideFragmentkeyboard(Context meraContext, View meraView) {
        final InputMethodManager imm = (InputMethodManager) meraContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(meraView.getWindowToken(), 0);
    }

    private void LoadRv(String type) {
        rvtrade.setVisibility(View.GONE);
        if (type.equalsIgnoreCase("1")) {
            viewTabPending.setVisibility(View.VISIBLE);
            viewTabActive.setVisibility(View.INVISIBLE);
            viewTabClose.setVisibility(View.INVISIBLE);
        } else if (type.equalsIgnoreCase("2")) {
            viewTabPending.setVisibility(View.INVISIBLE);
            viewTabActive.setVisibility(View.VISIBLE);
            viewTabClose.setVisibility(View.INVISIBLE);
        } else {
            viewTabPending.setVisibility(View.INVISIBLE);
            viewTabActive.setVisibility(View.INVISIBLE);
            viewTabClose.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onWebServiceError(String result, String url) {
        customToast.showToast(result, svContext);
    }

    private Context svContext;
    private ShowCustomToast customToast;
    private CheckInternet checkNetwork;
    private ViewGroup root;

    private void StartApp() {
        svContext = getActivity();
        customToast = new ShowCustomToast(svContext);
        checkNetwork = new CheckInternet(svContext);
        root = (ViewGroup) aiView.findViewById(R.id.mylayout);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setThemeColor(root, svContext, font);
        }
    }

    private void callWebService(String[] postUrl, Map<String, String> params) {
        WebService webService = new WebService(svContext, postUrl, params, this, true);
        webService.LoadData();
    }

    private void callWebServiceWithoutLoader(String[] postUrl, Map<String, String> params) {
        WebService webService = new WebService(svContext, postUrl, params, this, false);
        webService.LoadData();
    }

    private void OnClickCombineDeclare(int[] allViewWithClick) {
        for (int j = 0; j < allViewWithClick.length; j++) {
            (aiView.findViewById(allViewWithClickId[j])).setOnClickListener(v -> {

                switch (v.getId()) {
                    case R.id.lay_tabmcxfuture:
                        layCancelOrder.setVisibility(View.GONE);
                        layCancelPendingOrder.setVisibility(View.VISIBLE);
                        lstType = "1";
                        LoadRv(lstType);
                        LoadData(lstType);
                        break;
                    case R.id.lay_tabnsefuture:
                        layCancelOrder.setVisibility(View.VISIBLE);
                        layCancelPendingOrder.setVisibility(View.GONE);
                        lstType = "2";
                        LoadRv(lstType);
                        LoadData(lstType);
//                      HideError((svContext.getResources().getText(R.string.label_nodata)).toString());
                        break;
                    case R.id.lay_tabother:
                        layCancelOrder.setVisibility(View.GONE);
                        layCancelPendingOrder.setVisibility(View.GONE);
                        lstType = "3";
                        LoadRv(lstType);
                        LoadData(lstType);
                        break;
                    case R.id.cancelall_mcx:
                        CloseApplication(true, svContext, "Cancel All", "Are you sure you want to close all active MCX orders?", "Cancel All");
                        break;
                    case R.id.cancelall_nse:
                        CloseApplication(false, svContext, "Cancel All", "Are you sure you want to close all active NSE orders?", "Cancel All");
                        break;

                    case R.id.cancelall_mcxpending:
                        CancelApplication(true, svContext, "Cancel All", "Are you sure you want to cancel all pending MCX orders?", "Cancel All");
                        break;
                    case R.id.cancelall_nsepending:
                        CancelApplication(false, svContext, "Cancel All", "Are you sure you want to cancel all pending NSE orders?", "Cancel All");
                        break;
                }
            });
        }
    }

    public void CancelApplication(boolean isMcx, Context svContext, String head, String strTitle, String strDesc) {
        final Dialog dialog = new Dialog(svContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_header_twobutton);

        TextView textTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        textTitle.setText(strTitle);
        TextView textDesc = (TextView) dialog.findViewById(R.id.dialog_desc);
        textDesc.setText(strDesc);
        textDesc.setVisibility(View.GONE);
        TextView textHead = (TextView) dialog.findViewById(R.id.dialog_head);
        textHead.setText(head);

        Button declineDialogButton = (Button) dialog.findViewById(R.id.bt_decline);
        declineDialogButton.setOnClickListener(v -> dialog.dismiss());

        Button confirmDialogButton = (Button) dialog.findViewById(R.id.bt_confirm);
        confirmDialogButton.setOnClickListener(v -> {
            dialog.dismiss();
            Map<String, String> params;
            if (isMcx) {
                params = new HashMap<>();
                params.put("userID", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""));
                params.put("orderID", "1");
                params.put("type", "5");
                callWebService(WebService.CLOSECANCELTRADE, params);
            } else {
                params = new HashMap<>();
                params.put("userID", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""));
                params.put("orderID", "2");
                params.put("type", "5");
                callWebService(WebService.CLOSECANCELTRADE, params);
            }
        });
        dialog.show();
    }


    public void CloseApplication(boolean isMcx, Context svContext, String head, String strTitle, String strDesc) {
        final Dialog dialog = new Dialog(svContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_header_twobutton);

        TextView textTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        textTitle.setText(strTitle);
        TextView textDesc = (TextView) dialog.findViewById(R.id.dialog_desc);
        textDesc.setText(strDesc);
        textDesc.setVisibility(View.GONE);
        TextView textHead = (TextView) dialog.findViewById(R.id.dialog_head);
        textHead.setText(head);

        Button declineDialogButton = (Button) dialog.findViewById(R.id.bt_decline);
        declineDialogButton.setOnClickListener(v -> dialog.dismiss());

        Button confirmDialogButton = (Button) dialog.findViewById(R.id.bt_confirm);
        confirmDialogButton.setOnClickListener(v -> {
            dialog.dismiss();
            Map<String, String> params;
            if (isMcx) {
                params = new HashMap<>();
                params.put("userID", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""));
                params.put("orderID", "1");
                params.put("type", "4");
                callWebService(WebService.CLOSECANCELTRADE, params);
            } else {
                params = new HashMap<>();
                params.put("userID", PreferenceConnector.readString(svContext, PreferenceConnector.LOGINUSERID, ""));
                params.put("orderID", "2");
                params.put("type", "4");
                callWebService(WebService.CLOSECANCELTRADE, params);
            }
        });
        dialog.show();
    }

}