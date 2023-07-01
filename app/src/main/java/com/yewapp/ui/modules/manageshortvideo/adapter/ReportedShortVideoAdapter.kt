package com.yewapp.ui.modules.manageshortvideo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yewapp.data.network.api.report.ShortReportedVideo
import com.yewapp.databinding.ItemReportedShortVideoBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.managefeeds.vm.ReportedFeedsViewModel
import com.yewapp.ui.modules.manageshortvideo.vm.ReportedShortVideosItemViewModel

class ReportedShortVideoAdapter(
    private val reportedShortList: MutableList<ShortReportedVideo>,
    private val onReportedFeedOptionClickListener: ReportedShortVideosItemViewModel.OnReportedShortOptionClickListener,
) : BaseRecyclerAdapter<RecyclerView.ViewHolder, ShortReportedVideo>(reportedShortList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ReportedShortViewHolder(
            ItemReportedShortVideoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ReportedShortViewHolder).bind(
            ReportedShortVideosItemViewModel(
                reportedShortList[position],
                position,
                onReportedFeedOptionClickListener
            )
        )
    }

    inner class ReportedShortViewHolder(val binding: ItemReportedShortVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ReportedShortVideosItemViewModel) {
            binding.viewModel = viewModel
        }
    }
}