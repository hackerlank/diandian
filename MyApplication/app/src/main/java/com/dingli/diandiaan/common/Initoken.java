package com.dingli.diandiaan.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.login.LoginActivity;
import com.dingli.diandiaan.rollcall.SuiNewCallRollActivity;
import com.dingli.diandiaan.rollcall.WuKeChenActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/12/8.
 */
public class Initoken {
    public static void initoken(final Context activity){
        DianApplication.user.token="";
        DianApplication.sharedPreferences.saveString(Constant.DATA_TOKEN, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITONE, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITTWO, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITTHREE, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITFOUR, "");
        DianApplication.sharedPreferences.saveString(Constant.SPLITFIVE, "");
        DianApplication.sharedPreferences.saveString(Constant.DATA_TOKEN, "");
        DianApplication.sharedPreferences.saveString(Constant.USER_TOKEN, "");
        DianApplication.user.token_type="";
        if (DianTool.isConnectionNetWork(activity)){
            DianApplication.user.accounts=DianApplication.sharedPreferences.getStringValue(Constant.USER_ACCOUNT);
            DianApplication.user.passwords=DianApplication.sharedPreferences.getStringValue(Constant.USER_PASSWORD);
            DianApplication.user.refresh_token=DianApplication.sharedPreferences.getStringValue(Constant.REFRESHED);
            HttpParams params1=new HttpParams();
            HttpHeaders headers=new HttpHeaders();
            headers.put("Content-Type", Constant.APPLICATION_FORMURL);
            headers.put("Encoding", "UTF-8");
            headers.put("Accept", Constant.APPLICATION_JSON);
            headers.put("Authorization", "Basic ZGxlZHVBcHA6bXlTZWNyZXRPQXV0aFNlY3JldA==");
            params1.put("username", DianApplication.user.accounts);
            params1.put("password", DianApplication.user.passwords);
            params1.put("grant_type", "refresh_token");
            params1.put("refresh_token",DianApplication.user.refresh_token);
            params1.put("scope", "read write");
            params1.put("client_secret", "mySecretOAuthSecret");
            params1.put("client_id", "dleduApp");
            OkGo.getInstance().addCommonHeaders(headers);
            OkGo.post(HostAdress.getRequestUrl("/oauth/token"))     // 请求方式和请求url
                    .tag(activity).params(params1)                       // 请求的 tag, 主要用于取消对应的请求
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            // s 即为所需要的结果
                            ResultInfo resultInfotoken = JSON.parseObject(s, ResultInfo.class);
                            DianApplication.user.token = resultInfotoken.access_token;
                            DianApplication.user.token_type = resultInfotoken.token_type;
                            DianApplication.user.refresh_token=resultInfotoken.refresh_token;
                            String[] strings=resultInfotoken.access_token.split("-");
                            DianApplication.sharedPreferences.saveString(Constant.REFRESHED,resultInfotoken.refresh_token);
                            DianApplication.sharedPreferences.saveString(Constant.SPLITONE,strings[0]);
                            DianApplication.sharedPreferences.saveString(Constant.SPLITTWO,strings[1]);
                            DianApplication.sharedPreferences.saveString(Constant.SPLITTHREE,strings[2]);
                            DianApplication.sharedPreferences.saveString(Constant.SPLITFOUR,strings[3]);
                            DianApplication.sharedPreferences.saveString(Constant.SPLITFIVE,strings[4]);
                            DianApplication.sharedPreferences.saveString(Constant.DATA_TOKEN, resultInfotoken.access_token);
                            DianApplication.sharedPreferences.saveString(Constant.USER_TOKEN, resultInfotoken.token_type);
                        }
                        @Override
                        public void onError(Call call, Response response, Exception e) {
                           Intent intent=new Intent(activity, LoginActivity.class);
                                  intent.putExtra("initoken","token");
                                  activity.startActivity(intent);
                        }
                    });
        }else{
            DianTool.showTextToast(activity,"请检查网络");
        }
    }
    public static void SingId(final Context activity){
        if (DianTool.isConnectionNetWork(activity)){
            final List<Integer> alist=new ArrayList<>();
            DianApplication.user.token_type=DianApplication.sharedPreferences.getStringValue(Constant.USER_TOKEN);
            DianApplication.user.token=DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE)+"-"+
                    DianApplication.sharedPreferences.getStringValue(Constant.SPLITTWO)+"-"+DianApplication.sharedPreferences.getStringValue(Constant.SPLITTHREE)
                    +"-"+DianApplication.sharedPreferences.getStringValue(Constant.SPLITFOUR)+"-"+DianApplication.sharedPreferences.getStringValue(Constant.SPLITFIVE);
            HttpParams params = new HttpParams();
            HttpHeaders headers=new HttpHeaders();
            params.put("offset", 1);
            params.put("teachTime","");
            params.put("limit",500);
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/teacher/course/getList")).tag(activity).headers(headers)
                    .params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    ResultInfo resultInfo = JSON.parseObject(s, ResultInfo.class);
                    Intent intent=new Intent();
                    if (resultInfo.data[0].courseList == null ||resultInfo.data[0].courseList.size()==0) {
//                        DianTool.showTextToast(activity,"当天没有课程");
                        intent.setClass(activity, WuKeChenActivity.class);
                        activity.startActivity(intent);
                    }else{
                        for (int i=0;i<resultInfo.data[0].courseList.size();i++){
                            if (resultInfo.data[0].courseList.get(i).classrommRollCall==true){
                                alist.add(i);
                            }
                        }
                        if (alist.size()==1){
                            intent.setClass(activity, SuiNewCallRollActivity.class);
                            intent.putExtra(Constant.KE_ID, resultInfo.data[0].courseList.get(alist.get(0)).id);
                            intent.putExtra(Constant.COURSE_NAME,resultInfo.data[0].courseList.get(alist.get(0)).courseName);
                            intent.putExtra(Constant.ROLLCALL,resultInfo.data[0].courseList.get(alist.get(0)).rollCall);
                            activity.startActivity(intent);
                        }else if (alist.size()>1){
                            DianTool.showTextToast(activity,"当天有重复课程");
                        }else{
                            intent.setClass(activity, WuKeChenActivity.class);
                            activity.startActivity(intent);
                        }
                    }
                }
            });
        }else{
            DianTool.showTextToast(activity, "请检查网络");
        }
    }
}
