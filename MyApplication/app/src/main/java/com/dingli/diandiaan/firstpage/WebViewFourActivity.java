package com.dingli.diandiaan.firstpage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
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

public class WebViewFourActivity extends Activity implements View.OnClickListener{

    private X5WebView webView;
    private ImageView remenback;
    ProgressBar bar;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_viewfour);
        getData();
        webView.addJavascriptInterface(new AndroidObjectInJavascript(this), "aizhixin");
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
                if (progress == 100) {
                    bar.setVisibility(View.GONE);
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
    private void getData() {
        bar = (ProgressBar)findViewById(R.id.myProgressBarfour);
        webView= (X5WebView) findViewById(R.id.webViewfour);
        remenback=(ImageView) findViewById(R.id.remenbackfour);
        remenback.setOnClickListener(this);
        TextView tvtitle=(TextView) findViewById(R.id.tvtitle);
        String url=getIntent().getStringExtra("url");
        String title=getIntent().getStringExtra("title");
        tvtitle.setText(title);
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
            case R.id.remenbackfour:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
        }
    }
}


