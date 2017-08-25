package com.dingli.diandiaan.schedule;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dingli.diandiaan.R;
import com.dingli.diandiaan.common.DianTool;


/**
 * Created by dong on 2016/3/30.
 */
public class Label extends LinearLayout {

    private int labelTextSize;
    private int labelTextColor;
    private int rowNumber;

    public Label(Context context) {
        this(context, null);
    }

    public Label(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Schedule);
        labelTextSize = ta.getDimensionPixelSize(R.styleable.Schedule_labelTextSize, 24);
        labelTextColor = ta.getColor(R.styleable.Schedule_labelTextColor, Color.BLACK);
        rowNumber = ta.getInt(R.styleable.Schedule_rowNumber, 10);

        for (int i = 0; i < rowNumber; i++) {
            TextView textView = new TextView(context);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER);
            textView.setText("" + (i + 1));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, labelTextSize);
            textView.setTextColor(labelTextColor);
            textView.setBackgroundColor(Color.TRANSPARENT);
            addView(textView);
        }
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
        invalidate();
    }

    public int getRowNumber() {
        return rowNumber;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.rgb(0x00, 0x00, 0x00));
        paint.setColor(Color.rgb(224, 224, 224));
        paint.setStrokeWidth(1);

        int rowHeight = getHeight()/ rowNumber;
        for (int i = 0; i < rowNumber; i++) {
            canvas.drawLine(0, (i + 1) * rowHeight, getWidth(), (i + 1) * rowHeight, paint);
        }
    }


}
