package com.yewapp.ui.modules.dashboard.fragment.feeds.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yewapp.ui.modules.dashboard.fragment.feeds.AllFeedsFragment
import com.yewapp.ui.modules.dashboard.fragment.feeds.MyFeedsFragment

class FeedsPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllFeedsFragment()
            1 -> MyFeedsFragment()
            else -> throw IllegalStateException()
        }
    }
}