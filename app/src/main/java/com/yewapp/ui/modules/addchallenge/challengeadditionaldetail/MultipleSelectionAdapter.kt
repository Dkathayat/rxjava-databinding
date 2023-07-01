package com.yewapp.ui.modules.addchallenge.challengeadditionaldetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.profile.City
import com.yewapp.data.network.api.profile.Country
import com.yewapp.data.network.api.profile.State
import com.yewapp.ui.base.BaseRecyclerAdapter

class MultipleSelectionAdapter<T>(
    val list: MutableList<T>,
    val listener: (City, Int) -> Unit
) : BaseRecyclerAdapter<MultipleSelectionAdapter.ViewHolder<T>, T>(list) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_bottom_sheet_multiple_selection, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        holder.bind(list[position], list[position] as City, listener, position)
    }

    class ViewHolder<T>(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(
            item: T,
            itemValue: City,
            listener: (item: City, position: Int) -> Unit,
            position: Int
        ) {
            view.findViewById<TextView>(R.id.cityName).text = getName(item)

            //Manage checkbox click and set value
            val checkBox = view.findViewById<CheckBox>(R.id.cityCheckbox)
            checkBox.isChecked = itemValue.isSelected

            checkBox.setOnCheckedChangeListener { compoundButton, checkBoxStatus ->
                compoundButton?.isChecked = checkBoxStatus
                itemValue.isSelected= checkBoxStatus
                listener(itemValue, position)
            }

        }

        private fun getName(t: T): String {
            return when (t) {
                is Country -> t.name
                is City -> t.name
                is State -> t.name
                is String -> t
                else -> ""
            }
        }
    }
}