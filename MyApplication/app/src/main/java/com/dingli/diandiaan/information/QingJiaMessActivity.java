package com.dingli.diandiaan.information;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.Course;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.common.MyDialog;
import com.dingli.diandiaan.common.QingJiaSty;
import com.dingli.diandiaan.common.ResultInfo;
import com.dingli.diandiaan.information.adapter.QingJiaMessAdapter;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/7/25.
 */
public class QingJiaMessActivity extends BaseActivity implements View.OnClickListener{

    ImageView QingJiaMessback;
    ListView listviews;
    ResultInfo resultInfotoken;
    QingJiaMessAdapter adapter;
    List<Course> arr;
    HttpHeaders headers;
    boolean firstIn=true;
    VerticalSwipeRefreshLayout refqingjia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qingjiamessage);
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refqingjia.setRefreshing(true);
        initjieshu();
    }

    void initView(){
        refqingjia=(VerticalSwipeRefreshLayout)findViewById(R.id.refqingjia);
        refqingjia.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initjieshu();
            }
        });
        refqingjia.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
        QingJiaMessback=(ImageView)findViewById(R.id.QingJiaMessback);
        listviews=(ListView)findViewById(R.id.listviews);
        refqingjia.setViewGroup(listviews);
        listviews.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                     if (firstVisibleItem==0){
                         refqingjia.setEnabled(true);
                     }else{
                         refqingjia.setEnabled(false);
                     }
            }
        });
        QingJiaMessback.setOnClickListener(this);
        adapter=new QingJiaMessAdapter(this);
        listviews.setAdapter(adapter);
    }
    void initjieshu(){
        if (DianTool.isConnectionNetWork(this)) {
            if (firstIn) {
                firstIn=false;
            }
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getLiQ("/api/phone/v1/period/get")).tag(this)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    if (s.length() != 0) {
                        arr = JSON.parseArray(s, Course.class);
                        initdata();
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    refqingjia.setRefreshing(false);
                    DianTool.response(response,QingJiaMessActivity.this);
                }
            });
        }else{
            DianTool.showTextToast(this, "请检查网络");
        }
    }
    void initdata(){
        if (DianTool.isConnectionNetWork(this)){
             HttpParams params=new HttpParams();
            params.put("orderBy","asc");
            params.put("orderByKey", "createdTime");
            OkGo.get(HostAdress.getRequest("/api/phone/v1/teacher/getleaverequestforteach")).tag(this)
                    .headers(headers).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    refqingjia.setRefreshing(false);
                    if (s.length() != 0) {
                        List<QingJiaSty> arg = JSON.parseArray(s, QingJiaSty.class);
                        for (int i = 0; i < arg.size(); i++) {
                            String startname = "";
                            String endname = "";
                            if (arr.size() != 0) {
                                for (int j = 0; j < arr.size(); j++) {
                                    if (arg.get(i).startPeriodId != 0) {
                                        if (arg.get(i).endPeriodId != 0) {
                                            if (arg.get(i).startPeriodId == arr.get(j).id) {
                                                startname = arr.get(j).name;
                                            }
                                            if (arg.get(i).endPeriodId == arr.get(j).id) {
                                                endname = arr.get(j).name;
                                            }
                                            if (startname.equals(endname)) {
                                                arg.get(i).name = startname;
                                            } else {
                                                arg.get(i).name = startname + " " + endname;
                                            }
                                        }
                                    }
                                }
                            }
                        }
//                        List<QingJiaSty> aeg = ShiJiaClass.get(arg);
                        adapter.refreshQingJia(arg);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    refqingjia.setRefreshing(false);
                    DianTool.response(response,QingJiaMessActivity.this);
                }
            });
        }else{
         DianTool.dissMyDialog();
            DianTool.showTextToast(this, "请检查网络");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.QingJiaMessback:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
