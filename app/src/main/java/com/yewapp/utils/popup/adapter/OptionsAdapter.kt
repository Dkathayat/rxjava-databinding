package com.yewapp.utils.popup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.feed.Feed
import com.yewapp.databinding.ItemFeedOptionsBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.utils.popup.vm.ItemFeedOptionViewModel

class OptionsAdapter(
    val options: MutableList<String>,
    val feed: Feed,
    val listener: (String) -> Unit,
) : BaseRecyclerAdapter<OptionsAdapter.ViewHolder, String>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFeedOptionsBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_feed_options, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ItemFeedOptionViewModel(options[position], position, feed, listener))
    }

    class ViewHolder(val binding: ItemFeedOptionsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemFeedOptionViewModel) {
            binding.viewModel = viewModel
        }
    }
}