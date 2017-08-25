package com.dingli.diandiaan.information;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.Course;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.common.MyDialog;
import com.dingli.diandiaan.common.QingJiaSty;
import com.dingli.diandiaan.common.ResultInfo;
import com.dingli.diandiaan.login.FindActivity;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;
import com.dingli.diandiaan.view.XListView;
import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.information.adapter.YiBackAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.List;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2016/7/25.
 */
public class YiBackFragment extends Fragment implements View.OnClickListener{
    EntrtyActivity parent;
    ListView xlvyiback;
    YiBackAdapter adapter;
    List<Course> arr;
    HttpHeaders headers;
    VerticalSwipeRefreshLayout refyiback;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View views=inflater.inflate(R.layout.fragment_yiback,container,false);
        parent=(EntrtyActivity)getActivity();
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        initview(views);
        refyiback.setRefreshing(true);
        adapter=new YiBackAdapter(parent);
        xlvyiback.setAdapter(adapter);
        return views;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            initdata();
        }
    }

    void initview(View v){
        refyiback=(VerticalSwipeRefreshLayout)v.findViewById(R.id.refyiback);
        refyiback.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
        refyiback.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initdata();
            }
        });
        xlvyiback=(ListView)v.findViewById(R.id.xlvyiback);
        refyiback.setViewGroup(xlvyiback);
        xlvyiback.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                      if (firstVisibleItem==0){
                          refyiback.setEnabled(true);
                      }else{
                          refyiback.setEnabled(false);
                      }
            }
        });
    }

    void initdata(){
        if (DianTool.isConnectionNetWork(parent)) {
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getLiQ("/api/phone/v1/period/get")).tag(this)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    if (s.length() != 0) {
                        arr = JSON.parseArray(s, Course.class);
                        showlistyiback();
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    refyiback.setRefreshing(false);
                    DianTool.response(response,parent);
                }
            });
        }else{
            DianTool.showTextToast(parent, "请检查网络");
        }
    }
    void showlistyiback(){
        if(DianTool.isConnectionNetWork(parent)) {
            HttpParams params = new HttpParams();
            params.put("status", "cancle");
            params.put("orderByKey", "lastModifiedTime");
            params.put("orderBy", "asc");
            OkGo.get(HostAdress.getRequest("/api/phone/v1/teacher/getleaverequest")).tag(this)
                    .headers(headers).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                   refyiback.setRefreshing(false);
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
                    refyiback.setRefreshing(false);
                    DianTool.response(response,parent);
                }
            });
        }else{
            DianTool.showTextToast(parent,"请检查网络");
        }
    }
    @Override
    public void onClick(View v) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
