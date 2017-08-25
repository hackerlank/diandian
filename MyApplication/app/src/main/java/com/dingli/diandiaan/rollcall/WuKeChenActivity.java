package com.dingli.diandiaan.rollcall;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.BaseActivity;

/**
 * Created by dingliyuangong on 2017/1/13.
 */
public class WuKeChenActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wukechen);
        ImageView wukechens=(ImageView)findViewById(R.id.wukechens);
        wukechens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WuKeChenActivity.this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
            }
        });
    }
}
