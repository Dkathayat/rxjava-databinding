package com.yewapp.ui.modules.dashboard.fragment.feeds.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.feed.FavoriteList
import com.yewapp.databinding.ItemAllFavoriteBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.ItemFavoriteUserViewModel

class FavoriteUserAdapter(
    val userId: Int?,
    val userList: MutableList<FavoriteList>,
    val onClickListener: ItemFavoriteUserViewModel.OnItemClickListener
) :
    BaseRecyclerAdapter<FavoriteUserAdapter.ViewHolderWithImage, FavoriteList>(userList) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteUserAdapter.ViewHolderWithImage =

        FavoriteUserAdapter.ViewHolderWithImage(
            ItemAllFavoriteBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
        )


    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolderWithImage, position: Int) {


        holder.bind(ItemFavoriteUserViewModel(userList[position], onClickListener, position))


    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun clearFavUserItems() {
        userList.clear()
        notifyDataSetChanged()
    }

    fun addItems(list: List<FavoriteList>) {
        userList.addAll(list)
        notifyDataSetChanged()
    }


    class ViewHolderWithImage(val binding: ItemAllFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ItemFavoriteUserViewModel) {
            binding.viewModel = viewModel
        }
    }
}