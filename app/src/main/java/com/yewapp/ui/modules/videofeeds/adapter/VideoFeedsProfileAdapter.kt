package com.yewapp.ui.modules.videofeeds.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.video.VideoData
import com.yewapp.databinding.ItemVideoFeedsProfileBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.videofeeds.vm.ItemVideoFeedsProfileViewModel


class VideoFeedsProfileAdapter(
    val list: MutableList<VideoData>,
    val listener: ItemVideoFeedsProfileViewModel.OnItemClickListener
) : BaseRecyclerAdapter<VideoFeedsProfileAdapter.DashboardViewHolder, VideoData>(list) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder =
        DashboardViewHolder(
            ItemVideoFeedsProfileBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), parent.width, 4
        )


    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.bind(ItemVideoFeedsProfileViewModel(list[position], listener))
    }

    class DashboardViewHolder(
        private val binding: ItemVideoFeedsProfileBinding,
        private val height: Int, private val rows: Int
    ) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var textView: TextView
        fun bind(viewModel: ItemVideoFeedsProfileViewModel) {
            binding.viewModel = viewModel

            if (viewModel.data.files.isNotEmpty()) {
                for (i in 0 until viewModel.data.files.size) {
                    viewModel.thumbnil.set(viewModel.data.files[i].url)
                }
            }

            binding.root.post {
                binding.root.layoutParams.width = binding.root.rootView.width / 3
                binding.root.layoutParams.height = binding.root.rootView.width / 3
                binding.root.requestLayout()
            }
        }
    }


}