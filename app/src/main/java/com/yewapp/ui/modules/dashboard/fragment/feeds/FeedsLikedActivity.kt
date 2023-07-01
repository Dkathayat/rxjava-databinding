package com.yewapp.ui.modules.dashboard.fragment.feeds

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yewapp.R
import com.yewapp.databinding.ActivityFeedsLikedBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.dashboard.fragment.feeds.adapter.FeedsLikedAdapter
import com.yewapp.ui.modules.dashboard.fragment.feeds.navigator.FeedsLikedNavigator
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.FeedsLikedViewModel

class FeedsLikedActivity :
    BaseActivity<FeedsLikedNavigator, FeedsLikedViewModel, ActivityFeedsLikedBinding>(),
    FeedsLikedNavigator {

    override fun getLayout(): Int = R.layout.activity_feeds_liked

    private lateinit var adapter: FeedsLikedAdapter

    override fun init() {
        bind(FeedsLikedViewModel::class.java)
        viewModel.getFeedLikes()
    }

    override fun onViewModelCreated(viewModel: FeedsLikedViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityFeedsLikedBinding) {
        adapter = FeedsLikedAdapter(mutableListOf())
        adapter.setHasStableIds(true)
        viewDataBinding.likedFeeds.adapter = adapter
        viewDataBinding.likedFeeds.layoutManager = LinearLayoutManager(this)
        addObserver()
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.likedFeedList.observe(this, Observer {
            this.adapter.setItems(it)
        })
    }
}