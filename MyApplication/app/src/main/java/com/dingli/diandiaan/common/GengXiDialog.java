package com.dingli.diandiaan.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dingli.diandiaan.R;


/**
 * Created by lpf on 2014/9/23 0023.
 */
public class GengXiDialog extends Dialog {

    TextView mTvSelectDialogTitle,tvSelectDialogTitleguan,tvneicontent;
    Button mBtnSelectDialogCancel;
    Button mBtnSelectDialogDetermine;
    SelectDialogButtonListener listener;
    Context context;
    String content;
    boolean isRequre;
    public GengXiDialog(Context context) {
        super(context);
        this.context = context;
    }

    public GengXiDialog(Context context, int theme) {
        super(context, R.style.HintDialogStyle);
        this.context = context;
    }

    public GengXiDialog(Context context,boolean isRequred,String content,SelectDialogButtonListener listener) {
        super(context, R.style.HintDialogStyle);
        this.context = context;
        this.content = content;
        this.listener = listener;
        this.isRequre=isRequred;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View container = getLayoutInflater().inflate(R.layout.dialog_gengxi, null);
        setContentView(container);
        initview(container);
        initListener();
    }
   void initview(View v){
       tvneicontent=(TextView)v.findViewById(R.id.tvneicontent);
       tvSelectDialogTitleguan=(TextView)v.findViewById(R.id.tvSelectDialogTitleguan);
       mBtnSelectDialogCancel=(Button)v.findViewById(R.id.btnSelectDialogCancelguan);
       mBtnSelectDialogDetermine=(Button)v.findViewById(R.id.btnSelectDialogDetermineguan);
       tvneicontent.setText(content.replace("\\n", "\n"));
       setCanceledOnTouchOutside(false);
   }
    private void initListener() {
        ButtonOnClickListener buttonOnClickListener = new ButtonOnClickListener();
        mBtnSelectDialogCancel.setOnClickListener(buttonOnClickListener);
        mBtnSelectDialogDetermine.setOnClickListener(buttonOnClickListener);
    }

    private class ButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnSelectDialogCancelguan:
                    listener.checkButton(R.id.btnSelectDialogCancelguan);
                    if (!isRequre) {
                        dismiss();
                    }
                    break;
                case R.id.btnSelectDialogDetermineguan:
                    listener.checkButton(R.id.btnSelectDialogDetermineguan);
                    if (!isRequre) {
                        dismiss();
                    }
                    break;
            }
        }
    }

    public interface SelectDialogButtonListener {
        void checkButton(int id);
    }
}
