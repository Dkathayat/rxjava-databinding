package com.yewapp.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.profile.City
import com.yewapp.data.network.api.profile.Country
import com.yewapp.data.network.api.profile.State
import com.yewapp.ui.base.BaseRecyclerAdapter

class GenericBottomSheetAdapter<T>(
    val list: MutableList<T>,
    val listener: (T) -> Unit
) :
    BaseRecyclerAdapter<GenericBottomSheetAdapter.ViewHolder<T>, T>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_bottom_sheet, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        holder.bind(list[position], listener)
    }

    class ViewHolder<T>(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: T, listener: (item: T) -> Unit) {
            view.findViewById<TextView>(R.id.tv_name).text = getName(item)
            view.findViewById<ConstraintLayout>(R.id.ll_item).setOnClickListener {
                listener(item)
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