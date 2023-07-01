package com.yewapp.ui.modules.dashboard.fragment.feeds.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yewapp.data.network.api.feed.Feed
import com.yewapp.data.network.api.feed.Files
import com.yewapp.databinding.FeedsImageItemBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.ItemFeedsImageViewModel

class FeedsImageAdapter(
    val list: MutableList<Files>?,
    val feeds: Feed,
    val listener: ItemFeedsImageViewModel.OnItemClickListener
) : BaseRecyclerAdapter<FeedsImageAdapter.FeedsViewHolder, Files>(list!!) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedsViewHolder =
        FeedsViewHolder(
            FeedsImageItemBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
        )


    override fun onBindViewHolder(holder: FeedsViewHolder, position: Int) {
        holder.bind(ItemFeedsImageViewModel(list!![position], feeds, listener, position))


    }

    class FeedsViewHolder(var binding: FeedsImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("WrongConstant")
        fun bind(viewModel: ItemFeedsImageViewModel) {
            binding.viewModel = viewModel
            if (viewModel.item.type == "video") {
                Glide.with(itemView).load(viewModel.item.url).into(binding.imagePostGlide)
                binding.imagePostGlide.visibility = View.VISIBLE
                binding.videoView.setVideoURI(Uri.parse(viewModel.item.url))
                binding.videoView.setZOrderOnTop(true)
                binding.videoView.setZOrderMediaOverlay(true)

                //  binding.videoView.start()
            }
            binding.videoView.setOnPreparedListener {
                it.start()
                binding.Progressbar.visibility = View.GONE
                binding.imagePostGlide.visibility = View.GONE

            }

        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}