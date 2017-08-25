package com.dingli.diandiaan.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.DianTool;

/**
 *
 */
public class YTWebView extends RelativeLayout {

    private int mTouchSlop;
    // 上一次触摸时的X坐标
    private float mPrevX,mPrevY;
    private View view;
    private Context ctx;
    /**
     * 安全WebView
     */
    private SafeWebView  webView;
    /**
     * 下拉刷新View
     */
    private PullToRefreshView refreshView;

    public YTWebView(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initView(context);
    }

    public YTWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initView(context);
    }

    public YTWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initView(context);
    }

    private void initView(Context context) {
        ctx = context;
        LayoutInflater.from(context).inflate(R.layout.yt_webview_layout, this);
        refreshView = (PullToRefreshView) findViewById(R.id.refresh_view);
        webView = (SafeWebView) findViewById(R.id.safe_webview);
    }

    /**
     * 设置刷新成功
     */
    public void setRefreshSuccess() {
        refreshView.refreshFinish(PullToRefreshView.SUCCEED);
    }

    /**
     * 设置刷新失败
     */
    public void setRefreshFail() {
        refreshView.refreshFinish(PullToRefreshView.FAIL);
    }

    /**
     * 设置刷新是否启用
     *
     * @param isEnable
     */
    public void setRefreshEnable(boolean isEnable) {
        webView.setCanPullDown(isEnable);
    }

    /**
     * 设置刷新中回调
     *
     * @param listener
     */
    public void setOnRefreshWebViewListener(OnRefreshWebViewListener listener) {
        refreshView.setOnRefreshListener(listener);
    }

    /**
     * 获取WebView
     *
     * @return
     */
    public WebView getWebView() {
        return webView;
    }
}
