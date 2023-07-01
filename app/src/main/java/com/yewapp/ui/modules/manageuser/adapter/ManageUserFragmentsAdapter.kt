package com.yewapp.ui.modules.manageuser.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yewapp.ui.modules.manageuser.fragment.BlockedFragment
import com.yewapp.ui.modules.manageuser.fragment.FavoriteManageUserFragment
import com.yewapp.ui.modules.manageuser.fragment.MutedFragment
import com.yewapp.ui.modules.manageuser.fragment.ReportedManageUserFragment

class ManageUserFragmentsAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BlockedFragment()
            1 -> FavoriteManageUserFragment()
            2 -> ReportedManageUserFragment()
            3 -> MutedFragment()


            else -> throw IllegalStateException()
        }
    }
}