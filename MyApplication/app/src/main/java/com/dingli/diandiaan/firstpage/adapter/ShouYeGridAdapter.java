package com.dingli.diandiaan.firstpage.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.Constant;
import com.dingli.diandiaan.common.DianApplication;
import com.dingli.diandiaan.common.DianTool;
import com.dingli.diandiaan.common.Initoken;
import com.dingli.diandiaan.common.ResultOne;
import com.dingli.diandiaan.firstpage.FirstPageFragment;
import com.dingli.diandiaan.firstpage.WebViewOneActivity;
import com.dingli.diandiaan.firstpage.WebViewThreeActivity;
import com.dingli.diandiaan.firstpage.WebViewsActivity;
import com.dingli.diandiaan.information.EntrtyActivity;
import com.dingli.diandiaan.instruc.InstructorActivity;
import com.dingli.diandiaan.login.LoginActivity;
import com.dingli.diandiaan.login.daochu.DaochuWebActivity;
import com.dingli.diandiaan.login.daochu.DaochuWebsActivity;
import com.dingli.diandiaan.login.pingjiao.PingjiaoActivity;
import com.dingli.diandiaan.lostproperty.WebViewLostActivity;
import com.dingli.diandiaan.rule.RuleActivity;
import com.dingli.diandiaan.schedule.SyllFragments;
import com.dingli.diandiaan.school.SchoolWebActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by dingliyuangong on 2017/5/4.
 */

public class ShouYeGridAdapter extends BaseAdapter{
    List<ResultOne> listgride;
    public ShouYeGridAdapter(List<ResultOne> listgr){
        this.listgride=listgr;
    }
    @Override
    public int getCount() {
        return listgride.size();
    }

    @Override
    public Object getItem(int position) {
        return listgride.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ShouYeHolder shouYeHolder=null;
        if (convertView==null){
            shouYeHolder=new ShouYeHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shouyes,null);
            shouYeHolder.iv=(ImageView) convertView.findViewById(R.id.ivd);
            shouYeHolder.tv=(TextView) convertView.findViewById(R.id.titlepage);
            shouYeHolder.grideitem=(LinearLayout) convertView.findViewById(R.id.grideitem);
            convertView.setTag(shouYeHolder);
        }else {
            shouYeHolder=(ShouYeHolder) convertView.getTag();
        }
        Glide.with(parent.getContext()).load(listgride.get(position).iconUrl).into(shouYeHolder.iv);
        shouYeHolder.tv.setText(listgride.get(position).title);
        final String title=listgride.get(position).title.trim();
        final String targetTypes=listgride.get(position).targetType.trim();
        final String targetUrl=listgride.get(position).targetUrl.trim();
        final boolean isNeedLogin=listgride.get(position).isNeedLogin;
        final String targetTitle=listgride.get(position).targetTitle;
        shouYeHolder.grideitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(parent.getContext(),title);
                Intent intent=new Intent();
                if (targetTypes.equals("app")) {
                    switch (targetUrl) {
                        case "DLHomeDianMing":
                        if (TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))) {
                            intent.setClass(parent.getContext(), LoginActivity.class);
                        } else {
                            intent.setClass(parent.getContext(), RuleActivity.class);
                        }
                        parent.getContext().startActivity(intent);
                        break;
                        case "DLHomeCallSet":
                            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                intent.setClass(parent.getContext(), LoginActivity.class);
                                parent.getContext().startActivity(intent);
                            }else {
                                Initoken.SingId(parent.getContext());
                            }
                            break;
                        case "DLHomeApproval":
                            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                intent.setClass(parent.getContext(),LoginActivity.class);
                            }else {
                                intent.setClass(parent.getContext(), EntrtyActivity.class);
                            }
                            parent.getContext().startActivity(intent);
                            break;
                        case "DLHomeReMen":
                            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                intent.setClass(parent.getContext(),LoginActivity.class);
                            }else {
                                intent.setClass(parent.getContext(), FirstPageFragment.class);
                            }
                            parent.getContext().startActivity(intent);
                            break;
                        case "DLHomeCourseScore":
                            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                intent.setClass(parent.getContext(), LoginActivity.class);
                            }else {
                                intent.setClass(parent.getContext(), PingjiaoActivity.class);
                            }
                            parent.getContext().startActivity(intent);
                            break;
                        case "assistantCall":
                            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                intent.setClass(parent.getContext(), LoginActivity.class);
                            }else {
                                intent.setClass(parent.getContext(), InstructorActivity.class);
                            }
                            parent.getContext().startActivity(intent);
                            break;
                        case "DLHomeKeBiao":
                            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                intent.setClass(parent.getContext(), LoginActivity.class);
                            }else {
                                intent.setClass(parent.getContext(), SyllFragments.class);
                            }
                            parent.getContext().startActivity(intent);
                            break;
                        default:
                            DianTool.showTextToast(parent.getContext(),"当前版本不支持此功能，请升级最新版本");
                            break;
                    }
                }else if (targetTypes.equals("web")){
                    switch (title) {
                        case "导出报表":
                            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                intent.setClass(parent.getContext(), LoginActivity.class);
                            }else {
                                intent.setClass(parent.getContext(), DaochuWebsActivity.class);
                            }
                            parent.getContext().startActivity(intent);
                            break;
                        case "校园动态":
                            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                intent.setClass(parent.getContext(), LoginActivity.class);
                            }else {
                                intent.setClass(parent.getContext(), SchoolWebActivity.class);
                                intent.putExtra("url", "/mobileui/schoolNews");
                                intent.putExtra("title", "校园动态");
                                intent.putExtra("isRefresh", false);
                                intent.putExtra("isStatusBar", true);
                                intent.putExtra("domainName", "dd_mobile");
                            }
                            parent.getContext().startActivity(intent);
                            break;
                        case "失物招领":
                            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                intent.setClass(parent.getContext(), LoginActivity.class);
                            }else {
                                intent.setClass(parent.getContext(), WebViewLostActivity.class);
                            }
                            parent.getContext().startActivity(intent);
                            break;
                        case "点点心理":
                            if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                intent.setClass(parent.getContext(), LoginActivity.class);
                            }else {
                                intent.setClass(parent.getContext(), WebViewsActivity.class);
                                intent.putExtra("url", "/mobileui/allArticle");
                            }
                            parent.getContext().startActivity(intent);
                            break;
                        default:
                            if (isNeedLogin){
                                if(TextUtils.isEmpty(DianApplication.sharedPreferences.getStringValue(Constant.SPLITONE))){
                                    intent.setClass(parent.getContext(), LoginActivity.class);
                                }else {
                                    intent.setClass(parent.getContext(), WebViewThreeActivity.class);
                                    intent.putExtra("targetTitle",targetTitle);
                                    intent.putExtra("targetUrl",targetUrl);
                                }
                            }else{
                                intent.setClass(parent.getContext(), WebViewThreeActivity.class);
                                intent.putExtra("targetTitle",targetTitle);
                                intent.putExtra("targetUrl",targetUrl);
                            }
                            parent.getContext().startActivity(intent);
                             break;
                    }
                }
            }
        });
        return convertView;
    }
    class ShouYeHolder{
        ImageView iv;
        TextView tv;
        LinearLayout grideitem;
    }
}
