package com.yewapp.ui.dialogs.challengepopupdialogs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.sports.Sport
import com.yewapp.databinding.ItemChallengePopupBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.dialogs.challengepopupdialogs.vm.ItemChallengePopUpViewModel

class ChallengePopUpAdapter(val list: MutableList<Sport>, val listener: (Sport) -> Unit) :
    BaseRecyclerAdapter<ChallengePopUpAdapter.ViewHolder, Sport>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChallengePopupBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_challenge_popup, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemChallengePopUpViewModel(
                list[position], listener
            )
        )
    }

    class ViewHolder(val binding: ItemChallengePopupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemChallengePopUpViewModel) {
            binding.viewModel = viewModel
        }
    }
}