package com.yewapp.ui.dialogs.addyear

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.databinding.ItemAddYearPopupBinding
import com.yewapp.databinding.ItemFramSizePopupBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.dialogs.framesize.AddFrameSizePopUpAdapter
import com.yewapp.ui.dialogs.framesize.ItemFrameSizePopUpViewModel

class AddYearPopupAdapter(
    val list: MutableList<String>,
    val listener: (String) -> Unit
) :
    BaseRecyclerAdapter<AddYearPopupAdapter.ViewHolder, String>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAddYearPopupBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_add_year_popup, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemAddYearViewModel(
                list[position], listener
            )
        )
    }

    class ViewHolder(val binding: ItemAddYearPopupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemAddYearViewModel) {
            binding.viewModel = viewModel
        }
    }
}