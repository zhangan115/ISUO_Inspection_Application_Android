package com.isuo.inspection.application.ui.data.history.widget;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isuo.inspection.application.R;

import java.text.MessageFormat;

public class LayoutType3 extends LinearLayout {

    private Context context;

    public LayoutType3(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        inflate(context, R.layout.layout_item_type3, this);
    }

    public void setData(String positionText, String value1, String value2) {
        TextView textView = findViewById(R.id.text_position);
        textView.setText(positionText);
        TextView textView1 = findViewById(R.id.text_1);
        textView1.setText(MessageFormat.format("AE值：{0}", value1));
        TextView textView2 = findViewById(R.id.text_2);
        textView2.setText(MessageFormat.format("AE背景值：{0}", value2));
    }
}
