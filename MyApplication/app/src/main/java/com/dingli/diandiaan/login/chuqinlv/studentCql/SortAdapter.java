package com.dingli.diandiaan.login.chuqinlv.studentCql;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;


import com.dingli.diandiaan.R;

import java.util.List;

public class SortAdapter extends BaseAdapter implements SectionIndexer{
    private List<StudentModel> data;
    private Context context;

    public SortAdapter(Context context, List<StudentModel> data) {
        this.context = context;
        this.data = data;
    }

    public int getCount() {
        return this.data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup arg2) {
        View view=null;
        if(convertView==null){
            view=LayoutInflater.from(context).inflate(R.layout.item_student_cql,null);
        }else{
            view=convertView;
        }
        ViewHolder holder=null;
        if(holder==null){
            holder=new ViewHolder();
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        holder.typeCount1= (TextView) view.findViewById(R.id.typeCount1);
        holder.typeCount2= (TextView) view.findViewById(R.id.typeCount2);
        holder.typeCount3= (TextView) view.findViewById(R.id.typeCount3);
        holder.typeCount4= (TextView) view.findViewById(R.id.typeCount4);
        holder.code= (TextView) view.findViewById(R.id.code);
        holder.studentName= (TextView) view.findViewById(R.id.student_name);
        holder.cpl= (TextView) view.findViewById(R.id.cql);

        StudentModel studentModel=data.get(position);
        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            // 第一次出现该section
            holder.code.setVisibility(View.VISIBLE);
            holder.code.setText(studentModel.getCode());
        } else {
            holder.code.setVisibility(View.GONE);
        }
        holder.typeCount1.setText(studentModel.getType1Count() + "");
        holder.typeCount2.setText(studentModel.getType2Count()+"");
        holder.typeCount3.setText(studentModel.getType3Count()+"");
        holder.typeCount4.setText(studentModel.getType4Count()+"");
        holder.studentName.setText(studentModel.getStudentName());
//        holder.cpl.setText(studentModel.getAllCount()+"");

    return view;
    }
    class ViewHolder{
        private TextView typeCount1;
        private TextView typeCount2;
        private TextView typeCount3;
        private TextView typeCount4;
        private TextView code;
        private TextView cpl;
        private ImageView imageView;
        private TextView studentName;
    }
    @Override
    public Object[] getSections() {
        return null;
    }

    // 做字母索引的时候常常会用到SectionIndexer这个接口
    // 1. getSectionForPosition() 通过该项的位置，获得所在分类组的索引号
    // 2. getPositionForSection() 根据分类列的索引号获得该序列的首个位置

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String firstLetter = data.get(i).getCode();
            char firstChar = firstLetter.charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    // 根据position获取分类的首字母的Char ascii值
    @Override
    public int getSectionForPosition(int position) {
        return data.get(position).getCode().charAt(0);
    }
}
