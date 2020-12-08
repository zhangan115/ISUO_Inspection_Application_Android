package com.isuo.inspection.application.model.bean;

import java.util.List;


public class ChartBean {

    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {

        private long value;
        private float dataValue;

        public float getDataValue() {
            return dataValue;
        }

        public void setDataValue(float dataValue) {
            this.dataValue = dataValue;
        }


        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }
    }
}
