package com.yewapp.ui.dialogs.type

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.sports.Type
import com.yewapp.databinding.ItemTypePopupBinding
import com.yewapp.ui.base.BaseRecyclerAdapter

class AddTypePopupAdapter(val list: MutableList<Type>, val listener: (Type) -> Unit) :
    BaseRecyclerAdapter<AddTypePopupAdapter.ViewHolder, Type>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTypePopupBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_type_popup, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemTypeViewModel(
                list[position], listener
            )
        )
    }

    class ViewHolder(val binding: ItemTypePopupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemTypeViewModel) {
            binding.viewModel = viewModel
        }
    }
}