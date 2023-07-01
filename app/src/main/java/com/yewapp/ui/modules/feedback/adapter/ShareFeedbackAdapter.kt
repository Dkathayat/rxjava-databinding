package com.yewapp.ui.modules.feedback.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yewapp.ui.modules.feedback.fragment.BugFixesFragment
import com.yewapp.ui.modules.feedback.fragment.ShareFeedbackFragment

class ShareFeedbackAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ShareFeedbackFragment()
            1 -> BugFixesFragment()

            else -> throw IllegalStateException()
        }
    }
}