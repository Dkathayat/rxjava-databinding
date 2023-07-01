package com.yewapp.ui.modules.managefeeds.fragment.reported

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yewapp.R
import com.yewapp.data.network.api.report.ReportedPosts
import com.yewapp.databinding.ManageFeedsReportFragmentBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.managefeeds.vm.ReportedFeedsViewModel

class FeedReportedFragment(override val layoutId: Int = R.layout.manage_feeds_report_fragment) :
    BaseFragment<FeedReportedNavigator, FeedReportedViewModel, ManageFeedsReportFragmentBinding>(),
    FeedReportedNavigator {

//    var layoutManager = LinearLayoutManager(activity)
    private lateinit var adapter: ReportedFeedAdapter

    override fun onViewModelCreated(viewModel: FeedReportedViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ManageFeedsReportFragmentBinding) {
        viewModel?.getReportedPostList()
        initializeAdapter()
    }

    private fun initializeAdapter() {
        val list = mutableListOf<ReportedPosts>()
        adapter = ReportedFeedAdapter(
            list,
            object : ReportedFeedsViewModel.OnReportedFeedOptionClickListener {
                override fun onOptionMenuClick(commentId: ReportedPosts) {
                    viewModel?.deleteReportedPost(commentId.id)
                }
            },
        )
        viewDataBinding.rvFeeds.adapter = adapter.apply {

        }
//        viewDataBinding.rvFeeds.layoutManager = layoutManager
        //viewDataBinding.rvFeeds.addOnScrollListener(scrollListener)
    }

    private fun addObserver() {
        viewModel?.reportedListLive?.observe(this, Observer {
            adapter.setItems(it)
        })
    }

    override fun onStart() {
        super.onStart()
        addObserver()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bind(FeedReportedViewModel::class.java, inflater, container)
    }


//    val scrollListener = object : RecyclerView.OnScrollListener() {
//
//        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//            super.onScrollStateChanged(recyclerView, newState)
//        }
//
//        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//            super.onScrolled(recyclerView, dx, dy)
//            val viewModel = viewModel ?: return
//            val visibleItemCount: Int = layoutManager.childCount
//            val totalItemCount: Int = layoutManager.itemCount
//            val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
//            //val isLastPage = viewModel.currentPage > viewModel.lastPage
//
//            viewModel.getReportedPostList()
//        }
//    }


    override fun onBackPress() {
    }
}