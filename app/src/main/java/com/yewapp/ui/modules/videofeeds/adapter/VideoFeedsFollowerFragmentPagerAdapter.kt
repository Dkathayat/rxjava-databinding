package com.yewapp.ui.modules.videofeeds.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yewapp.ui.modules.videofeeds.followers.VideoFeedFollowerFragment
import com.yewapp.ui.modules.videofeeds.following.VideoFeedFollowingFragment

class VideoFeedsFollowerFragmentPagerAdapter(
    val userId: Int,
    val userName: String,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> VideoFeedFollowerFragment.newInstance(userId, userName)
            1 -> VideoFeedFollowingFragment.newInstance(userId, userName)
            else -> throw IllegalStateException()
        }
    }
}