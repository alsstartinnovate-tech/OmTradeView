package com.onlinetradeview.tv.act;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.onlinetradeview.tv.R;

public class SetActionTopBar {

    public SetActionTopBar(View view, Activity act, String strHead) {
        TextView textTopBarTitle = (TextView) view.findViewById(R.id.heading);
        ImageView imgBack = (ImageView) view.findViewById(R.id.img_back);

        textTopBarTitle.setText(strHead);
        imgBack.setOnClickListener(arg0 -> onBackButtonClicked(act));
    }

    private void onBackButtonClicked(Activity act) {
        act.onBackPressed();
    }
}
