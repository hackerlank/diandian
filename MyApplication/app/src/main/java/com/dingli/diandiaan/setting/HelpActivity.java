package com.dingli.diandiaan.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.X5WebView;
import com.dingli.diandiaan.firstpage.hybrid.AndroidObjectInJavascript;
import com.dingli.diandiaan.firstpage.hybrid.JsInterface;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by dingliyuangong on 2016/11/25.
 */
public class HelpActivity extends BaseActivity implements View.OnClickListener{
    X5WebView wbhelp;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    wbhelp.loadUrl("javascript:getClientType()");
                    break;
                case 2:
                    if(mFailingUrl !=null){
                        wbhelp.loadUrl(mFailingUrl);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private String mFailingUrl = null;
    ProgressBar helpProgressBars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initView();
    }
    private void initView() {
       final ImageView helpback = (ImageView) findViewById(R.id.helpback);
        helpProgressBars=(ProgressBar)findViewById(R.id.helpProgressBars);
        helpback.setOnClickListener(this);
        wbhelp=(X5WebView)findViewById(R.id.wbhelp);
        wbhelp.addJavascriptInterface(new AndroidObjectInJavascript(this), "aizhixin");
        wbhelp.addJavascriptInterface(new JsInterface(handler), "retry");
        wbhelp.loadUrl(Constant.webhelp+"/mobileui/helperCenter");
        wbhelp.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 0) {
                    handler.sendEmptyMessage(1);
                } else if (newProgress == 100) {
                    helpProgressBars.setVisibility(View.GONE);
                }else{
                    if (View.GONE == helpProgressBars.getVisibility()) {
                        helpProgressBars.setVisibility(View.VISIBLE);
                    }
                    helpProgressBars.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        wbhelp.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                mFailingUrl = failingUrl;
                view.loadUrl("file:///android_asset/error.html");
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                if (s != null) {
                    webView.loadUrl(s);
                }
                return true;
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.helpback:
                if (wbhelp.getUrl().equals(Constant.webhelp+"/mobileui/helperCenter")) {
                    finish();
                    overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                }else{
                    wbhelp.loadUrl(Constant.webhelp+"/mobileui/helperCenter");
                }
                break;
        }
    }
}
