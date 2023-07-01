package com.yewapp.ui.dialogs.challengepopupdialogs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.sports.Model
import com.yewapp.databinding.ItemChallengePopupModelBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.dialogs.challengepopupdialogs.vm.ItemChallengePopUpModelViewModel

class ChallengePopUpModelAdapter(val list: MutableList<Model>, val listener: (Model) -> Unit) :
    BaseRecyclerAdapter<ChallengePopUpModelAdapter.ViewHolder, Model>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChallengePopupModelBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_challenge_popup, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemChallengePopUpModelViewModel(
                list[position], listener
            )
        )
    }

    class ViewHolder(val binding: ItemChallengePopupModelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemChallengePopUpModelViewModel) {
            binding.viewModel = viewModel
        }
    }
}