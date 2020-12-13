package com.isuo.inspection.application.adapter

import android.view.Gravity
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.isuo.inspection.application.R
import com.isuo.inspection.application.model.bean.MeasuringPointBean

object PositionChooseAdapter {

    @JvmStatic
    @BindingAdapter(
        "app:select_position_state"
    )
    fun bindPositionSelect(
        textView: TextView,
        position: MeasuringPointBean
    ) {
        textView.textSize = 15f
        textView.gravity = Gravity.CENTER
        if (position.id.get() == position.selectId.get()) {
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