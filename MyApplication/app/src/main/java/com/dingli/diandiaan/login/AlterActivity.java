package com.dingli.diandiaan.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dingli.diandiaan.MainActivity;
import com.dingli.diandiaan.MaintiAcvity;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Result;
import com.dingli.diandiaan.common.ResultInfo;
import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.setting.BindPhoneActivity;
import com.dingli.diandiaan.setting.SettingActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/7.
 */
public class AlterActivity extends BaseActivity implements View.OnClickListener{

    Button btrequire;
    EditText etnew,etnewagain,etoldpd;
    ImageView xgback;
    HttpHeaders headers;
    String login;
    String  luyou;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter);
        DianTool.huoqutoken();
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
        init();
        String accout=DianApplication.sharedPreferences.getStringValue(Constant.USER_ACCOUNT);
        if (!TextUtils.isEmpty(accout)) {
            initdata(accout);
        }
    }
    public void init(){
        etnew=(EditText)findViewById(R.id.etnew);
        etnewagain=(EditText)findViewById(R.id.etnewagain);
        btrequire=(Button)findViewById(R.id.btrequire);
        xgback=(ImageView)findViewById(R.id.xgback);
        ImageView xgbacks=(ImageView)findViewById(R.id.xgbacks);
        TextView tvjinggao=(TextView)findViewById(R.id.tvjinggao);
        String back=getIntent().getStringExtra("back");
        login=getIntent().getStringExtra("login");
        if (TextUtils.isEmpty(back)){
            xgback.setVisibility(View.VISIBLE);
            xgbacks.setVisibility(View.GONE);
            tvjinggao.setText("");
        }else{
            xgback.setVisibility(View.GONE);
            xgbacks.setVisibility(View.VISIBLE);
            tvjinggao.setText("提示:请修改初始密码,才能登录");
        }
        etoldpd=(EditText)findViewById(R.id.etoldpd);
        btrequire.setOnClickListener(this);
        xgback.setOnClickListener(this);
    }
    void initdata(String accout){
        if (DianTool.isConnectionNetWork(this)) {
            HttpParams params = new HttpParams();
            params.put("account", accout);
            OkGo.get(HostAdress.getRequestUrl("/api/web/v1/users/checkuserisexist")).tag(this)
                    .params(params).headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    if (!s.equals("{}")) {
                        Result result = JSON.parseObject(s, Result.class);
                        if (result.phone!=null) {
                           luyou="bangding";
                        }else{
                            luyou="nobind";
                        }
                    } else {
                        luyou="nobind";
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
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
                String jiumima=etoldpd.getText().toString().trim();
                String newmima=etnew.getText().toString().trim();
                final String newagain=etnewagain.getText().toString().trim();
                if(DianTool.isConnectionNetWork(AlterActivity.this)){
                    DianTool.showDialog(this,"");
                    if(TextUtils.isEmpty(jiumima)){
                        DianTool.dissMyDialog();
                        DianTool.showTextToast(AlterActivity.this,"旧密码不能为空");
                    }else if(!DianApplication.sharedPreferences.getStringValue(Constant.USER_PASSWORD).equals(jiumima)){
                        DianTool.dissMyDialog();
                        DianTool.showTextToast(AlterActivity.this,"旧密码错误");
                    }else if(TextUtils.isEmpty(newmima)){
                        DianTool.dissMyDialog();
                        DianTool.showTextToast(AlterActivity.this,"新密码不能为空");
                    }else if(TextUtils.isEmpty(newagain)){
                        DianTool.dissMyDialog();
                        DianTool.showTextToast(AlterActivity.this,"再次输入新密码不能为空");
                    }else if(!newmima.equals(newagain)){
                        DianTool.dissMyDialog();
                        DianTool.showTextToast(AlterActivity.this,"新密码不一致");
                    }else if(newmima.equals(jiumima)){
                        DianTool.dissMyDialog();
                        DianTool.showTextToast(AlterActivity.this,"新密码不能与旧密码一致");
                    }else if (newmima.equals("123456")){
                        DianTool.dissMyDialog();
                        DianTool.showTextToast(AlterActivity.this,"新密码过于简单,请重设");
                    }else if(newmima.getBytes().length<6) {
                        DianTool.dissMyDialog();
                        DianTool.showTextToast(AlterActivity.this,"密码不能小于6位");
                    }else if(newagain.getBytes().length<6) {
                        DianTool.dissMyDialog();
                        DianTool.showTextToast(AlterActivity.this,"密码不能小于6位");
                    }else{
                        HttpParams params=new HttpParams();
                        params.put("newpassword",newmima);
                        params.put("oldpassword", jiumima);
                        OkGo.post(HostAdress.getRequestUrl("/api/web/v1/users/changepasswordpost")).tag(this)
                                .headers(headers).params(params).execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                DianTool.dissMyDialog();
                                DianApplication.user.libiao=null;
                                DianApplication.sharedPreferences.saveString(Constant.USER_PASSWORD, "");
                                DianTool.showTextToast(AlterActivity.this, "修改密码成功");
                                Intent intent=new Intent();
                                if (TextUtils.isEmpty(login)) {
                                    DianTool.kongtoken();
                                    intent.setClass(AlterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    for (int i = 0; i < DianApplication.activityList.size(); i++) {
                                        if (DianApplication.activityList.get(i) != DianApplication.user.login) {
                                            DianApplication.activityList.get(i).finish();
                                        }
                                    }
                                }else{
                                    DianApplication.sharedPreferences.saveString(Constant.USER_PASSWORD, newagain);
                                    if (luyou.equals("nobind")) {
                                        intent.setClass(AlterActivity.this, BindPhoneActivity.class);
                                        intent.putExtra("bind","bind");
                                        startActivity(intent);
                                    }else{
                                        intent.setClass(AlterActivity.this, MaintiAcvity.class);
                                        startActivity(intent);
                                    }
                                    AlterActivity.this.finish();
                                }
                                overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                DianTool.dissMyDialog();
                                DianTool.showTextToast(AlterActivity.this, "修改密码失败");
                            }
                        });
                    }
                }else{
                    DianTool.showTextToast(AlterActivity.this, "网络出错,请检查一下网络");
                }
                break;
            case R.id.xgback:
                Intent inedt=new Intent(this, SettingActivity.class);
                startActivity(inedt);
                AlterActivity.this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
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
