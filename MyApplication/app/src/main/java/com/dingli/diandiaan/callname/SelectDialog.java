package com.dingli.diandiaan.callname;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dingli.diandiaan.R;



public class SelectDialog extends Dialog {

    TextView mTvSelectDialogTitle;
    Button mBtnSelectDialogCancel;
    Button mBtnSelectDialogDetermine;
    SelectDialogButtonListener listener;
    Context context;
    String content;

    public SelectDialog(Context context) {
        super(context);
        this.context = context;
    }

    public SelectDialog(Context context, int theme) {
        super(context, R.style.HintDialogStyle);
        this.context = context;
    }

    public SelectDialog(Context context, String content, SelectDialogButtonListener listener) {
        super(context, R.style.HintDialogStyle);
        this.context = context;
        this.content = content;
        this.listener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View container = getLayoutInflater().inflate(R.layout.dialog_select, null);
        setContentView(container);
        initview(container);
        initListener();
        mTvSelectDialogTitle.setText(content);
    }
   void initview(View v){
       mBtnSelectDialogCancel=(Button)v.findViewById(R.id.btnSelectDialogCancel);
       mBtnSelectDialogDetermine=(Button)v.findViewById(R.id.btnSelectDialogDetermine);
       mTvSelectDialogTitle=(TextView)v.findViewById(R.id.tvSelectDialogTitle);
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
                case R.id.btnSelectDialogCancel:
                    listener.checkButton(R.id.btnSelectDialogCancel);
                    dismiss();
                    break;

                case R.id.btnSelectDialogDetermine:
                    listener.checkButton(R.id.btnSelectDialogDetermine);
                    dismiss();
                    break;
            }
        }
    }

    public interface SelectDialogButtonListener {
        public void checkButton(int id);
    }
}
