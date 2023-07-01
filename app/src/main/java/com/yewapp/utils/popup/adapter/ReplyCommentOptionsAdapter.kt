package com.yewapp.utils.popup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.video.Reply
import com.yewapp.databinding.ItemCommentReplyOptionsBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.utils.popup.vm.ItemCommentReplyOptionViewModel

class ReplyCommentOptionsAdapter(
    val options: MutableList<String>,
    val reply: Reply,
    val listener: (String) -> Unit,
) : BaseRecyclerAdapter<ReplyCommentOptionsAdapter.ViewHolder, String>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCommentReplyOptionsBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_comment_reply_options, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ItemCommentReplyOptionViewModel(options[position], position, reply, listener))
    }

    class ViewHolder(val binding: ItemCommentReplyOptionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemCommentReplyOptionViewModel) {
            binding.viewModel = viewModel
        }
    }
}