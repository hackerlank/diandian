package com.dingli.diandiaan.firstpage.callset;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.DuiHuaDialog;
import com.dingli.diandiaan.common.DuiHuaDialogs;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.common.Result;
import com.dingli.diandiaan.rollcall.NewCallRollActivity;
import com.dingli.diandiaan.setting.InstructionActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/11/23.
 */
public class CallSetActivity extends BaseActivity implements View.OnClickListener,WheelPicker.OnItemSelectedListener {
    private TextView savekechen;
    private TextView tvfangshi;
    private TextView etshuzi;
    private Button btsave;
    private PopupWindow popupWindow;
    View views;
    int courseId;
    int couid;
    String calltype="automatic";
    String rukou;
    String[] PLANETSS = new String[]{"0","1","5","10","15", "20","25","30","35","40","45","50","55","60","70","80","90","100","110","120"};
    int positons;
    View viheiping;
    ImageView callsetback;
    LinearLayout llfangshi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callset);
        views=LayoutInflater.from(this).inflate(R.layout.activity_callset, null);
        initView();
        InitPopwindows();
    }
    private void initView() {
        courseId=getIntent().getIntExtra(Constant.KE_ID, 0);
        couid=getIntent().getIntExtra(Constant.COURSEID, 0);
        rukou=getIntent().getStringExtra(Constant.RUKOU);
       String name= getIntent().getStringExtra(Constant.COURSE_NAME);
        String code=getIntent().getStringExtra(Constant.CODE);
        int lateTime=getIntent().getIntExtra(Constant.LATE_TIME,0);
        callsetback = (ImageView) findViewById(R.id.callsetback);
        savekechen = (TextView) findViewById(R.id.savekechen);
        if (!TextUtils.isEmpty(name)) {
            savekechen.setText(name);
        }
        tvfangshi = (TextView) findViewById(R.id.tvfangshi);
        if (code.equals("automatic")){
            tvfangshi.setText("定位点名");
        }else if(code.equals("e")){
            tvfangshi.setText("定位点名");
        } else if (code.equals("")){
            tvfangshi.setText("定位点名");
        }else{
            tvfangshi.setText("数字点名");
        }
        etshuzi = (TextView) findViewById(R.id.etshuzi);
        viheiping=findViewById(R.id.viheiping);
        viheiping.getBackground().setAlpha(110);
        if (DianApplication.user.latetime!=0){
            etshuzi.setText(String.valueOf(DianApplication.user.latetime));
        }else {
            if (lateTime != 0) {
                etshuzi.setText(String.valueOf(lateTime));
            } else {
                etshuzi.setText("15");
            }
        }
        btsave = (Button) findViewById(R.id.btsave);
        llfangshi=(LinearLayout)findViewById(R.id.llfangshis);
        llfangshi.setOnClickListener(this);
        btsave.setOnClickListener(this);
        etshuzi.setOnClickListener(this);
        callsetback.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btsave:
                iscan();
                break;
            case R.id.etshuzi:
                viheiping.setVisibility(View.VISIBLE);
                popupWindow.showAtLocation(views, Gravity.BOTTOM, 0, 0);
                btsave.setClickable(false);
                llfangshi.setClickable(false);
                callsetback.setClickable(false);
                break;
            case R.id.btn_cancel:
                popupWindow.dismiss();
                viheiping.setVisibility(View.GONE);
                btsave.setClickable(true);
                llfangshi.setClickable(true);
                callsetback.setClickable(true);
                break;
            case R.id.btn_submit:
                etshuzi.setText(PLANETSS[positons]);
                popupWindow.dismiss();
                viheiping.setVisibility(View.GONE);
                btsave.setClickable(true);
                llfangshi.setClickable(true);
                callsetback.setClickable(true);
                break;
            case R.id.callsetback:
                finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                break;
            case R.id.llfangshis:
                String fangshi="";
                if (tvfangshi.getText().toString().equals("定位点名")){
                     fangshi="dingwei";
                }else{
                    fangshi="shuzi";
                }
                Intent intent=new Intent(this, InstructionActivity.class);
                intent.putExtra(Constant.FANGSHI,fangshi);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                break;
        }
    }
   private void iscan(){
       DianTool.huoqutoken();
       DianTool.showDialog(this,"");
       HttpHeaders headers=new HttpHeaders();
       HttpParams params=new HttpParams();
       headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
       params.put("courseId",couid);
       OkGo.post(HostAdress.getRequest("/api/phone/v1/teacher/isCanUpdateRollcall")).tag(this)
               .params(params).headers(headers)
               .execute(new StringCallback() {
                   @Override
                   public void onSuccess(String s, Call call, Response response) {
                       DianTool.dissMyDialog();
                       if (!TextUtils.isEmpty(s)) {
                           Result result = JSON.parseObject(s, Result.class);
                           if (result.success == false) {
                               DuiHuaDialogs dialog = new DuiHuaDialogs(CallSetActivity.this, "", "现在正在上课哦，保存成功后，所有学生本节课的状态将被初始为“未提交”，是否继续？", "", "确认", new DuiHuaDialogs.SelectDialogButtonListener() {
                                   @Override
                                   public void checkButton(int id) {
                                       switch (id) {
                                           case R.id.btnok:
                                               btsave.setClickable(false);
                                               submit();
                                               break;
                                           case R.id.btncal:
                                               break;
                                       }
                                   }
                               });
                               dialog.show();
                           } else {
                               DuiHuaDialogs dialog = new DuiHuaDialogs(CallSetActivity.this, "", "该课程及后续将开启点名，是否确定？", "", "确认", new DuiHuaDialogs.SelectDialogButtonListener() {
                                   @Override
                                   public void checkButton(int id) {
                                       switch (id) {
                                           case R.id.btnok:
                                               btsave.setClickable(false);
                                               submit();
                                               break;
                                           case R.id.btncal:
                                               break;
                                       }
                                   }
                               });
                               dialog.show();
                           }
                       }
                   }

                   @Override
                   public void onError(Call call, Response response, Exception e) {
                       DianTool.dissMyDialog();

                   }
               });
   }
    private void submit() {
        String etshuziString = etshuzi.getText().toString().trim();
        if(tvfangshi.getText().toString().equals("数字点名")){
            calltype = "digital";
        }else{
            calltype = "automatic";
        }
        if (DianTool.isConnectionNetWork(this)) {
            if (!TextUtils.isEmpty(etshuziString)) {
                DianTool.showDialog(this,"");
                final int lasttime=Integer.parseInt(etshuziString);
                DianTool.huoqutoken();
                HttpHeaders headers=new HttpHeaders();
                HttpParams params=new HttpParams();
                headers.put("Content-Type", Constant.APPLICATION_FORMURL);
                headers.put("Encoding", "UTF-8");
                headers.put("Accept", Constant.APPLICATION_JSON);
                headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
                params.put("courseId",couid);
                params.put("rollCallType",calltype);
                params.put("lateTime",lasttime);
                params.put("reUser",20);
                params.put("isOpen","enable");
                OkGo.post(HostAdress.getRequest("/api/phone/v1/teacher/updateRollcall")).tag(this).headers(headers)
                        .params(params).execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        DianTool.dissMyDialog();
                        DianTool.showTextToast(CallSetActivity.this,"开启成功");
                        if (!TextUtils.isEmpty(s)) {
                            Result result = JSON.parseObject(s, Result.class);
                            btsave.setClickable(true);
                            if (rukou.equals("2")) {
                                DianApplication.user.latetime = lasttime;
                                finish();
                                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                            } else {
                                Intent intent = new Intent(CallSetActivity.this, NewCallRollActivity.class);
                                intent.putExtra(Constant.KE_ID, courseId);
                                intent.putExtra(Constant.ISSHOOL, "true");
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                            }
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        DianTool.dissMyDialog();
                        btsave.setClickable(true);
                        DianTool.showTextToast(CallSetActivity.this,"保存失败");
                        DianTool.response(response,CallSetActivity.this);
                    }
                });
            }else{
                DianTool.showTextToast(this,"时间不能为空");
            }
        }else{
            DianTool.showTextToast(this,"请检查网络");
        }
    }
    private void InitPopwindows() {
        View view = LayoutInflater.from(this).inflate(R.layout.cql_dialog,null);
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);
        WheelPicker picker = (WheelPicker) view.findViewById(R.id.main_wheel_center);
        picker.setData(Arrays.asList(PLANETSS));
       TextView tv_cancle = (TextView) view.findViewById(R.id.btn_cancel);
       TextView tv_selected = (TextView) view.findViewById(R.id.btn_submit);
        tv_cancle.setOnClickListener(this);
        tv_selected.setOnClickListener(this);
        picker.setOnItemSelectedListener(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1&&resultCode==RESULT_OK){
            String fangshi=data.getStringExtra("result");
            if (fangshi.equals("one")){
                tvfangshi.setText("定位点名");
                calltype = "automatic";
            }else{
                tvfangshi.setText("数字点名");
                calltype = "digital";
            }
        }
    }
    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
         positons=position;
    }
}
