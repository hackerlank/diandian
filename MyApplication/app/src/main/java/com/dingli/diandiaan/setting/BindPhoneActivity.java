package com.dingli.diandiaan.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.MaintiAcvity;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Result;
import com.dingli.diandiaan.common.ResultInfo;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.lang.Override;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/7.
 */
public class BindPhoneActivity extends BaseActivity {

    TextView tvbindsend;
   EditText etbindphone,codete;
    ImageView bindphoneback;
    Button donerequire;
    HttpHeaders headers;
    TimeCount waitTime;
    String bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindphone);
        DianTool.huoqutoken();
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        waitTime=new TimeCount(60000,1000);
        init();
    }
    public void init(){
        codete=(EditText)findViewById(R.id.codete);
        tvbindsend=(TextView)findViewById(R.id.tvbindsend);
        etbindphone=(EditText)findViewById(R.id.etbindphone);
        bindphoneback=(ImageView)findViewById(R.id.bindphoneback);
        ImageView bindphonebacks=(ImageView)findViewById(R.id.bindphonebacks);
        bind=getIntent().getStringExtra("bind");
        if (TextUtils.isEmpty(bind)){
            bindphoneback.setVisibility(View.VISIBLE);
            bindphonebacks.setVisibility(View.GONE);
        }else{
            bindphonebacks.setVisibility(View.VISIBLE);
            bindphoneback.setVisibility(View.GONE);
        }
        donerequire=(Button)findViewById(R.id.donerequire);
       tvbindsend.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String etphone = etbindphone.getText().toString().trim();
               if (TextUtils.isEmpty(etphone)) {
                   DianTool.showTextToast(BindPhoneActivity.this, getResources().getString(R.string.shoukong));
               } else if (DianTool.isConnectionNetWork(BindPhoneActivity.this)) {
                       waitTime.start();
                       HttpParams params = new HttpParams();
                       params.put("phone", etphone);
                       params.put("module", "dd_bp");
                       OkGo.getInstance().addCommonHeaders(headers);
                       OkGo.post(HostAdress.getRequestUrl("/api/web/v1/users/sendmessagecodebak"))
                               .tag(this).params(params).execute(new StringCallback() {
                           @Override
                           public void onSuccess(String s, Call call, Response response) {
                               DianTool.showTextLongToast(BindPhoneActivity.this, getResources().getString(R.string.fagong));
                           }
                           @Override
                           public void onError(Call call, Response response, Exception e) {
                               DianTool.showTextToast(BindPhoneActivity.this, "手机号已绑定，请换个手机号");
                           }
                       });

               } else {
                   DianTool.showTextToast(BindPhoneActivity.this, getResources().getString(R.string.jianwang));
               }
           }
       });
        bindphoneback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BindPhoneActivity.this.finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
            }
        });
        donerequire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DianTool.showDialog(BindPhoneActivity.this,"");
                final String cophone = etbindphone.getText().toString().trim();
                final String code = codete.getText().toString().trim();
                if (TextUtils.isEmpty(cophone)) {
                    DianTool.dissMyDialog();
                    DianTool.showTextToast(BindPhoneActivity.this, getResources().getString(R.string.shoukong));
                } else if (TextUtils.isEmpty(code)) {
                    DianTool.dissMyDialog();
                    DianTool.showTextToast(BindPhoneActivity.this, getResources().getString(R.string.yankong));
                } else if (DianTool.isConnectionNetWork(BindPhoneActivity.this)) {
                    HttpParams paramsre = new HttpParams();
                    paramsre.put("phone", cophone);
                    paramsre.put("code", code);
                    paramsre.put("module", "dd_bp");
                    headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
                    OkGo.getInstance().addCommonHeaders(headers);
                    OkGo.post(HostAdress.getRequestUrl("/api/web/v1/users/bindingphonebak")).tag(this).
                            params(paramsre).headers(headers).execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            DianTool.dissMyDialog();
                            if (!TextUtils.isEmpty(s)) {
                                Result result = JSON.parseObject(s, Result.class);
                                Intent intent = new Intent();
                                if (result.msg.equals("ok")) {
                                    DianTool.showTextToast(BindPhoneActivity.this, "绑定成功");
                                    DianApplication.sharedPreferences.saveString(Constant.BINGDING, cophone.substring(0, 3) + "****" + cophone.substring(7, 11));
                                    if (TextUtils.isEmpty(bind)) {
                                        BindPhoneActivity.this.finish();
                                        overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                                    } else {
                                        intent.setClass(BindPhoneActivity.this, MaintiAcvity.class);
                                        startActivity(intent);
                                        BindPhoneActivity.this.finish();
                                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                                    }
                                } else if (result.code.equals("200")) {
                                    DianTool.showTextToast(BindPhoneActivity.this, "绑定成功");
                                    DianApplication.sharedPreferences.saveString(Constant.BINGDING, cophone.substring(0, 3) + "****" + cophone.substring(7, 11));
                                    if (TextUtils.isEmpty(bind)) {
                                        BindPhoneActivity.this.finish();
                                        overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                                    } else {
                                        intent.setClass(BindPhoneActivity.this, MaintiAcvity.class);
                                        startActivity(intent);
                                        BindPhoneActivity.this.finish();
                                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                                    }
                                } else {
                                    String codes = result.code.substring(0, 4);
                                    if (codes.equals("4002")) {
                                        DianTool.showTextToast(BindPhoneActivity.this, "验证码错误");
                                    } else if (result.code.equals("40001001")) {
                                        DianTool.showTextToast(BindPhoneActivity.this, "此手机号码已经绑定过");
                                    } else if (result.code.equals("40001014")) {
                                        DianTool.showTextToast(BindPhoneActivity.this, "该账号已绑定过手机");
                                    }
                                }
                            }
                        }
                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            DianTool.dissMyDialog();
                        }
                    });
                } else {
                    DianTool.dissMyDialog();
                    DianTool.showTextToast(BindPhoneActivity.this, getResources().getString(R.string.jianwang));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    private class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tvbindsend.setText(R.string.send);
            tvbindsend.setClickable(true);
            tvbindsend.setBackgroundResource(R.drawable.circle_jieshuocorner);
            tvbindsend.setTextColor(getResources().getColor(R.color.qianblue));
        }
        @Override
        public void onTick(long millisUntilFinished) {
            tvbindsend.setClickable(false);
            tvbindsend.setBackgroundResource(R.drawable.code_click);
            tvbindsend.setTextColor(getResources().getColor(R.color.bg_White));
            tvbindsend.setText("(" + millisUntilFinished / 1000 + "S)"+getResources().getString(R.string.chongfa));
        }
    }
}
