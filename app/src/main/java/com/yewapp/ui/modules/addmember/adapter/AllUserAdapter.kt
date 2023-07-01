package com.yewapp.ui.modules.addmember.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.UserList
import com.yewapp.databinding.ItemAllUserToFollowBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.addmember.vm.ItemAllUserViewModel

class AllUserAdapter(
    val userId: Int?,
    val userList: MutableList<UserList>,
    val onClickListener: ItemAllUserViewModel.OnItemClickListener
) :
    BaseRecyclerAdapter<AllUserAdapter.ViewHolderWithImage, UserList>(userList) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllUserAdapter.ViewHolderWithImage =

        AllUserAdapter.ViewHolderWithImage(
            ItemAllUserToFollowBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
        )

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolderWithImage, position: Int) {
        holder.bind(ItemAllUserViewModel(userList[position], onClickListener, position))
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun clearAllUserItems() {
        userList.clear()
        notifyDataSetChanged()
    }

    fun addItems(list: List<UserList>) {
        userList.addAll(list)
        notifyDataSetChanged()
    }

    fun updateLikedItem(feed: UserList, position: Int) {
        userList[position] = feed
        notifyItemChanged(position)
    }


    class ViewHolderWithImage(val binding: ItemAllUserToFollowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ItemAllUserViewModel) {
            binding.viewModel = viewModel
            if (viewModel.item.following) {
                binding.unblockBtn.text = "UnFollow"
            } else {
                binding.unblockBtn.text = "Follow"
            }

        }
    }

}