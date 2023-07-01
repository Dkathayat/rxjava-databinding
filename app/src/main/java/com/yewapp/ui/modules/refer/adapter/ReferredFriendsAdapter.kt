package com.yewapp.ui.modules.refer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.refer.ReferResponse
import com.yewapp.databinding.ItemReferredFriendsBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.refer.vm.ItemReferredFriendViewModel

class ReferredFriendsAdapter(
    var referredList: MutableList<ReferResponse>
) : BaseRecyclerAdapter<ReferredFriendsAdapter.ReferredFriendsViewHolder, ReferResponse>(
    referredList
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReferredFriendsViewHolder =
        ReferredFriendsViewHolder(
            ItemReferredFriendsBinding
                .inflate(
                    LayoutInflater
                        .from(parent.context), parent, false
                )
        )

    override fun onBindViewHolder(holder: ReferredFriendsViewHolder, position: Int) {
        holder.bind(ItemReferredFriendViewModel(referredList[position]))
    }


    class ReferredFriendsViewHolder(
        private val binding: ItemReferredFriendsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemReferredFriendViewModel) {
            binding.viewModel = viewModel

        }
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}