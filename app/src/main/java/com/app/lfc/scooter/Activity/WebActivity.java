package com.app.lfc.scooter.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.app.lfc.scooter.R;

public class WebActivity extends AppCompatActivity {

    WebView webView;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        initView();

        Intent intent = getIntent();
        String url = intent.getStringExtra("link");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_click));
                onBackPressed();
                overridePendingTransition(R.anim.intent_enter, R.anim.intent_exit);
            }
        });
    }


    private void initView() {
        btnBack = findViewById(R.id.btn_back_web);
        webView = findViewById(R.id.webview);
    }


}