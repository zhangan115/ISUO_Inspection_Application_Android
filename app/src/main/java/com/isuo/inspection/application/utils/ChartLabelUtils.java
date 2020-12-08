package com.isuo.inspection.application.utils;

public class ChartLabelUtils {

    private static String[][] labels = new String[3][4];

    public ChartLabelUtils() {
        init();
    }

    private void init() {
        labels[0][0] = "放电峰值";
        labels[0][1] = "背景峰值";
        labels[0][2] = "频率成分1";
        labels[0][3] = "频率成分2";

        labels[1][0] = "放电峰值";
        labels[1][1] = "背景峰值";

        labels[2][0] = "AE值";
        labels[2][1] = "AE背景值";

    }

    public String getLabel(int inputType, int position) {
        return labels[inputType][position];
    }
}
