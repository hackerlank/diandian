package com.dingli.diandiaan.school;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.firstpage.hybrid.Datas;
import com.dingli.diandiaan.login.LoginActivity;

/**
 * Created by dingliyuangong on 2017/5/31.
 */

public class SchoolInJavascript {
    private Activity activity;
    public SchoolInJavascript(Activity activity){
        this.activity = activity;
    }
    @JavascriptInterface
    public String accessTokenAddTokenType(){
        DianTool.huoqutoken();
        return  DianApplication.user.token;
    }
    @JavascriptInterface
    public void hideNavigation(String navitaga){
    }
    @JavascriptInterface
    public void backNative(){
        activity.finish();
        activity.overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
    }
    @JavascriptInterface
    public void errorHandle(String unvalid_token){
        Intent intent=new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }
    @JavascriptInterface
    public void errorHandle(){
        Intent intent=new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }
    @JavascriptInterface
    public void operateType(String url){
        Datas datas= JSON.parseObject(url, Datas.class);
        String id=datas.url.substring(datas.url.indexOf("?") + 1);
        DianApplication.user.wenzhangid=id;
        Intent intent=new Intent(activity, SchoolWebsActivity.class);
        intent.putExtra("url",datas.url);
        intent.putExtra("title",datas.title);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
    @JavascriptInterface
    public String homeLisWithID(){
        return DianApplication.user.wenzhangid;
    }
    @JavascriptInterface
    public void examCurrentStateName(String url){
    }
}
