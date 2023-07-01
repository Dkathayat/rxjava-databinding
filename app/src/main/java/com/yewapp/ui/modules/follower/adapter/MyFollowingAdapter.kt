package com.yewapp.ui.modules.follower.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.follower.MyFollowing
import com.yewapp.databinding.LayoutMyfollowingItemBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.follower.vm.ItemMyFollowingViewModel

class MyFollowingAdapter(
    val list: MutableList<MyFollowing>
) : BaseRecyclerAdapter<MyFollowingAdapter.MyFollowingViewHolder, MyFollowing>(
    list
) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyFollowingViewHolder =
        MyFollowingViewHolder(
            LayoutMyfollowingItemBinding
                .inflate(
                    LayoutInflater
                        .from(parent.context), parent, false
                )
        )

    override fun onBindViewHolder(holder: MyFollowingViewHolder, position: Int) {
        holder.bind(ItemMyFollowingViewModel(list[position]))
    }

    class MyFollowingViewHolder(
        private val binding: LayoutMyfollowingItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemMyFollowingViewModel) {
            binding.viewModel = viewModel

        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}