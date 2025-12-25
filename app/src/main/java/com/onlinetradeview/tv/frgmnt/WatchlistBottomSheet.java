package com.onlinetradeview.tv.frgmnt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.onlinetradeview.tv.act.ActSearchMetal;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.onlinetradeview.tv.adptr.WatchlistAdapter;
import com.onlinetradeview.tv.cmonulty.GlobalVariables;

import java.util.Arrays;
import java.util.List;

public class WatchlistBottomSheet extends BottomSheetDialogFragment {
    private static final String ARG_LEVEL = "arg_level";
    private static final String ARG_BASE = "arg_base";

    public static WatchlistBottomSheet newInstance(int level, int baseId) {
        WatchlistBottomSheet f = new WatchlistBottomSheet();
        Bundle b = new Bundle();
        b.putInt(ARG_LEVEL, level);
        b.putInt(ARG_BASE, baseId);
        f.setArguments(b);
        return f;
    }

    private int level = 1;
    private int baseId = 1;
    private final List<String> items = Arrays.asList(
            "Nse future#:#NFO",
            "Mcx future#:#MCX",
            "Option and future#:#OPFUT",
            "Global future#:#GBFUT",
            "Comex future#:#COMEX",
            "Others#:#OTHERS"
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            level = getArguments().getInt(ARG_LEVEL, 1);
            baseId = getArguments().getInt(ARG_BASE, 1);
        }

        Context ctx = requireContext();

        LinearLayout root = new LinearLayout(ctx);
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (16 * getResources().getDisplayMetrics().density);
        root.setPadding(pad, pad, pad, pad);

        TextView header = new TextView(ctx);
        header.setText("Select Exchange");
        Typeface font = Typeface.createFromAsset(ctx.getAssets(), GlobalVariables.CUSTOMFONTNAME);
        header.setTypeface(font);
        header.setTextSize(18);
        header.setGravity(Gravity.START);
        root.addView(header, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        RecyclerView rv = new RecyclerView(ctx);
        rv.setLayoutManager(new LinearLayoutManager(ctx));
        WatchlistAdapter adapter = new WatchlistAdapter(items, baseId, (pos, id) -> {
            Intent intent = new Intent(ctx, ActSearchMetal.class);
            intent.putExtra("ischeckshow", "2");
            intent.putExtra("exchangeName", items.get(pos).split("#:#")[1]);
            ctx.startActivity(intent);
        }, ctx);
        rv.setAdapter(adapter);

        LinearLayout.LayoutParams rvParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rvParams.topMargin = pad / 2;
        root.addView(rv, rvParams);

        return root;
    }
}

