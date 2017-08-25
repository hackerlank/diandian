package com.dingli.diandiaan.evaluation;

import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;

import java.util.List;

/**
 * Created by dingliyuangong on 2016/6/13.
 */
public class PinDataSet extends PieDataSet implements IPieDataSet {
    private ValuePosition mYValuePosition = ValuePosition.INSIDE_SLICE;
    public PinDataSet(List<PieEntry> yVals, String label) {
        super(yVals, label);
    }
    public void setYValuePosition(ValuePosition yValuePosition)
    {
        this.mYValuePosition = yValuePosition;
    }

    public ValuePosition getmYValuePosition() {
        return mYValuePosition;
    }

    public enum ValuePosition {
        INSIDE_SLICE,
        OUTSIDE_SLICE
    }
}
