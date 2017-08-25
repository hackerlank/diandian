package com.dingli.diandiaan.information;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.MainActivity;
import com.dingli.diandiaan.MaintiAcvity;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.common.MyDialog;
import com.dingli.diandiaan.common.QingJiaSty;
import com.dingli.diandiaan.common.ResultInfo;
import com.dingli.diandiaan.information.adapter.InfomationAdapter;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;
import com.dingli.diandiaan.view.XListView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by dingliyuangong on 2016/6/13.
 */
public class InformationFragment extends Fragment implements View.OnClickListener{

  RelativeLayout rlqingjia,rltongzhi,rljinggao;
    public static InformationFragment newInstance(){
        InformationFragment informationFragment=new InformationFragment();
        return  informationFragment;
    }
    TextView tvshuzi,tvshuzis,tvshenqing,tvyuedu,tvjinggaod;
    TextView tvtzsj,tvtzsjs,tvjinggaosd,tvjinggaos;
    View views;
    HttpHeaders headers;
    String dianjiId;
    VerticalSwipeRefreshLayout refinfor;
    int refre;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         views=inflater.inflate(R.layout.fragment_information,container,false);
        headers=new HttpHeaders();
        headers.put("Content-Type", Constant.APPLICATION_FORMURL);
        headers.put("Encoding", "UTF-8");
        headers.put("Accept", Constant.APPLICATION_JSON);
        initview(views);
        return views;
    }
    @Override
    public void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
            if (refre==0) {
                refre=1;
                refinfor.setRefreshing(true);
            }
            initdata();
        }
    }

    void initview(View v){
        refinfor=(VerticalSwipeRefreshLayout)v.findViewById(R.id.refinfor);
        refinfor.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
        refinfor.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initdata();
            }
        });
        tvyuedu=(TextView)v.findViewById(R.id.tvyuedu);
        tvshenqing=(TextView)v.findViewById(R.id.tvshenqing);
        rlqingjia=(RelativeLayout)v.findViewById(R.id.rlqingjia);
        rltongzhi=(RelativeLayout)v.findViewById(R.id.rltongzhi);
        tvshuzi=(TextView)v.findViewById(R.id.tvshuzi);
        tvshuzis=(TextView)v.findViewById(R.id.tvshuzis);
        tvtzsj=(TextView)v.findViewById(R.id.tvtzsj);
        tvtzsjs=(TextView)v.findViewById(R.id.tvtzsjs);
        tvjinggaosd=(TextView)v.findViewById(R.id.tvjinggaosd);
        tvjinggaod=(TextView)v.findViewById(R.id.tvjinggaod);
        tvjinggaos=(TextView)v.findViewById(R.id.tvjinggaos);
        rljinggao=(RelativeLayout)v.findViewById(R.id.rljinggao);
        View viewjinggao=v.findViewById(R.id.viewjinggao);
        if (DianApplication.sharedPreferences.getStringValue(Constant.ROLE).equals("ROLE_CLASSROOMTEACHE")){
            rljinggao.setVisibility(View.VISIBLE);
            viewjinggao.setVisibility(View.VISIBLE);
        }else{
            rljinggao.setVisibility(View.GONE);
            viewjinggao.setVisibility(View.GONE);
        }
        rljinggao.setOnClickListener(this);
        rltongzhi.setOnClickListener(this);
        rlqingjia.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rljinggao:
                dianjiId="jinggao";
                rljinggao.setClickable(false);
                initdatas();
                break;
            case R.id.rlqingjia:
                dianjiId="qingjia";
                rlqingjia.setClickable(false);
                initdatas();
                break;
            case R.id.rltongzhi:
                dianjiId="tongzhi";
                rltongzhi.setClickable(false);
                initdatas();
                break;
        }
    }
    void initdata(){
        if(DianTool.isConnectionNetWork(getActivity())){
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/getstatus")).tag(this)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    refinfor.setRefreshing(false);
                    if (s.length() != 0) {
                        List<QingJiaSty> arg = JSON.parseArray(s, QingJiaSty.class);
                        for (int i = 0; i < arg.size(); i++) {
                            if (arg.get(i).module.equals("leave")) {
                                if (!TextUtils.isEmpty(arg.get(i).function)) {
                                    if (arg.get(i).function.equals("teacher_approval")) {
                                        if (arg.get(i).notRead != 0) {
                                            tvshuzi.setVisibility(View.VISIBLE);
                                            tvshuzi.setText(String.valueOf(arg.get(i).notRead));
                                            tvshuzi.setBackgroundResource(R.drawable.circle_shuzi);
                                            tvshenqing.setText("你有新的请假申请未处理");
                                        } else {
                                            tvshenqing.setText("你的请假申请已经处理");
                                            tvshuzi.setVisibility(View.GONE);
                                        }
                                        tvtzsj.setText(arg.get(i).lastPushTime);
                                    }
                                    if (arg.get(i).function.equals("teacher_notice")) {
                                        if (arg.get(i).notRead != 0) {
                                            tvshuzis.setVisibility(View.VISIBLE);
                                            tvshuzis.setText(String.valueOf(arg.get(i).notRead));
                                            tvshuzis.setBackgroundResource(R.drawable.circle_shuzi);
                                            tvyuedu.setText("你有新的请假通知未阅读");
                                        } else {
                                            tvshuzis.setVisibility(View.GONE);
                                            tvyuedu.setText("你的请假通知已阅读");
                                        }
                                        tvtzsjs.setText(arg.get(i).lastPushTime);
                                    }
                                }
                            }
                            if (arg.get(i).module.equals("electricFence")) {
                                if (!TextUtils.isEmpty(arg.get(i).function)) {
                                    if (arg.get(i).function.equals("leaveTeacher_notice")) {
                                        if (arg.get(i).notRead!=0){
                                            tvjinggaos.setVisibility(View.VISIBLE);
                                            tvjinggaos.setText(String.valueOf(arg.get(i).notRead));
                                            tvjinggaod.setText("你有新的警报提醒未查看");
                                        }else{
                                            tvjinggaos.setVisibility(View.GONE);
                                            tvjinggaod.setText("无警报提醒");
                                        }
                                        tvjinggaosd.setText(arg.get(i).lastPushTime);
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    refinfor.setRefreshing(false);
                   DianTool.response(response,getActivity());
                }
            });
        } else {
            refinfor.setRefreshing(false);
            DianTool.showTextToast(getActivity(), "请检查网络");
        }
    }
    void initdatas(){
        if(DianTool.isConnectionNetWork(getActivity())){
            DianTool.huoqutoken();
            HttpParams params=new HttpParams();
            if (dianjiId.equals("qingjia")){
                params.put("module","leave");
                params.put("function","teacher_approval");
            }else if (dianjiId.equals("tongzhi")){
                params.put("module","leave");
                params.put("function","teacher_notice");
            }else{
                params.put("module","electricFence");
                params.put("function","leaveTeacher_notice");
            }
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getRequest("/api/phone/v1/get")).tag(this).params(params)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    rlqingjia.setClickable(true);
                    rltongzhi.setClickable(true);
                    rljinggao.setClickable(true);
                    if (dianjiId.equals("tongzhi")) {
                        Intent intent1 = new Intent(getActivity(), QingJiaMessActivity.class);
                        tvshuzis.setVisibility(View.GONE);
                        tvshuzis.setBackgroundResource(R.drawable.circle_shuziwu);
                        tvyuedu.setText("你的请假通知已阅读");
                        startActivity(intent1);
                        getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    }else if (dianjiId.equals("jinggao")){
                        Intent intent=new Intent(getActivity(),WarningActivity.class);
                        tvjinggaos.setVisibility(View.GONE);
                        tvjinggaod.setText("无警报提醒");
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
                    }else if (dianjiId.equals("qingjia")){
                        Intent intent=new Intent(getActivity(),EntrtyActivity.class);
                        tvshuzi.setVisibility(View.GONE);
                        tvshuzi.setBackgroundResource(R.drawable.circle_shuziwu);
                        tvshenqing.setText("你的请假申请已经处理");
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    }
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    rlqingjia.setClickable(true);
                    rltongzhi.setClickable(true);
                    rljinggao.setClickable(true);
                }
            });
        } else {
            DianTool.showTextToast(getActivity(), "请检查网络");
        }
    }
}
