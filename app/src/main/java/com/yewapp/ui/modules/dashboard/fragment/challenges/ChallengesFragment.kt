package com.yewapp.ui.modules.dashboard.fragment.challenges

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.yewapp.R
import com.yewapp.data.local.PreferenceKeys.Companion.CHALLENGE_CREATED_STATUS
import com.yewapp.data.local.PreferencesHelper
import com.yewapp.data.network.CREATE_CHALLENGE
import com.yewapp.databinding.FragmentChallengesBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.dashboard.fragment.challenges.adapter.ChallengesFragmentPagerAdapter
import com.yewapp.ui.modules.dashboard.fragment.challenges.navigator.ChallengesNavigator
import com.yewapp.ui.modules.dashboard.fragment.challenges.vm.ChallengesViewModel

class ChallengesFragment(override val layoutId: Int = R.layout.fragment_challenges) :
    BaseFragment<ChallengesNavigator, ChallengesViewModel, FragmentChallengesBinding>(),
    ChallengesNavigator {
    override fun onViewModelCreated(viewModel: ChallengesViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: FragmentChallengesBinding) {
        viewDataBinding.layoutChallenge.viewPagerChallenge.adapter =
            ChallengesFragmentPagerAdapter(childFragmentManager, lifecycle)
        bindViewPager()
        viewDataBinding.layoutChallenge.viewPagerChallenge.isUserInputEnabled =
            false// stop scrolling
    }

    override fun onResume() {
        super.onResume()
        if (viewModel?.dataManager?.getPreference()?.getBoolean(CHALLENGE_CREATED_STATUS, false)
                ?: return
        ) {
            viewDataBinding.layoutChallenge.viewPagerChallenge.currentItem = 1
            viewModel?.dataManager?.getPreference()?.saveBoolean(CHALLENGE_CREATED_STATUS, false)
        }
    }

    private fun bindViewPager() {
        TabLayoutMediator(
            viewDataBinding.layoutChallenge.tabLayoutChallenge,
            viewDataBinding.layoutChallenge.viewPagerChallenge
        ) { tab, position ->

            when (position) {
                0 -> tab.text = activity?.getString(R.string.upcoming)
                1 -> tab.text = activity?.getString(R.string.created_by_me)
                2 -> tab.text = activity?.getString(R.string.active)
                3 -> tab.text = activity?.getString(R.string.favorite)
                4 -> tab.text = activity?.getString(R.string.past_challenge)
            }
        }.attach()
    }

    override fun onBackPress() {
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bind(ChallengesViewModel::class.java, inflater, container)
    }
}

