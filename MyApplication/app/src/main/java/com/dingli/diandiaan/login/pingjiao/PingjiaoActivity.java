package com.dingli.diandiaan.login.pingjiao;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.Course;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.common.MyDialog;
import com.dingli.diandiaan.common.ResultInfo;
import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class PingjiaoActivity extends BaseActivity implements View.OnClickListener,WheelPicker.OnItemSelectedListener {

    private RelativeLayout title_bar;
    private PopupWindow popupWindow;
    private ListView pj_listView;
    private TextView tv_selected_week;
    private List<JSONObject> list;
    private ImageView back;
    private ResultInfo resultInfoweek;
    private PjFirstAdapter adapter;
    private String currentWeekName;
    private  long currentWeekId;
    private List<String> dataList;//存放wheelpicker的数据集合
    private List<Map<String,Object>> dataListWeeks;//存放周列表的数据集合
    private WheelPicker picker;
    private static int positions=0;
    private TextView tv_cancle;
    private TextView tv_title;
    private TextView tv_selected;
    private SharedPreferences preferences;
    boolean firstIn = true;
    HttpHeaders headers;
    VerticalSwipeRefreshLayout refpingjiao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pingjiao);
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        initView();
        InitPopwindows();
        refpingjiao.setRefreshing(true);
        getCurrentWeek();
    }
    //获取当前周
    private void getCurrentWeek() {
        if (DianTool.isConnectionNetWork(this)) {
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/week/get")).tag(this)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    if (android.text.TextUtils.isEmpty(s)) {
                        refpingjiao.setRefreshing(false);
                        DianTool.showTextToast(PingjiaoActivity.this, "当前周没有数据");
                    } else {
                        resultInfoweek = JSON.parseObject(s, ResultInfo.class);
                        currentWeekName = resultInfoweek.name;
                        currentWeekId = resultInfoweek.id;
                        getAllWeek();
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                   refpingjiao.setRefreshing(false);
                    DianTool.response(response,PingjiaoActivity.this);
                }
            });
        }else{
            DianTool.showTextToast(this, "请检查网络");
        }
    }
    //获取全部周的列表
    private void getAllWeek() {
        if (DianTool.isConnectionNetWork(this)) {
            OkGo.get(HostAdress.getRequest("/api/phone/v1/week/getList")).tag(this)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    if (TextUtils.isEmpty(s)) {
                        refpingjiao.setRefreshing(false);
                        DianTool.showTextToast(PingjiaoActivity.this, "当前周没有数据");
                    } else {
                        if (dataListWeeks.size()!=0){
                            dataListWeeks.clear();
                        }
                        if (dataList.size()!=0){
                            dataList.clear();
                        }
                        try {
                            JSONObject obj = new JSONObject(s);
                            JSONArray weekList = obj.getJSONArray("weekList");
                            if (weekList != null && weekList.length() > 0) {
                                for (int i = 0; i < weekList.length(); i++) {
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    JSONObject optJSONObject = weekList.optJSONObject(i);
                                    String name = optJSONObject.getString("name");
                                    if (name.equals(currentWeekName)) {
                                        name = "第" + name + "周(本周)";
                                    } else {
                                        name = "第" + name + "周";
                                    }
                                    map.put("name", name);

                                    map.put("id", optJSONObject.getInt("id"));
                                    dataListWeeks.add(map);
                                    dataList.add(name);
                                }
                                if (dataList != null && dataList.size() > 0) {
                                    picker.setData(dataList);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        tv_selected_week.setText("第" + currentWeekName + "周");
                        getData(currentWeekId);
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                   refpingjiao.setRefreshing(false);
                   DianTool.response(response,PingjiaoActivity.this);
                }
            });
        }else {
            DianTool.showTextToast(this, "请检查网络");
        }
    }

    /**
     * 获取某一周的评教信息
     * @param weekid
     */
    private void getData(long weekid ) {
       HttpParams params =new HttpParams();
         params.put("weekId", weekid);
        OkGo.get(HostAdress.getZheRe("/api/phone/v1/teacher/getassessbyweek")).tag(this)
                .headers(headers).params(params).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                refpingjiao.setRefreshing(false);
                if (TextUtils.isEmpty(s)) {
                    DianTool.showTextToast(PingjiaoActivity.this, "数据为空");
                } else {
                    List<Course> array = JSON.parseArray(s, Course.class);
                    adapter = new PjFirstAdapter(array, PingjiaoActivity.this);
                    pj_listView.setAdapter(adapter);
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                refpingjiao.setRefreshing(false);
                DianTool.response(response,PingjiaoActivity.this);
            }
        });
    }

    /**
     * 获取全部周的评分
     * @param
     */
    private void InitPopwindows() {
        View view = LayoutInflater.from(this).inflate(R.layout.cql_dialog,null);
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);
        picker = (WheelPicker) view.findViewById(R.id.main_wheel_center);
        tv_cancle = (TextView) view.findViewById(R.id.btn_cancel);
        tv_selected = (TextView) view.findViewById(R.id.btn_submit);
        tv_cancle.setOnClickListener(this);
        tv_selected.setOnClickListener(this);
        picker.setOnItemSelectedListener(this);
