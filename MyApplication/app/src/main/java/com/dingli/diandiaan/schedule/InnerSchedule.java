package com.dingli.diandiaan.schedule;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;


import com.dingli.diandiaan.R;
import com.dingli.diandiaan.bean.CourseData;

import java.util.List;

/**
 * Created on 2016/3/13.
 */

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)


public class InnerSchedule extends GridLayout {

    private int columnWidth;
    private int rowHeight;
    private float contentTextSize;
    private int contentTextColor;
    private int rowNumber;
    private List<CourseData> modelCollection;
    private OnCourseClickListener listener;

    public InnerSchedule(Context context) {
        this(context, null);
    }

    public InnerSchedule(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InnerSchedule(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (getOrientation() == VERTICAL) {
            setColumnCount(getColumnCount() + 1);
        } else if (getOrientation() == HORIZONTAL) {
            setRowCount(getRowCount() + 1);
        }

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Schedule);

        contentTextSize = ta.getDimensionPixelSize(R.styleable.Schedule_contentTextSize, 20);
        contentTextColor = ta.getColor(R.styleable.Schedule_contentTextColor, Color.WHITE);
        columnWidth = ta.getDimensionPixelSize(R.styleable.Schedule_columnWidth, (context.getResources().getDisplayMetrics().widthPixels - dp2px(30)) / 5);

        rowHeight = ta.getDimensionPixelSize(R.styleable.Schedule_rowHeight, (int) (columnWidth * 1.27));
        rowNumber = ta.getInt(R.styleable.Schedule_rowNumber, 10);

        ta.recycle();

        setMinimumHeight(rowHeight * rowNumber);

    }

    private int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.rgb(0x00, 0x00, 0x00));
        paint.setStrokeWidth(1);

        for (int i = 0; i < getRowCount(); i++) {
            canvas.drawLine(0, (i + 1) * rowHeight, getWidth(), (i + 1) * rowHeight, paint);
        }
    }


    /**
     *
     * @param content
     * @return
     */
    private TextView getTextView(String content) {
        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, contentTextSize);
        textView.setTextColor(contentTextColor);
        textView.setGravity(Gravity.CENTER);
        LayoutParams params = new LayoutParams();
        params.width = columnWidth;
        params.height = rowHeight;
        textView.setLayoutParams(params);

        textView.setText(content);

        return textView;
    }


    public void addContentTextView(CourseData.CourseListEntity model, int startColumn, int startRow, int size, int backgroundRes, OnClickListener listener) {
        String content = getContent(model);
        TextView textView = getTextView(content);
        textView.setBackgroundResource(backgroundRes);

        if (listener != null)
            textView.setOnClickListener(listener);

        LayoutParams params = (LayoutParams) textView.getLayoutParams();
        params.columnSpec = GridLayout.spec(startColumn);
        params.rowSpec = GridLayout.spec(startRow, size, GridLayout.TOP);

        params.height *= size;

        addView(textView);

    }

    private String getContent(CourseData.CourseListEntity model) {
        StringBuilder builder = new StringBuilder();
        builder.append(model.getCourseName());
        builder.append("["+model.getClassRoom()+"]");


        return builder.toString();
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
        invalidate();
    }

    public int getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
        invalidate();
    }

    /**
     *
     * @param collection
     */
//    public void fillSchedule(SparseArray<List<NewCourseModel>> collection) {
//        if (getChildCount() > 0) {
//            removeAllViews();
//        }
//        this.modelCollection = collection;
//        for (int i = 0; i < collection.size(); i++) {
//            List<NewCourseModel> modelList = modelCollection.get(i);
//            for (int j = 0; j < modelList.size(); j++) {
//                if (!modelList.get(j).isEmpty()) {
//                    int lastCount = getCourseLast(modelList, j);
//                    final NewCourseModel model = modelList.get(j);
//                    Log.i("Schedule", "column:" + i + "  row:" + j+"  spec:"+lastCount);
//                    addContentTextView(model, i, j, lastCount, R.drawable.round_corner_bg, new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (listener != null) {
//                                listener.onCourseClick(model);
//                            }
//                        }
//                    });
//                    j += lastCount - 1;
//                }
//
//            }
//        }
//    }

    public void fillSchedule(List<CourseData> collection) {
        if (getChildCount() > 0) {
            removeAllViews();
        }
        this.modelCollection = collection;
        for (int i = 0; i < collection.size(); i++) {
            List<CourseData.CourseListEntity> modelList = modelCollection.get(i).getCourseList();
            for (int j = 0; j < modelList.size(); j++) {
                if (!modelList.get(j).isEmpty()) {
                    int lastCount = getCourseLast(modelList, j);
                    final CourseData.CourseListEntity model = modelList.get(j);
                    Log.i("Schedule", "column:" + i + "  row:" + j+"  spec:"+lastCount);
                    addContentTextView(model, i, j, lastCount, R.drawable.round_corner_bg, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                listener.onCourseClick(model);
                            }
                        }
                    });
                    j += lastCount - 1;
                }

            }
        }
    }


    private int getCourseLast(List<CourseData.CourseListEntity> list, int startPosition) {
        int number = 0;
        for (int i = startPosition; i < list.size(); i++) {
            if (list.get(startPosition).equals(list.get(i))) {
                number++;
            } else {
                break;
            }
        }
        return number;
    }

    public void setOnCourseClickListener(OnCourseClickListener listener) {
        this.listener = listener;

    }


    public interface OnCourseClickListener {
        void onCourseClick(CourseData.CourseListEntity model);
    }


}


