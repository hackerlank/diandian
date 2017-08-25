package com.dingli.diandiaan.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.DianTool;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/7.
 */
public class FindActivity extends BaseActivity {

    TextView donerequire,tvsendword;
    EditText  etphone;
    String etp;
    String code;
    String phones;
    EditText codeet;
    ImageView findpdback;
    TimeCount waitTime;
    HttpHeaders headers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        DianTool.huoqutoken();
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        waitTime=new TimeCount(60000,1000);
          init();
    }
    public void init(){
        findpdback=(ImageView)findViewById(R.id.findpdback);
        donerequire=(TextView)findViewById(R.id.donerequiref);
        etphone=(EditText)findViewById(R.id.etphone);
        tvsendword=(TextView)findViewById(R.id.tvsendword);
        etphone.setFocusable(false);
        etphone.setFocusableInTouchMode(false);
        codeet=(EditText)findViewById(R.id.codeet);
        Bundle bundle=getIntent().getExtras();
        final String phone=bundle.getString("account");
        etphone.setText(phone);
        tvsendword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etp = etphone.getText().toString().trim();
                if (TextUtils.isEmpty(etp)) {
                    DianTool.showTextToast(FindActivity.this, getResources().getString(R.string.shoukong));
                } else if (DianTool.isConnectionNetWork(FindActivity.this)) {
                        waitTime.start();
                        HttpParams params = new HttpParams();
                        params.put("phone", etp);
                        params.put("module", "dd_fp");
                        OkGo.post(HostAdress.getRequestUrl("/api/web/v1/users/sendmessagecodebak")).tag(this).headers(headers)
                                .params(params).execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                DianTool.showTextToast(FindActivity.this, getResources().getString(R.string.fagong));
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                DianTool.showTextToast(FindActivity.this, "手机号需要绑定");
                            }
                        });
                } else {
                    DianTool.showTextToast(FindActivity.this, getResources().getString(R.string.jianwang));
                }
            }
        });
        donerequire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code=codeet.getText().toString().trim();
                phones=etphone.getText().toString().trim();
                if(TextUtils.isEmpty(code)) {
                    DianTool.showTextToast(FindActivity.this, getResources().getString(R.string.yankong));
                }else if(TextUtils.isEmpty(phones)){
                    DianTool.showTextToast(FindActivity.this, getResources().getString(R.string.shoukong));
                }else {
                    if(DianTool.isConnectionNetWork(FindActivity.this)){
                       DianTool.showDialog(FindActivity.this, "");
                        HttpParams requestParams=new HttpParams();
                        requestParams.put("phone",phones);
                        requestParams.put("code",code);
                        requestParams.put("module","dd_fp");
                        OkGo.get(HostAdress.getRequestUrl("/api/web/v1/users/validmessagecode")).tag(this).headers(headers)
                                .params(requestParams).execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                DianTool.dissMyDialog();
                                if (!TextUtils.isEmpty(s)) {
                                    if (s.contains("true")) {
                                        Intent intent = new Intent(FindActivity.this, ModificationActivity.class);
                                        intent.putExtra("phone", phones);
                                        intent.putExtra("code", code);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                                    } else {
                                        DianTool.showTextToast(FindActivity.this, "验证码错误");
                                    }
                                }else{
                                    DianTool.showTextToast(FindActivity.this, "验证码错误");
                                }
                            }
                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                DianTool.dissMyDialog();
                                DianTool.showTextToast(FindActivity.this,"验证码或手机号错误");
                            }
                        });
                    }else{
                        DianTool.showTextToast(FindActivity.this, "请检查网络");
                    }
                }
            }
        });
        findpdback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindActivity.this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
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
            tvsendword.setText(R.string.send);
            tvsendword.setClickable(true);
            tvsendword.setBackgroundResource(R.color.qianblue);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvsendword.setClickable(false);
            tvsendword.setBackgroundResource(R.drawable.check_btn_not_click);
            tvsendword.setText("(" + millisUntilFinished / 1000 + "S)"+getResources().getString(R.string.chongfa));
        }
    }
}
