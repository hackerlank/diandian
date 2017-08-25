package com.dingli.diandiaan.setting;

import android.content.Intent;
import android.hardware.camera2.params.MeteringRectangle;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;
import com.dingli.diandiaan.common.Constant;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2016/11/24.
 */
public class InstructionActivity extends BaseActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
        initView();
    }

    private void initView() {
        String fangshi=getIntent().getStringExtra(Constant.FANGSHI);
       ImageView instructionback = (ImageView) findViewById(R.id.instructionback);
        SwitchButton tbdingwei=(SwitchButton)findViewById(R.id.tbdingwei);
        SwitchButton tbshuzi=(SwitchButton)findViewById(R.id.tbshuzi);
        if (fangshi.equals("dingwei")){
            tbdingwei.setChecked(true);
            tbshuzi.setChecked(false);

        }else{
            tbdingwei.setChecked(false);
            tbshuzi.setChecked(true);
        }
        tbdingwei.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent result=new Intent();
                if (isChecked){
                    result.putExtra("result", "one");
                    setResult(RESULT_OK, result);
                    InstructionActivity.this.finish();
                    overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                }
            }
        });
        tbshuzi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent result=new Intent();
                if (isChecked){
                    result.putExtra("result", "two");
                    setResult(RESULT_OK, result);
                    InstructionActivity.this.finish();
                    overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                }
            }
        });
        instructionback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.instructionback:
                finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
        }
    }
}
