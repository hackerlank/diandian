package com.dingli.diandiaan.rollcall;

import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by dingliyuangong on 2016/3/25.
 */
public class ViewHolder {
    public TextView bantv,tvren;
    GridView gdv;
    public ListView lvs;
    private onCancelCollectListener mOnCancelInterface;
    int id;
    View v;
    String type;
    public interface onCancelCollectListener {
        void onCancelCollect(ImageView iv,RelativeLayout le,View v,int classId,String className,int courseId,long id,int scheduleId,
                             int studentScheduleId,int teacherId,String type,int userId,String userName,ImageView checkbox);
        void refresh(int i,long ids);
    }
    public void setmOnCancelInterface(onCancelCollectListener mInter) {
        mOnCancelInterface = mInter;
    }
    public void setId(int id) {
        this.id = id;
  }

    public void setView(View v) {
        this.v= v;
    }
    public void settype(String type) {
        this.type= type;
    }
}
