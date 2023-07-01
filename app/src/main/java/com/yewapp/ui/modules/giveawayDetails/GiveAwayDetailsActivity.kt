package com.yewapp.ui.modules.giveawayDetails

import com.yewapp.R
import com.yewapp.databinding.ActivityGiveAwayDetailsBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.giveawayDetails.navigator.GiveAwayDetailsNavigator
import com.yewapp.ui.modules.giveawayDetails.vm.GiveAwayDetailsViewModel


class GiveAwayDetailsActivity :
    BaseActivity<GiveAwayDetailsNavigator, GiveAwayDetailsViewModel, ActivityGiveAwayDetailsBinding>(),
    GiveAwayDetailsNavigator {

    override fun getLayout(): Int = R.layout.activity_give_away_details
    override fun init() {
        bind(GiveAwayDetailsViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: GiveAwayDetailsViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityGiveAwayDetailsBinding) {

    }
}
