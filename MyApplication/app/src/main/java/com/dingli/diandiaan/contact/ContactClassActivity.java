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
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.bean.ServerApi;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.HostAdress;
import com.dingli.diandiaan.common.ResultInfoCallOne;
import com.dingli.diandiaan.login.ActionSheetDialog;
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
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by dingliyuangong on 2017/5/16.
 */

public class ContactClassActivity extends BaseActivity implements ListContactView.ContactListener, View.OnClickListener, XListView.IXListViewListener{

    VerticalSwipeRefreshLayout referstaff;
    int currentpage;
    int id;
    XListView listviewstaff;
    ContactAdapter adapter;
    String phones, names, stuId;
    RelativeLayout rlxianstaff;
    private static final int RC_CALL_PHONE= 123;
    private static final int RC_LOCATION_CONTACTS_PERM = 124;
    int totalCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staffcontact);
        initview();
        referstaff.setRefreshing(true);
        initdata(1, Constant.REFRESH);
    }

    void initview() {
        referstaff = (VerticalSwipeRefreshLayout) findViewById(R.id.referstaff);
        DianTool.refresh(referstaff, this);
        listviewstaff = (XListView) findViewById(R.id.listviewstaff);
        rlxianstaff=(RelativeLayout) findViewById(R.id.rlxianstaff);
        listviewstaff.setXListViewListener(this);
        listviewstaff.setPullLoadEnable(true);
        listviewstaff.setPullRefreshEnable(true);
        TextView tvstaff = (TextView) findViewById(R.id.tvstaffc);
        ImageView ivstaffback = (ImageView) findViewById(R.id.ivstaffback);
        ivstaffback.setOnClickListener(this);
        String name = getIntent().getStringExtra("name");
        id = getIntent().getIntExtra("id", 0);
        tvstaff.setText(name);
        adapter = new ContactAdapter(this, "student", this);
        listviewstaff.setAdapter(adapter);
    }

    void initdata(int page, final int style) {
        if (DianTool.isConnectionNetWork(this)) {
            HttpHeaders headers = new HttpHeaders();
            HttpParams params = new HttpParams();
            params.put("offset", page);
            params.put("classId", id);
            currentpage = page;
            DianTool.huoqutoken();
            headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
            OkGo.get(HostAdress.getRequest(ServerApi.StaffClasse)).tag(this).params(params)
                    .headers(headers).execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    onrefreshs();
                    if (!TextUtils.isEmpty(s)) {
                        if (!s.equals("{}")) {
                            ResultInfoCallOne resultCallOne = JSON.parseObject(s, ResultInfoCallOne.class);
                            totalCount=resultCallOne.totalCount;
                            if (resultCallOne.data.size() != 0) {
                                listviewstaff.setVisibility(View.VISIBLE);
                                rlxianstaff.setVisibility(View.GONE);
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
                            } else {
                                if (currentpage == 1) {
                                    listviewstaff.setVisibility(View.GONE);
                                    rlxianstaff.setVisibility(View.VISIBLE);
                                } else {
                                    listviewstaff.setVisibility(View.VISIBLE);
                                    rlxianstaff.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    onrefreshs();
                    listviewstaff.setVisibility(View.GONE);
                    rlxianstaff.setVisibility(View.VISIBLE);
                }
            });
        } else {
            onrefreshs();
            DianTool.showTextToast(this, "请检查网络");
        }
    }

    void onrefreshs() {
        referstaff.setRefreshing(false);
        referstaff.setEnabled(false);
        listviewstaff.stopLoadMore();
        listviewstaff.stopRefresh();
    }

    @Override
    public void onContactListener(String tv, final String phone, String name, final String stuIds) {
        if (!TextUtils.isEmpty(tv)) {
            phones = phone;
            names = name;
            stuId = stuIds;
            new ActionSheetDialog(this)
                    .builder()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem("拨打电话", ActionSheetDialog.SheetItemColor.Green,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public void onClick(int which) {
                                    callphone(phone, stuIds);
                                }
                            })
                    .addSheetItem("保存到手机通讯录", ActionSheetDialog.SheetItemColor.Green,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public void onClick(int which) {
                                    baocuncontact(phone);
                                }
                            })
                    .addSheetItem("复制", ActionSheetDialog.SheetItemColor.Green,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    clipboardManager.setText(phone);
                                }
                            })
                    .show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivstaffback:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                break;
        }
    }

    @Override
    public void onRefresh() {
        nonerefresh();
        initdata(1, Constant.REFRESH);
    }
   void nonerefresh(){
       referstaff.setRefreshing(false);
       referstaff.setEnabled(false);
   }
    @Override
    public void onLoadMore() {
        nonerefresh();
        if (currentpage<totalCount) {
            initdata(++currentpage, Constant.LOAD_MORE);
        }else{
            onrefreshs();
            DianTool.showTextToast(this,"没有数据啦!");
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    void callphone(String phone, String stuIds) {
        if (DianTool.getsdkbanbe() > 22) {
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.CALL_PHONE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},
                        1004);
                return;
            }
        }
            baoda(phone, stuIds);
    }

    void baoda(String phone, String stuIds) {
        if (!phone.equals("null")) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
            if (DianTool.isConnectionNetWork(this)) {
                JSONObject jsonObject = new JSONObject();
                HttpHeaders headers = new HttpHeaders();
                DianTool.huoqutoken();
                headers.put("Authorization", DianApplication.user.token_type + "" + DianApplication.user.token);
                try {
                    jsonObject.put("calledPhone", phone);
                    jsonObject.put("calledStudentId", stuIds);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (DianTool.isConnectionNetWork(this)) {
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

    @TargetApi(Build.VERSION_CODES.M)
    void baocuncontact(String phone) {
        if (DianTool.getsdkbanbe() > 22) {
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_CONTACTS);
            int hasWriteContactsPermissions = checkSelfPermission(Manifest.permission.READ_CONTACTS);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED||
                    hasWriteContactsPermissions != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS,
                        },
                        1002);
                return;
            }
        }
        if (!phone.equals("null")) {
            DianTool.zhiding(this, phones, names);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1002) {
            if (permissions[0].equals(Manifest.permission.WRITE_CONTACTS)
                    &&grantResults[0] == PackageManager.PERMISSION_GRANTED
                    &&permissions[1].equals(Manifest.permission.READ_CONTACTS)
                    &&grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (!phones.equals("null")) {
                    DianTool.zhiding(this, phones, names);
                }
            } else {
                //用户不同意，向用户展示该权限作用
                quanxian(Manifest.permission.WRITE_CONTACTS, "保存到通讯录且读取通讯录");
            }
        } else if (requestCode == 1004) {
            if (permissions[0].equals(Manifest.permission.CALL_PHONE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!phones.equals("null")) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phones));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                }
            }else{
                quanxian(Manifest.permission.CALL_PHONE,"打电话");
            }
        }
    }
    int zizengs;
    void quanxian(String quanxian, String s){
        if (zizengs==0) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,quanxian)) {
                AlertDialog dialog = new AlertDialog.Builder(this)
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
}
