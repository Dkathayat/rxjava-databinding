package com.yewapp.ui.modules.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.yewapp.R
import com.yewapp.databinding.FragmentMainProfileBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.profile.adapter.ProfileFragmentAdapter
import com.yewapp.ui.modules.profile.navigator.MainProfileNavigator
import com.yewapp.ui.modules.profile.vm.MainProfileViewModel

class MainProfileFragment(override val layoutId: Int = R.layout.fragment_main_profile) :
    BaseFragment<MainProfileNavigator, MainProfileViewModel, FragmentMainProfileBinding>(),
    MainProfileNavigator {

    override fun onViewModelCreated(viewModel: MainProfileViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: FragmentMainProfileBinding) {
        viewDataBinding.viewPager.adapter = ProfileFragmentAdapter(
            childFragmentManager, lifecycle, viewModel?.dataManager?.getUser() ?: return
        )
        viewDataBinding.viewPager.offscreenPageLimit = 1
        viewDataBinding.viewPager.isUserInputEnabled = false// stop scrolling
        bindViewPager()
    }

    private fun bindViewPager() {
        TabLayoutMediator(
            viewDataBinding.tabLayout, viewDataBinding.viewPager
        ) { tab, position ->
            if (viewModel?.dataManager?.getUser()?.isAssociate == true) {
                when (position) {
                    0 -> tab.text = activity?.getString(R.string.profile)
                    1 -> tab.text = activity?.getString(R.string.activities)
                    2 -> tab.text = activity?.getString(R.string.my_points)
                    3 -> tab.text = activity?.getString(R.string.spectors)
                }
            } else {
                when (position) {
                    0 -> tab.text = activity?.getString(R.string.profile)
                    1 -> tab.text = activity?.getString(R.string.activities)
                    2 -> tab.text = activity?.getString(R.string.my_points)
                    3 -> tab.text = activity?.getString(R.string.associate)
                    4 -> tab.text = activity?.getString(R.string.spectors)
                }
            }
        }.attach()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return bind(MainProfileViewModel::class.java, inflater, container)
    }

    override fun onBackPress() {

    }
}