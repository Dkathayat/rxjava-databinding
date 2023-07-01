package com.yewapp.ui.bindingAdapters

import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputLayout
import com.yewapp.R

class TextInputBindingAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter("app:showError")
        fun showErrorMessage(view: TextInputLayout, message: String) {
            view.isErrorEnabled = message.isNotEmpty()
            view.error = message
        }

        @JvmStatic
        @BindingAdapter("app:isActive")
        fun toggleButtonColor(button: MaterialButton, isActive: Boolean) {
            if (isActive)
                button.setStrokeColorResource(R.color.colorPrimary)
            else
                button.setStrokeColorResource(R.color.colorAccent)
        }

        @JvmStatic
        @BindingAdapter("app:progress")
        fun setProgress(progressBar: LinearProgressIndicator, progress: Int) {
            progressBar.progress = progress
        }
    }
}