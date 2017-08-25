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
public class GoSetDialog extends Dialog {

    TextView mTvSelectDialogTitle;
    Button mBtnSelectDialogCancel;
    Button mBtnSelectDialogDetermine;
    SelectDialogButtonListener listener;
    Context context;
    String content;

    public GoSetDialog(Context context) {
        super(context);
        this.context = context;
    }

    public GoSetDialog(Context context, int theme) {
        super(context, R.style.HintDialogStyle);
        this.context = context;
    }

    public GoSetDialog(Context context, String content, SelectDialogButtonListener listener) {
        super(context, R.style.HintDialogStyle);
        this.context = context;
        this.content = content;
        this.listener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View container = getLayoutInflater().inflate(R.layout.dialog_sett, null);
        setContentView(container);
        initview(container);
        initListener();
        mTvSelectDialogTitle.setText(content);
    }
   void initview(View v){
       mBtnSelectDialogCancel=(Button)v.findViewById(R.id.btnSelectDialogCancelset);
       mBtnSelectDialogDetermine=(Button)v.findViewById(R.id.btnSelectDialogDetermineset);
       mTvSelectDialogTitle=(TextView)v.findViewById(R.id.tvSelectDialogTitleset);
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
                case R.id.btnSelectDialogCancelset:
                    listener.checkButton(R.id.btnSelectDialogCancelset);
                    dismiss();
                    break;

                case R.id.btnSelectDialogDetermineset:
                    listener.checkButton(R.id.btnSelectDialogDetermineset);
                    dismiss();
                    break;
            }
        }
    }

    public interface SelectDialogButtonListener {
        void checkButton(int id);
    }
}
