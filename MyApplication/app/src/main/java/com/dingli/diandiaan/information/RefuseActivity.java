package com.dingli.diandiaan.information;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/7/25.
 */
public class RefuseActivity extends BaseActivity implements View.OnClickListener{
    EditText etliyou;
    Button tbtijiao;
    ImageView refuseback;
    HttpHeaders headers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refuse);
        DianTool.huoqutoken();
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
        initview();
    }
    void initview(){
        refuseback=(ImageView)findViewById(R.id.refuseback);
        etliyou=(EditText)findViewById(R.id.etliyou);
        tbtijiao=(Button)findViewById(R.id.tbtijiao);
        tbtijiao.setOnClickListener(this);
        refuseback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tbtijiao:
                tbtijiao.setBackgroundResource(R.color.beijinan);
                tbtijiao.setClickable(false);
                initdata();
                break;
            case R.id.refuseback:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
        }
    }
    void  initdata(){
        if(DianTool.isConnectionNetWork(this)){
           DianTool.showDialog(this,"");
            String content=etliyou.getText().toString();
            if(!TextUtils.isEmpty(content)){
                HttpParams params=new HttpParams();
                    params.put("leaveId",DianApplication.user.leaveId);
                    params.put("content", content);
                OkGo.get(HostAdress.getRequest("/api/phone/v1/teacher/rejectleaverequest")).tag(this)
                        .headers(headers).params(params).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        DianTool.dissMyDialog();
                        DianTool.showTextToast(RefuseActivity.this, "成功");
                        RefuseActivity.this.finish();
                        overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                        tbtijiao.setBackgroundResource(R.color.qianblue);
                        tbtijiao.setClickable(true);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        DianTool.dissMyDialog();
                    }
                });

            }else{
                DianTool.dissMyDialog();
                tbtijiao.setBackgroundResource(R.color.qianblue);
                tbtijiao.setClickable(true);
                DianTool.showTextToast(this,"理由不能为空");
            }
        }else{
            tbtijiao.setBackgroundResource(R.color.qianblue);
            tbtijiao.setClickable(true);
            DianTool.showTextToast(this,"网络出错,请检查网络");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
