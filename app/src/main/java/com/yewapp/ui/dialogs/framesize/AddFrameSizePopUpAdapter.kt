package com.yewapp.ui.dialogs.framesize

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.sports.FrameSize
import com.yewapp.databinding.ItemFramSizePopupBinding
import com.yewapp.ui.base.BaseRecyclerAdapter

class AddFrameSizePopUpAdapter(
    val list: MutableList<FrameSize>,
    val listener: (FrameSize) -> Unit
) :
    BaseRecyclerAdapter<AddFrameSizePopUpAdapter.ViewHolder, FrameSize>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFramSizePopupBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_fram_size_popup, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemFrameSizePopUpViewModel(
                list[position], listener
            )
        )
    }

    class ViewHolder(val binding: ItemFramSizePopupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemFrameSizePopUpViewModel) {
            binding.viewModel = viewModel
        }
    }
}