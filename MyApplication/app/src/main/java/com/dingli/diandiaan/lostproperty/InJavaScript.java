package com.dingli.diandiaan.lostproperty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.X5WebView;
import com.dingli.diandiaan.firstpage.hybrid.Datas;
import com.dingli.diandiaan.login.ActionSheetDialog;
import com.dingli.diandiaan.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dingliyuangong on 2017/5/12.
 */

public class InJavaScript {
    UpPhone phone;
    Activity context;
    X5WebView webView;
    public InJavaScript(Activity context,UpPhone upPhone,X5WebView webViews){
        this.phone=upPhone;
        this.context=context;
        this.webView=webViews;
    }
    @JavascriptInterface
    public String accessTokenAddTokenType(){
        DianTool.huoqutoken();
        return  DianApplication.user.token;
    }
    @JavascriptInterface
    public void callAppNative(String url){
        Datas datas= JSON.parseObject(url,Datas.class);
        switch (datas.type){
            case "camera":
                new ActionSheetDialog(context)
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Green,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        phone.upphone("photograph","");
                                    }
                                })
                        .addSheetItem("从相册选取", ActionSheetDialog.SheetItemColor.Green,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        phone.upphone("album","");
                                    }
                                })
                        .show();
                break;
            case"phone":
                phone.upphone("phone",datas.param1);
                break;
            case "copy":
                phone.upphone("copy",datas.param1);
                break;
        }
    }
    @JavascriptInterface
    public String getUserInfo(){
        return DianApplication.sharedPreferences.getStringValue(Constant.INFO);
    }
    @JavascriptInterface
    public String noticeMessage(String json){
        return json;
    }
    @JavascriptInterface
    public String callAppNativeWithPhotosUrl(String trs){
        return trs;
    }
    @JavascriptInterface
    public void backNative(){
        context.finish();
        context.overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
    }
    @JavascriptInterface
    public void errorHandle(){
        Intent intent=new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        context.finish();
        context.overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }
    public  interface UpPhone{
        void upphone(String name, String str);
    }
}
