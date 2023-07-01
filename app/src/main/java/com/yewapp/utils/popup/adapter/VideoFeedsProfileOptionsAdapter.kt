package com.yewapp.utils.popup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.databinding.ItemVideoFeedsProfileOptionsBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.utils.popup.vm.ItemVideoProfileOptionViewModel

class VideoFeedsProfileOptionsAdapter(
    val options: MutableList<String>,
    val id: Int,
    val listener: (String) -> Unit
) : BaseRecyclerAdapter<VideoFeedsProfileOptionsAdapter.ViewHolder, String>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemVideoFeedsProfileOptionsBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_video_feeds_profile_options, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ItemVideoProfileOptionViewModel(options[position], position, id, listener))
    }

    class ViewHolder(val binding: ItemVideoFeedsProfileOptionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemVideoProfileOptionViewModel) {
            binding.viewModel = viewModel
        }
    }
}