package com.yewapp.ui.modules.manageshortvideo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.yewapp.R
import com.yewapp.data.network.api.report.ReportedComment
import com.yewapp.data.network.api.report.ShortReportedVideo
import com.yewapp.databinding.FragmentManageShortCommentBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.manageshortvideo.adapter.ReportedShortCommentAdapter
import com.yewapp.ui.modules.manageshortvideo.navigator.ReportedShortCommentsNavigator
import com.yewapp.ui.modules.manageshortvideo.vm.ReportedShortCommentItemsViewModel
import com.yewapp.ui.modules.manageshortvideo.vm.ReportedShortCommentViewModel

class ReportedShortCommentFragment(override val layoutId: Int = R.layout.fragment_manage_short_comment) :
    BaseFragment<ReportedShortCommentsNavigator, ReportedShortCommentViewModel, FragmentManageShortCommentBinding>(),
    ReportedShortCommentsNavigator {
    private lateinit var adapter: ReportedShortCommentAdapter
    override fun onViewModelCreated(viewModel: ReportedShortCommentViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: FragmentManageShortCommentBinding) {
        viewModel?.getReportedShortVideoComments()
        initializeAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bind(ReportedShortCommentViewModel::class.java, inflater, container)
    }

    override fun onStart() {
        super.onStart()
        addObserver()
    }
    private fun initializeAdapter() {
        val list = mutableListOf<ReportedComment>()
        adapter = ReportedShortCommentAdapter(
            list,
            object : ReportedShortCommentItemsViewModel.OnReportedShortOptionClickListener {
                override fun onOptionMenuClick(commentId: ReportedComment) {
                    viewModel?.deleteReportedShortVideo(commentId.id)
                }
            },
        )
        viewDataBinding.rvShortVideo.adapter = adapter.apply {

        }
//        viewDataBinding.rvFeeds.layoutManager = layoutManager
        //viewDataBinding.rvFeeds.addOnScrollListener(scrollListener)
    }
    private fun addObserver() {
        viewModel?.reportedCommentListLive?.observe(this, Observer {
            adapter.setItems(it)

        })
    }
    override fun onBackPress() {
        TODO("Not yet implemented")
    }

}