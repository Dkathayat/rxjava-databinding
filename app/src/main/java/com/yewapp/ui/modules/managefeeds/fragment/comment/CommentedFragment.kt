package com.yewapp.ui.modules.managefeeds.fragment.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.yewapp.R
import com.yewapp.data.network.api.comment.ReportedComments
import com.yewapp.databinding.ManageFeedsCommentedFragmentBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.managefeeds.vm.ReportedCommentViewModel

class CommentedFragment(override val layoutId: Int = R.layout.manage_feeds_commented_fragment) :
    BaseFragment<ReportedCommentsNavigator, CommentedFragmentViewModel, ManageFeedsCommentedFragmentBinding>(),
    ReportedCommentsNavigator {

    private lateinit var commentReportedAdapter: CommentReportedAdapter
    override fun onStart() {
        super.onStart()
        addObserver()
    }

    override fun onBackPress() {

    }


    override fun onViewModelCreated(viewModel: CommentedFragmentViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ManageFeedsCommentedFragmentBinding) {
        viewModel?.getCommentReportedList()
        initializeAdapter()
    }


    private fun initializeAdapter() {
        val list = mutableListOf<ReportedComments>()
        commentReportedAdapter = CommentReportedAdapter(
            list, object : ReportedCommentViewModel.OnReportedFeedCommentsOptionClickListener {
                override fun onOptionMenuClick(commentId: ReportedComments) {
                    viewModel?.deleteCommentedReport(commentId.id)
                }
            },
        )
        viewDataBinding.rvFeeds.adapter = commentReportedAdapter.apply {
            setHasStableIds(true)
        }
//        viewDataBinding.swipeRefreshListener.setOnRefreshListener {
//            viewModel?.clearList()
//            commentReportedAdapter.clearItems()
//            viewModel?.getCommentReportedList()
//        }
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel._commentList.observe(this, Observer {
            commentReportedAdapter.setItems(it)

        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bind(CommentedFragmentViewModel::class.java, inflater, container)
    }


}