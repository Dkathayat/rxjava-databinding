package com.yewapp.ui.modules.dashboard.fragment.feeds.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.feed.FeedLike
import com.yewapp.databinding.ItemFeedsLikedBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.ItemFeedsLikedViewModel

class FeedsLikedAdapter(
    val feedLikeList: MutableList<FeedLike>,
) : BaseRecyclerAdapter<FeedsLikedAdapter.FeedsLikedViewHolder, FeedLike>(
    feedLikeList
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FeedsLikedViewHolder =
        FeedsLikedViewHolder(
            ItemFeedsLikedBinding
                .inflate(
                    LayoutInflater
                        .from(parent.context), parent, false
                )
        )

    override fun onBindViewHolder(holder: FeedsLikedViewHolder, position: Int) {
        holder.bind(
            ItemFeedsLikedViewModel(feedLikeList[position], position)
        )
    }

    class FeedsLikedViewHolder(
        private val binding: ItemFeedsLikedBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemFeedsLikedViewModel) {
            binding.viewModel = viewModel
        }
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}