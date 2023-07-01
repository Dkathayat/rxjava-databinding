package com.yewapp.ui.modules.dashboard.fragment.challenges.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.active.ActiveFragment
import com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.created.CreatedByMeFragment
import com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.favorite.FavoriteFragment
import com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.past.PastChallengeFragment
import com.yewapp.ui.modules.dashboard.fragment.challenges.fragments.upcoming.UpcomingFragment

class ChallengesFragmentPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UpcomingFragment()
            1 -> CreatedByMeFragment()
            2 -> ActiveFragment()
            3 -> FavoriteFragment()
            4 -> PastChallengeFragment()
            else -> throw IllegalStateException()
        }
    }
}