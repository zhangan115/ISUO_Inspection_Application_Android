package com.isuo.inspection.application.utils;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.isuo.inspection.application.R;
import com.sito.tool.library.utils.DisplayUtil;

import java.lang.reflect.Field;

public class MyNumberPicker extends NumberPicker {

    public MyNumberPicker(Context context) {
        super(context);

        setNumberPickerDivider();
    }

    public MyNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        setNumberPickerDivider();
    }

    public MyNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setNumberPickerDivider();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyNumberPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setNumberPickerDivider();
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    public void updateView(View view) {
        if (view instanceof EditText) {
            EditText et = (EditText) view;
            et.setTextColor(ContextCompat.getColor(getContext(), R.color.text_title));
            et.setTextSize(14);
        }
    }

    private void setNumberPickerDivider() {
        try {
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                } else {
                    Field field = NumberPicker.class.getDeclaredField("mSelectionDivider");
                    field.setAccessible(true);
                    field.set(this, ContextCompat.getDrawable(getContext(), R.drawable.number_pick_drawable));
                }

            }
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                } else {
                    Field field = NumberPicker.class.getDeclaredField("mSelectionDividerHeight");
                    field.setAccessible(true);
                    field.set(this, DisplayUtil.dip2px(getContext(), 2));
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
