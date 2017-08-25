package com.dingli.diandiaan.firstpage.hybrid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.firstpage.FirstPageFragment;
import com.dingli.diandiaan.firstpage.WebViewActivity;
import com.dingli.diandiaan.firstpage.WebViewOneActivity;
import com.dingli.diandiaan.firstpage.WebViewsActivity;
import com.dingli.diandiaan.information.EntrtyActivity;
import com.dingli.diandiaan.instruc.InstructorActivity;
import com.dingli.diandiaan.login.LoginActivity;
import com.dingli.diandiaan.login.daochu.DaochuWebActivity;
import com.dingli.diandiaan.login.pingjiao.PingjiaoActivity;
import com.dingli.diandiaan.rule.RuleActivity;
import com.dingli.diandiaan.schedule.SyllFragments;

/**
 * Created by dingliyuangong on 2016/9/29.
 */
public class AndroidObjectInJavascript {
      private Activity activity;
    public AndroidObjectInJavascript(Activity activity){
        this.activity = activity;
    }
    @JavascriptInterface
    public String accessTokenAddTokenType(){
        DianTool.huoqutoken();
        return  DianApplication.user.token;
    }
    @JavascriptInterface
    public String homeLisWithID(){
        return DianApplication.user.wenzhangid;
    }
    @JavascriptInterface
    public void exportAttendanceReport(String url){
        Intent intent=new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        activity.startActivity(intent);
    }
    /**
     * 设置 对应fragment的标题. 通过 消息机制来实现.
     */
    @JavascriptInterface
    public String getClientType(){
        return "teacher";
    }
    @JavascriptInterface
    public String getClientRoleType(){
        return DianApplication.sharedPreferences.getStringValue(Constant.ROLE);
    }
    @JavascriptInterface
    public String getUserInfo(){
        return DianApplication.sharedPreferences.getStringValue(Constant.INFO);
    }
    @JavascriptInterface
    public String BundleShortVersion(){
        return DianApplication.code;
    }
    @JavascriptInterface
    public void operateType(String url){
        Datas datas= JSON.parseObject(url, Datas.class);
        Bundle bundle=new Bundle();
        Intent intent=new Intent();
            if (datas.operateType.equals("DLHomeDianMing")) {
                if (TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
                    intent.setClass(activity, LoginActivity.class);
                    activity.startActivity(intent);
                } else {
                    intent.setClass(activity, RuleActivity.class);
                    activity.startActivity(intent);
                }
            }else if(datas.operateType.equals("DLHomeKeBiao")){
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(activity,LoginActivity.class);
                    activity.startActivity(intent);
                }else {
                    intent.setClass(activity, SyllFragments.class);
                    intent.putExtra(Constant.XIANSHI,"OK");
                    activity.startActivity(intent);
                }
            }else if(datas.operateType.equals("DLHomeReMen")){
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(activity,LoginActivity.class);
                    activity.startActivity(intent);
                }else {
                    intent.setClass(activity, FirstPageFragment.class);
                    activity.startActivity(intent);
                }
            }else if (datas.operateType.equals("DLHomeApproval")){
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(activity,LoginActivity.class);
                    activity.startActivity(intent);
                }else {
                    intent.setClass(activity, EntrtyActivity.class);
                    activity.startActivity(intent);
                }
            }else if(datas.operateType.equals("DLHomeCallSet")){
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(activity, LoginActivity.class);
                    activity.startActivity(intent);
                }else {
                    Initoken.SingId(activity);
                }
            }else if (datas.operateType.equals("DLHomeHeadList")){
                    intent.setClass(activity, WebViewActivity.class);
                    bundle.putString("url", datas.url);
                    bundle.putString("list","list");
                    intent.putExtras(bundle);
                activity.startActivity(intent);
            }else  if (datas.operateType.equals("DLHomeHeadListV2")){
                if (TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
                    intent.setClass(activity, LoginActivity.class);
                }else {
                    String id = datas.url.substring(datas.url.indexOf("?") + 1);
                    intent.setClass(activity, WebViewActivity.class);
                    bundle.putString("url", datas.url);
                    bundle.putString("id", id);
                    bundle.putString("list","listv2");
                    intent.putExtras(bundle);
                }
                activity.startActivity(intent);
            }else if (datas.operateType.equals("DLHomeBanner")){
                intent.setClass(activity, WebViewsActivity.class);
                intent.putExtra("url", datas.url);
                intent.putExtra("title",datas.title);
                activity.startActivity(intent);
            }else if (datas.operateType.equals("DLHomeArticleList")){
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(activity, LoginActivity.class);
                }else {
                    intent.setClass(activity, WebViewsActivity.class);
                    intent.putExtra("url", datas.url);
                }
                activity.startActivity(intent);
            }else if (datas.operateType.equals("DLHomeReserveNoRefrash1")){
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(activity, LoginActivity.class);
                }else {
                    intent.setClass(activity, WebViewOneActivity.class);
                    intent.putExtra("url",datas.url);
                    intent.putExtra("title",datas.title);
                }
                activity.startActivity(intent);
            }else if (datas.operateType.equals("DLHomeReserveNoRefrash2")){
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(activity, LoginActivity.class);
                }else {
                    intent.setClass(activity, WebViewOneActivity.class);
                    intent.putExtra("url",datas.url);
                    intent.putExtra("title",datas.title);
                }
                activity.startActivity(intent);
            }else if (datas.operateType.equals("DLHomeReserveNoRefrash3")){
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(activity, LoginActivity.class);
                }else {
                    intent.setClass(activity, WebViewOneActivity.class);
                    intent.putExtra("url",datas.url);
                    intent.putExtra("title",datas.title);
                }
                activity.startActivity(intent);
            }else  if (datas.operateType.equals("DLHomeReserveNoRefrash4")){
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(activity, LoginActivity.class);
                }else {
                    intent.setClass(activity, WebViewOneActivity.class);
                    intent.putExtra("url",datas.url);
                    intent.putExtra("title",datas.title);
                }
                activity.startActivity(intent);
            }else if(datas.operateType.equals("DLHomeReserveNoRefrash5")){
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(activity, LoginActivity.class);
                }else {
                    intent.setClass(activity, WebViewOneActivity.class);
                    intent.putExtra("url",datas.url);
                    intent.putExtra("title",datas.title);
                }
                activity.startActivity(intent);
            }else if (datas.operateType.equals("DLHomeRedirect")){
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(activity, LoginActivity.class);
                }else {
                    if (datas.domainName.equals("dd_mobile")){
                        intent.setClass(activity, DaochuWebActivity.class);
                        intent.putExtra("url",datas.url);
                        intent.putExtra("title",datas.title);
                        intent.putExtra("isRefresh",datas.isRefresh);
                        intent.putExtra("isStatusBar",datas.isStatusBar);
                        intent.putExtra("domainName",datas.domainName);
                    }else if (datas.domainName.equals("hy_mobile")){
                        intent.setClass(activity, WebViewOneActivity.class);
                        intent.putExtra("url",datas.url);
                        intent.putExtra("title",datas.title);
                    }
                }
                activity.startActivity(intent);
            }else if (datas.operateType.equals("DLHomeCourseScore")){
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(activity, LoginActivity.class);
                }else {
                    intent.setClass(activity, PingjiaoActivity.class);
                }
                activity.startActivity(intent);
            }else if (datas.operateType.equals("assistantCall")){
                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                    intent.setClass(activity, LoginActivity.class);
                }else {
                    intent.setClass(activity, InstructorActivity.class);
                }
                activity.startActivity(intent);
            }
        activity.overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
    @JavascriptInterface
    public void operateTypeV2(String url){
        Datas datas= JSON.parseObject(url, Datas.class);
        Intent intent=new Intent();
        if (datas.operateType.equals("DLHomeReserveNoRefrash1")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity, LoginActivity.class);
            }else {
                intent.setClass(activity, WebViewOneActivity.class);
                intent.putExtra("url",datas.url);
                intent.putExtra("title",datas.title);
            }
            activity.startActivity(intent);
        }else if (datas.operateType.equals("DLHomeReserveNoRefrash2")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity, LoginActivity.class);
            }else {
                intent.setClass(activity, WebViewOneActivity.class);
                intent.putExtra("url",datas.url);
                intent.putExtra("title",datas.title);
            }
            activity.startActivity(intent);
        }else if (datas.operateType.equals("DLHomeReserveNoRefrash3")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity, LoginActivity.class);
            }else {
                intent.setClass(activity, WebViewOneActivity.class);
                intent.putExtra("url",datas.url);
                intent.putExtra("title",datas.title);
            }
            activity.startActivity(intent);
        }else  if (datas.operateType.equals("DLHomeReserveNoRefrash4")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity, LoginActivity.class);
            }else {
                intent.setClass(activity, WebViewOneActivity.class);
                intent.putExtra("url",datas.url);
                intent.putExtra("title",datas.title);
            }
            activity.startActivity(intent);
        }else if(datas.operateType.equals("DLHomeReserveNoRefrash5")){
            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                intent.setClass(activity, LoginActivity.class);
            }else {
                intent.setClass(activity, WebViewOneActivity.class);
                intent.putExtra("url",datas.url);
                intent.putExtra("title",datas.title);
            }
            activity.startActivity(intent);
        }
    }
    @JavascriptInterface
    public void backNative(){
        activity.finish();
        activity.overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
    }
    @JavascriptInterface
    public void errorHandle(){
        Intent intent=new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }
}
