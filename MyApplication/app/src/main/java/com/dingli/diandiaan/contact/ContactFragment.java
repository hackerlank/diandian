package com.dingli.diandiaan.contact;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.bean.ServerApi;
import com.dingli.diandiaan.common.CommInterface;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.Data;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.ResultInfoCallOne;
import com.dingli.diandiaan.login.ActionSheetDialog;
import com.dingli.diandiaan.rule.ListItemRulView;
import com.dingli.diandiaan.view.MyListView;
import com.dingli.diandiaan.view.VerticalSwipeRefreshLayout;
import com.dingli.diandiaan.view.XListView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dingliyuangong on 2017/5/9.
 */

public class ContactFragment extends Fragment implements ListContactView.ContactListener,XListView.IXListViewListener,CommInterface,View.OnClickListener{

    VerticalSwipeRefreshLayout refercontact;
    LinearLayout linearClass,linearInstruc,lineafrag;
    MyListView myClasslist,myInstruclist;
    XListView listviewcontact;
    HttpHeaders header;
    ContactSencondAdapter adapterSecond;
    ContactInstrucAdapter adaterintruc;
    ContactAdapter adapter;
    int currentpage;
    String phones,names,stuId;
    int call,contact,resume;
    int totalCount;
    View viewfrag;
    int sdkversion;
    int refr;
    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_contact,container,false);
        initview(view);
        return view;
    }
    void initview(View v){
        refercontact=(VerticalSwipeRefreshLayout) v.findViewById(R.id.refercontact);
        DianTool.refresh(refercontact,getActivity());
        listviewcontact=(XListView) v.findViewById(R.id.listviewcontact);
        ImageView ivsearch=(ImageView) v.findViewById(R.id.ivsearch);
        ivsearch.setOnClickListener(this);
        listviewcontact.addHeaderView(getViews());
        adapter=new ContactAdapter(getActivity(),"staff",this);
        listviewcontact.setAdapter(adapter);
        listviewcontact.setXListViewListener(this);
        listviewcontact.setPullRefreshEnable(true);
        listviewcontact.setPullLoadEnable(true);
    }
    private View getViews(){
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.contact_header,null);
        linearClass=(LinearLayout) view.findViewById(R.id.linearclass);
        linearInstruc=(LinearLayout) view.findViewById(R.id.linearinstruc);
        lineafrag=(LinearLayout) view.findViewById(R.id.lineafrag);
        viewfrag=view.findViewById(R.id.viewfrag);
        myClasslist=(MyListView)view.findViewById(R.id.myclasslist);
        adapterSecond=new ContactSencondAdapter(this);
        myClasslist.setAdapter(adapterSecond);
        myInstruclist=(MyListView) view.findViewById(R.id.myinstruclist);
        adaterintruc=new ContactInstrucAdapter(this);
        myInstruclist.setAdapter(adaterintruc);
        return view;
    }
    void initeacher(){
       OkGo.get(HostAdress.getRequest(ServerApi.StaffClass)).tag(this)
               .headers(header).execute(new StringCallback() {
           @Override
           public void onSuccess(String s, Call call, Response response) {
               if (!TextUtils.isEmpty(s)) {
                   if (!s.equals("[]")) {
                       List<Data> listdata = JSON.parseArray(s, Data.class);
                       if (listdata.size() != 0) {
                           linearClass.setVisibility(View.VISIBLE);
                           adapterSecond.refreshClass(listdata);
                           adapterSecond.notifyDataSetChanged();
                       } else {
                           linearClass.setVisibility(View.GONE);
                       }
                   }
               }
           }
           @Override
           public void onError(Call call, Response response, Exception e) {
               linearClass.setVisibility(View.GONE);
               DianTool.response(response,getActivity());
           }
       });
    }
    void initinstruc(){
        OkGo.get(HostAdress.getRequest(ServerApi.StaffIn)).tag(this)
                .headers(header).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                if (!TextUtils.isEmpty(s)) {
                    if (!s.equals("{}")) {
                        Data data = JSON.parseObject(s, Data.class);
                        if (data.success) {
                            linearInstruc.setVisibility(View.VISIBLE);
                            initstruc();
                        } else {
                            linearInstruc.setVisibility(View.GONE);
                        }
                    }
                }
            }
            @Override
            public void onError(Call call, Response response, Exception e) {
                linearInstruc.setVisibility(View.GONE);
                DianTool.response(response,getActivity());
            }
        });
    }
    void initstruc(){
        OkGo.get(HostAdress.getRequest(ServerApi.StaffIns)).tag(this)
                .headers(header).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                if (!TextUtils.isEmpty(s)) {
                    if (!s.equals("{}")) {
                        ResultInfoCallOne resultCallOne = JSON.parseObject(s, ResultInfoCallOne.class);
                        if (resultCallOne.data.size() != 0) {
                            adaterintruc.refreInsContact(resultCallOne.data);
                            adaterintruc.notifyDataSetChanged();
                        }
                    }
                }
            }
            @Override
            public void onError(Call call, Response response, Exception e) {
                DianTool.response(response,getActivity());
            }
        });
    }
    void initdata(int page,final int style){
        HttpParams params=new HttpParams();
        params.put("offset",page);
        currentpage=page;
        OkGo.get(HostAdress.getRequest(ServerApi.Staff)).tag(this)
                .headers(header).params(params).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                      onrefresh();
                if (!TextUtils.isEmpty(s)) {
                    if (!s.equals("{}")) {
                        ResultInfoCallOne resultCallOne = JSON.parseObject(s, ResultInfoCallOne.class);
                        totalCount=resultCallOne.totalCount;
                        if (resultCallOne.data.size() != 0) {
                            lineafrag.setVisibility(View.VISIBLE);
                            viewfrag.setVisibility(View.VISIBLE);
                            switch (style) {
                                case Constant.REFRESH:
                                    adapter.refreshListContact(resultCallOne.data);
                                    adapter.notifyDataSetChanged();
                                    break;
                                case Constant.LOAD_MORE:
                                    adapter.addListContact(resultCallOne.data);
                                    adapter.notifyDataSetChanged();
                                    break;
                            }
                        }
                    }
                }
            }
            @Override
            public void onError(Call call, Response response, Exception e) {
                   onrefresh();
                DianTool.response(response,getActivity());
            }
        });
    }
    void onrefresh(){
        refercontact.setRefreshing(false);
        refercontact.setEnabled(false);
        listviewcontact.stopRefresh();
        listviewcontact.stopLoadMore();
    }
    void nonerefr(){
        refercontact.setRefreshing(false);
        refercontact.setEnabled(false);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
    @Override
    public void onContactListener(String tv, final String phone, String name, final String stuIds) {
        if (!TextUtils.isEmpty(tv)) {
            phones = phone;
            names = name;
            stuId = stuIds;
            new ActionSheetDialog(getActivity())
                    .builder()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem("拨打电话", ActionSheetDialog.SheetItemColor.Green,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public void onClick(int which) {
                                    call = 1;
                                    callphone(phone, stuIds);
                                }
                            })
                    .addSheetItem("保存到手机通讯录", ActionSheetDialog.SheetItemColor.Green,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public void onClick(int which) {
                                    contact = 1;
                                    baocuncontact(phone);
                                }
                            })
                    .addSheetItem("复制", ActionSheetDialog.SheetItemColor.Green,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                    clipboardManager.setText(phone);
                                }
                            })
                    .show();
        }
    }
    @Override
    public void onRefresh() {
        nonerefr();
        if (DianTool.isConnectionNetWork(getActivity())) {
            DianTool.huoqutoken();
            header.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            initeacher();
            initinstruc();
            initdata(1, Constant.REFRESH);
        } else {
            onrefresh();
            DianTool.showTextToast(getActivity(), "请检查网络");
        }
    }

    @Override
    public void onLoadMore() {
        nonerefr();
        DianTool.huoqutoken();
        header.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
        initeacher();
        initinstruc();
        if (currentpage<totalCount) {
            initdata(++currentpage, Constant.LOAD_MORE);
        }else{
            onrefresh();
            DianTool.showTextToast(getActivity(),"没有数据啦!");
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    void callphone(String phone, String stuIds){
        if (DianTool.getsdkbanbe() > 22) {
            sdkversion=1;
            int hasWriteContactsPermission = getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                        1004);
                return;
            }
        }
        resume=1;
        baoda(phone, stuIds);
    }
    void baocuncontact(String phone){
        if (DianTool.getsdkbanbe()>22){
            int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_CONTACTS);
            int hasWriteContactsPermissions = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_CONTACTS);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED||
                    hasWriteContactsPermissions != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS
                        },
                        1002);
                return;
            }
        }
        if (!phone.equals("null")) {
            DianTool.zhiding(getActivity(),phones,names);
        }
    }
    void baoda(String phone,String stuIds){
        if (!phone.equals("null")) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
            startActivity(intent);
            if (DianTool.isConnectionNetWork(getActivity())){
                JSONObject jsonObject=new JSONObject();
                HttpHeaders headers=new HttpHeaders();
                DianTool.huoqutoken();
                headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
                try {
                    jsonObject.put("calledPhone",phone);
                    jsonObject.put("calledStudentId",stuIds);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (DianTool.isConnectionNetWork(getActivity())) {
                    OkGo.post(HostAdress.getRequest(ServerApi.StaffBo)).headers(headers).tag(this).upJson(jsonObject)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                }
                            });
                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1002){
            if (permissions[0].equals(Manifest.permission.WRITE_CONTACTS)
                    &&grantResults[0] == PackageManager.PERMISSION_GRANTED
                    &&permissions[1].equals(Manifest.permission.READ_CONTACTS)
                    &&grantResults[1] == PackageManager.PERMISSION_GRANTED){
                if (!phones.equals("null")) {
                    DianTool.zhiding(getActivity(),phones,names);
                }
            }else{
                //用户不同意，向用户展示该权限作用
                quanxian(Manifest.permission.WRITE_CONTACTS,"保存到通讯录且读取通讯录");
            }
        }else if (requestCode==1004){
            if (permissions[0].equals(Manifest.permission.CALL_PHONE)
                    &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (!phones.equals("null")) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phones));
                    startActivity(intent);
                }
            }else{
                quanxian(Manifest.permission.CALL_PHONE,"打电话");
            }
        }
    }
    int zizengs;
    void quanxian(String quanxian,String s){
        if (zizengs==0) {
            if (!shouldShowRequestPermissionRationale(quanxian)) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setMessage("该功能需要赋予调用" + s + "的权限，不开启将无法正常工作！")
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
                zizengs=1;
                return;
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
            header=new HttpHeaders();
            DianTool.huoqutoken();
            header.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            if (DianTool.isConnectionNetWork(getActivity())) {
                if (refr==0) {
                    refr=1;
                    refercontact.setRefreshing(true);
                }
                initeacher();
                initinstruc();
                initdata(1, Constant.REFRESH);
            } else {
                onrefresh();
                DianTool.showTextToast(getActivity(), "请检查网络");
            }
        }
    }
    @Override
    public void KaiOrGuan(ListItemRulView ruleview, int id, String str, String name, int lateTime) {
        Intent intent=new Intent();
        if (str.equals("sencond")){
            intent.setClass(getActivity(),ContactStaffActivity.class);
            intent.putExtra("id",id);
            startintents(intent);
        }else{
            intent.setClass(getActivity(),ContactClassActivity.class);
            intent.putExtra("id",id);
            intent.putExtra("name",name);
            startintents(intent);
        }
    }
    @Override
    public void select(int id, ImageView iv) {
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivsearch:
                Intent intent=new Intent(getActivity(),ContactSearchActivity.class);
                startintents(intent);
                break;
        }
    }
    void startintents(Intent intent){
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }
}
