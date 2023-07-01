package com.yewapp.ui.dialogs.addbrandmake

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.sports.Brand
import com.yewapp.databinding.ItemAddBrandPopupBinding
import com.yewapp.databinding.ItemAddModelPopupBinding
import com.yewapp.ui.base.BaseRecyclerAdapter

class AddBrandPopUpAdapter(val list: MutableList<Brand>, val listener: (Brand) -> Unit) :
    BaseRecyclerAdapter<AddBrandPopUpAdapter.ViewHolder, Brand>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAddBrandPopupBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_add_brand_popup, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemAddBrandPopUpViewModel(
                list[position], listener
            )
        )
    }

    class ViewHolder(val binding: ItemAddBrandPopupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemAddBrandPopUpViewModel) {
            binding.viewModel = viewModel
        }
    }
}