package com.yewapp.ui.modules.manageshortvideo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.yewapp.R
import com.yewapp.data.network.api.report.ReportedPosts
import com.yewapp.data.network.api.report.ShortReportedVideo
import com.yewapp.databinding.FragmentManageShortVideoBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.managefeeds.fragment.reported.ReportedFeedAdapter
import com.yewapp.ui.modules.managefeeds.vm.ReportedFeedsViewModel
import com.yewapp.ui.modules.manageshortvideo.adapter.ReportedShortVideoAdapter
import com.yewapp.ui.modules.manageshortvideo.navigator.ReportedShortVideoNavigator
import com.yewapp.ui.modules.manageshortvideo.vm.ReportedShortVideoViewModel
import com.yewapp.ui.modules.manageshortvideo.vm.ReportedShortVideosItemViewModel

class ReportedShortVideoFragment(override val layoutId: Int = R.layout.fragment_manage_short_video) :
    BaseFragment<ReportedShortVideoNavigator, ReportedShortVideoViewModel, FragmentManageShortVideoBinding>(),
    ReportedShortVideoNavigator {
    private lateinit var adapter: ReportedShortVideoAdapter
    override fun onViewModelCreated(viewModel: ReportedShortVideoViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: FragmentManageShortVideoBinding) {
        viewModel?.getReportedShortVideo()
        initializeAdapter()

    }

    override fun onBackPress() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bind(ReportedShortVideoViewModel::class.java,inflater,container)
    }

    override fun onStart() {
        super.onStart()
        addObserver()
    }

    private fun initializeAdapter() {
        val list = mutableListOf<ShortReportedVideo>()
        adapter = ReportedShortVideoAdapter(
            list,
            object : ReportedShortVideosItemViewModel.OnReportedShortOptionClickListener {
                override fun onOptionMenuClick(commentId: ShortReportedVideo) {
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
        viewModel?.reportedListLive?.observe(this, Observer {
            adapter.setItems(it)

        })
    }

}