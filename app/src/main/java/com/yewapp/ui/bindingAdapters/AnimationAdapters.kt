package com.yewapp.ui.bindingAdapters

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter("app:rotate")
fun rotateView(view: ImageView, rotating: Boolean) {
    val animator = ObjectAnimator.ofFloat(view, View.ROTATION, -360f, 0f)
    animator.duration = 1000
    animator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {

        }

        override fun onAnimationEnd(animation: Animator) {

        }
    })

    animator.repeatCount = Animation.INFINITE
    animator.interpolator = LinearInterpolator()
    animator.start()
}

@BindingAdapter("app:drawableRotate")
fun rotateDrawable(view: TextView, rotating: Boolean) {
    val drawables = view.compoundDrawablesRelative
    val rotateDrawable = when {
        drawables[0] != null -> drawables[0]
        drawables[1] != null -> drawables[1]
        drawables[2] != null -> drawables[2]
        else -> drawables[3]
    }

    val anim = ObjectAnimator.ofInt(rotateDrawable, "level", 0, 10000)
    anim.duration = 5000
    anim.repeatCount = ValueAnimator.INFINITE
    anim.start()
}

fun animateVisibility(view: TextView, values: Float) {
    val animator = ObjectAnimator.ofFloat(view, View.ALPHA, values)
    animator.duration = 1000
//    animator.repeatCount = 1
//    animator.repeatMode = ObjectAnimator.REVERSE
    animator.start()
}

@BindingAdapter("app:animateAlpha")
fun animateLayout(view: ViewGroup, animate: Boolean) {
    val fadeIn = AlphaAnimation(0f, 1f)
    fadeIn.interpolator = DecelerateInterpolator() //add this
    fadeIn.duration = 1000

    val fadeOut = AlphaAnimation(1f, 0f)
    fadeOut.interpolator = AccelerateInterpolator() //and this
    fadeOut.startOffset = 1000
    fadeOut.duration = 1000

    val animation = AnimationSet(false) //change to false
    animation.addAnimation(fadeIn)
    animation.addAnimation(fadeOut)
    animation.setRepeatCount(Animation.INFINITE)
    view.animation = animation
}