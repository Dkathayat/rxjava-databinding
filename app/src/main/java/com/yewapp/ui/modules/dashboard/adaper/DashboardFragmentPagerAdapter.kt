package com.yewapp.ui.modules.dashboard.adaper

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yewapp.ui.modules.dashboard.fragment.challenges.ChallengesFragment
import com.yewapp.ui.modules.dashboard.fragment.feeds.FeedsFragment
import com.yewapp.ui.modules.profile.MainProfileFragment

const val DASHBOARD_FRAGMENT_COUNT = 5

class DashboardFragmentPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = DASHBOARD_FRAGMENT_COUNT

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FeedsFragment()
            1 -> ChallengesFragment()
            2 -> FeedsFragment()
            3 -> FeedsFragment()
            4 -> MainProfileFragment()
            else -> throw IllegalArgumentException()
        }
    }
}