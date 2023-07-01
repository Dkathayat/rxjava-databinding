package com.yewapp.ui.modules.addchallenge.challengeroutes.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.ui.modules.addchallenge.challengeroutes.favorites.FavoritesRoutesFragment
import com.yewapp.ui.modules.addchallenge.challengeroutes.latest.LatestRoutesFragment
import com.yewapp.ui.modules.addchallenge.challengeroutes.popular.PopularRoutesFragment

class RouteListingTabsAdapter(
    val fragmentManager: FragmentManager, lifecycle: Lifecycle,
    val challengeModel: ChallengeModel
) : FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PopularRoutesFragment.newInstance(challengeModel)
            1 -> LatestRoutesFragment.newInstance(challengeModel)
            2 -> FavoritesRoutesFragment.newInstance(challengeModel)
            else -> throw IllegalAccessException()
        }
    }
}