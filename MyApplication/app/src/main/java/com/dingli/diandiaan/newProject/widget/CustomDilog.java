package com.dingli.diandiaan.newProject.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 自定义DiaLog
 * 用于权限判断 跳转应用设置界面
 * Created by Administrator on 2016/5/31.
 */
public class CustomDilog {

    public static void dialog(final Context context) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setMessage("当前应用缺少相机权限\n" + "请打开所需权限");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public static void dialogFinishActivity(final Context context, final Activity activity) {
        try{
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
            builder.setMessage("当前应用缺少相机权限\n" + "请打开所需权限");
            builder.setTitle("提示");
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    activity.finish();
                }
            });
            builder.create().show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void  ordersDialog(Context context, String title, String message, DialogInterface.OnClickListener negativeClickListener){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("确定", negativeClickListener);
        builder.setPositiveButton("取消" ,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }


}
