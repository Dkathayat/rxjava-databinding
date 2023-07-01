package com.yewapp.ui.modules.managefeeds.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yewapp.ui.modules.managefeeds.fragment.comment.CommentedFragment
import com.yewapp.ui.modules.managefeeds.fragment.reported.FeedReportedFragment

class ManageFeedsFragmentsAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FeedReportedFragment()
            1 -> CommentedFragment()
            else -> throw IllegalStateException()
        }
    }
}