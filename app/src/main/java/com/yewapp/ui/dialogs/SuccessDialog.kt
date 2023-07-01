package com.yewapp.ui.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.yewapp.BuildConfig
import com.yewapp.R

class SuccessDialog private constructor(
    private var description: String,
    private var positiveListener: () -> Unit
) {

    var alertDialog: AlertDialog? = null

    fun dismiss() {
        if (alertDialog == null) return
        alertDialog?.dismiss()
    }

    fun show(context: Context) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogStyle)

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_success, null)

        val tvDescription = view.findViewById<TextView>(R.id.tv_description)
        val positiveBtn = view.findViewById<Button>(R.id.btn_confirm)

        tvDescription.text = description

        positiveBtn.setOnClickListener {
            positiveListener()
            dismiss()
        }

        builder.setView(view)

        alertDialog = builder.create() ?: return
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (!BuildConfig.DEBUG) {
            alertDialog?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }

        alertDialog?.setCancelable(false)

        alertDialog?.show()
    }


    class Builder {
        private var mDescription: String = ""
        private var positiveListener: () -> Unit = {}
        private var cancelable = false

        fun setDescription(description: String): Builder {
            this.mDescription = description
            return this
        }

        fun addPositiveListener(block: () -> Unit = {}): Builder {
            this.positiveListener = block
            return this
        }

        fun isCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun build(): SuccessDialog {
            return SuccessDialog(
                mDescription,
                this.positiveListener
            )
        }
    }
}