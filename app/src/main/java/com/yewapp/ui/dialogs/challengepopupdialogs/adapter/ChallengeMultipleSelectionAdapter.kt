package com.yewapp.ui.dialogs.challengepopupdialogs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.createchallenge.StaticMultipleSelection
import com.yewapp.databinding.ItemChallengeMultipleSelectionPopupBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.dialogs.challengepopupdialogs.vm.ItemMultipleSelectionViewModel


class ChallengeMultipleSelectionAdapter(
    val list: MutableList<StaticMultipleSelection>,
    val listener: (Int, Boolean) -> Unit
) :
    BaseRecyclerAdapter<ChallengeMultipleSelectionAdapter.ViewHolder, StaticMultipleSelection>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChallengeMultipleSelectionPopupBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_challenge_multiple_selection_popup, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemMultipleSelectionViewModel(
                list[position], listener, position
            )
        )
    }

    class ViewHolder(val binding: ItemChallengeMultipleSelectionPopupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemMultipleSelectionViewModel) {
            binding.viewModel = viewModel
        }
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}