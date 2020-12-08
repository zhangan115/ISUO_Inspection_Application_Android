package com.isuo.inspection.application.ui.data.chart.widget;


import androidx.annotation.Nullable;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.isuo.inspection.application.model.bean.ChartBean;

import java.util.List;


public class ChartYFormatter implements IAxisValueFormatter {

    @Nullable
    private List<ChartBean> dataValues;

    ChartYFormatter(@Nullable List<ChartBean> dataValues) {
        this.dataValues = dataValues;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int position = (int) value;
        return String.valueOf(position);
    }
}
