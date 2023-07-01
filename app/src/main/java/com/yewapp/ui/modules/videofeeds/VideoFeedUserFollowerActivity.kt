package com.yewapp.ui.modules.videofeeds

import com.google.android.material.tabs.TabLayoutMediator
import com.yewapp.R
import com.yewapp.databinding.ActivityVideofeedFollowerBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.videofeeds.adapter.VideoFeedsFollowerFragmentPagerAdapter
import com.yewapp.ui.modules.videofeeds.navigator.VideoFeedUserFollowerNavigator
import com.yewapp.ui.modules.videofeeds.vm.VideoFeedUserFollowerViewModel

class VideoFeedUserFollowerActivity :
    BaseActivity<VideoFeedUserFollowerNavigator, VideoFeedUserFollowerViewModel, ActivityVideofeedFollowerBinding>(),
    VideoFeedUserFollowerNavigator {
    override fun getLayout(): Int {
        return R.layout.activity_videofeed_follower
    }

    override fun init() {
        bind(VideoFeedUserFollowerViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: VideoFeedUserFollowerViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityVideofeedFollowerBinding) {

    }

    override fun bindPager(userId: Int, userName: String) {

        viewDataBinding.layoutfollower.viewPagerFollower.adapter =
            VideoFeedsFollowerFragmentPagerAdapter(
                userId,
                userName,
                supportFragmentManager,
                lifecycle
            )
        viewDataBinding.layoutfollower.viewPagerFollower.isUserInputEnabled = false
        TabLayoutMediator(
            viewDataBinding.layoutfollower.tabLayoutFollower,
            viewDataBinding.layoutfollower.viewPagerFollower
        ) { tab, position ->

            when (position) {
                0 -> tab.text = getString(R.string.followers)
                1 -> tab.text = getString(R.string.following)
            }
        }.attach()
    }

}