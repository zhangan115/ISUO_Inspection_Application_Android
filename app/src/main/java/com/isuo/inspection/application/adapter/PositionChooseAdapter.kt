package com.isuo.inspection.application.adapter

import android.view.Gravity
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.isuo.inspection.application.R

object PositionChooseAdapter {

    @JvmStatic
    @BindingAdapter(
        "app:select_position_state"
    )
    fun bindPositionSelect(
        textView: TextView,
        position: Int = 0
    ) {
        textView.textSize = 15f
        textView.gravity = Gravity.CENTER
        val tag = textView.tag.toString()
        if (position == tag.toInt()) {
            textView.background =
                textView.context.resources.getDrawable(R.drawable.button_position_select, null)
            textView.setTextColor(textView.context.resources.getColor(R.color.colorWhite))
        } else {
            textView.background =
                textView.context.resources.getDrawable(R.drawable.button_position_un_select, null)
            textView.setTextColor(textView.context.resources.getColor(R.color.dialog_cancel))
        }
    }
}