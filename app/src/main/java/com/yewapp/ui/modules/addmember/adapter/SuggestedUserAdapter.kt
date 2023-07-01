package com.yewapp.ui.modules.addmember.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.feed.SuggestedUserList
import com.yewapp.databinding.ItemSuggestedUserBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.addmember.vm.ItemSuggestedViewModel

class SuggestedUserAdapter(
    val userId: Int?,
    val userList: MutableList<SuggestedUserList>,
    val onClickListener: ItemSuggestedViewModel.OnItemClickListener
) :
    BaseRecyclerAdapter<SuggestedUserAdapter.ViewHolderWithImage, SuggestedUserList>(userList) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SuggestedUserAdapter.ViewHolderWithImage =

        SuggestedUserAdapter.ViewHolderWithImage(
            ItemSuggestedUserBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
        )

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolderWithImage, position: Int) {
        holder.bind(ItemSuggestedViewModel(userList[position], onClickListener, position))
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun clearFavUserItems() {
        userList.clear()
        notifyDataSetChanged()
    }

    fun addItems(list: List<SuggestedUserList>) {
        userList.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolderWithImage(val binding: ItemSuggestedUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ItemSuggestedViewModel) {
            binding.viewModel = viewModel
            if (viewModel.item.following) {
                binding.followTextview.text = "UnFollow"
            }
        }
    }

}