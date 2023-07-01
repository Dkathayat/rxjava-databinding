package com.yewapp.ui.modules.videofeeds.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.video.Comment
import com.yewapp.data.network.api.video.Reply
import com.yewapp.databinding.ItemVedioFeedCommentsBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.videofeedcomment.adapter.ReplyAdapter
import com.yewapp.ui.modules.videofeedcomment.vm.ItemReplyViewModel
import com.yewapp.ui.modules.videofeeds.vm.ItemVideoFeedsCommentViewModel


class VideoFeedsCommentAdapter(
    var listData: MutableList<Comment>,
    var userId: Int,
    val listener: ItemVideoFeedsCommentViewModel.OnItemClickListener,
    val replyListener: ItemReplyViewModel.OnReplyItemClickListener//

) : BaseRecyclerAdapter<VideoFeedsCommentAdapter.CommentViewHolder, Comment>(listData) {
    // var allReplyLength = ObservableField<Int>(3)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CommentViewHolder =
        CommentViewHolder(
            ItemVedioFeedCommentsBinding
                .inflate(
                    LayoutInflater
                        .from(parent.context), parent, false
                )
        )

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(
            ItemVideoFeedsCommentViewModel(
                listData[position],
                listener,
                userId,
                position,
                replyListener
            )
        )//
        val adapterReply = ReplyAdapter(
            listData[position].id,
            userId,
            position,
            listData[position].reply as MutableList<Reply>,
            replyListener
        )//
        var replyRecyclerView = holder.itemView.findViewById<RecyclerView>(R.id.replyRecycler)
        replyRecyclerView.adapter = adapterReply
        replyRecyclerView!!.adapter?.notifyDataSetChanged()


        if (listData[position].reply?.size!! >= 4) {
            adapterReply.updateLength(3)
            holder.binding.viewAllTextview.visibility = View.VISIBLE
            holder.binding.viewLessTextview.visibility = View.GONE


        } else {
            adapterReply.updateLength(listData[position].reply?.size!!)
            holder.binding.viewAllTextview.visibility = View.GONE
            holder.binding.viewLessTextview.visibility = View.GONE
        }

        holder.binding.viewAllTextview.setOnClickListener {

            adapterReply.updateLength(listData[position].reply?.size!!)
            holder.binding.viewAllTextview.visibility = View.GONE
            holder.binding.viewLessTextview.visibility = View.VISIBLE
            replyRecyclerView!!.adapter?.notifyDataSetChanged()
        }
        holder.binding.viewLessTextview.setOnClickListener {

            adapterReply.updateLength(3)
            holder.binding.viewAllTextview.visibility = View.VISIBLE
            holder.binding.viewLessTextview.visibility = View.GONE
            replyRecyclerView!!.adapter?.notifyDataSetChanged()
        }
    }

    class CommentViewHolder(
        val binding: ItemVedioFeedCommentsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemVideoFeedsCommentViewModel) {
            binding.viewModel = viewModel
            if (viewModel.commentItem.likeStatus) {
                binding.ivLikes.setImageResource(R.drawable.ic_like_heart)
            } else {
                binding.ivLikes.setImageResource(R.drawable.ic_heart)
            }

        }
    }

    fun updateLikedItem(comment: Comment, position: Int) {
        listData[position] = comment
//        listData[position] = comment
        notifyItemChanged(position)
    }

    fun clearItems() {
        listData.clear()
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
//    override fun getItemCount(): Int {
//        return listData
//      //  return allReplyLength.get()!!
//    }
//
//    fun updateLength(length: Int) {
//        allReplyLength.set(length)
//
//    }
}