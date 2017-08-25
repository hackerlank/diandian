package com.dingli.diandiaan.rollcall;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingli.diandiaan.login.CircleImageView;

/**
 * Created by dingliyuangong on 2016/3/25.
 */
public class ViewHolderOne {
    RelativeLayout rlbgone,rlbgtwo,rlbgthree,rlbgfour;
   TextView tvnameone,tvnametwo,tvnamethree,tvnamefour;
    CircleImageView igvone,nametwo,namethree,namefour;
   public TextView nametv,tvtype,tvbeizhu;
    ImageView checkBox;
    public RelativeLayout llbgs;
    public ImageView ivphone;
//    TextView bantv;
//    GridView gdv;
    private ViewHolder.onCancelCollectListener mOnCancelInterface;
    int id;
    View v;
    String type;
    public interface onCancelCollectListener {
        void onCancelCollect(RelativeLayout le,View v,int classId,String className,int courseId,int id,int scheduleId,
                             int studentScheduleId,int teacherId,String type,int userId,String userName);
    }
    public void setmOnCancelInterface(ViewHolder.onCancelCollectListener mInter) {
        mOnCancelInterface = mInter;
    }
    public void setId(int id) {
        this.id = id;
  }

    public void setView(View v) {
        this.v= v;
    }

    public void setType(String type) {
        this.type= type;
    }

}
