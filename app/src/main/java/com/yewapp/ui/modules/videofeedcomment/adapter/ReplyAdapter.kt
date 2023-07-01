package com.yewapp.ui.modules.videofeedcomment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.video.Reply
import com.yewapp.databinding.ReplyItemBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.videofeedcomment.vm.ItemReplyViewModel

class ReplyAdapter(
    val actualCommentId: Int,
    val userId: Int,
    val commentItemIndex: Int,
    val list: MutableList<Reply>,
    val listener: ItemReplyViewModel.OnReplyItemClickListener//

) : BaseRecyclerAdapter<ReplyAdapter.AllTabViewHolder, Reply>(list) {

    var allReplyLength = ObservableField<Int>(0)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllTabViewHolder =
        AllTabViewHolder(
            ReplyItemBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), parent.width, 4
        )


    override fun onBindViewHolder(holder: AllTabViewHolder, position: Int) {
        holder.bind(
            ItemReplyViewModel(
                actualCommentId,
                userId,
                commentItemIndex,
                list[position],
                position,
                listener
            )
        )

    }

    class AllTabViewHolder(
        private val binding: ReplyItemBinding,
        private val height: Int, private val rows: Int
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemReplyViewModel) {
            binding.viewModel = viewModel
            if (viewModel.item.likeStatus) {
                binding.ivLikes.setImageResource(R.drawable.ic_like_heart)
            } else {
                binding.ivLikes.setImageResource(R.drawable.ic_heart)
            }

        }

    }

    fun clearItems() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {

        return allReplyLength.get()!!
    }

    fun updateLength(length: Int) {
        allReplyLength.set(length)

    }
}