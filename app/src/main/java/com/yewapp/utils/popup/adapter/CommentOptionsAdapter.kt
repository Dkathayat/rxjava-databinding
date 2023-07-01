package com.yewapp.utils.popup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.video.Comment
import com.yewapp.databinding.ItemCommentOptionsBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.utils.popup.vm.ItemCommentOptionViewModel

class CommentOptionsAdapter(
    val options: MutableList<String>,
    val comment: Comment,
    val listener: (String) -> Unit,
) : BaseRecyclerAdapter<CommentOptionsAdapter.ViewHolder, String>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCommentOptionsBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_comment_options, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ItemCommentOptionViewModel(options[position], position, comment, listener))
    }

    class ViewHolder(val binding: ItemCommentOptionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemCommentOptionViewModel) {
            binding.viewModel = viewModel
        }
    }
}