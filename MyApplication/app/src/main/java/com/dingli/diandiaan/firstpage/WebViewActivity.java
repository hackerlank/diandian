package com.dingli.diandiaan.firstpage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.X5WebView;
import com.dingli.diandiaan.firstpage.hybrid.AndroidObjectInJavascript;
import com.tencent.smtt.sdk.WebChromeClient;

public class WebViewActivity extends Activity implements View.OnClickListener{

    private X5WebView webView;
    private String url="";
    private ImageView remenback;
    ProgressBar bar;
    String id;
    String list;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 1:
                        webView.loadUrl("javascript:homeLisWithID()");
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        bar = (ProgressBar)findViewById(R.id.myProgressBars);
        getData();
        initView();
        fillWebView();
        webView.addJavascriptInterface(new AndroidObjectInJavascript(this), "aizhixin");
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(com.tencent.smtt.sdk.WebView view, int progress) {// 载入进度改变而触发
                if (progress == 0) {
                    handler.sendEmptyMessage(1);
                }
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

    private void fillWebView() {
        if (list.equals("list")){
            webView.loadUrl(url);
        }else  if (list.equals("listv2")){
            webView.loadUrl(Constant.webwenzhang + url);
        }
    }

    private void getData() {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            url=bundle.getString("url");
            id=bundle.getString("id");
            list=bundle.getString("list");
            DianApplication.user.wenzhangid=id;
        }
    }

    private void initView() {
        webView= (X5WebView) findViewById(R.id.webView);
        remenback=(ImageView) findViewById(R.id.remenback);
        remenback.setOnClickListener(this);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remenback:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
        }
    }
}


