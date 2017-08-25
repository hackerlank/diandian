package com.dingli.diandiaan.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.dingli.diandiaan.firstpage.FirstPageFragment;
import com.dingli.diandiaan.setting.BindPhoneActivity;
import com.dingli.diandiaan.setting.SettingActivity;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/7.
 */
public class FirstAlterActivity extends BaseActivity implements View.OnClickListener{

    Button btrequire;
    EditText etnew,etnewagain,etoldpd,etbindphone,codete;
    ImageView xgback;
    HttpHeaders headers;
    String  luyou;
   VerticalSwipeRefreshLayout refalter;
    LinearLayout llbds;
    TextView tvbindsend;
    TimeCount waitTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstalter);
        DianTool.huoqutoken();
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
        init();
        String accout=DianApplication.sharedPreferences.getStringValue(Constant.USER_ACCOUNT);
        refalter.setRefreshing(true);
        if (!TextUtils.isEmpty(accout)) {
            initdata(accout);
        }
        waitTime=new TimeCount(60000,1000);
    }
    public void init(){
        llbds=(LinearLayout)findViewById(R.id.llbdsp);
        codete=(EditText)findViewById(R.id.codeet);
        etbindphone=(EditText)findViewById(R.id.etbindphone);
        tvbindsend=(TextView)findViewById(R.id.tvbindsend);
        refalter=(VerticalSwipeRefreshLayout)findViewById(R.id.reffirstalter);
        refalter.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
        refalter.setEnabled(false);
        etnew=(EditText)findViewById(R.id.etnew);
        etnewagain=(EditText)findViewById(R.id.etnewagain);
        btrequire=(Button)findViewById(R.id.btrequire);
        xgback=(ImageView)findViewById(R.id.alertback);
        etoldpd=(EditText)findViewById(R.id.etoldpd);
        tvbindsend.setOnClickListener(this);
        btrequire.setOnClickListener(this);
        xgback.setOnClickListener(this);
        tvbindsend.setClickable(false);
        tvbindsend.setTextColor(getResources().getColor(R.color.bg_White));
        tvbindsend.setBackgroundResource(R.drawable.check_btn_not_click);
        etbindphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String wenzi=etbindphone.getText().toString().trim();
                if (TextUtils.isEmpty(wenzi)){
                    tvbindsend.setClickable(false);
                    tvbindsend.setBackgroundResource(R.drawable.check_btn_not_click);
                    tvbindsend.setTextColor(getResources().getColor(R.color.bg_White));
                }else{
                    tvbindsend.setClickable(true);
                    tvbindsend.setBackgroundResource(R.drawable.circle_jieshuocorner);
                    tvbindsend.setTextColor(getResources().getColor(R.color.qianblue));
                }
            }
        });
        codete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String wenzi=codete.getText().toString().trim();
                if (TextUtils.isEmpty(wenzi)){
                    btrequire.setClickable(false);
                    btrequire.setBackgroundResource(R.drawable.check_btn_not_click);
                }else{
                    btrequire.setClickable(true);
                    btrequire.setBackgroundResource(R.drawable.login_click);
                }
            }
        });
    }
    void initdata(String accout){
        if (DianTool.isConnectionNetWork(this)) {
            HttpParams params = new HttpParams();
            params.put("account", accout);
            OkGo.get(HostAdress.getRequestUrl("/api/web/v1/users/checkuserisexist")).tag(this)
                    .params(params).headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    refalter.setRefreshing(false);
                    refalter.setEnabled(false);
                    if (!s.equals("{}")) {
                        Result result = JSON.parseObject(s, Result.class);
                        if (result.phone!=null) {
                            luyou="bangding";
                            llbds.setVisibility(View.GONE);
                        }else{
                            luyou="nobind";
                            llbds.setVisibility(View.VISIBLE);
                        }
                    } else {
                        luyou="nobind";
                        llbds.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    refalter.setRefreshing(false);
                    refalter.setEnabled(false);
                    luyou="nobind";
                }
            });
        } else {
            DianTool.showTextToast(this,"请检查网络");
            luyou="nobind";
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btrequire:
                  if (luyou.equals("bangding")){
                      initwuphone();
                  }else {
                      initbinds();
                  }
                break;
            case R.id.alertback:
                DianTool.kongtoken();
                FirstAlterActivity.this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
            case R.id.tvbindsend:
                initsend();
                break;
        }
    }
    void initwuphone(){
        String jiumima=etoldpd.getText().toString().trim();
        String newmima=etnew.getText().toString().trim();
        final String newagain=etnewagain.getText().toString().trim();
        if(DianTool.isConnectionNetWork(this)){
            DianTool.showDialog(this,"");
            if(TextUtils.isEmpty(jiumima)){
                DianTool.dissMyDialog();
                DianTool.showTextToast(this,"旧密码不能为空");
            }else if(!DianApplication.sharedPreferences.getStringValue(Constant.USER_PASSWORD).equals(jiumima)){
                DianTool.dissMyDialog();
                DianTool.showTextToast(this,"旧密码错误");
            }else if(TextUtils.isEmpty(newmima)){
                DianTool.dissMyDialog();
                DianTool.showTextToast(this,"新密码不能为空");
            }else if(TextUtils.isEmpty(newagain)){
                DianTool.dissMyDialog();
                DianTool.showTextToast(this,"再次输入新密码不能为空");
            }else if(!newmima.equals(newagain)){
                DianTool.dissMyDialog();
                DianTool.showTextToast(this,"新密码不一致");
            }else if(newmima.equals(jiumima)){
                DianTool.dissMyDialog();
                DianTool.showTextToast(this,"新密码不能与旧密码一致");
            }else if (newmima.equals("123456")){
                DianTool.dissMyDialog();
                DianTool.showTextToast(this,"新密码过于简单,请重设");
            }else if(newmima.getBytes().length<6) {
                DianTool.dissMyDialog();
                DianTool.showTextToast(this,"密码不能小于6位");
            }else if(newagain.getBytes().length<6) {
                DianTool.dissMyDialog();
                DianTool.showTextToast(this,"密码不能小于6位");
            }else{
                HttpParams params=new HttpParams();
                params.put("newpassword",newmima);
                params.put("oldpassword", jiumima);
                OkGo.post(HostAdress.getRequestUrl("/api/web/v1/users/changepasswordpost")).tag(this)
                        .headers(headers).params(params).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        DianTool.dissMyDialog();
                        DianApplication.sharedPreferences.saveString(Constant.USER_PASSWORD, "");
                        DianTool.showTextToast(FirstAlterActivity.this, "修改密码成功");
                        Intent intent=new Intent();
                            DianTool.kongtoken();
                            intent.setClass(FirstAlterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            for (int i = 0; i < DianApplication.activityList.size(); i++) {
                                if (DianApplication.activityList.get(i) != DianApplication.user.login) {
                                    DianApplication.activityList.get(i).finish();
                                }
                            }
                        overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        DianTool.dissMyDialog();
                        DianTool.showTextToast(FirstAlterActivity.this, "修改密码失败");
                    }
                });
            }
        }else{
            DianTool.showTextToast(this, "网络出错,请检查一下网络");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
    void initbinds(){
        String cophone = etbindphone.getText().toString().trim();
        String code = codete.getText().toString().trim();
        String jiumima=etoldpd.getText().toString().trim();
        String newmima=etnew.getText().toString().trim();
        String newagain=etnewagain.getText().toString().trim();
        if (TextUtils.isEmpty(cophone)) {
            DianTool.showTextToast(this, getResources().getString(R.string.shoukong));
        } else if (TextUtils.isEmpty(code)) {
            DianTool.showTextToast(this, getResources().getString(R.string.yankong));
        }else if(TextUtils.isEmpty(jiumima)){
            DianTool.showTextToast(FirstAlterActivity.this,"旧密码不能为空");
        }else if(!DianApplication.sharedPreferences.getStringValue(Constant.USER_PASSWORD).equals(jiumima)){
            DianTool.showTextToast(FirstAlterActivity.this,"旧密码错误");
        }else if(TextUtils.isEmpty(newmima)){
            DianTool.showTextToast(FirstAlterActivity.this,"新密码不能为空");
        }else if(TextUtils.isEmpty(newagain)){
            DianTool.showTextToast(FirstAlterActivity.this,"再次输入新密码不能为空");
        }else if(!newmima.equals(newagain)){
            DianTool.showTextToast(FirstAlterActivity.this,"新密码不一致");
        }else if(newmima.equals(jiumima)){
            DianTool.showTextToast(FirstAlterActivity.this,"新密码不能与旧密码一致");
        }else if (newmima.equals("123456")){
            DianTool.showTextToast(this,"新密码设置过于简单,请重设");
        }else if(newmima.getBytes().length<6) {
            DianTool.showTextToast(FirstAlterActivity.this,"密码不能小于6位");
        }else if(newagain.getBytes().length<6) {
            DianTool.showTextToast(FirstAlterActivity.this,"密码不能小于6位");
        }else if (DianTool.isConnectionNetWork(this)) {
            DianTool.showDialog(this, "");
            HttpParams paramsre = new HttpParams();
            paramsre.put("phone", cophone);
            paramsre.put("code", code);
            paramsre.put("module", "dd_bp");
            paramsre.put("oldpassword",jiumima);
            paramsre.put("newpassword",newmima);
            OkGo.getInstance().addCommonHeaders(headers);
            OkGo.post(HostAdress.getRequestUrl("/api/web/v1/users/changepasswordAndBindPhone")).tag(this).
                    params(paramsre).headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    DianTool.dissMyDialog();
                    if (!TextUtils.isEmpty(s)){
                        Result result=JSON.parseObject(s,Result.class);
                        String codes = result.code.substring(0, 4);
                        if (codes.equals("4002")) {
                            DianTool.showTextToast(FirstAlterActivity.this, "验证码错误");
                        } else if (result.code.equals("40001001")) {
                            DianTool.showTextToast(FirstAlterActivity.this, "此手机号码已经绑定过");
                        } else if (result.code.equals("40001014")) {
                            DianTool.showTextToast(FirstAlterActivity.this, "该账号已绑定过手机");
                        }
                    }else {
                        DianApplication.sharedPreferences.saveString(Constant.USER_PASSWORD, "");
                        DianTool.showTextToast(FirstAlterActivity.this, "修改密码成功");
                        Intent intent = new Intent();
                        intent.setClass(FirstAlterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        for (int i = 0; i < DianApplication.activityList.size(); i++) {
                            if (DianApplication.activityList.get(i) != DianApplication.user.login) {
                                DianApplication.activityList.get(i).finish();
                            }
                        }
                        overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    DianTool.dissMyDialog();
                    DianTool.showTextToast(FirstAlterActivity.this,"修改密码失败");
                }
            });
        } else {
            DianTool.dissMyDialog();
            DianTool.showTextToast(this, getResources().getString(R.string.jianwang));
        }
    }
    void initsend(){
        String etphone = etbindphone.getText().toString().trim();
        if (TextUtils.isEmpty(etphone)) {
            DianTool.showTextToast(this, getResources().getString(R.string.shoukong));
        } else if (DianTool.isConnectionNetWork(this)) {
                waitTime.start();
                HttpParams params = new HttpParams();
                params.put("phone", etphone);
                params.put("module", "dd_bp");
                OkGo.getInstance().addCommonHeaders(headers);
                OkGo.post(HostAdress.getRequestUrl("/api/web/v1/users/sendmessagecodebak"))
                        .tag(this).params(params).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        DianTool.showTextLongToast(FirstAlterActivity.this, getResources().getString(R.string.fagong));
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        DianTool.showTextToast(FirstAlterActivity.this, "手机号已绑定，请换个手机号");
                    }
                });
        } else {
            DianTool.showTextToast(FirstAlterActivity.this, getResources().getString(R.string.jianwang));
        }
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
//            tvbindsend.setBackgroundResource(R.drawable.check_btn_not_click);
            tvbindsend.setBackgroundResource(R.drawable.code_click);
            tvbindsend.setTextColor(getResources().getColor(R.color.bg_White));
            tvbindsend.setText("(" + millisUntilFinished / 1000 + "S)"+getResources().getString(R.string.chongfa));
        }
    }
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(event.getAction()==KeyEvent.ACTION_DOWN
                &&KeyEvent.KEYCODE_BACK==keyCode){
            return false;
        }
        return super.onKeyDown(keyCode,event);
    }
}
