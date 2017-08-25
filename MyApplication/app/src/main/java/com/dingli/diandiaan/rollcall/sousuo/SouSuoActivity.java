package com.dingli.diandiaan.rollcall.sousuo;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.bean.Event;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.common.ResultInfoCall;
import com.dingli.diandiaan.rollcall.NewRollCallAdapter;
import com.dingli.diandiaan.rollcall.ViewHolder;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/6/8.
 */
public class SouSuoActivity  extends BaseActivity implements View.OnClickListener,ViewHolder.onCancelCollectListener{

    ImageView sousuoback;
    EditText sousuoroll;
    TextView callrollsousuo,tvss;
    ListView lvsousuo;
    ImageView ivicon;
    int keid;
    String trueorfalse;
    PopupWindow pop;
    NewRollCallAdapter adapter;
    ResultInfoCall resultInfoCall;
    int classIds;String classNames;int courseIds;long ids;int scheduleIds;int studentScheduleIds;
    int teacherIds;String types;int userIds;String userNames;
    RelativeLayout llbgs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sousuo);
        initview();
        initpop();
        adapter=new NewRollCallAdapter(this,pop,this,"",0,0);
        lvsousuo.setAdapter(adapter);
    }
    void initview(){
        keid=getIntent().getIntExtra(Constant.KE_ID, 0);
        trueorfalse=getIntent().getStringExtra(Constant.ISSHOOL);
        callrollsousuo=(TextView)findViewById(R.id.callrollsousuo);
        sousuoroll=(EditText)findViewById(R.id.sousuoroll);
        sousuoback=(ImageView)findViewById(R.id.sousuoback);
        lvsousuo=(ListView)findViewById(R.id.lvsousuo);
        tvss=(TextView)findViewById(R.id.tvss);
        ivicon=(ImageView)findViewById(R.id.ivicon);
        sousuoback.setOnClickListener(this);
        callrollsousuo.setOnClickListener(this);
        ivicon.setOnClickListener(this);
        sousuoroll.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                callrollsousuo.setText("取消");
            }
        });
    }
   void initpop(){
       View view= LayoutInflater.from(this).inflate(R.layout.popupwindow_change,null);
       TextView tvyidao=(TextView)view.findViewById(R.id.tvyidao);
       TextView tvlaste=(TextView)view.findViewById(R.id.tvlaste);
       TextView tvearly=(TextView)view.findViewById(R.id.tvearly);
       TextView tvkuds=(TextView)view.findViewById(R.id.tvkuds);
       TextView tvleav=(TextView)view.findViewById(R.id.tvleav);
       RelativeLayout rela=(RelativeLayout)view.findViewById(R.id.rela);
       rela.setOnClickListener(this);
       tvyidao.setOnClickListener(this);
       tvearly.setOnClickListener(this);
       tvlaste.setOnClickListener(this);
       tvkuds.setOnClickListener(this);
       tvleav.setOnClickListener(this);
       pop=new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
       view.getBackground().setAlpha(110);
       pop.setOutsideTouchable(true);
       pop.setFocusable(true);
       pop.setBackgroundDrawable(new BitmapDrawable());
       pop.setAnimationStyle(R.style.mypopwindow_anim_style);
       pop.update();
   }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rela:
                pop.dismiss();
                break;
            case R.id.sousuoback:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
            case R.id.callrollsousuo:
                sousuoroll.setText("");
                callrollsousuo.setText("");
                break;
            case R.id.ivicon:
                sousuoroll.setVisibility(View.VISIBLE);
                ivicon.setVisibility(View.GONE);
                callrollsousuo.setVisibility(View.VISIBLE);
                tvss.setVisibility(View.GONE);
                break;
            case R.id.tvyidao:
                pop.dismiss();
                update("1");
                break;
            case R.id.tvleav:
                pop.dismiss();
                update("4");
                break;
            case R.id.tvearly:
                pop.dismiss();
                update("5");
                break;
            case R.id.tvlaste:
                pop.dismiss();
                update("3");
                break;
            case R.id.tvkuds:
                pop.dismiss();
                update("2");
                break;
        }
    }
    void update(String type){
        JSONObject json=new JSONObject();
        try {
            json.put("classId",classIds);
            json.put("className",classNames);
            json.put("courseId",courseIds);
            json.put("id",ids);
            json.put("scheduleId",scheduleIds);
            json.put("studentScheduleId",studentScheduleIds);
            json.put("teacherId",teacherIds);
            json.put("type",type);
            json.put("userId",userIds);
            json.put("userName",userNames);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (DianTool.isConnectionNetWork(this)) {
            DianTool.showDialog(this,"");
            HttpHeaders headers=new HttpHeaders();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.post(HostAdress.getRequest("/api/phone/v1/teacher/rollcall/update")).tag(this)
                    .headers(headers).upJson(json)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            DianTool.dissMyDialog();
                            initdata(sousuoroll.getText().toString().trim(), "");
                        }
                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            DianTool.showTextToast(SouSuoActivity.this,"修改失败");
                            DianTool.response(response,SouSuoActivity.this);
                        }
                    });
        }else{
            DianTool.showTextToast(this, "请检查网络");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
            /*隐藏软键盘*/
//            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            if(inputMethodManager.isActive()){
//                inputMethodManager.hideSoftInputFromWindow(SouSuoActivity.this.getCurrentFocus().getWindowToken(), 0);
//            }
            tvss.setVisibility(View.VISIBLE);
            sousuoroll.setVisibility(View.GONE);
            ivicon.setVisibility(View.VISIBLE);
            callrollsousuo.setVisibility(View.GONE);
            initdata(sousuoroll.getText().toString().trim(),"");
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
   void initdata(String str,String type){
       HttpParams params=new HttpParams();
       HttpHeaders headers=new HttpHeaders();
       headers.put("Content-Type", Constant.APPLICATION_FORMURL);
       headers.put("Encoding", "UTF-8");
       headers.put("Accept", Constant.APPLICATION_JSON);
       headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
       params.put("name",str);
       params.put("schedule_id", keid);
       if (trueorfalse.equals("true")) {
           params.put("isSchoolTime", true);
       }else{
           params.put("isSchoolTime",false);
       }
       params.put("type",type);
       if(DianTool.isConnectionNetWork(this)) {
           OkGo.get(HostAdress.getRequest("/api/phone/v1/teacher/rollcall/get")).tag(this)
                   .headers(headers).params(params).execute(new StringCallback() {
               @Override
               public void onSuccess(String s, Call call, Response response) {
                   if (!TextUtils.isEmpty(s)) {
                       resultInfoCall = JSON.parseObject(s, ResultInfoCall.class);
                       if (resultInfoCall.data.size() == 0) {
                           DianTool.showTextToast(SouSuoActivity.this, "你搜索的人物不存在或者你输入的不是汉字");
                       } else {
                           adapter.refreshNewCalllist(resultInfoCall.data);
                           adapter.notifyDataSetChanged();
                       }
                   }
               }

               @Override
               public void onError(Call call, Response response, Exception e) {
                   DianTool.response(response,SouSuoActivity.this);
               }
           });
       }else{
           DianTool.showTextToast(this,"请检查网络!");
       }
   }
    @Override
    public void onCancelCollect(ImageView iv,RelativeLayout lela,View v, int classId, String className, int courseId, long id,
                                int scheduleId, int studentScheduleId, int teacherId, String type, int userId, String userName,ImageView ch) {
        if (!resultInfoCall.data.get(0).rollCallList.get(0).type.equals("9")){
            classIds = classId;
            classNames = className;
            courseIds = courseId;
            ids = id;
            scheduleIds = scheduleId;
            studentScheduleIds = studentScheduleId;
            teacherIds = teacherId;
            types = type;
            userIds = userId;
            userNames = userName;
            llbgs=lela;
            lela.setBackgroundResource(R.color.xuanzhong);
            pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void refresh(int i,long ids) {
    }
}
