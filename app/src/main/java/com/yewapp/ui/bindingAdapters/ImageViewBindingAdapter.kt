package com.yewapp.ui.bindingAdapters

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("app:imageBitmap")
fun loadImage(iv: ImageView, bitmap: Bitmap?) {
    bitmap ?: return
    iv.setImageBitmap(bitmap)
}

@BindingAdapter("app:imageDrawable")
fun setImageDrawable(imgView: ImageView, drawable: Int) {
    if (drawable == 0) return
    imgView.setImageDrawable(ContextCompat.getDrawable(imgView.context, drawable))
}