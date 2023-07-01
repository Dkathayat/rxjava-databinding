package com.yewapp.ui.modules.manageshortvideo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yewapp.data.network.api.report.ReportedComment
import com.yewapp.data.network.api.report.ShortReportedVideo
import com.yewapp.databinding.ItemReportedshortsCommentBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.manageshortvideo.vm.ReportedShortCommentItemsViewModel
import com.yewapp.ui.modules.manageshortvideo.vm.ReportedShortVideosItemViewModel

class ReportedShortCommentAdapter(
    private val reportedShortList: MutableList<ReportedComment>,
    private val onReportedFeedOptionClickListener: ReportedShortCommentItemsViewModel.OnReportedShortOptionClickListener,
) : BaseRecyclerAdapter<RecyclerView.ViewHolder, ReportedComment>(reportedShortList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ReportedShortCommentViewHolder(
            ItemReportedshortsCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ReportedShortCommentViewHolder).bind(
            ReportedShortCommentItemsViewModel(
                reportedShortList[position],
                position,
                onReportedFeedOptionClickListener
            )
        )
    }

    inner class ReportedShortCommentViewHolder(val binding: ItemReportedshortsCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel:ReportedShortCommentItemsViewModel){
            binding.viewModel = viewModel
        }
    }
}