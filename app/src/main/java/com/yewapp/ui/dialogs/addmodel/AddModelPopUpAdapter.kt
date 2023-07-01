package com.yewapp.ui.dialogs.addmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.sports.Model
import com.yewapp.databinding.ItemAddModelPopupBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.dialogs.addbrandmake.AddBrandPopUpAdapter
import com.yewapp.ui.dialogs.addbrandmake.ItemAddBrandPopUpViewModel

class AddModelPopUpAdapter (val list: MutableList<Model>, val listener: (Model) -> Unit) :
    BaseRecyclerAdapter<AddModelPopUpAdapter.ViewHolder, Model>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAddModelPopupBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_add_model_popup, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemAddModelPopUpViewModel(
                list[position], listener
            )
        )
    }

    class ViewHolder(val binding: ItemAddModelPopupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemAddModelPopUpViewModel) {
            binding.viewModel = viewModel
        }
    }
}