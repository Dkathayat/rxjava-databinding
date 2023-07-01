package com.yewapp.ui.modules.comments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.comment.Comment
import com.yewapp.databinding.ItemCommentBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.comments.vm.ItemCommentsViewModel

class CommentsAdapter(
    val     commentList: MutableList<Comment>,
) : BaseRecyclerAdapter<CommentsAdapter.CommentsViewHolder, Comment>(
    commentList
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CommentsViewHolder =
        CommentsViewHolder(
            ItemCommentBinding
                .inflate(
                    LayoutInflater
                        .from(parent.context), parent, false
                )
        )


    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bind(ItemCommentsViewModel(commentList[position], position))

    }
    /*fun clearItems() {
        commentList.clear()
        notifyDataSetChanged()
    }*/

    fun addItems(list: List<Comment>) {
        commentList.addAll(list)
        notifyDataSetChanged()
    }

    class CommentsViewHolder(
        private val binding: ItemCommentBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemCommentsViewModel) {
            binding.viewModel = viewModel
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun clearList() {
        commentList.clear()
        notifyDataSetChanged()
    }
}