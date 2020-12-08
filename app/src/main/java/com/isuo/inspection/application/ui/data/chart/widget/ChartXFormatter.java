package com.isuo.inspection.application.ui.data.chart.widget;


import androidx.annotation.Nullable;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.isuo.inspection.application.model.bean.ChartBean;
import com.isuo.inspection.application.utils.DataUtil;

import java.util.List;

/**
 * x轴显示
 * Created by zhangan on 2017-05-19.
 */

public class ChartXFormatter extends ValueFormatter {

    @Nullable
    private List<ChartBean> dataValues;

    public ChartXFormatter() {

    }

    public ChartXFormatter(@Nullable List<ChartBean> dataValues) {
        this.dataValues = dataValues;
    }


    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        int position = (int) value;
        long time = 0;
        if (dataValues != null) {
            time = dataValues.get(0).getData().get(position).getValue();
            return DataUtil.timeFormat(time, "MM-dd");
        } else {
            return "";
        }

    }
}
