package com.yewapp.ui.dialogs

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.yewapp.BuildConfig.DEBUG
import com.yewapp.R

class PermissionsDialog private constructor(
    private val title: String,
    private val message: String,
    private val negativeText: String,
    private val positiveText: String,
    private val cancelable: Boolean,
    private var negativeListener: () -> Unit
) {

    fun show(context: Context) {
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(context, R.style.AlertDialogStyle)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton(positiveText) { dialog, _ ->
            dialog.dismiss()
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        }

        builder.setNegativeButton(negativeText) { dialog, _ ->
            dialog.dismiss()
            negativeListener()
        }

        builder.setCancelable(cancelable)
        alertDialog = builder.create()

        if (!DEBUG)
            alertDialog.window?.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )

        alertDialog.show()
    }


    class Builder {
        private var mTitle: String? = null
        private var mDescription: String? = null
        private var mNegativeText: String = "Not now"
        private var mPositiveText: String = "App Settings"
        private var negativeListener: () -> Unit = {}
        private var cancelable = false

        fun setTitle(title: String): Builder {
            this.mTitle = title
            return this
        }

        fun setDescription(description: String): Builder {
            this.mDescription = description
            return this
        }

        fun setNegativeText(text: String): Builder {
            this.mNegativeText = text
            return this
        }

        fun setPositiveText(text: String): Builder {
            this.mPositiveText = text
            return this
        }

        fun addNegativeListener(block: () -> Unit = {}): Builder {
            this.negativeListener = block
            return this
        }

        fun isCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun build(): PermissionsDialog {
            val title = requireNotNull(mTitle)
            val description = requireNotNull(mDescription)
            return PermissionsDialog(
                title, description, mPositiveText,
                mNegativeText, cancelable, negativeListener
            )
        }
    }
}