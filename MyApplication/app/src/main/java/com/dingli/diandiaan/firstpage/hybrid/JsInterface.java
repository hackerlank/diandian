package com.dingli.diandiaan.firstpage.hybrid;

import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;

import org.json.JSONObject;

/**
 * Created by dingliyuangong on 2017/1/14.
 */
public class JsInterface {
    Handler handler;
    public JsInterface(Handler handler){
        this.handler=handler;
    }
    @JavascriptInterface
    public void errorReload() {
        new Thread(){
            public void run() {
                Message msg = Message.obtain();
                msg.what = 2;
                handler.sendMessage(msg);
            }
        }.start();
    }
}
