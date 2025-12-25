package com.onlinetradeview.tv.cmonulty.customviews;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;

public class ShowCustomToast {
    private Context con;

    public final int ToastyError = 0;
    public final int ToastySuccess = 1;
    public final int ToastyInfo = 2;
    public final int ToastyWarning = 3;
    public final int ToastyNormal = 4;
    public final int ToastyNormalWithIcon = 5;
    public final int SweetAlertSuccess = 6;
    public final int SweetAlertFailed = 7;

    public ShowCustomToast(Context context) {
        this.con = context;
    }

    public void showCustomToast(final Context con, final String toastString, final int ToastType) {
        ((Activity) con).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (ToastType) {
                    case ToastyError:
                        Toast.makeText(con, "❌ " + toastString, Toast.LENGTH_LONG).show();
                        break;
                    case ToastySuccess:
                        Toast.makeText(con, "✅ " + toastString, Toast.LENGTH_LONG).show();
                        break;
                    case ToastyInfo:
                        Toast.makeText(con, "ℹ️ " + toastString, Toast.LENGTH_LONG).show();
                        break;
                    case ToastyWarning:
                        Toast.makeText(con, "⚠️ " + toastString, Toast.LENGTH_LONG).show();
                        break;
                    case ToastyNormal:
                        Toast.makeText(con, toastString, Toast.LENGTH_LONG).show();
                        break;
                    case ToastyNormalWithIcon:
                        Toast.makeText(con, toastString, Toast.LENGTH_LONG).show();
                        break;
                    case SweetAlertSuccess:
                        new MaterialAlertDialogBuilder(con)
                                .setTitle("Success")
                                .setMessage(toastString)
                                .setPositiveButton("Close", (dialog, which) -> dialog.dismiss())
                                .show();
                        break;
                    case SweetAlertFailed:
                        new MaterialAlertDialogBuilder(con)
                                .setTitle("Failed")
                                .setMessage(toastString)
                                .setPositiveButton("Close", (dialog, which) -> dialog.dismiss())
                                .show();
                        break;
                    default:
                        break;
                }
                if (GlobalVariables.ISTESTING) {
                    System.out.println(toastString + "..........toastString__print......");
                }
            }
        });
    }

    public void showCustomToast(final String toastString, final int ToastType) {
        showCustomToast(con, toastString, ToastType);
    }

    public void showToast(final String string, final Context con) {
        ((Activity) con).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showCustomToast(con, string, ToastyNormal);

                if (GlobalVariables.ISTESTING) {
                    System.out.println(string + "..........toast print......");
                }
            }
        });
    }
}
