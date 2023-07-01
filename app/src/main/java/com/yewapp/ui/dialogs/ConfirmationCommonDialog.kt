package com.yewapp.ui.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.yewapp.BuildConfig
import com.yewapp.R

class ConfirmationCommonDialog private constructor(
    private var description: String,
    private var buttonTitle: String,
    private var positiveListener: () -> Unit
) {

    var alertDialog: AlertDialog? = null

    fun dismiss() {
        if (alertDialog == null) return
        alertDialog?.dismiss()
    }

    fun show(context: Context) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogStyle)

        val view = LayoutInflater.from(context).inflate(R.layout.confirmation_dialog, null)
        val crossImage = view.findViewById<ImageView>(R.id.cross)
        val tvDescription = view.findViewById<TextView>(R.id.tv_description)
        val tvTittle = view.findViewById<TextView>(R.id.tv_title)
        val positiveBtn = view.findViewById<TextView>(R.id.btnYes)
        val negativeBan = view.findViewById<TextView>(R.id.btnSkip)
        tvDescription.text = description
        positiveBtn.text = buttonTitle

        crossImage.setOnClickListener {
            dismiss()
        }
        negativeBan.setOnClickListener {
            dismiss()
        }
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
        private var buttonTitle: String = ""
        private var positiveListener: () -> Unit = {}
        private var cancelable = false

        fun setDescription(description: String): Builder {
            this.mDescription = description
            return this
        }

        fun setTitle(title: String): Builder {
            this.buttonTitle = title
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

        fun build(): ConfirmationCommonDialog {
            return ConfirmationCommonDialog(
                mDescription,
                buttonTitle,
                this.positiveListener

            )
        }
    }
}