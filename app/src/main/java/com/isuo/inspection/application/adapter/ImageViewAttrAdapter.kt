package com.isuo.inspection.application.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.isuo.inspection.application.app.ISUOApplication

object ImageViewAttrAdapter {

    @BindingAdapter("android:src")
    fun setSrc(view: ImageView, bitmap: Bitmap?) {
        view.setImageBitmap(bitmap)
    }

    @BindingAdapter("android:src")
    fun setSrc(view: ImageView, resId: Int) {
        view.setImageResource(resId)
    }

    @JvmStatic
    @BindingAdapter("app:imageUrl", "app:placeHolder", "app:error")
    fun loadImage(
        imageView: ImageView,
        url: String?,
        holderDrawable: Drawable?,
        errorDrawable: Drawable?
    ) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageDrawable(holderDrawable)
            return
        }
        if (url!!.startsWith("http")) {
            Glide.with(imageView.context)
                .load(url)
                .error(errorDrawable)
                .placeholder(holderDrawable)
                .into(imageView)
        } else {
            Glide.with(imageView.context)
                .load(ISUOApplication.appHost() + url)
                .error(errorDrawable)
                .placeholder(holderDrawable)
                .into(imageView)
        }
    }
}