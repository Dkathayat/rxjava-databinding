package com.yewapp.ui.dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.yewapp.R

class CompleteProfileDialog(
    val description: String,
    val listener: () -> Unit
) {

    lateinit var dialog: Dialog

    fun show(context: Activity) {
        val view = LayoutInflater.from(context).inflate(
            R.layout.dialog_complete_profile,
            context.findViewById(android.R.id.content), false
        )

        val tvTitle = view.findViewById<TextView>(R.id.tv_description)
        val btnOk = view.findViewById<MaterialButton>(R.id.btn_ok)

        tvTitle.text = description

        dialog = Dialog(context)
        dialog.setContentView(view)

        btnOk.setOnClickListener {
            dialog.dismiss()
            listener()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
    }

    class Builder {
        private var mDescription: String? = null
        private var listener: () -> Unit = {}
        private var cancelable = false

        fun setDescription(description: String): Builder {
            mDescription = description
            return this
        }

        fun setClickListener(listener: () -> Unit): Builder {
            this.listener = listener
            return this
        }

        fun build(): CompleteProfileDialog {
            val description = requireNotNull(mDescription)
            return CompleteProfileDialog(description, listener)
        }
    }
}