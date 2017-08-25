package com.dingli.diandiaan.common;

import android.widget.ImageView;

import com.dingli.diandiaan.rule.ListItemRulView;

/**
 * Created by dingliyuangong on 2016/11/30.
 */
public interface CommInterface {
     void KaiOrGuan(ListItemRulView ruleview,int id,String str,String name,int lateTime);
     void select(int id, ImageView iv);
}
