package com.yewapp.ui.modules.follower.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.follower.MyFollowers
import com.yewapp.databinding.LayoutMyfollowerItemBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.dashboard.fragment.feeds.UserAction
import com.yewapp.ui.modules.follower.vm.ItemMyFollowersViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFollowersAdapter(
    val followerList: MutableList<MyFollowers>,
    val onItemClickListener: (String, MyFollowers, Int) -> Unit,
) : BaseRecyclerAdapter<MyFollowersAdapter.MyFollowersViewHolder, MyFollowers>(
    followerList
) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyFollowersViewHolder =
        MyFollowersViewHolder(
            LayoutMyfollowerItemBinding
                .inflate(
                    LayoutInflater
                        .from(parent.context), parent, false
                )
        )


    override fun onBindViewHolder(holder: MyFollowersViewHolder, position: Int) {
        holder.bind(ItemMyFollowersViewModel(followerList[position], position, onItemClickListener))
    }

    fun addItems(list: List<MyFollowers>) {
        followerList.addAll(list)
        notifyDataSetChanged()
    }

    fun updateItem(myFollowers: MyFollowers, position: Int, action: UserAction) {
        CoroutineScope(Dispatchers.Main).launch {
            followerList.forEachIndexed { pos, it ->
                if (it.userId == myFollowers.userId) {
                    updateFollowerItem(myFollowers, pos, action)
                }
            }
            notifyDataSetChanged()
        }
    }

    private fun updateFollowerItem(myFollowers: MyFollowers, position: Int, action: UserAction) {
        when (action) {
            UserAction.BLOCK -> followerList[position].blocked = !myFollowers.blocked
            UserAction.FOLLOW -> followerList[position].following =
                !myFollowers.following
            UserAction.MUTE -> followerList[position].muted = !myFollowers.muted
            UserAction.FAVOURITE -> followerList[position].favourite =
                !myFollowers.favourite
        }
    }

    class MyFollowersViewHolder(
        private val binding: LayoutMyfollowerItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemMyFollowersViewModel) {
            binding.viewModel = viewModel
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}