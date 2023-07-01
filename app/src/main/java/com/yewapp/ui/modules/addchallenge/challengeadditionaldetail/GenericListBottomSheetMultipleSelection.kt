package com.yewapp.ui.modules.addchallenge.challengeadditionaldetail

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.yewapp.R
import com.yewapp.data.network.api.profile.City

class GenericListBottomSheetMultipleSelection(
    val title: String,
    val list: List<City>,
    val updatedListener: (MutableList<City>) -> Unit
//    val listener: (T) -> Unit
) {
    lateinit var bottomSheet: BottomSheetDialog

    fun show(context: Activity) {
        val view = LayoutInflater.from(context).inflate(
            R.layout.bottom_sheet_generic_list_multiple_selection,
            context.findViewById(android.R.id.content), false
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_city_list)
        val cityName = view.findViewById<TextView>(R.id.tv_title)
        val imgClose = view.findViewById<ImageView>(R.id.img_close)

        val setButton = view.findViewById<MaterialButton>(R.id.set_button)

        recyclerView.adapter = MultipleSelectionAdapter(list as MutableList<City>) { item, position ->
            list[position] = item
            recyclerView.adapter?.notifyItemChanged(position)
        }

        imgClose.setOnClickListener {
            bottomSheet.dismiss()
        }

        setButton.setOnClickListener {
            bottomSheet.dismiss()
            updatedListener(list)
        }

        cityName.text = title

        bottomSheet = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
        bottomSheet.setContentView(view)

        bottomSheet.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        bottomSheet.dismissWithAnimation = true

        bottomSheet.show()
    }

    class Builder() {
        var title = ""
        var list = mutableListOf<City>()

        //        var listener = { _: T -> }
        lateinit var updatedListener: (List<City>) -> Unit


        fun setTitleText(title: String): Builder {
            this.title = title
            return this
        }

        fun setDataList(list: MutableList<City>): Builder {
            this.list = list
            return this
        }

        fun setUpdatedListener(listener: (List<City>) -> Unit): Builder {
            this.updatedListener = listener
            return this
        }


        fun build(): GenericListBottomSheetMultipleSelection {
            return GenericListBottomSheetMultipleSelection(
                title,
                list,
                updatedListener
            )
        }
    }
}