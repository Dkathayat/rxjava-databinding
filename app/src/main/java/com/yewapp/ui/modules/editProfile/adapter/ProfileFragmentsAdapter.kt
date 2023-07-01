package com.yewapp.ui.modules.editProfile.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yewapp.ui.modules.editProfile.fragment.ProfileDetailFragment
import com.yewapp.ui.modules.editProfile.fragment.SportDetailFragment

class ProfileFragmentsAdapter(
    val associateID: String,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SportDetailFragment.newInstance(associateID)
            1 -> ProfileDetailFragment.newInstance(associateID)
            else -> throw IllegalStateException()
        }
    }
}