package com.dingli.diandiaan.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dingli.diandiaan.MainActivity;
import com.dingli.diandiaan.MaintiAcvity;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.ResultInfo;
import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.common.DianTool;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/3/14.
 */
public class ModificationActivity extends BaseActivity {
    ImageView modificationorderback,ddmodica;
    EditText etnewmodification,etnewagainmodification;
    Button btrequiremodification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification);
        DianTool.huoqutoken();
        String phone=getIntent().getStringExtra("phone");
        String code=getIntent().getStringExtra("code");
        init(phone, code);
    }
    public void init(final String phone,final String code){
        modificationorderback=(ImageView)findViewById(R.id.modificationorderback);
        etnewmodification=(EditText)findViewById(R.id.etnewmodification);
        etnewagainmodification=(EditText)findViewById(R.id.etnewagainmodification);
        btrequiremodification=(Button)findViewById(R.id.btrequiremodification);
        btrequiremodification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newmodification=etnewmodification.getText().toString().trim();
                String againmodification=etnewagainmodification.getText().toString().trim();
                 if(TextUtils.isEmpty(newmodification)){
                     DianTool.showTextToast(ModificationActivity.this, getResources().getString(R.string.xinmi));
                 }else if(TextUtils.isEmpty(againmodification)){
                     DianTool.showTextToast(ModificationActivity.this,getResources().getString(R.string.zaixinmi));
                 }else if (newmodification.equals("123456")){
                     DianTool.showTextToast(ModificationActivity.this,"新密码过于简单,请重设");
                 }else if(newmodification.getBytes().length<6){
                     DianTool.showTextToast(ModificationActivity.this,"密码不能小于6位");
                 }
                 else if(newmodification.equals(againmodification)){
                     if(DianTool.isConnectionNetWork(ModificationActivity.this)){
                         DianTool.showDialog(ModificationActivity.this,"");
                         HttpParams params=new HttpParams();
                         HttpHeaders headers=new HttpHeaders();
                         params.put("code",code);
                         params.put("pwd",againmodification);
                         params.put("phone",phone);
                         params.put("module","dd_fp");
                         headers.put("Content-Type", Constant.APPLICATION_FORMURL);
                         headers.put("Encoding", "UTF-8");
                         headers.put("Accept", Constant.APPLICATION_JSON);
                         headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
                         OkGo.post(HostAdress.getRequestUrl("/api/web/v1/users/findandsetpwd")).tag(this).params(params).headers(headers).execute(new StringCallback() {
                             @Override
                             public void onSuccess(String s, Call call, Response response) {
                                 DianTool.dissMyDialog();
                                 DianTool.showTextToast(ModificationActivity.this, getResources().getString(R.string.xiugong));
//                                 DianApplication.sharedPreferences.saveString(Constant.USER_PASSWORD, againmodification);
                                 DianApplication.user.accounts = null;
                                 DianApplication.user.passwords = null;
                                 Intent intent = new Intent(ModificationActivity.this, LoginActivity.class);
                                 startActivity(intent);
                                 ModificationActivity.this.finish();
                                 for (int i = 0; i < DianApplication.activityList.size(); i++) {
                                     if (DianApplication.activityList.get(i) != DianApplication.user.login) {
                                         DianApplication.activityList.get(i).finish();
                                     }
                                 }
                                 overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                             }
                             @Override
                             public void onError(Call call, Response response, Exception e) {
                                 DianTool.dissMyDialog();
                                 DianTool.showTextToast(ModificationActivity.this,"修改失败");
                             }
                         });
                     }else{
                         DianTool.showTextToast(ModificationActivity.this,getResources().getString(R.string.jianwang));
                     }
                 }else{
                     DianTool.showTextToast(ModificationActivity.this,getResources().getString(R.string.shubuyi));
                 }
            }
        });
        modificationorderback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModificationActivity.this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
            }
        });
    }
}
