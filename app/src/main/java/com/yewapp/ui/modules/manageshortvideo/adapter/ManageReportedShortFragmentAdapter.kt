package com.yewapp.ui.modules.manageshortvideo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yewapp.ui.modules.manageshortvideo.fragment.ReportedShortCommentFragment
import com.yewapp.ui.modules.manageshortvideo.fragment.ReportedShortVideoFragment

class ManageReportedShortFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return  when(position){
            0 -> ReportedShortVideoFragment()
            1 -> ReportedShortCommentFragment()

            else -> throw IllegalStateException()
        }
    }
}