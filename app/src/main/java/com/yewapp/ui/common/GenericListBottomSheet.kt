package com.yewapp.ui.common

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yewapp.R

class GenericListBottomSheet<T>(
    val title: String,
    val list: List<T>,
    val listener: (T) -> Unit
) {
    lateinit var bottomSheet: BottomSheetDialog

    fun show(context: Activity) {
        val view = LayoutInflater.from(context).inflate(
            R.layout.bottom_sheet_generic_list,
            context.findViewById(android.R.id.content), false
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_list)
        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        val imgClose = view.findViewById<ImageView>(R.id.img_close)

        recyclerView.adapter = GenericBottomSheetAdapter(list as MutableList<T>) {
            bottomSheet.dismiss()
            listener(it)
        }

        imgClose.setOnClickListener {
            bottomSheet.dismiss()
        }

        tvTitle.text = title

        bottomSheet = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
        bottomSheet.setContentView(view)

        bottomSheet.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        bottomSheet.dismissWithAnimation = true

        bottomSheet.show()
    }

    class Builder<T>() {
        var title = ""
        var list = mutableListOf<T>()
        var listener = { _: T -> }

        fun setTitleText(title: String): Builder<T> {
            this.title = title
            return this
        }

        fun setDataList(list: MutableList<T>): Builder<T> {
            this.list = list
            return this
        }

        fun setClickListener(listener: (T) -> Unit): Builder<T> {
            this.listener = listener
            return this
        }

        fun setVisibiltyCheckbox(view:View){
            val tvTitle = view.findViewById<TextView>(R.id.tv_title)
            tvTitle.visibility = View.VISIBLE
        }

        fun build(): GenericListBottomSheet<T> {
            return GenericListBottomSheet(
                title,
                list,
                listener
            )
        }
    }
}