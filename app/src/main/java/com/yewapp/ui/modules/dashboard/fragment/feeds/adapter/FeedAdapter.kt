package com.yewapp.ui.modules.dashboard.fragment.feeds.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.feed.Feed
import com.yewapp.data.network.api.feed.Files
import com.yewapp.databinding.ItemFeedBinding
import com.yewapp.databinding.ItemFeedWithoutImageBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.dashboard.fragment.feeds.UserAction
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.ItemFeedViewModel
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.ItemFeedsImageViewModel
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.OnFeedOptionClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class FeedAdapter(
    val userId: Int?,
    val myFeed: Boolean,
    val feedList: MutableList<Feed>,
    val onFeedOptionClickListener: OnFeedOptionClickListener,
    val onMediaClickListener: ItemFeedsImageViewModel.OnItemClickListener
) :
    BaseRecyclerAdapter<RecyclerView.ViewHolder, Feed>(feedList) {

    companion object {
        const val VIEW_TYPE_WITH_IMAGE = 1
        const val VIEW_TYPE_WITHOUT_IMAGE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == VIEW_TYPE_WITH_IMAGE) {
            return ViewHolderWithImage(
                ItemFeedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return ViewHolderWithOutImage(
                ItemFeedWithoutImageBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (feedList[position].files!!.isNotEmpty()) {
            (holder as ViewHolderWithImage).bind(
                ItemFeedViewModel(
                    userId,
                    myFeed,
                    feedList[position],
                    position,
                    onFeedOptionClickListener
                )
            )

            var imageList = arrayListOf<Files>()
            var videoList = arrayListOf<Files>()
            if (feedList[position].files.isNullOrEmpty()) {
                holder.binding.wormDotsIndicator.visibility = View.GONE
            } else {
                for (i in 0 until feedList[position].files!!.size) {
                    imageList.add(feedList[position].files?.get(i) ?: return)
                }
                if (feedList[position].files!!.size == 1) {
                    holder.binding.wormDotsIndicator.visibility = View.GONE
                } else {
                    holder.binding.wormDotsIndicator.visibility = View.VISIBLE
                }
                val adapterImage = FeedsImageAdapter(
                    imageList as MutableList<Files>,
                    feedList[position],
                    onMediaClickListener
                )
                adapterImage.setHasStableIds(true)

                holder.binding.viewPager2.apply {
                    adapter = adapterImage
                }
                holder.binding.wormDotsIndicator.setViewPager2(holder.binding.viewPager2)


            }
        } else {
            (holder as ViewHolderWithOutImage).bind(
                ItemFeedViewModel(
                    userId,
                    myFeed,
                    feedList[position],
                    position,
                    onFeedOptionClickListener
                )
            )
        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (feedList[position].files!!.isEmpty()) {
            VIEW_TYPE_WITHOUT_IMAGE
        } else {
            VIEW_TYPE_WITH_IMAGE
        }
    }


    fun clearItems() {
        feedList.clear()
        notifyDataSetChanged()
    }

    fun clearFavUserItems() {
        feedList.clear()
        notifyDataSetChanged()
    }

    fun addItems(list: List<Feed>) {
        feedList.addAll(list)
        notifyDataSetChanged()
    }

    fun updateItem(feed: Feed, position: Int, action: UserAction) {
        CoroutineScope(Main).launch {
            feedList.forEachIndexed { pos, it ->
                if (it.createdBy!!.id == feed.createdBy!!.id) {
                    updateFeedItem(feed, pos, action)
                }
            }
            notifyDataSetChanged()
        }
    }

    private fun updateFeedItem(feed: Feed, position: Int, action: UserAction) {
        when (action) {
            UserAction.BLOCK -> feedList[position].createdBy!!.blocked = !feed.createdBy!!.blocked
            UserAction.FOLLOW -> feedList[position].createdBy!!.following = !feed.createdBy!!.following
            UserAction.MUTE -> feedList[position].createdBy!!.muted = !feed.createdBy!!.muted
            UserAction.FAVOURITE -> feedList[position].createdBy!!.favourite =
                !feed.createdBy!!.favourite
        }
    }

    fun updateLikedItem(feed: Feed, position: Int) {
        feedList[position] = feed
        notifyItemChanged(position)
    }

    class ViewHolderWithImage(val binding: ItemFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemFeedViewModel) {
            binding.viewModel = viewModel
        }
    }

    class ViewHolderWithOutImage(val binding: ItemFeedWithoutImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemFeedViewModel) {
            binding.viewModel = viewModel
        }
    }
}