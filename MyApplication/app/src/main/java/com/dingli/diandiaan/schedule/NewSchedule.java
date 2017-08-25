package com.dingli.diandiaan.schedule;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.dingli.diandiaan.R;
import com.dingli.diandiaan.bean.TableData;

import java.util.List;

/**
 * Created by admin on 2016/4/26.
 */

public class NewSchedule extends LinearLayout {
    private int contentTextSize;
    private int contentTextColor;
    private int columnWidth;
    private int columnWidths;
    private int rowHeight;
    private int rowNumber;
    private int columnCount;
    private OnCourseClickListener listener;
    private List<TableData> modelCollection;
    public NewSchedule(Context context) {
        this(context, null);
    }

    public NewSchedule(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    private void init(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Schedule);
        contentTextSize = ta.getDimensionPixelSize(R.styleable.Schedule_contentTextSize, 20);
        contentTextColor = ta.getColor(R.styleable.Schedule_contentTextColor, getResources().getColor(R.color.bg_Black));
        columnWidths =
                ta.getDimensionPixelSize(R.styleable.Schedule_columnWidth, (context.getResources().getDisplayMetrics().widthPixels - dp2px(30)) /5);
        columnWidth =
                ta.getDimensionPixelSize(R.styleable.Schedule_columnWidth, (context.getResources().getDisplayMetrics().widthPixels - dp2px(30)) /7);
//        rowHeight = ta.getDimensionPixelSize(R.styleable.Schedule_rowHeight, (int) (columnWidths / 1.4));
         rowHeight = ta.getDimensionPixelSize(R.styleable.Schedule_rowHeight, (int) (columnWidths * 1.27));
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
//        paint.setColor(Color.rgb(224, 224, 224));
        paint.setColor(Color.rgb(213,233,255));
        paint.setStrokeWidth(1);

        for (int i = 0; i < rowNumber; i++) {
            canvas.drawLine(0, (i + 1) * rowHeight, getWidth(), (i + 1) * rowHeight, paint);
        }
    }

    public int getColumnWidth() {
        return columnWidth;
    }


    private String getContent(TableData.CourseListEntity model) {
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

        LayoutParams params = new LayoutParams(columnWidth, rowHeight - 2);

        textView.setLayoutParams(params);
        textView.setText(content);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    private ViewGroup getColumnContainer(int indexOfColumn, int spaceFromPrev) {
        if (getChildAt(indexOfColumn) != null) {
            return (ViewGroup) getChildAt(indexOfColumn);
        }
        LinearLayout container = new LinearLayout(getContext());
        container.setMinimumWidth(columnWidth - 1);
        container.setOrientation(VERTICAL);
        LayoutParams params = new LayoutParams(columnWidth - 1, rowHeight * rowNumber);
        params.setMargins(columnWidth * spaceFromPrev+2, 1, 2, 2);
        container.setLayoutParams(params);
        addView(container, indexOfColumn);
        return container;
    }

    public void addContentTextView(ViewGroup container, TableData.CourseListEntity model, int spaceFromFront, int size, int backgroundRes, OnClickListener listener) {
        String content = getContent(model);
        TextView textView = getTextView(content);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        //设置课程信息
        textView.setBackgroundResource(backgroundRes);
        //显示不全省略号
        if (size == 1) {
            textView.setMaxLines(2);
            textView.setEllipsize(TextUtils.TruncateAt.END);
        } else{
            textView.setMaxLines(5);
            textView.setEllipsize(TextUtils.TruncateAt.END);
        }
        if (listener != null)
            textView.setOnClickListener(listener);
        LayoutParams params = (LayoutParams) textView.getLayoutParams();
        params.topMargin = spaceFromFront * rowHeight + 2;
        params.height *= size;
        textView.setPadding(dp2px(3),dp2px(2),dp2px(3),dp2px(2));
        container.addView(textView);

    }

    /**
     * @param collection
     */
    public void fillSchedule(List<TableData> collection) {
        if (getChildCount() > 0) {
            removeAllViews();
        }
        this.modelCollection = collection;
        for (int i = 0; i < collection.size(); i++) {
            List<TableData.CourseListEntity> modelList =modelCollection.get(i).getCourseList();
            if (i == 0) {
                getColumnContainer(i, Integer.valueOf(modelCollection.get(i).getDayOfWeek())-1);
            } else {
                getColumnContainer(i, Integer.valueOf(modelCollection.get(i).getDayOfWeek()) - Integer.valueOf(modelCollection.get(i - 1).getDayOfWeek()) - 1);
            }
            //距离上边父布局的距离
            int spaceFromFront = -1;
            for (int j = 0; j < modelList.size(); j++) {
                final TableData.CourseListEntity model = modelList.get(j);
                if (j == 0) {
//                    第7～8节课
                    spaceFromFront = model.getLessonOrderNum()- 1;
                } else {
                    spaceFromFront = model.getLessonOrderNum() - modelList.get(j - 1).getLessonOrderNum() - Integer.valueOf(modelList.get(j - 1).getPeriodType());
                }
                addContentTextView(getColumnContainer(i, -1), model, spaceFromFront, Integer.valueOf(model.getPeriodType()), ScheduleDrawableSelector.get(model.getCourseName()), new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onCourseClick(model);
                        }
                    }
                });
            }
        }
    }

    public void setOnCourseClickListener(OnCourseClickListener listener) {
        this.listener = listener;
    }
    public interface OnCourseClickListener {
        void onCourseClick(TableData.CourseListEntity model);
    }

}

