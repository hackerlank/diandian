package com.dingli.diandiaan.information;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.Course;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.common.MyDialog;
import com.dingli.diandiaan.common.QingJiaSty;
import com.dingli.diandiaan.common.ResultInfo;
import com.dingli.diandiaan.information.adapter.FangDaAdapter;
import com.dingli.diandiaan.information.adapter.ListItemWeiDoneView;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;
import com.dingli.diandiaan.view.WheelView;
import com.dingli.diandiaan.view.XListView;
import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.TongyiDialog;
import com.dingli.diandiaan.information.adapter.WeiDoneAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/7/25.
 */
public class WeiDonFragment extends Fragment implements ListItemWeiDoneView.onTongyiListener {

    ListView xlvweidone;
    EntrtyActivity parent;
    WeiDoneAdapter adapter;
    boolean firstIn=true;
    List<Course> arr;
    HttpHeaders headers;
    VerticalSwipeRefreshLayout refweidone;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_weidone,container,false);
        parent=(EntrtyActivity)getActivity();
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        initview(view);
        refweidone.setRefreshing(true);
        xlvweidone.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initdata();
    }
    void initview(View v){
        refweidone=(VerticalSwipeRefreshLayout)v.findViewById(R.id.refweidone);
        refweidone.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
        refweidone.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initdata();
            }
        });
        xlvweidone=(ListView)v.findViewById(R.id.xlvweidone);
        refweidone.setViewGroup(xlvweidone);
        xlvweidone.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                      if (firstVisibleItem==0){
                          refweidone.setEnabled(true);
                      }else{
                          refweidone.setEnabled(false);
                      }
            }
        });
        adapter=new WeiDoneAdapter(parent,this);
    }
    void initdata(){
        if (DianTool.isConnectionNetWork(parent)) {
            DianTool.huoqutoken();
            if (firstIn) {
                firstIn=false;
            }
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getLiQ("/api/phone/v1/period/get")).tag(this)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    if (s.length() != 0) {
                        arr = JSON.parseArray(s, Course.class);
                        showlistweidon();
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    refweidone.setRefreshing(false);
                    DianTool.response(response,parent);
                }
            });
        }else{
            DianTool.showTextToast(parent, "请检查网络");
        }
    }
    void showlistweidon(){
        if(DianTool.isConnectionNetWork(parent)) {
            HttpParams params = new HttpParams();
            params.put("status", "request");
            params.put("orderByKey", "createdTime");
            params.put("orderBy", "asc");
            OkGo.get(HostAdress.getRequest("/api/phone/v1/teacher/getleaverequest")).tag(this)
                    .headers(headers).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                   refweidone.setRefreshing(false);
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
                                                arg.get(i).name = startname + "～" + endname;
                                            }
                                        }
                                    }
                                }
                            }
                        }
//                        List<QingJiaSty> aeg = ShiJiaClass.get(arg);
                        adapter.refreshWeiDone(arg);
                        adapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    refweidone.setRefreshing(false);
                    DianTool.response(response,parent);
                }
            });
        }else{
            DianTool.showTextToast(parent,"请检查网络");
        }
    }
    @Override
    public void OnTongYiListener(String jie,final int ids,String url) {
        ArrayList<View> list=new ArrayList<>();
        switch (jie){
            case "jieshou":
                TongyiDialog dialog=new TongyiDialog(parent, "同意后将无法撤回,且该同学请假消息将发给相关老师", new TongyiDialog.SelectDialogButtonListener() {
                    @Override
                    public void checkButton(int id) {
                        switch (id){
                            case R.id.btnTongyiDialogDetermine:
                                tongyi(ids);
                                break;
                        }
                    }
                });
                dialog.show();
                break;
            case "jujue":
                Intent intent=new Intent(parent,RefuseActivity.class);
                startActivity(intent);
                DianApplication.user.leaveId=ids;
                parent.overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case "tupian":
                Intent intent1=new Intent(parent,FangDaActivity.class);
                intent1.putExtra(Constant.URL,url);
                intent1.putExtra(Constant.YESHU,0);
                startActivity(intent1);
                parent.overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                break;
            case "tupianone":
                Intent intent2=new Intent(parent,FangDaActivity.class);
                intent2.putExtra(Constant.URL,url);
                intent2.putExtra(Constant.YESHU,1);
                startActivity(intent2);
                parent.overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                break;
            case "tupiantwo":
                Intent intent3=new Intent(parent,FangDaActivity.class);
                intent3.putExtra(Constant.URL,url);
                intent3.putExtra(Constant.YESHU,2);
                startActivity(intent3);
                parent.overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                break;
        }
    }
    void tongyi(int ids){
        DianTool.showDialog(parent, "");
        HttpParams params=new HttpParams();
        params.put("leaveId", ids);
        OkGo.get(HostAdress.getRequest("/api/phone/v1/teacher/passleaverequest")).tag(this)
                .headers(headers).params(params).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                DianTool.dissMyDialog();
                showlistweidon();
            }
            @Override
            public void onError(Call call, Response response, Exception e) {
                DianTool.dissMyDialog();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
