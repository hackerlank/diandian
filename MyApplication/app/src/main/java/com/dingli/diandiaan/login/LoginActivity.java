package com.dingli.diandiaan.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dingli.diandiaan.MainActivity;
import com.dingli.diandiaan.MaintiAcvity;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.YiDaoTuActivity;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Result;
import com.dingli.diandiaan.common.ResultInfo;
import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.setting.ForgetActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/7.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    EditText etphoneid,etpassowrd;
    Button btlogin;
    TextView tvforgert;
     String phone;
    String password;
    ResultInfo resultInfo;
    HttpHeaders headers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DianApplication.user.login=this;
         init();
         headers=new HttpHeaders();
}
    public void init(){
        tvforgert=(TextView)findViewById(R.id.tvforgert);
        btlogin=(Button)findViewById(R.id.btlogin);
        etphoneid=(EditText)findViewById(R.id.etphoneid);
        etpassowrd=(EditText)findViewById(R.id.etpassowrd);
        String accounts=DianApplication.sharedPreferences.getStringValue(Constant.USER_ACCOUNT);
        String password=DianApplication.sharedPreferences.getStringValue(Constant.USER_PASSWORD);
        if(!TextUtils.isEmpty(accounts)){
            etphoneid.setText(accounts);
        }
        if(!TextUtils.isEmpty(password)){
            etpassowrd.setText(password);
        }
        btlogin.setOnClickListener(this);
        tvforgert.setOnClickListener(this);
    }
    void yanzhen(){
        if (DianTool.isConnectionNetWork(this)) {
            HttpHeaders headers = new HttpHeaders();
            headers.put("Content-Type", Constant.APPLICATION_FORMURL);
            headers.put("Encoding", "UTF-8");
            headers.put("Accept", Constant.APPLICATION_JSON);
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.getInstance().addCommonHeaders(headers);
            OkGo.get(HostAdress.getLiQ("/api/phone/v1/user/info")).tag(this).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    DianTool.dissMyDialog();
                    DianApplication.sharedPreferences.saveString(Constant.INFO,s);
                    btlogin.setClickable(true);
                    if (!TextUtils.isEmpty(s)) {
                        ResultInfo resultInfo = JSON.parseObject(s, ResultInfo.class);
                        DianApplication.sharedPreferences.saveString(Constant.ROLE, resultInfo.role);
                        if (resultInfo.role.equals("ROLE_TEACHER")) {
                            DianApplication.sharedPreferences.saveString(Constant.ORGAINER, resultInfo.organName);
                            if (password.equals("123456")) {
                                DianTool.dissMyDialog();
                                Intent intent = new Intent(LoginActivity.this, FirstAlterActivity.class);
                                intent.putExtra("accout", phone);
                                intent.putExtra(Constant.PANDUAN, "no");
                                startActivity(intent);
                            } else {
                                Intent intenttwo = new Intent(LoginActivity.this, MaintiAcvity.class);
                                startActivity(intenttwo);
                                for (int i = 0; i < DianApplication.activityList.size(); i++) {
                                    DianApplication.activityList.get(i).finish();
                                }
                            }
                            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                        } else if (resultInfo.role.equals("ROLE_CLASSROOMTEACHE")) {
                            DianApplication.sharedPreferences.saveString(Constant.ORGAINER, resultInfo.organName);
                            if (password.equals("123456")) {
                                DianTool.dissMyDialog();
                                Intent intent = new Intent(LoginActivity.this, FirstAlterActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intenttwo = new Intent(LoginActivity.this, MaintiAcvity.class);
                                startActivity(intenttwo);
                                for (int i = 0; i < DianApplication.activityList.size(); i++) {
                                    DianApplication.activityList.get(i).finish();
                                }
                            }
                            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                        } else {
                            DianTool.kongtoken();
                            DianApplication.sharedPreferences.saveString(Constant.ROLE, "");
                            DianTool.showTextToast(LoginActivity.this, "这不是老师账号,请输入老师账号!");
                        }
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    DianTool.dissMyDialog();
                    DianTool.kongtoken();
                    btlogin.setClickable(true);
                    if (response != null) {
                        if (response.code() == 400) {
                            DianTool.showTextToast(LoginActivity.this, "账号或密码错误");
                        } else {
                            DianTool.showTextToast(LoginActivity.this, "登录失败");
                        }
                    }else{
                        DianTool.showTextToast(LoginActivity.this, "登录失败");
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btlogin:
                HttpParams params=new HttpParams();
                phone=etphoneid.getText().toString().trim();
                password=etpassowrd.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    DianTool.showTextToast(LoginActivity.this,getResources().getString(R.string.shoukong));
                }else if(TextUtils.isEmpty(password)){
                    DianTool.showTextToast(LoginActivity.this,getResources().getString(R.string.mikong));
                }else if(DianTool.isConnectionNetWork(LoginActivity.this)) {
                    btlogin.setClickable(false);
                    DianTool.showDialog(this,"");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btlogin.setClickable(true);
                            DianTool.dissMyDialog();
                        }
                    },10000);
                    params.put("username", phone);
                    params.put("password", password);
                    params.put("grant_type", "password");
                    params.put("scope", "read write");
                    params.put("client_secret", "mySecretOAuthSecret");
                    params.put("client_id", "dleduApp");
                    headers.put("Content-Type", Constant.APPLICATION_FORMURL);
                    headers.put("Encoding", "UTF-8");
                    headers.put("Accept", Constant.APPLICATION_JSON);
                    headers.put("Authorization", "Basic ZGxlZHVBcHA6bXlTZWNyZXRPQXV0aFNlY3JldA==");
                    OkGo.getInstance().addCommonHeaders(headers);
                    OkGo.post(HostAdress.getRequestUrl("/oauth/token")).tag(this).params(params).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            btlogin.setClickable(true);
                            if (!s.equals("{}")) {
                                resultInfo = JSON.parseObject(s, ResultInfo.class);
                                DianApplication.user.accounts = phone;
                                DianApplication.user.passwords = password;
                                DianApplication.user.token = resultInfo.access_token;
                                DianApplication.user.token_type = resultInfo.token_type;
                                DianApplication.user.refresh_token = resultInfo.refresh_token;
                                String[] strings = resultInfo.access_token.split("-");
                                DianApplication.sharedPreferences.saveString(Constant.SPLITONE, strings[0]);
                                DianApplication.sharedPreferences.saveString(Constant.SPLITTWO, strings[1]);
                                DianApplication.sharedPreferences.saveString(Constant.SPLITTHREE, strings[2]);
                                DianApplication.sharedPreferences.saveString(Constant.SPLITFOUR, strings[3]);
                                DianApplication.sharedPreferences.saveString(Constant.SPLITFIVE, strings[4]);
                                DianApplication.sharedPreferences.saveString(Constant.REFRESHED, resultInfo.refresh_token);
                                DianApplication.sharedPreferences.saveString(Constant.USER_PASSWORD, password);
                                DianApplication.sharedPreferences.saveString(Constant.DATA_TOKEN, resultInfo.access_token);
                                DianApplication.sharedPreferences.saveString(Constant.USER_ACCOUNT, phone);
                                DianApplication.sharedPreferences.saveString(Constant.USER_TOKEN, resultInfo.token_type);
                                    yanzhen();
                            }else{
                                DianTool.dissMyDialog();
                                DianTool.kongtoken();
                                DianTool.showTextToast(LoginActivity.this, "账号或密码错误");
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            DianTool.dissMyDialog();
                            btlogin.setClickable(true);
                            DianTool.kongtoken();
                            if(response!=null) {
                                if (response.code()==400) {
                                    DianTool.showTextToast(LoginActivity.this, "账号或密码错误");
                                }else{
                                    DianTool.showTextToast(LoginActivity.this, "登录失败");
                                }
                            }else{
                                DianTool.showTextToast(LoginActivity.this, "登录失败");
                            }
                        }
                    });
                }else{
                    DianTool.showTextToast(LoginActivity.this, getResources().getString(R.string.jianwang));
                }
                break;
            case R.id.tvforgert:
                Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
        }
    }
}
