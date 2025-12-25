package com.onlinetradeview.tv.act;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.onlinetradeview.tv.R;
import com.onlinetradeview.tv.cmonulty.PreferenceConnector;
import com.onlinetradeview.tv.cmonulty.customviews.ShowCustomToast;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {
    private WebView webViewSuite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_webview);

        webViewSuite = findViewById(R.id.webViewSuite);
        ImageView imgBack = findViewById(R.id.img_back);
        TextView txtTitle = findViewById(R.id.heading);
        txtTitle.setText(PreferenceConnector.readString(this, PreferenceConnector.WEBHEADING, ""));

        imgBack.setOnClickListener(this);

        ProgressBar pDialog = new ProgressBar(this);

        webViewSuite.getSettings().setJavaScriptEnabled(true);
        webViewSuite.getSettings().setDomStorageEnabled(true);
        webViewSuite.getSettings().setLoadWithOverviewMode(true);
        webViewSuite.getSettings().setUseWideViewPort(true);

        webViewSuite.setWebChromeClient(new android.webkit.WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (pDialog != null) {
                    if (newProgress < 100) pDialog.setVisibility(View.VISIBLE);
                    else pDialog.setVisibility(View.GONE);
                }
            }
        });

        webViewSuite.setWebViewClient(new android.webkit.WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (pDialog != null) pDialog.setVisibility(View.VISIBLE);
                // user callback logic from original customizeClient onPageStarted can go here
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (pDialog != null) pDialog.setVisibility(View.GONE);
                // user callback logic from original customizeClient onPageFinished can go here
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // preserve original behavior
                if (url != null && url.contains("wa.om")) {
                    openWhatsApp();
                    onBackPressed();
                    return true;
                }

                // If it's a PDF, open with external viewer and finish the activity (acts like setOpenPDFCallback(this::finish))
                if (url != null && url.toLowerCase().endsWith(".pdf")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(android.net.Uri.parse(url), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                            finish(); // call the original finish callback
                            return true;
                        } else {
                            new ShowCustomToast(WebViewActivity.this).showToast("No PDF viewer installed.", WebViewActivity.this);
                        }
                    } catch (Exception e) {
                        new ShowCustomToast(WebViewActivity.this).showToast("Unable to open PDF.", WebViewActivity.this);
                    }
                }

                view.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, android.webkit.WebResourceRequest request) {
                return shouldOverrideUrlLoading(view, request.getUrl().toString());
            }
        });

        String initialUrl = PreferenceConnector.readString(this, PreferenceConnector.WEBURL, "");
        if (initialUrl != null && !initialUrl.isEmpty()) {
            webViewSuite.loadUrl(initialUrl);
        }
    }

    private void openWhatsApp() {
        String smsNumber = "919116669453"; // E164 format without '+' sign
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "I want to know about your services.");
        sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
        sendIntent.setPackage("com.whatsapp");
        if (sendIntent.resolveActivity(getPackageManager()) == null) {
            new ShowCustomToast(WebViewActivity.this).showToast("Whatsapp have not been installed.", WebViewActivity.this);
            return;
        }
        startActivity(sendIntent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_back) {
            finish();
        }
    }

    @Override
    public void onBackPressed () {
        if (webViewSuite != null && webViewSuite.canGoBack()) {
            webViewSuite.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