//        picker.setData(dataList);
    }
    /**
     * 初始化组件
     */
    private void initView() {
        refpingjiao=(VerticalSwipeRefreshLayout)findViewById(R.id.refpingjiao);
        refpingjiao.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
        refpingjiao.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCurrentWeek();
            }
        });
        title_bar= (RelativeLayout) findViewById(R.id.title_bar);
        tv_selected_week= (TextView) findViewById(R.id.tv_selected_week);
        tv_selected_week.setOnClickListener(this);
        back= (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        preferences=getSharedPreferences("aizhixin_info",MODE_PRIVATE);
        pj_listView= (ListView) findViewById(R.id.pj_listView);
        refpingjiao.setViewGroup(pj_listView);
        pj_listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                  if (firstVisibleItem==0){
                      refpingjiao.setEnabled(true);
                  }else{
                      refpingjiao.setEnabled(false);
                  }
             }
        });
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        list=new ArrayList<>();
        dataList=new ArrayList<>();
        dataListWeeks=new ArrayList<>();
//        dataList.add("全部周");
//        Map<String,Object> map1 = new HashMap<String, Object>();
//        map1.put("name", "全部周");
//        map1.put("id", 0);
//        dataListWeeks.add(map1);
    }

    //对周进行设置的弹出框
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_selected_week:
                popupWindow.showAsDropDown(title_bar,0,0);
                break;
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
            case R.id.btn_cancel:
                if (popupWindow.isShowing()&&popupWindow != null){
                    popupWindow.dismiss();
                }
                break;
            case R.id.btn_submit:
                if (popupWindow.isShowing()&&popupWindow != null){
                    popupWindow.dismiss();
                }
                if(dataList.size()!=0) {
                    if(dataList.get(positions).contains("本周")){
                        tv_selected_week.setText(dataList.get(positions).substring(0,dataList.get(positions).indexOf("(")));
                    }else {
                        tv_selected_week.setText(dataList.get(positions));
                    }
                }
//                if (positions==0){
//                    //请求全部周
//                    getQbData();
//                    preferences.edit().putBoolean("isAll",true).commit();
//                }else {
                    //请求单独周
                   if(dataListWeeks.size()!=0) {
                       getData(Integer.parseInt(dataListWeeks.get(positions).get("id").toString()));
                       preferences.edit().putBoolean("isAll", false).commit();
                   }
//                }
                break;
        }
    }

    //全部周
    private void getQbData() {
        headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
        OkGo.get(HostAdress.getRequest("/api/phone/v1/teacher/getassessallweek")).tag(this)
                .headers(headers).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                if (android.text.TextUtils.isEmpty(s)) {
                    DianTool.showTextToast(getApplicationContext(), getResources().getString(R.string.meiju));
                } else {
                    List<Course> array = JSON.parseArray(s, Course.class);
                    adapter = new PjFirstAdapter(array, PingjiaoActivity.this);
                    pj_listView.setAdapter(adapter);
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                DianTool.response(response,PingjiaoActivity.this);
            }
        });
    }

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        positions=position;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
