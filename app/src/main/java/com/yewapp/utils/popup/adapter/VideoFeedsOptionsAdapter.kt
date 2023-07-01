package com.yewapp.utils.popup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.video.VideoData
import com.yewapp.databinding.ItemVideoFeedsOptionsBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.utils.popup.vm.ItemVideoOptionViewModel

class VideoFeedsOptionsAdapter(
    val options: MutableList<String>,
    val videoData: VideoData,
    val listener: (String) -> Unit,
) : BaseRecyclerAdapter<VideoFeedsOptionsAdapter.ViewHolder, String>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemVideoFeedsOptionsBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_video_feeds_options, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ItemVideoOptionViewModel(options[position], position, videoData, listener))
    }

    class ViewHolder(val binding: ItemVideoFeedsOptionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemVideoOptionViewModel) {
            binding.viewModel = viewModel
        }
    }
}