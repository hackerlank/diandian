package com.dingli.diandiaan.login.chuqinlv;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.dingli.diandiaan.R;

import java.util.ArrayList;
import java.util.List;

public class CustomPopupWindow extends PopupWindow implements WheelPicker.OnItemSelectedListener{

    private Context mContext;
    private View view;
    private TextView btn_submit, btn_cancel;
    private String text_data;
    private List<String> list = new ArrayList<>();
    private static int  positions;

    public static int getPositions() {
        return positions;
    }

    public static void setPositions(int positions) {
        CustomPopupWindow.positions = positions;
    }

    public CustomPopupWindow(Context mContext, View.OnClickListener itemsOnClick, List<String> data) {

        this.view = LayoutInflater.from(mContext).inflate(R.layout.cql_dialog, null);
        btn_submit = (TextView) view.findViewById(R.id.btn_submit);
        btn_cancel = (TextView) view.findViewById(R.id.btn_cancel);
        list = data;
        WheelPicker wheelCenter = (WheelPicker) view.findViewById(R.id.main_wheel_center);
        wheelCenter.setData(list);
        wheelCenter.setOnItemSelectedListener(this);
            // 取消按钮
            btn_cancel.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // 销毁弹出框
                    dismiss();
                }
            });
            // 设置按钮监听
            btn_submit.setOnClickListener(itemsOnClick);

            // 设置外部可点击
            this.setOutsideTouchable(true);
            // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
            this.view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {

                    int height = view.findViewById(R.id.pop_layout).getTop();

                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y > height) {
                            dismiss();
                        }
                    }
                    return true;
                }
            });


    /* 设置弹出窗口特征 */
            // 设置视图
            this.setContentView(this.view);
            // 设置弹出窗体的宽和高
            this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
            this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
            // 设置弹出窗体可点击
            this.setFocusable(true);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置弹出窗体的背景
            this.setBackgroundDrawable(dw);

            // 设置弹出窗体显示时的动画，从底部向上弹出
            this.setAnimationStyle(R.style.pop_anim);

        }

//    public void setData(List list){this.list=list;}
//    private List getData() {
//        return list;
//    }
//    public void setText(String name1,String name2){
//        btn_submit.setText(name1);
//        btn_cancel.setText(name2);
//    }

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        String text = "";
        switch (picker.getId()) {
            case R.id.main_wheel_center:
                text = "选择";
                break;
        }
        text_data=String.valueOf(data);
       setPositions(position);
//        Toast.makeText(mContext, text + String.valueOf(data), Toast.LENGTH_SHORT).show();
    }
    public void setText(TextView textView){
        textView.setText(text_data);
    }


}

