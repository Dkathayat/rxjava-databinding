package com.yewapp.ui.modules.subscription.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yewapp.ui.modules.subscription.fragment.CurrentPlanFragment
import com.yewapp.ui.modules.subscription.fragment.PricePlanFragment

class SubscriptionFragmentsAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CurrentPlanFragment()
            1 -> PricePlanFragment()
            else -> throw IllegalStateException()


        }
    }
}