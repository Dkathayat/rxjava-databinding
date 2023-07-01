package com.yewapp.ui.bindingAdapters

import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.yewapp.R

@BindingAdapter(
    "app:spannableText",
    "app:spanStart",
    "app:spanEnd",
    "app:secondSpanStart",
    "app:secondSpanEnd",
    "app:spanColor",
    "app:initialListener",
    "app:nextListener",
    "app:requireUnderline",
    requireAll = false
)
fun spannableText(
    tv: TextView, string: String, spanStart: Int, spanEnd: Int,
    secondSpanStart: Int = 0, secondSpanEnd: Int = 0,
    color: Int? = null,
    initialListener: (() -> Unit)? = null,
    nextListener: (() -> Unit)? = {},
    isUnderline: Boolean
) {


    val spannableString = SpannableString(string)
    spannableString.apply {
        setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                initialListener?.let { it() }
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = ContextCompat.getColor(
                    tv.context,
                    color ?: R.color.colorPrimary
                );    // you can use custom color
                ds.isUnderlineText = isUnderline
                //   ds.typeface = Typeface.DEFAULT_BOLD
            }
        }, spanStart, spanEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
    }

    if (secondSpanStart == 0) {
        tv.text = spannableString
        tv.movementMethod = LinkMovementMethod.getInstance()
        return
    }

    spannableString.apply {
        setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                nextListener?.let { it() }
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = ContextCompat.getColor(
                    tv.context,
                    color ?: R.color.colorPrimaryDark
                );    // you can use custom color
                ds.isUnderlineText = isUnderline;
                // ds.typeface = Typeface.DEFAULT_BOLD
            }
        }, secondSpanStart, secondSpanEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
    }

    tv.text = spannableString
    tv.movementMethod = LinkMovementMethod.getInstance()
}

@BindingAdapter("app:startDrawable")
fun setDrawableStart(tv: TextView, drawable: Int) {
    val drawableStart = ContextCompat.getDrawable(tv.context, drawable)
    tv.setCompoundDrawables(drawableStart, null, null, null)

}