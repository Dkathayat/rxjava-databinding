package com.yewapp.ui.modules.addmodelandequipments

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.yewapp.R
import com.yewapp.databinding.ActivityAddModelEquipmentDetailsBinding
import com.yewapp.ui.base.BaseActivity

class AddModelEquipmentDetailsActivity :
    BaseActivity<AddModelEquipmentDetailsNavigator, AddModelEquipmentDetailsViewModel, ActivityAddModelEquipmentDetailsBinding>(),
    AddModelEquipmentDetailsNavigator {
    override fun getLayout(): Int = R.layout.activity_add_model_equipment_details

    var modelEquipmentDataCount = 0

    override fun init() {
        bind(AddModelEquipmentDetailsViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: AddModelEquipmentDetailsViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityAddModelEquipmentDetailsBinding) {
    }


    override fun adapterInitialize(extras: Bundle?) {
        viewDataBinding.associateViewPager.adapter =
            AddModelEquipmentDetailsAdapter(supportFragmentManager, lifecycle, extras)
        viewDataBinding.associateViewPager.isUserInputEnabled = false// stop scrolling
        bindViewPager()
    }

    private fun bindViewPager() {
        viewDataBinding.tabLayout.isTabIndicatorFullWidth = false
        TabLayoutMediator(
            viewDataBinding.tabLayout, viewDataBinding.associateViewPager
        ) { tab, position ->

            when (position) {
                0 -> tab.text = getString(R.string.add_model)
                1 -> tab.text = getString(R.string.add_equipment)
            }
        }.attach()
    }
}