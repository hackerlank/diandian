package com.dingli.diandiaan.schedule;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.dingli.diandiaan.R;
import com.dingli.diandiaan.bean.CourseData;

import java.util.List;
import java.util.Random;

/**
 * Created by admin on 2016/4/7.
 */
public class Schedule extends LinearLayout {


    private int contentTextSize;
    private int contentTextColor;
    private int columnWidth;
    private int rowHeight;
    private int rowNumber;
    private int columnCount;
    private OnCourseClickListener listener;
    private List<CourseData> modelCollection;


    public Schedule(Context context) {
        this(context, null);
    }

    public Schedule(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Schedule);

        contentTextSize = ta.getDimensionPixelSize(R.styleable.Schedule_contentTextSize, 20);
        contentTextColor = ta.getColor(R.styleable.Schedule_contentTextColor, Color.WHITE);
        columnWidth = ta.getDimensionPixelSize(R.styleable.Schedule_columnWidth, (context.getResources().getDisplayMetrics().widthPixels - dp2px(30)) / 5);
        rowHeight = ta.getDimensionPixelSize(R.styleable.Schedule_rowHeight, (int) (columnWidth / 1.4));

        // rowHeight = ta.getDimensionPixelSize(R.styleable.Schedule_rowHeight, (int) (columnWidth * 1.27));

        rowNumber = ta.getInt(R.styleable.Schedule_rowNumber, 12);
        columnCount = ta.getInt(R.styleable.Schedule_columnCount, 7);
        ta.recycle();

        setMinimumHeight(rowHeight * rowNumber);
        setMinimumWidth(columnCount * columnWidth);

    }


    private int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.rgb(224, 224, 224));
        paint.setStrokeWidth(1);

        for (int i = 0; i < rowNumber; i++) {
            canvas.drawLine(0, (i + 1) * rowHeight, getWidth(), (i + 1) * rowHeight, paint);
        }
    }

    public int getColumnWidth() {
        return columnWidth;
    }


    private String getContent(CourseData.CourseListEntity model) {
        StringBuilder builder = new StringBuilder();
        builder.append(model.getCourseName());
        builder.append("@" + model.getClassRoom());

        return builder.toString();
    }

    /**
     * @param content
     * @return
     */
    private TextView getTextView(String content) {
        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, contentTextSize);
        textView.setTextColor(contentTextColor);
      //  textView.setGravity(Gravity.CENTER);
        LayoutParams params = new LayoutParams(columnWidth, rowHeight-1);

        textView.setLayoutParams(params);

        textView.setText(content);

        return textView;
    }

    private ViewGroup getColumnContainer(int indexOfColumn) {
        if (getChildAt(indexOfColumn) != null) {
            return (ViewGroup) getChildAt(indexOfColumn);
        }
        LinearLayout container = new LinearLayout(getContext());
        container.setMinimumWidth(columnWidth-1);
        container.setOrientation(VERTICAL);
        LayoutParams params = new LayoutParams(columnWidth-1, rowHeight * rowNumber);
        params.setMargins(0,0,1,0);
        container.setLayoutParams(params);
        addView(container, indexOfColumn);
        return container;
    }

    public void addContentTextView(ViewGroup container, CourseData.CourseListEntity model, int spaceFromFront, int size, int backgroundRes, OnClickListener listener) {
        String content = getContent(model);
        TextView textView = getTextView(content);
        //设置课程信息
        textView.setBackgroundResource(backgroundRes);

        //显示不全省略号
        textView.setEllipsize(TextUtils.TruncateAt.valueOf("END"));

        if (listener != null)
            textView.setOnClickListener(listener);

        LayoutParams params = (LayoutParams) textView.getLayoutParams();
        params.topMargin = spaceFromFront * rowHeight+1;

        params.height *= size;

        container.addView(textView);

    }


    /**
     * @param collection
     */
    public void fillSchedule(List<CourseData> collection) {
        if (getChildCount() > 0) {
            removeAllViews();
        }
//        ScheduleDrawableSelector.clearSelector();


        this.modelCollection = collection;
        for (int i = 0; i < collection.size(); i++) {
            List<CourseData.CourseListEntity> modelList = modelCollection.get(i).getCourseList();
            int spaceFromFront = 0;
            getColumnContainer(i);
            for (int j = 0; j < modelList.size(); j++) {
                if (!modelList.get(j).isEmpty()) {
                    final int lastCount = getCourseLast(modelList, j);
                    final CourseData.CourseListEntity model = modelList.get(j);
                    Log.i("Schedule", "column:" + i + "  row:" + j + "  spec:" + lastCount);
                    Random random = new Random();
//                    int one=random.nextInt(20);
              //     Log.i("getLessonOrderNum:"," = "+ model.getLessonOrderNum() + "  name  "+" ="+ model.getCourseName());
                    addContentTextView(getColumnContainer(i), model, spaceFromFront, lastCount, ScheduleDrawableSelector.get(model.getCourseName()), new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                listener.onCourseClick(model);
                            }
                        }
                    });
                    j += lastCount - 1;
                    spaceFromFront = 0;
                } else {
                    spaceFromFront++;
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
            if (number == 2) {
                return 2;
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
