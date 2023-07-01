package com.yewapp.ui.modules.refer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.refer.ReferRewardHistory
import com.yewapp.databinding.ItemReferRewardHistoryBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.refer.vm.ItemReferRewardViewModel

class ReferRewardHistoryAdapter(
    var referRewardHistory: MutableList<ReferRewardHistory>
) : BaseRecyclerAdapter<ReferRewardHistoryAdapter.ReferRewardViewHolder, ReferRewardHistory>(
    referRewardHistory
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReferRewardViewHolder =
        ReferRewardViewHolder(
            ItemReferRewardHistoryBinding
                .inflate(
                    LayoutInflater
                        .from(parent.context), parent, false
                )
        )

    override fun onBindViewHolder(holder: ReferRewardViewHolder, position: Int) {
        holder.bind(ItemReferRewardViewModel(referRewardHistory[position]))
    }


    class ReferRewardViewHolder(
        private val binding: ItemReferRewardHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemReferRewardViewModel) {
            binding.viewModel = viewModel

        }
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}