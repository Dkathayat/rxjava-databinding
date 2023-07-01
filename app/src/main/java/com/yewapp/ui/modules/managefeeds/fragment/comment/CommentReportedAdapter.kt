package com.yewapp.ui.modules.managefeeds.fragment.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yewapp.data.network.api.comment.ReportedComments
import com.yewapp.databinding.ReportedFeedCommentsImageBinding
import com.yewapp.databinding.ReportedFeedCommentsWithoutimageBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.managefeeds.fragment.reported.ReportedFeedAdapter
import com.yewapp.ui.modules.managefeeds.vm.ReportedCommentViewModel

class CommentReportedAdapter(
    private val reportedCommentList: MutableList<ReportedComments>,
    private val onReportedFeedOptionClickListener: ReportedCommentViewModel.OnReportedFeedCommentsOptionClickListener,
) :
    BaseRecyclerAdapter<RecyclerView.ViewHolder, ReportedComments>(reportedCommentList) {

    companion object {
        const val VIEW_TYPE_WITH_IMAGE = 1
        const val VIEW_TYPE_WITHOUT_IMAGE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ReportedFeedAdapter.VIEW_TYPE_WITH_IMAGE) {
            return ViewHolderWithImageComments(
                ReportedFeedCommentsImageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return ViewHolderWithoutImageComments(
                ReportedFeedCommentsWithoutimageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (reportedCommentList[position].feeds.url.isNotEmpty()) {

            (holder as ViewHolderWithImageComments).bind(
                ReportedCommentViewModel(
                    reportedCommentList[position],
                    position,
                    onReportedFeedOptionClickListener
                )
            )
            Glide.with(holder.itemView).load(reportedCommentList[position].feeds.url)
                .into(holder.binding.feedBgImg)
        } else {
            (holder as ViewHolderWithoutImageComments).bind(
                ReportedCommentViewModel(
                    reportedCommentList[position],
                    position,
                    onReportedFeedOptionClickListener
                )
            )
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (reportedCommentList[position].feeds.url.isEmpty()) {
            VIEW_TYPE_WITHOUT_IMAGE
        } else {
            VIEW_TYPE_WITH_IMAGE
        }
    }

    fun addReportedFeedItems(list: List<ReportedComments>) {
        reportedCommentList.addAll(list)
        notifyDataSetChanged()
    }


    class ViewHolderWithImageComments(val binding: ReportedFeedCommentsImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ReportedCommentViewModel) {
            binding.viewModel = viewModel
        }
    }

    class ViewHolderWithoutImageComments(val binding: ReportedFeedCommentsWithoutimageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ReportedCommentViewModel) {
            binding.viewModel = viewModel
        }
    }
    fun clearItems() {
        reportedCommentList.clear()
        notifyDataSetChanged()


    }
}