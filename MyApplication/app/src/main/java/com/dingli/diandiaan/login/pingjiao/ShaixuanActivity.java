package com.dingli.diandiaan.login.pingjiao;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.bean.Event;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.Coursecenter;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.evaluation.choceclassAdapter.ChoceClassAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Response;

public class ShaixuanActivity extends BaseActivity implements View.OnClickListener {

   private TextView shaiok;
    private ListView lvbanji;
    private ImageView shaiback;
    private ChoceClassAdapter adapter;
    boolean firstIn = true;
    int classId,scheduleId;
    HttpHeaders headers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shaixuan);
        EventBus.getDefault().register(this);
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
        initview();
        Bundle bundle1 = getIntent().getExtras();
        if (bundle1 != null) {
            scheduleId = bundle1.getInt("scheduleId");
            initdata();
        }
        EventBus.getDefault().register(this);

    }

    private void initview() {
        shaiok=(TextView) findViewById(R.id.shaiok);
        lvbanji=(ListView)findViewById(R.id.lvbanji);
        shaiback=(ImageView)findViewById(R.id.shaiback);
        shaiback.setOnClickListener(this);
        shaiok.setOnClickListener(this);
        adapter=new ChoceClassAdapter(this);
        lvbanji.setAdapter(adapter);
    }

    private void initdata() {
        if (DianTool.isConnectionNetWork(this)) {
            DianTool.showDialog(this, "加载中...");
            HttpParams params = new HttpParams();
            params.put("scheduleId", scheduleId);
            OkGo.get(HostAdress.getLiQ("/api/phone/v1/teacher/getclassbyschedule")).tag(this)
                    .headers(headers).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    DianTool.dissMyDialog();
                    if (!TextUtils.isEmpty(s)) {
                        final List<Coursecenter> resultInfo = JSON.parseArray(s, Coursecenter.class);
                        adapter.addChoceClass(resultInfo);
                        adapter.notifyDataSetChanged();
                        lvbanji.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                for (int i = 0; i < resultInfo.size(); i++) {
                                    if (i == position) {
                                        ImageView c = (ImageView) lvbanji.getChildAt(i).findViewById(R.id.selected);
                                        c.setVisibility(View.VISIBLE);
                                    } else {
                                        ImageView c = (ImageView) lvbanji.getChildAt(i).findViewById(R.id.selected);
                                        c.setVisibility(View.GONE);
                                    }
                                }
                                classId = ((Coursecenter) parent.getItemAtPosition(position)).classId;
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
            case R.id.shaiback:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
            case R.id.shaiok:
                if (classId==0){
                    DianTool.showTextToast(ShaixuanActivity.this,"你还没有选择班级");
                }else {
                    EventBus.getDefault().post(new Event(Event.EVENT_SOUSUO, String.valueOf(classId)));
                    this.finish();
                    overridePendingTransition(R.anim.activity_vertical_pop_enter, R.anim.activity_vertical_pop_exit);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        OkGo.getInstance().cancelTag(this);
    }
}
