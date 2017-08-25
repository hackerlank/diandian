package com.dingli.diandiaan.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Result;
import com.dingli.diandiaan.login.AlterActivity;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.login.LoginActivity;
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
public class SettingActivity extends BaseActivity implements View.OnClickListener{

    TextView lloutlogin,tvshoujihm,banbenmin,huancunshu;
    LinearLayout llalter;
    LinearLayout llxiazai;
    View viset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        initdata();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.BINGDING))){
            tvshoujihm.setText(DianApplication.sharedPreferences.getStringValue(Constant.BINGDING));
        }
    }

    public void init(){
        banbenmin=(TextView)findViewById(R.id.banbenmin);
        llalter=(LinearLayout)findViewById(R.id.llalter);
        LinearLayout llbindphone=(LinearLayout)findViewById(R.id.llbindphone);
        lloutlogin=(TextView)findViewById(R.id.outlogin);
        ImageView settingback=(ImageView)findViewById(R.id.settingback);
        tvshoujihm=(TextView)findViewById(R.id.tvshoujihm);
        LinearLayout llhelpcenter=(LinearLayout)findViewById(R.id.llhelpcenter);
        huancunshu=(TextView) findViewById(R.id.huancundaxiao);
        viset=findViewById(R.id.viset);
        try {
            if (DataCleanManager.getTotalCacheSize(this).equals("0K")){
                huancunshu.setText("");
            }else {
                huancunshu.setText(DataCleanManager.getTotalCacheSize(this));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        llhelpcenter.setOnClickListener(this);
        llalter.setOnClickListener(this);
        llbindphone.setOnClickListener(this);
        lloutlogin.setOnClickListener(this);
        settingback.setOnClickListener(this);
        banbenmin.setText("V" + DianApplication.code);
        llxiazai=(LinearLayout)findViewById(R.id.llxiazai);
        llxiazai.setOnClickListener(this);
    }
  void initdata(){
      if (DianTool.isConnectionNetWork(SettingActivity.this)) {
          DianApplication.user.accounts=DianApplication.sharedPreferences.getStringValue(Constant.USER_ACCOUNT);
         HttpParams params = new HttpParams();
          params.put("account", DianApplication.user.accounts);
          OkGo.get(HostAdress.getRequestUrl("/api/web/v1/users/checkuserisexist")).tag(this).params(params).execute(new StringCallback() {
              @Override
              public void onSuccess(String s, Call call, Response response) {
                  if (s.equals("{}")) {
                      tvshoujihm.setText("未绑定");
                  } else {
                      Result result = JSON.parseObject(s, Result.class);
                      if(result.phone!=null) {
                          tvshoujihm.setText(result.phone.substring(0, 3) + "****" + result.phone.substring(7, 11));
                          DianApplication.sharedPreferences.saveString(Constant.BINGDING,result.phone.substring(0, 3) + "****" + result.phone.substring(7, 11));
                      }else{
                          tvshoujihm.setText("未绑定");
                      }
                  }
                  initbanben();
              }
              @Override
              public void onError(Call call, Response response, Exception e) {
                  tvshoujihm.setText("未绑定");
                  initbanben();
              }
          });
      } else {
          DianTool.showTextToast(SettingActivity.this, getResources().getString(R.string.jianwang));
      }
  }
    void initbanben(){
        DianTool.huoqutoken();
        HttpHeaders headers=new HttpHeaders();
        HttpParams params=new HttpParams();
        headers.put("Authorization",DianApplication.user.token_type + " " + DianApplication.user.token);
        params.put("type","android");
        params.put("role","teacher");
        OkGo.get(HostAdress.getBanben("/api/web/v1/upgrade/getInfo")).tag(this)
                .params(params).headers(headers).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                if (!TextUtils.isEmpty(s)) {
                    if (!s.equals("{}")) {
                        Result result = JSON.parseObject(s, Result.class);
                        String str = result.version.substring(0, 1) + result.version.substring(2, 3) + result.version.substring(4, 5);
                        String strs = DianApplication.code.substring(0, 1) + DianApplication.code.substring(2, 3) + DianApplication.code.substring(4, 5);
                        if (Integer.parseInt(str) > Integer.parseInt(strs)) {
                            llxiazai.setVisibility(View.VISIBLE);
                            viset.setVisibility(View.VISIBLE);
                        } else {
                            llxiazai.setVisibility(View.GONE);
                            viset.setVisibility(View.GONE);
                        }
                    }
                }
            }
            @Override
            public void onError(Call call, Response response, Exception e) {
                llxiazai.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llalter:
                Intent intent=new Intent(SettingActivity.this,AlterActivity.class);
                startActivity(intent);
                this.finish();
                overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                break;
            case R.id.llbindphone:
                Intent intentone=new Intent(SettingActivity.this,BindPhoneActivity.class);
                startActivity(intentone);
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.settingback:
                SettingActivity.this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
            case R.id.outlogin:
                DianApplication.sharedPreferences.saveString("percenter","");
                DianApplication.sharedPreferences.saveString("touxiang","");
                DianApplication.sharedPreferences.saveString("organName","");
                DianApplication.sharedPreferences.saveString(Constant.SPLITONE,"");
                DianApplication.sharedPreferences.saveString(Constant.SPLITTWO,"");
                DianApplication.sharedPreferences.saveString(Constant.SPLITTHREE,"");
                DianApplication.sharedPreferences.saveString(Constant.SPLITFOUR,"");
                DianApplication.sharedPreferences.saveString(Constant.SPLITFIVE,"");
                DianApplication.sharedPreferences.saveString(Constant.ORGAINER,"");
                DianApplication.sharedPreferences.saveString(Constant.BINGDING,"");
                DianApplication.sharedPreferences.saveString("phone","");
                DianApplication.sharedPreferences.saveString("organName","");
                DianApplication.sharedPreferences.saveString("personId","");
                DianApplication.sharedPreferences.saveString("mingName","");
                DianApplication.sharedPreferences.saveString(Constant.INFO,"");
                DianApplication.user.latetime=0;
                DianApplication.user.libiao=null;
                DianTool.showTextToast(SettingActivity.this,"退出登录");
                Intent intents=new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intents);
                finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                break;
            case R.id.llhelpcenter:
                if (!huancunshu.getText().toString().equals("")) {
                    DianTool.showTextToast(this, "清除成功");
                    DataCleanManager.clearAllCache(this);
                    huancunshu.setText("");
                }
                break;
            case R.id.llxiazai:
                DianTool.genxin(this);
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
