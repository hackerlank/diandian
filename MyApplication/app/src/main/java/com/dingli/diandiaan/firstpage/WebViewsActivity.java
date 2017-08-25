package com.dingli.diandiaan.firstpage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.X5WebView;
import com.dingli.diandiaan.firstpage.hybrid.AndroidObjectInJavascript;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by dingliyuangong on 2016/12/23.
 */
public class WebViewsActivity extends Activity implements View.OnClickListener{
    X5WebView webView;
    private TextView tvtoutiao;
    ProgressBar bar;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 1:
                        webView.loadUrl("javascript:accessTokenAddTokenType()");
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_views);
        initView();
        initview();
    }
    void initview() {
        webView = (X5WebView) findViewById(R.id.webViewd);
        bar = (ProgressBar) findViewById(R.id.myProgressBard);
        TextView tvtoutiaos=(TextView)findViewById(R.id.tvtoutiaos);
        String url=getIntent().getStringExtra("url");
        String title=getIntent().getStringExtra("title");
        if (url.contains("allArticle")){
            tvtoutiaos.setText("点点心理");
        }else{
            tvtoutiaos.setText(title);
        }
        webView.loadUrl(Constant.webwenzhang+url);
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
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void initView() {
        ImageView remenbackd = (ImageView) findViewById(R.id.remenbackd);
        tvtoutiao = (TextView) findViewById(R.id.tvtoutiaos);
        remenbackd.setOnClickListener(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.remenbackd:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                break;
        }
    }
}
