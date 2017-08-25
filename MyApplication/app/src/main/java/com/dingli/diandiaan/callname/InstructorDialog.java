package com.dingli.diandiaan.callname;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;


public class InstructorDialog extends Dialog {

    LinearLayout linoutlocation,linclick;
    SelectDialogButtonListener listener;
    Context context;
    String content;
    ImageView ivlook,ivboda,ivcancel;
    TextView tvlocalook,tvboda;
    public InstructorDialog(Context context) {
        super(context);
        this.context = context;
    }

    public InstructorDialog(Context context, int theme) {
        super(context, R.style.HintDialogStyle);
        this.context = context;
    }

    public InstructorDialog(Context context, String content, SelectDialogButtonListener listener) {
        super(context, R.style.HintDialogStyle);
        this.context = context;
        this.content = content;
        this.listener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View container = getLayoutInflater().inflate(R.layout.dialog_instructor, null);
        setContentView(container);
        initview(container);
        initListener();
    }
   void initview(View v){
       linoutlocation=(LinearLayout) v.findViewById(R.id.linoutlocation);
       linclick=(LinearLayout) v.findViewById(R.id.linclick);
       ivlook=(ImageView)v.findViewById(R.id.ivlook);
       ivboda=(ImageView) v.findViewById(R.id.ivboda);
       tvlocalook=(TextView)v.findViewById(R.id.tvlocalook);
       tvboda=(TextView)v.findViewById(R.id.tvboda);
       ivcancel=(ImageView) v.findViewById(R.id.ivcancel);
       if (content.equals("call")){
           ivlook.setVisibility(View.GONE);
           tvlocalook.setVisibility(View.GONE);
           ivboda.setVisibility(View.VISIBLE);
           tvboda.setVisibility(View.VISIBLE);
       }else{
           ivlook.setVisibility(View.VISIBLE);
           tvlocalook.setVisibility(View.VISIBLE);
           ivboda.setVisibility(View.GONE);
           tvboda.setVisibility(View.GONE);
       }
   }
    private void initListener() {
        ButtonOnClickListener buttonOnClickListener = new ButtonOnClickListener();
        linoutlocation.setOnClickListener(buttonOnClickListener);
        linclick.setOnClickListener(buttonOnClickListener);
        ivcancel.setOnClickListener(buttonOnClickListener);
    }

    private class ButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.linclick:
                    listener.checkButton(R.id.linclick);
                    dismiss();
                    break;
                case R.id.linoutlocation:
                    listener.checkButton(R.id.linoutlocation);
                    dismiss();
                    break;
                case R.id.ivcancel:
                    listener.checkButton(R.id.ivcancel);
                    dismiss();
                    break;
            }
        }
    }

    public interface SelectDialogButtonListener {
        public void checkButton(int id);
    }
}
