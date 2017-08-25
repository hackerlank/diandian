package com.dingli.diandiaan.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dingli.diandiaan.R;


/**
 * Created by lpf on 2014/9/23 0023.
 */
public class TongyiDialog extends Dialog {

    TextView mTvSelectDialogTitle,tvzhongxin;
    Button mBtnSelectDialogCancel;
    Button mBtnSelectDialogDetermine;
    SelectDialogButtonListener listener;
    Context context;
    String content;
    public TongyiDialog(Context context) {
        super(context);
        this.context = context;
    }

    public TongyiDialog(Context context, int theme) {
        super(context, R.style.HintDialogStyle);
        this.context = context;
    }

    public TongyiDialog(Context context,String content, SelectDialogButtonListener listener) {
        super(context, R.style.HintDialogStyle);
        this.context = context;
        this.content = content;
        this.listener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View container = getLayoutInflater().inflate(R.layout.dialog_tongyi, null);
        setContentView(container);
        initview(container);
        initListener();
        mTvSelectDialogTitle.setText(content);
    }
   void initview(View v){
       mBtnSelectDialogCancel=(Button)v.findViewById(R.id.btnTongyiDialogCancel);
       mBtnSelectDialogDetermine=(Button)v.findViewById(R.id.btnTongyiDialogDetermine);
       mTvSelectDialogTitle=(TextView)v.findViewById(R.id.tvTongyiDialogTitle);
       tvzhongxin=(TextView)v.findViewById(R.id.tvzhongxin);
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
                case R.id.btnTongyiDialogCancel:
                    listener.checkButton(R.id.btnTongyiDialogCancel);
                    dismiss();
                    break;

                case R.id.btnTongyiDialogDetermine:
                    listener.checkButton(R.id.btnTongyiDialogDetermine);
                    dismiss();
                    break;
            }
        }
    }

    public interface SelectDialogButtonListener {
        public void checkButton(int id);
    }
}
