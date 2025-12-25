package com.onlinetradeview.tv.cmonulty;

import android.os.Environment;

import java.io.File;

public class GlobalVariables {
    public static final String defaultAppPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
            File.separator + "AppName/";
    public static final String CUSTOMFONTNAME = "font/regularfont.otf";
    public static final String CURRENCYSYMBOL = "₹ ";
    public static final boolean ISMOBILELOGINONLY = false;
    public static final boolean ISINTROENABLED = false;
    public static final boolean ISTESTING = true;
    public static final String TAGPOSTTEXT = ".............tagprint..............";
    public static final String PRIVACYPOLICYURL = "https://www.onlinetradelearn.com/privacy-policy/";
}