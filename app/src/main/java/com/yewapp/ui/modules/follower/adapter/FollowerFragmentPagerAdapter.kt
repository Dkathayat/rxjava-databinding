package com.yewapp.ui.modules.follower.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yewapp.ui.modules.follower.fragments.MyFollowersFragment
import com.yewapp.ui.modules.follower.fragments.MyFollowingFragment

class FollowerFragmentPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyFollowersFragment()
            1 -> MyFollowingFragment()
            else -> throw IllegalStateException()
        }
    }
}