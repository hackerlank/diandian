package com.dingli.diandiaan.callname;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dingli.diandiaan.R;


public class GuanBiDialog extends Dialog {

    TextView mTvSelectDialogTitle;
    Button mBtnSelectDialogCancel;
    Button mBtnSelectDialogDetermine;
    SelectDialogButtonListener listener;
    Context context;
    String content;

    public GuanBiDialog(Context context) {
        super(context);
        this.context = context;
    }

    public GuanBiDialog(Context context, int theme) {
        super(context, R.style.HintDialogStyle);
        this.context = context;
    }

    public GuanBiDialog(Context context, String content, SelectDialogButtonListener listener) {
        super(context, R.style.HintDialogStyle);
        this.context = context;
        this.content = content;
        this.listener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View container = getLayoutInflater().inflate(R.layout.dialog_guanbi, null);
        setContentView(container);
        initview(container);
        initListener();
        mTvSelectDialogTitle.setText(content);
    }
   void initview(View v){
       mBtnSelectDialogCancel=(Button)v.findViewById(R.id.btnSelectDialogCancelguan);
       mBtnSelectDialogDetermine=(Button)v.findViewById(R.id.btnSelectDialogDetermineguan);
       mTvSelectDialogTitle=(TextView)v.findViewById(R.id.tvSelectDialogTitleguan);
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
                    dismiss();
                    break;

                case R.id.btnSelectDialogDetermineguan:
                    listener.checkButton(R.id.btnSelectDialogDetermineguan);
                    dismiss();
                    break;
            }
        }
    }

    public interface SelectDialogButtonListener {
        public void checkButton(int id);
    }
}
