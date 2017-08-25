package com.dingli.diandiaan.evaluation;

import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.Coursecenter;
import com.dingli.diandiaan.common.MyDialog;
import com.dingli.diandiaan.evaluation.choceclassAdapter.ChoceClassAdapter;
import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.bean.Event;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/6/13.
 */
public class ChoceClassActivity extends BaseActivity implements View.OnClickListener{

    TextView chocedone;
    ListView choceclasslv;
    ImageView choceback;
  ChoceClassAdapter adapter;
    int classId;
    String className;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choceclass);
        EventBus.getDefault().register(this);
        initview();
        initdata();
    }
    void initview(){
        chocedone=(TextView)findViewById(R.id.chocedone);
        choceclasslv=(ListView)findViewById(R.id.choceclasslv);
        choceback=(ImageView)findViewById(R.id.choceback);
        choceback.setOnClickListener(this);
        chocedone.setOnClickListener(this);
        adapter=new ChoceClassAdapter(this);
        choceclasslv.setAdapter(adapter);
    }
    void initdata(){
        if (DianTool.isConnectionNetWork(this)){
            DianTool.huoqutoken();
            DianTool.showDialog(this,"加载中...");
            HttpParams params=new HttpParams();
            HttpHeaders headers=new HttpHeaders();
            headers.put("Content-Type", Constant.APPLICATION_FORMURL);
            headers.put("Encoding", "UTF-8");
            headers.put("Accept", Constant.APPLICATION_JSON);
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            params.put("scheduleId", DianApplication.user.KE_ID);
            OkGo.getInstance().addCommonHeaders(headers);
            OkGo.get(HostAdress.getLiQ("/api/phone/v1/teacher/getclassbyschedule")).tag(this).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                           DianTool.dissMyDialog();
                     if (!TextUtils.isEmpty(s)) {
                         final List<Coursecenter> resultInfo = JSON.parseArray(s, Coursecenter.class);
                         adapter.addChoceClass(resultInfo);
                         adapter.notifyDataSetChanged();
                         choceclasslv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                             @Override
                             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                 for (int i = 0; i < resultInfo.size(); i++) {
                                     if (i == position) {
                                         ImageView c = (ImageView) choceclasslv.getChildAt(i).findViewById(R.id.selected);
                                         c.setVisibility(View.VISIBLE);
                                         classId = ((Coursecenter) parent.getItemAtPosition(position)).classId;
                                         className = ((Coursecenter) parent.getItemAtPosition(position)).className;
                                     } else {
                                         ImageView c = (ImageView) choceclasslv.getChildAt(i).findViewById(R.id.selected);
                                         c.setVisibility(View.GONE);
                                     }
                                 }
                             }
                         });
                     }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                   DianTool.dissMyDialog();
                }
            });
        }else{
            DianTool.showTextToast(this,"请检查网络");
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.choceback:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
            case R.id.chocedone:
                if (classId==0){
                    DianTool.showTextToast(ChoceClassActivity.this,"你还没有选择班级");
                }else {
                    EventBus.getDefault().post(new Event(Event.EVENT_SOUSUO, String.valueOf(classId)));
                    EventBus.getDefault().post(new Event(Event.EVENT_SHAKE_INFO,className));
                    this.finish();
                    overridePendingTransition(R.anim.activity_vertical_pop_enter, R.anim.activity_vertical_pop_exit);
                }
                break;
        }
    }
    public void onEvent(Event event) {
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        OkGo.getInstance().cancelTag(this);
    }
}
