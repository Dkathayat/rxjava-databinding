package com.yewapp.ui.modules.managenotification.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yewapp.ui.modules.managenotification.fragment.NotificationFragment
import com.yewapp.ui.modules.managenotification.fragment.SettingsFragment

class ManageNotificationFragmentsAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NotificationFragment()
            1 -> SettingsFragment()
            else -> throw IllegalStateException()
        }
    }
}