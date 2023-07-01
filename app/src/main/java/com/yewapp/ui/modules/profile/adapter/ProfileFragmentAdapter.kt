package com.yewapp.ui.modules.profile.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yewapp.data.network.api.signup.Profile
import com.yewapp.ui.modules.profile.fragment.activities.ActivitiesFragment
import com.yewapp.ui.modules.profile.fragment.associate.AssociateFragment
import com.yewapp.ui.modules.profile.fragment.mypoints.MyPointsFragment
import com.yewapp.ui.modules.profile.fragment.profile.ProfileFragment
import com.yewapp.ui.modules.profile.fragment.spectator.SpectatorFragment

class ProfileFragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val user: Profile
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = if (user.isAssociate == true) 4 else 5

    override fun createFragment(position: Int): Fragment {
        if (user.isAssociate == true) {
            return when (position) {
                0 -> ProfileFragment()
                1 -> ActivitiesFragment()
                2 -> MyPointsFragment()
                3 -> SpectatorFragment()
                else -> throw IllegalAccessException()
            }
        } else {
            return when (position) {
                0 -> ProfileFragment()
                1 -> ActivitiesFragment()
                2 -> MyPointsFragment()
                3 -> AssociateFragment()
                4 -> SpectatorFragment()
                else -> throw IllegalAccessException()
            }
        }
    }

}