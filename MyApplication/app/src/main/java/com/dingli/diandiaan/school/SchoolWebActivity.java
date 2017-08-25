package com.dingli.diandiaan.school;

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
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.X5WebView;
import com.dingli.diandiaan.firstpage.hybrid.AndroidObjectInJavascript;
import com.dingli.diandiaan.firstpage.hybrid.JsInterface;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import static android.view.KeyEvent.KEYCODE_BACK;


/**
 * Created by dingliyuangong on 2017/2/6.
 */

public class SchoolWebActivity extends BaseActivity implements View.OnClickListener{
    ProgressBar bar;
    X5WebView daochuwebView;
    private String mFailingUrl = null;
    String domainName;
    String url;
    int web;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()){
                switch (msg.what) {
                    case 2:
                        if (mFailingUrl != null) {
                            daochuwebView.loadUrl(mFailingUrl);
                        }
                        break;
                    case 1:
                        daochuwebView.loadUrl("javascript:accessTokenAddTokenType()");
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daochuweb);
        inits();
    }
    void inits(){
        ImageView daochuback=(ImageView) findViewById(R.id.daochuback);
        daochuback.setOnClickListener(this);
        bar=(ProgressBar) findViewById(R.id.daochuprogress);
        daochuwebView=(X5WebView) findViewById(R.id.daochuwebView);
        LinearLayout llkaoqing=(LinearLayout) findViewById(R.id.llkaoqing);
        TextView tvdaochu=(TextView) findViewById(R.id.tvdaochu);
        url=getIntent().getStringExtra("url");
        String title=getIntent().getStringExtra("title");
        boolean isRefresh=getIntent().getBooleanExtra("isRefresh",true);
        boolean isStatusBar=getIntent().getBooleanExtra("isStatusBar",true);
        domainName=getIntent().getStringExtra("domainName");
         if (!TextUtils.isEmpty(title)){
             tvdaochu.setText(title);
             llkaoqing.setVisibility(View.VISIBLE);
         }else{
             llkaoqing.setVisibility(View.GONE);
         }
        daochuwebView.addJavascriptInterface(new SchoolInJavascript(this), "aizhixin");
        daochuwebView.addJavascriptInterface(new JsInterface(handler), "retry");
        if (domainName.equals("dd_mobile")){
            daochuwebView.loadUrl(Constant.webhelp+url);
        }else if (domainName.equals("hy_mobile")){
            daochuwebView.loadUrl(Constant.hy+url);
        }
        daochuwebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
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
        daochuwebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url!=null){
                    web=1;
                    view.loadUrl(url);
                }
                return true;
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                mFailingUrl = failingUrl;
                view.loadUrl("file:///android_asset/error.html");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.daochuback:
                if (web==1){
                    daochuwebView.goBack();
                    web=0;
                }else {
                    finish();
                    overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                }
                break;
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction()==KeyEvent.ACTION_DOWN&&
                KeyEvent.KEYCODE_BACK==keyCode) {
            if (web==1){
                web=0;
                daochuwebView.goBack();
            }else {
                finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
