package com.dingli.diandiaan.firstpage;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.X5WebView;
import com.dingli.diandiaan.firstpage.hybrid.AndroidObjectInJavascript;
import com.tencent.smtt.sdk.WebChromeClient;

public class WebViewOneActivity extends Activity implements View.OnClickListener{

    private X5WebView webView;
    private String url="";
    private ImageView remenback;
    ProgressBar bar;
    String id;
    String title;
    GifView webimg;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 1:
                        webView.loadUrl("javascript:accessTokenAddTokenType()");
                        webView.loadUrl("javascript:getClientType()");
                        webView.loadUrl("javascript:BundleShortVersion()");
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web_viewone);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bar = (ProgressBar)findViewById(R.id.myProgressBarone);
        getData();
        initView();
        fillWebView();
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        webView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        webView.addJavascriptInterface(new AndroidObjectInJavascript(this), "aizhixin");
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(com.tencent.smtt.sdk.WebView webView, int i) {
                if (i == 0) {
                    handler.sendEmptyMessage(1);
                }
                if (i == 100) {
                    webimg.setVisibility(View.GONE);
                    bar.setVisibility(View.GONE);
                } else {
                    if (View.GONE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(i);
                }
                super.onProgressChanged(webView, i);
            }
        });
    }

    private void fillWebView() {
            String url=getIntent().getStringExtra("url");
            webView.loadUrl(Constant.hy+"/mobileui_hy/");
    }

    private void getData() {
            url=getIntent().getStringExtra("url");
            title=getIntent().getStringExtra("title");
    }

    private void initView() {
        webView= (X5WebView) findViewById(R.id.webViewone);
        remenback=(ImageView) findViewById(R.id.remenbackone);
        remenback.setOnClickListener(this);
        TextView tvtileone=(TextView) findViewById(R.id.tvtileone);
        LinearLayout webone=(LinearLayout) findViewById(R.id.webone);
        webimg=(GifView)findViewById(R.id.webivg);
        webimg.setGifImage(R.mipmap.gifpigeimg);
        webimg.setGifImageType(GifView.GifImageType.COVER);
            webone.setVisibility(View.GONE);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remenbackone:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
        }
    }
}


