package com.yewapp.ui.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yewapp.R

/**
 * @author Narbir Singh
 * @description This bottom sheet is used where you can remove or delete account ot any data
 * 1. remove associate
 * 2. remove spectator
 * 3. delete user & associate profile sports type data(models and equipments)
 */
class RemoveAccountBottomSheet(
    var title: String,
    var message: String,
    private var btnRemoveAccount: () -> Unit,
) {

    fun show(context: Context) {

        val view = LayoutInflater.from(context).inflate(R.layout.remove_account_bottom_sheet2, null)

        val dialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
        dialog.setCancelable(false)
        dialog.setContentView(view)

        val bottomSheetBehavior: BottomSheetBehavior<View> =
            BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        val tvRemoveAccount = view.findViewById<TextView>(R.id.tvRemoveAccount)
        val tvMessage = view.findViewById<TextView>(R.id.tvMessage)

        val crossIcon = view.findViewById<AppCompatImageView>(R.id.icCross)
        val btnRemoveAccount = view.findViewById<TextView>(R.id.btnRemoveAccount)
        if(title.contains("sport",ignoreCase = true))
            btnRemoveAccount.setText("Delete Sport Type")

        tvRemoveAccount.text = title
        tvMessage.text = message


        crossIcon.setOnClickListener {
            dialog.dismiss()
        }
        btnRemoveAccount.setOnClickListener {
            btnRemoveAccount()
            dialog.dismiss()
        }

        dialog.show()
    }

    class Builder {
        private var title = ""
        private var message = ""
        private var btnRemoveAccount: () -> Unit = {}


        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        fun setButtonRemove(listener: () -> Unit): Builder {
            this.btnRemoveAccount = listener
            return this
        }


        fun build(): RemoveAccountBottomSheet {
            return RemoveAccountBottomSheet(title, message, btnRemoveAccount)
        }
    }

    fun dismiss() {
        this.dismiss()
    }

}