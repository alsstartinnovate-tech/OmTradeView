package com.onlinetradeview.tv.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.onesignal.OneSignal;
import com.onesignal.notifications.IPermissionObserver;
import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.frgmnt.FragCreateOrder;
import com.onlinetradeview.tv.frgmnt.FragSetting;
import com.onlinetradeview.tv.frgmnt.FragTradeHistory;
import com.onlinetradeview.tv.frgmnt.FragPortfolio;
import com.onlinetradeview.tv.frgmnt.FragWatchlist;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;
import com.onlinetradeview.tv.cmonulty.customfont.FontUtils;
import com.onlinetradeview.tv.cmonulty.PreferenceConnector;
import com.onlinetradeview.tv.cmonulty.customviews.ShowCustomToast;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActMain extends AppCompatActivity implements IPermissionObserver {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        StartApp();
        resumeApp();
        checkInAppUpdate();
    }

    public void resumeApp() {
        PreferenceConnector.writeString(svContext, PreferenceConnector.FCMID, OneSignal.getUser().getPushSubscription().getId());
        initToolbar();
        initBottomMenu();
        switchContent(new FragWatchlist());
        if (PreferenceConnector.readString(svContext, PreferenceConnector.ISPWDUPDATED, "0").equalsIgnoreCase("1")) {
            onDrawerItemClick("Change password", svContext);
        }
    }

    @Override
    public void onNotificationPermissionChange(boolean permission) {
        if (permission) {
            PreferenceConnector.writeString(svContext, PreferenceConnector.FCMID, OneSignal.getUser().getPushSubscription().getId());
        }
    }

    private Context svContext;
    private ViewGroup root;

    private void StartApp() {
        svContext = this;
        root = findViewById(R.id.headlayout);
        if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
            Typeface font = Typeface.createFromAsset(getAssets(), GlobalVariables.CUSTOMFONTNAME);
            FontUtils.setFont(root, font);
        }
        hideKeyboard();
        SetColorTheme();
    }

    private void SetColorTheme() {
        if (PreferenceConnector.readInteger(svContext, PreferenceConnector.THEMESELECTED, 0) == 0) {
            root.setBackgroundResource(R.color.appbackcolor);
        } else if (PreferenceConnector.readInteger(svContext, PreferenceConnector.THEMESELECTED, 0) == 1) {
            root.setBackgroundResource(R.color.colorPrimary);
        } else {
            root.setBackgroundResource(R.color.colorPrimary);
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideKeyboard();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void switchBack() {
        hideKeyboard();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }

    private void goBackOrExit() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof FragCreateOrder) {
            defaultImage();
            switchBack();
        } else {
            CloseApplication(svContext, "Close Application", "Are you confirm?", "Are you surely to close the application.");
        }
    }


    private ImageView imgMenu;

    private void initToolbar() {
        imgMenu = (ImageView) findViewById(R.id.img_menu);
        imgMenu.setOnClickListener(view -> {
            goBackOrExit();
        });
        TextView toolbar_txt_logo = (TextView) findViewById(R.id.txt_action);
        ImageView imgColorMenu = (ImageView) findViewById(R.id.img_colortheme);
        imgColorMenu.setOnClickListener(view -> {
            String[] colors = {"Light", "Golden", "Dark"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select a color theme");
            builder.setItems(colors, (dialog, which) -> {
                dialog.dismiss();
                PreferenceConnector.writeInteger(svContext, PreferenceConnector.THEMESELECTED, which);
                startActivity(getIntent());
                finish();
                overridePendingTransition(0, 0);
            });
            builder.show();
        });

        Typeface face = Typeface.createFromAsset(getAssets(), "font/logofont.ttf");
        toolbar_txt_logo.setTypeface(face);

        LinearLayout imgToolBareWallet = (LinearLayout) findViewById(R.id.img_ewallet);
        imgToolBareWallet.setVisibility(View.VISIBLE);
        imgToolBareWallet.setOnClickListener(view -> {
            Intent svIntent = new Intent(svContext, ActWallet.class);
            startActivity(svIntent);
        });
    }


    private void initBottomMenu() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_watchlist:
                    switchContent(new FragWatchlist());
                    break;
                case R.id.nav_trade:
                    switchContent(new FragTradeHistory());
                    break;
                case R.id.nav_portfolio:
                    switchContent(new FragPortfolio());
                    break;
                case R.id.nav_setting:
                    switchContent(new FragSetting());
                    break;
            }
            return false;
        });
    }

    public void switchContent(Fragment fragment) {
        hideKeyboard();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public static void LogOut(Context svContext) {
        PreferenceConnector.cleanPrefrences(svContext);
        Intent svIntent = new Intent(svContext, ActLogin.class);
        svContext.startActivity(svIntent);
        ((Activity) svContext).finishAffinity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBackOrExit();
    }

    public static void onDrawerItemClick(String title, Context svContext) {
        ShowCustomToast customToast = new ShowCustomToast(svContext);
        Intent svIntent;

        if ((title).equalsIgnoreCase("Profile")) {
            svIntent = new Intent(svContext, ActViewProfile.class);
            svContext.startActivity(svIntent);
        } else if ((title).equalsIgnoreCase("Profile Two")) {
            svIntent = new Intent(svContext, ActViewProfileTwo.class);
            svContext.startActivity(svIntent);
        } else if ((title).equalsIgnoreCase("Change password")) {
            svIntent = new Intent(svContext, ActChngePwd.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Add Fund")) {
            svIntent = new Intent(svContext, ActFnd.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Funds")) {
            svIntent = new Intent(svContext, ActWallet.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Notification")) {
            svIntent = new Intent(svContext, ActNotification.class);
            svContext.startActivity(svIntent);
        } else if (title.equalsIgnoreCase("Privacy Policy")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalVariables.PRIVACYPOLICYURL));
            svContext.startActivity(intent);
        } else if (title.equalsIgnoreCase("Logout")) {
            ShowConfirmLogout(svContext, "Logout", "Are you confirm?",
                    "Are you are ready to end your current session. You have to enter login detail again");
        } else {
            customToast.showCustomToast(svContext, "No item found", customToast.ToastyInfo);
        }
    }

    public static void CloseApplication(Context svContext, String head, String strTitle, String strDesc) {
        final Dialog dialog = new Dialog(svContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_header_twobutton);

        TextView textTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        textTitle.setText(strTitle);
        TextView textDesc = (TextView) dialog.findViewById(R.id.dialog_desc);
        textDesc.setText(strDesc);
        TextView textHead = (TextView) dialog.findViewById(R.id.dialog_head);
        textHead.setText(head);

        Button declineDialogButton = (Button) dialog.findViewById(R.id.bt_decline);
        declineDialogButton.setOnClickListener(v -> dialog.dismiss());

        Button confirmDialogButton = (Button) dialog.findViewById(R.id.bt_confirm);
        confirmDialogButton.setOnClickListener(v -> {
            dialog.dismiss();
            ((Activity) svContext).finish();
        });
        dialog.show();
    }


    public static void ShowConfirmLogout(Context svContext, String head, String strTitle, String strDesc) {
        final Dialog dialog = new Dialog(svContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_header_twobutton);

        TextView textTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        textTitle.setText(strTitle);
        TextView textDesc = (TextView) dialog.findViewById(R.id.dialog_desc);
        textDesc.setText(strDesc);
        TextView textHead = (TextView) dialog.findViewById(R.id.dialog_head);
        textHead.setText(head);

        Button declineDialogButton = (Button) dialog.findViewById(R.id.bt_decline);
        declineDialogButton.setOnClickListener(v -> dialog.dismiss());

        Button confirmDialogButton = (Button) dialog.findViewById(R.id.bt_confirm);
        confirmDialogButton.setOnClickListener(v -> LogOut(svContext));
        dialog.show();
    }

    public void defaultImage() {
        int padding_in_dp = 0;
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
        imgMenu.setImageResource(R.drawable.logo_back);
        imgMenu.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
    }

    public void changeImage(int resourceId) {
        int padding_in_dp = 12;
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
        imgMenu.setImageResource(resourceId);
        imgMenu.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
    }

    private AppUpdateManager appUpdateManager;
    private final int APP_UPDATE_REQUEST_CODE = 1312;
    private final int UPDATE_TYPE = AppUpdateType.FLEXIBLE;

    private void checkInAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(svContext);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(this::handleAppUpdateInfo);

        View viewUpdate = findViewById(R.id.lay_update);
        if (PreferenceConnector.readBoolean(svContext, PreferenceConnector.ISUPGADEUVAILABLE, false)) {
            viewUpdate.setVisibility(View.VISIBLE);
        } else {
            viewUpdate.setVisibility(View.GONE);
        }
        TextView btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(view -> checkInAppUpdate());
    }

    String TAG = "InAppUpdate";

    private void handleAppUpdateInfo(AppUpdateInfo appUpdateInfo) {
        switch (appUpdateInfo.updateAvailability()) {
            case UpdateAvailability.UPDATE_AVAILABLE:
                if (appUpdateInfo.isUpdateTypeAllowed(UPDATE_TYPE)) {
                    Log.d(TAG, "Update available");
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISUPGADEUVAILABLE, true);
                    startInAppUpdate(appUpdateInfo);
                } else {
                    Log.d(TAG, "Update available but update type not allowed.");
                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISUPGADEUVAILABLE, false);
                }
                break;
            case UpdateAvailability.UPDATE_NOT_AVAILABLE:
                Log.d(TAG, "Update Not Available");
                PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISUPGADEUVAILABLE, false);
                break;
            case UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS:
                Log.d(TAG, "Update In Progress.");
                break;
            case UpdateAvailability.UNKNOWN:
                Log.d(TAG, "Error In Update.");
                break;
        }
    }

    private void startInAppUpdate(AppUpdateInfo appUpdateInfo) {
        appUpdateManager.registerListener(updatedListener);
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    ActMain.this,
                    AppUpdateOptions.newBuilder(UPDATE_TYPE)
                            .setAllowAssetPackDeletion(true)
                            .build(),
                    APP_UPDATE_REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private final InstallStateUpdatedListener updatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(@NonNull InstallState installState) {
            switch (installState.installStatus()) {
                case InstallStatus.DOWNLOADED:
                    // After the update is downloaded, show a notification and request user confirmation to restart the app.
                    Log.d(TAG, "An update has been downloaded");

                    PreferenceConnector.writeBoolean(svContext, PreferenceConnector.ISUPGADEUVAILABLE, false);
                    appUpdateManager.completeUpdate();
                    break;
                case InstallStatus.DOWNLOADING:
                    Log.d(TAG, "An update downloading");
                    long bytesDownloaded = installState.bytesDownloaded();
                    long totalBytesToDownload = installState.totalBytesToDownload();
                    // Implement progress bar.
                    break;
                case InstallStatus.CANCELED:
                case InstallStatus.FAILED:
                case InstallStatus.INSTALLED:
                case InstallStatus.UNKNOWN:
                    Log.d(TAG, "An update failed");
                    appUpdateManager.unregisterListener(updatedListener);
                    break;
                case InstallStatus.INSTALLING:
                case InstallStatus.REQUIRES_UI_INTENT:
                case InstallStatus.PENDING:
                    Log.d(TAG, "An update pending");
                    break;
            }
        }
    };
}