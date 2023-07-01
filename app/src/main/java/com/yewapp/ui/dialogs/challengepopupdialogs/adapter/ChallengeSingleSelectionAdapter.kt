package com.yewapp.ui.dialogs.challengepopupdialogs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.databinding.ItemChallengeSingleSelectionPopupBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.dialogs.challengepopupdialogs.vm.ItemSingleSelectionViewModel

class ChallengeSingleSelectionAdapter(
    val list: MutableList<String>,
    val listener: (String) -> Unit
) :
    BaseRecyclerAdapter<ChallengeSingleSelectionAdapter.ViewHolder, String>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChallengeSingleSelectionPopupBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_challenge_single_selection_popup, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemSingleSelectionViewModel(
                list[position], listener
            )
        )
    }

    class ViewHolder(val binding: ItemChallengeSingleSelectionPopupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemSingleSelectionViewModel) {
            binding.viewModel = viewModel
        }
    }
}