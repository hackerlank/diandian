package com.dingli.diandiaan.firstpage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.X5WebView;
import com.dingli.diandiaan.firstpage.hybrid.AndroidObjectInJavascript;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import static android.view.KeyEvent.KEYCODE_BACK;

public class WebViewThreeActivity extends Activity implements View.OnClickListener{

    private X5WebView webView;
    ProgressBar bar;
    LinearLayout webllthree;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_viewthree);
        bar = (ProgressBar)findViewById(R.id.myProgressBarthree);
        initView();
        webView.addJavascriptInterface(new AndroidObjectInJavascript(this), "aizhixin");
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
                if (progress == 100) {
                    bar.setVisibility(View.GONE);
                    webllthree.setVisibility(View.GONE);
                } else {
                    if (View.GONE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(progress);
                }
                super.onProgressChanged(view, progress);
            }
        });
    }
    private void initView() {
        webView= (X5WebView) findViewById(R.id.webViewthree);
        ImageView remenback=(ImageView) findViewById(R.id.remenbackthree);
        TextView webtv=(TextView) findViewById(R.id.webtv);
        webllthree=(LinearLayout) findViewById(R.id.webllthree);
        remenback.setOnClickListener(this);
        String url=getIntent().getStringExtra("targetUrl");
        String targetTitle=getIntent().getStringExtra("targetTitle");
        if (!TextUtils.isEmpty(targetTitle)) {
            webtv.setText(targetTitle);
        }
        webView.loadUrl(url);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remenbackthree:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}


