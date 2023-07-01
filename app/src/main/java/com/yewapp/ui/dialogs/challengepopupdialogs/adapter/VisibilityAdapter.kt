package com.yewapp.ui.dialogs.challengepopupdialogs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.databinding.ItemVisibilityPopupBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.dialogs.challengepopupdialogs.vm.VisibilityViewModel

class VisibilityAdapter(val list: MutableList<String>, val listener: (String) -> Unit) :
    BaseRecyclerAdapter<VisibilityAdapter.ViewHolder, String>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemVisibilityPopupBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_challenge_single_selection_popup, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            VisibilityViewModel(
                list[position], listener
            )
        )
    }

    class ViewHolder(val binding: ItemVisibilityPopupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: VisibilityViewModel) {
            binding.viewModel = viewModel
        }
    }
}