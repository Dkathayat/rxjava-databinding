package com.yewapp.ui.modules.dashboard.fragment.feeds.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.feed.Feed
import com.yewapp.data.network.api.feed.Files
import com.yewapp.databinding.FeedsVideoListItemBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.ItemFeedsImageViewModel

class FeedsVideoAdapter(
    val list: MutableList<Files>?,
    val feeds: Feed,
    val listener: ItemFeedsImageViewModel.OnItemClickListener
) : BaseRecyclerAdapter<FeedsVideoAdapter.FeedsViewHolder, Files>(list!!) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedsViewHolder =
        FeedsViewHolder(
            FeedsVideoListItemBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
        )


    override fun onBindViewHolder(holder: FeedsViewHolder, position: Int) {
        holder.bind(ItemFeedsImageViewModel(list!![position], feeds, listener, position))


    }

    class FeedsViewHolder(var binding: FeedsVideoListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("WrongConstant")
        fun bind(viewModel: ItemFeedsImageViewModel) {
            binding.viewModel = viewModel

        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}