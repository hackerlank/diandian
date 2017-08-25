package com.dingli.diandiaan.information;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.X5WebView;
import com.dingli.diandiaan.firstpage.hybrid.AndroidObjectInJavascript;
import com.dingli.diandiaan.firstpage.hybrid.JsInterface;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by dingliyuangong on 2017/1/16.
 */
public class WarningActivity extends BaseActivity{
   VerticalSwipeRefreshLayout refwarn;
    X5WebView wbwarn;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    wbwarn.loadUrl("javascript:accessTokenAddTokenType()");
                    break;
                case 2:
                    if(mFailingUrl !=null){
                        wbwarn.loadUrl(mFailingUrl);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private String mFailingUrl = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);
        initview();
    }
    void initview(){
        ImageView warnback=(ImageView)findViewById(R.id.warnback);
        warnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WarningActivity.this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
            }
        });
        refwarn=(VerticalSwipeRefreshLayout)findViewById(R.id.refwarn);
        refwarn.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
        wbwarn=(X5WebView)findViewById(R.id.wbwarn);
        wbwarn.addJavascriptInterface(new AndroidObjectInJavascript(this), "aizhixin");
        wbwarn.addJavascriptInterface(new JsInterface(handler), "retry");
        refwarn.setRefreshing(true);
        refwarn.setRefreshing(true);
        wbwarn.loadUrl(Constant.webwarn+"/mobileui/warning");
        wbwarn.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 0) {
                    handler.sendEmptyMessage(1);
                } else if (newProgress == 100) {
                    refwarn.setRefreshing(false);
                    refwarn.setEnabled(false);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        wbwarn.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                mFailingUrl = failingUrl;
                view.loadUrl("file:///android_asset/error.html");
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String s) {
                view.loadUrl(s);
                return true;
            }
        });
    }
}
