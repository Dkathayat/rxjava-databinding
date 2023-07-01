package com.yewapp.ui.modules.editProfile

import com.google.android.material.tabs.TabLayoutMediator
import com.yewapp.R
import com.yewapp.databinding.ActivityEditProfileBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.editProfile.adapter.ProfileFragmentsAdapter
import com.yewapp.ui.modules.editProfile.navigator.EditProfileNavigator
import com.yewapp.ui.modules.editProfile.vm.EditProfileViewModel


class EditProfileActivity :
    BaseActivity<EditProfileNavigator, EditProfileViewModel, ActivityEditProfileBinding>(),
    EditProfileNavigator {
    override fun getLayout(): Int = R.layout.activity_edit_profile

    override fun init() {
        bind(EditProfileViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: EditProfileViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityEditProfileBinding) {

    }

    override fun initializeAdapter() {
        viewDataBinding.viewPager.adapter =
            ProfileFragmentsAdapter(viewModel.associateID,supportFragmentManager, lifecycle)
        if (viewModel.fromSignUp)
            viewDataBinding.viewPager.currentItem = 1

        bindViewPager()
    }

    private fun bindViewPager() {
        TabLayoutMediator(
            viewDataBinding.tabLayout,
            viewDataBinding.viewPager
        ) { tab, position ->

            when (position) {
                0 -> tab.text = getString(R.string.sport_images)
                1 -> tab.text = getString(R.string.profile_details)
            }
        }.attach()
    }

//    override fun onBackClick() {
//        if (viewDataBinding.viewPager.currentItem == 1) {
//            val fragment = ProfileDetailFragment()
//            if (fragment.validateOnBackPress()) {
//                finish()
//            } else {
//                Toast.makeText(this,"Please fill mandatory fields first",Toast.LENGTH_LONG).show()
//            }
//        }
//        else{
//            finish()
//        }
//    }

}