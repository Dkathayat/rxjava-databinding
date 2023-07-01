package com.yewapp.ui.modules.videofeeds.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.follower.MyFollowers
import com.yewapp.databinding.ItemVideofeedFollowerListBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.videofeeds.vm.ItemVideoFeedFollowerViewModel

class VideoFeedFollowerFollowingAdapter(
    private val userList: MutableList<MyFollowers>,
    val onItemClickListener: ItemVideoFeedFollowerViewModel.OnItemClickListener
) :
    BaseRecyclerAdapter<VideoFeedFollowerFollowingAdapter.ViewHolder, MyFollowers>(userList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemVideofeedFollowerListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemVideoFeedFollowerViewModel(
                userList[position],
                onItemClickListener,
                position
            )
        )
    }

    class ViewHolder(val binding: ItemVideofeedFollowerListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemVideoFeedFollowerViewModel) {
            binding.viewModel = viewModel
            if (viewModel.item.following) {
                viewModel.followVisibility.set(true)
            } else {
                viewModel.followVisibility.set(false)
            }
        }
    }

    fun updateLikedItem(user: MyFollowers, position: Int) {
        userList[position] = user
        notifyItemChanged(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
}