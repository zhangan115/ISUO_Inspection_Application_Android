package com.isuo.inspection.application.adapter

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.widget.Button
import androidx.databinding.BindingAdapter
import com.isuo.inspection.application.R

object CommonButtonAdapter {

    @JvmStatic
    @BindingAdapter(
        "app:button_enable_bg",
        "app:button_disabled_bg",
        "app:button_enable"
    )
    fun bindYellowButton(
        button: Button,
        enableBg: Drawable?,
        disableBg: Drawable?,
        isEnable: Boolean = false
    ) {
        button.isEnabled = isEnable
        button.gravity = Gravity.CENTER
        if (isEnable) {
            button.background = enableBg
            button.setTextColor(button.context.resources.getColor(R.color.buttonEnableColor))
        } else {
            button.background = disableBg
            button.setTextColor(button.context.resources.getColor(R.color.buttonDisabledColor))
        }
    }
}