package com.onlinetradeview.tv.cmonulty;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.onlinetradeview.tv.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlobalData {
    private Context _context;

    public GlobalData(Context context) {
        this._context = context;
    }

    public static int getHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return height;
    }

    public static String getUniqueString() {
        Calendar today = Calendar.getInstance();
        int date = today.get(Calendar.DATE);
        int month = today.get(Calendar.MONTH);
        int year = today.get(Calendar.YEAR);
        int hour = today.get(Calendar.HOUR);
        int minute = today.get(Calendar.MINUTE);
        int second = today.get(Calendar.SECOND);

        return date + "" + month + "" + year + "" + hour + "" + minute + "" + second;
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (GlobalVariables.ISTESTING) Log.e("DeviceId -> ", deviceId);
        return deviceId;
    }

    public static int getWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    public static String removeFirstCountChar(String word, int count) {
        return word.substring(count);
    }

    public static String removeLastCountChar(String word, int count) {
        return word.substring(0, word.length() - count);
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + "_" + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public String getDeviceVersion() {
        String myVersion = Build.VERSION.RELEASE; // e.g. myVersion := "1.6"
        //		int sdkVersion = android.os.Build.VERSION.SDK_INT; // e.g. sdkVersion := 8;
        return myVersion;
    }

    public String getDeviceVersionName() {
        // Names taken from android.os.build.VERSION_CODES
        String[] mapper = new String[]{
                "ANDROID_BASE", "ANDROID_BASE1.1", "CUPCAKE", "DONUT",
                "ECLAIR", "ECLAIR_0_1", "ECLAIR_MR1", "FROYO", "GINGERBREAD",
                "GINGERBREAD_MR1", "HONEYCOMB", "HONEYCOMB_MR1", "HONEYCOMB_MR2",
                "ICE_CREAM_SANDWICH", "ICE_CREAM_SANDWICH_MR1", "JELLY_BEAN", "JELLY_BEAN_MR1", "JELLY_BEAN_MR2",
                "KITKAT", "LOLLYPOP"
        };
        int index = Build.VERSION.SDK_INT - 1;
        String versionName = index < mapper.length ? mapper[index] : "UNKNOWN_VERSION"; // > KITKAT)
        return versionName;
    }

    public static double roundUp(double value, int roundAfterDecimal) {
        BigDecimal totaalAmt = BigDecimal.valueOf(value);
        BigDecimal strtotaalAmt = totaalAmt.setScale(roundAfterDecimal, RoundingMode.HALF_UP);
        return strtotaalAmt.doubleValue();
    }


    public static void print(String strPrint) {
        System.out.println(strPrint + "..........print..............");
    }

    public static int getApiVesion() {
        return Build.VERSION.SDK_INT;
    }

    public static boolean createDirIfNotExists(String path) {
        boolean ret = true;
        File file = new File(Environment.getExternalStorageDirectory(), path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                //System.out.println("Problem creating Image folder");
                ret = false;
            }
        }
        return ret;
    }

    public static boolean createDirIfNotExist(File file) {
        boolean ret = true;
        if (!file.exists()) {
            if (!file.mkdirs()) {
                //System.out.println("Problem creating Image folder");
                ret = false;
            }
        }
        return ret;
    }

    public static String getRGBtoHEX(String RGB) {
        String[] rgb = RGB.split(",");
        int r = Integer.parseInt(rgb[0]);
        int g = Integer.parseInt(rgb[1]);
        int b = Integer.parseInt(rgb[2]);
        String hexColor = String.format("#%02x%02x%02x", r, g, b);
        return hexColor;
    }

    public static int getSpinnerPosByValue(List<String> spinnerItem, String myString) {
        int index = 0;
        for (int i = 0; i < spinnerItem.size(); i++) {
            System.out.println(spinnerItem.get(i) + "........." + myString);
            // For compare with id write [0] and for value write [1]
            if (spinnerItem.get(i).trim().split("#:#")[0].equalsIgnoreCase(myString.trim())) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static int getSpinnerPosByValueWithoutSplit(List<String> spinnerItem, String myString) {
        int index = 0;
        for (int i = 0; i < spinnerItem.size(); i++) {
            System.out.println(spinnerItem.get(i) + "....spinner pos....." + myString);
            // For compare with id write [0] and for value write [1]
            if (spinnerItem.get(i).trim().equalsIgnoreCase(myString.trim())) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    public static String getDeviceType(Context context) {
        //		System.out.println("Quick Mobi Indfo "
        //				+ android.os.Build.VERSION.RELEASE + "  "
        //				+ android.os.Build.MODEL);
        return Build.MODEL;
    }

    public static String getOsVersion(Context context) {
        return Build.VERSION.RELEASE;
    }

    public static void SaveStringInFile(Context svContext, final String fileData, String fileName) throws IOException {
        File dir = new File(GlobalVariables.defaultAppPath);
        dir.mkdirs();
        // create the file in which we will write the contents
        File file = new File(dir, fileName + ".txt");
        FileOutputStream os;
        try {
            os = new FileOutputStream(file);
            os.write(fileData.getBytes());
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void OpenInstagramPage(String InstaPath, Context context) {
        Uri uri = Uri.parse("http://instagram.com/_u/" + InstaPath);
        Intent instaPath = new Intent(Intent.ACTION_VIEW, uri);

        instaPath.setPackage("com.instagram.android");

        try {
            context.startActivity(instaPath);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/" + InstaPath)));
        }
    }

    public static void OpenWhatsappContact(String number, Context context) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        //		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i, null);
    }

    public static void OpenHikeContact(String number, Context context) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.bsb.hike");
        //		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i, null);
    }

    public static String getStringRes(Context aContext, int strId) {
        String str = aContext.getResources().getString(strId);
        return str;
    }

    public static void SetLanguage(Context svContext) {
        if (PreferenceConnector.readString(svContext, PreferenceConnector.LANGUAGE, "en").equalsIgnoreCase("en")) {
            LocaleHelper.setLocale(svContext, "en");
        } else {
            LocaleHelper.setLocale(svContext, "hi");
        }
    }

    public static void Fullscreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void statusbarBackgroundTrans(Activity activity, int drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(drawable);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            // window.setNavigationBarColor(activity.getResources().getColor(R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    public static void statusbarBackgroundTransformURL(Activity activity, String imageurl) {
        Bitmap bmp = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            URL url = null;
            try {
                url = new URL(imageurl);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            } catch (Exception e) {
                e.printStackTrace();
            }

            Window window = activity.getWindow();
            Drawable background = new BitmapDrawable(activity.getResources(), bmp);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            // window.setNavigationBarColor(activity.getResources().getColor(R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    //For Changing Status Bar Color if Device is above 5.0(Lollipop)
    public static void changeStatusBarColor(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(activity.getResources().getColor(R.color.black));
        }
    }

    public static void changeStatusBarColorNew(Activity activity, int color) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(activity.getResources().getColor(color));
        }
    }

    public static String extractYoutubeVideoId(String ytUrl) {
        String vId = "";
        Pattern pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()) {
            vId = matcher.group(1);
        }
        return vId;
    }
}