package com.yewapp.ui.modules.refer.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yewapp.ui.modules.refer.fragments.ChooseContactsFragment
import com.yewapp.ui.modules.refer.fragments.ReferRewardHistoryFragment
import com.yewapp.ui.modules.refer.fragments.ReferredFriendsFragment

class ReferFragmentPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ChooseContactsFragment()
            1 -> ReferredFriendsFragment()
            2 -> ReferRewardHistoryFragment()
            else -> throw IllegalStateException()
        }
    }
}