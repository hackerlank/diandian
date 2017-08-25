package com.dingli.diandiaan.login.chuqinlv;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.common.MyDialog;
import com.dingli.diandiaan.common.ResultInfo;
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

public class CqlActivity extends BaseActivity implements View.OnClickListener,WheelPicker.OnItemSelectedListener{

    private RelativeLayout title_bar;
    private ListView cql_listView;
    private TextView tv_selected_week;
    private List<JSONObject> list0;
    private ImageView cqlBack;
    private List<String> dataList;//存放wheelpicker的数据集合
    private List<Map<String,Object>> dataListWeeks;//存放周列表的数据集合
    private CqlFirstAdapter adapterFirst;
    private ResultInfo resultInfoweek;
    private static String currentWeekName;
    private static int currentWeekId = 0;
    private PopupWindow popupWindow;
    private TextView tv_cancle;
    private TextView tv_title;
    private TextView tv_selected;
    private WheelPicker picker;
    private static int positions=0;
    private SharedPreferences preferences;
    boolean firstIn = true;
    HttpHeaders headers;
    VerticalSwipeRefreshLayout refcql;
//    private int weekid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cql);
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        initView();
       InitPopwindows();
        refcql.setRefreshing(true);
        getCurrentWeek();
    }
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
        picker.setData(dataList);
    }

    private void initView() {
        refcql=(VerticalSwipeRefreshLayout)findViewById(R.id.refcql);
        refcql.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
        refcql.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCurrentWeek();
            }
        });
        title_bar= (RelativeLayout) findViewById(R.id.title_bar);
        tv_selected_week= (TextView) findViewById(R.id.tv_selected_weeks);
        tv_selected_week.setOnClickListener(this);
        preferences=getSharedPreferences("aizhixin_info",MODE_PRIVATE);
        cqlBack= (ImageView) findViewById(R.id.cqlback);
        cqlBack.setOnClickListener(this);
        cql_listView= (ListView) findViewById(R.id.cql_listView);
        refcql.setViewGroup(cql_listView);
        cql_listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                 if (firstVisibleItem==0){
                     refcql.setEnabled(true);
                 }else{
                     refcql.setEnabled(false);
                 }
            }
        });
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        list0=new ArrayList<>();
        dataList=new ArrayList<>();
        dataListWeeks=new ArrayList<>();

        dataList.add("全部周");
        Map<String,Object> map1 = new HashMap<String, Object>();
        map1.put("name","全部周");
        map1.put("id",0);
        dataListWeeks.add(map1);

    }

    /**
     * 获取全部周的出勤率
     */
    private void initQbData(){
        headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
        OkGo.get(HostAdress.getRequest("/api/phone/v1/teacher/getrollcallbyallweek")).tag(this)
                .headers(headers).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                if (TextUtils.isEmpty(s)) {
                    DianTool.showTextToast(getApplicationContext(), getResources().getString(R.string.meiju));
                } else {
                    if (list0.size()!=0){
                        list0.clear();
                    }
                    try {
                        JSONArray array = new JSONArray(s);
                        if (array.length() > 0) {
                            for (int j = 0; j < array.length(); j++) {
                                JSONObject object = array.optJSONObject(j);
                                list0.add(object);
                            }
                            adapterFirst = new CqlFirstAdapter(list0, CqlActivity.this);
                            cql_listView.setAdapter(adapterFirst);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                DianTool.response(response,CqlActivity.this);
            }
        });
    }
    /**
     * 获取单独某一周的出勤率
     */
    private void initData(int weekid) {
        if (DianTool.isConnectionNetWork(this)) {
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            HttpParams params = new HttpParams();
            params.put("weekId", weekid);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/teacher/getrollcallbyweek")).tag(this)
                    .headers(headers).params(params).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    refcql.setRefreshing(false);
                    if (TextUtils.isEmpty(s)) {
                        DianTool.showTextToast(CqlActivity.this, "数据为空");
                    } else {
                        if (list0.size()!=0){
                            list0.clear();
                        }
                        try {
                            JSONArray array = new JSONArray(s);
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.optJSONObject(i);
                                    list0.add(object);
                                }
                                adapterFirst = new CqlFirstAdapter(list0, CqlActivity.this);
                                cql_listView.setAdapter(adapterFirst);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    refcql.setRefreshing(false);
                    DianTool.response(response,CqlActivity.this);
                }
            });
        }else{
            DianTool.showTextToast(this, "请检查网络");
        }
    }

    /**
     * 请求所有周的(周列表)
     */
    public void getAllWeek(){
        headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
        OkGo.get(HostAdress.getRequest("/api/phone/v1/week/getList")).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                if (TextUtils.isEmpty(s)) {
                    refcql.setRefreshing(false);
                    DianTool.showTextToast(CqlActivity.this, "当前周没有数据");
                } else {
                    if (dataList.size()!=0){
                        dataList.clear();
                    }
                    if (dataListWeeks.size()!=0){
                        dataListWeeks.clear();
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
                    initData(currentWeekId);
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                DianTool.dissMyDialog();
                DianTool.response(response,CqlActivity.this);
            }
        });
    }

    /**
     * 获取当前周
     */
    public void getCurrentWeek(){
        if(DianTool.isConnectionNetWork(this)) {
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/week/get")).tag(this).headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    if (TextUtils.isEmpty(s)) {
                        refcql.setRefreshing(false);
                        DianTool.showTextToast(CqlActivity.this, "当前周没有数据");
                    } else {
                        resultInfoweek = JSON.parseObject(s, ResultInfo.class);
                        currentWeekName = resultInfoweek.name;
                        currentWeekId = resultInfoweek.id;
                        getAllWeek();
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    refcql.setRefreshing(false);
                    DianTool.response(response,CqlActivity.this);
                }
            });
        }else{
            DianTool.showTextToast(this,"请检查网络");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_selected_weeks:
                popupWindow.showAsDropDown(title_bar,0,0);
                break;
            case R.id.cqlback:
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

                //全部就是true...
                if(dataList.size()!=0) {
                    if(dataList.get(positions).contains("本周")){
                        tv_selected_week.setText(dataList.get(positions).substring(0,dataList.get(positions).indexOf("(")));
                    }else {
                        tv_selected_week.setText(dataList.get(positions));
                    }
                }
                    if (positions == 0) {
                        initQbData();
                        preferences.edit().putBoolean("isAll", true).commit();
                    } else {
                if(dataListWeeks.size()!=0) {
                        initData(Integer.parseInt(dataListWeeks.get(positions).get("id").toString()));
                        preferences.edit().putBoolean("isAll", false).commit();
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        positions=position;
    }
}
