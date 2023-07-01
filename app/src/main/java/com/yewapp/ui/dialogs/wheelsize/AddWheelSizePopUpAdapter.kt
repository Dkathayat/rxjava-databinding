package com.yewapp.ui.dialogs.wheelsize

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.sports.WheelSize
import com.yewapp.databinding.ItemAddModelPopupBinding
import com.yewapp.databinding.ItemWheelSizePopupBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.dialogs.addmodel.AddModelPopUpAdapter
import com.yewapp.ui.dialogs.addmodel.ItemAddModelPopUpViewModel

class AddWheelSizePopUpAdapter(val list: MutableList<WheelSize>, val listener: (WheelSize) -> Unit) :
    BaseRecyclerAdapter<AddWheelSizePopUpAdapter.ViewHolder, WheelSize>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemWheelSizePopupBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_wheel_size_popup, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemAddWheelSizeViewModel(
                list[position], listener
            )
        )
    }

    class ViewHolder(val binding: ItemWheelSizePopupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemAddWheelSizeViewModel) {
            binding.viewModel = viewModel
        }
    }
}