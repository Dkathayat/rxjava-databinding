package com.yewapp.ui.modules.manageuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.UserList
import com.yewapp.data.network.api.feed.FavoriteList
import com.yewapp.databinding.ItemManageUserListBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.manageuser.vm.ItemManageUserViewModel

class FavoriteUserListAdapter(
    private val favoriteUserList: MutableList<UserList>,
    val onItemClickListener: ItemManageUserViewModel.OnItemClickListener,
    val type: String
) : BaseRecyclerAdapter<FavoriteUserListAdapter.ViewHolder, UserList>(favoriteUserList) {

    class ViewHolder(val binding: ItemManageUserListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemManageUserViewModel) {
            binding.viewModel = viewModel
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemManageUserListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemManageUserViewModel(
                favoriteUserList[position],
                onItemClickListener,
                position,
                type
            )
        )
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }


    fun clearItems() {
        favoriteUserList.clear()
        notifyDataSetChanged()
    }
}