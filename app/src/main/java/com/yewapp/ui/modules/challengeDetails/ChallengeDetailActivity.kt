package com.yewapp.ui.modules.challengeDetails

import com.yewapp.R
import com.yewapp.databinding.ActivityChallengeDetailBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.challengeDetails.navigator.ChallengeDetailNavigator
import com.yewapp.ui.modules.challengeDetails.vm.ChallengeDetailViewModel
import com.yewapp.ui.modules.giveawayDetails.GiveAwayDetailsActivity

class ChallengeDetailActivity :
    BaseActivity<ChallengeDetailNavigator, ChallengeDetailViewModel, ActivityChallengeDetailBinding>(),
    ChallengeDetailNavigator {

    override fun getLayout(): Int = R.layout.activity_challenge_detail
    override fun init() {
        bind(ChallengeDetailViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: ChallengeDetailViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityChallengeDetailBinding) {

    }

    override fun navigateToGiveAwayDetails() {
        startActivity(
            intentProviderFactory.create(
                GiveAwayDetailsActivity::class.java,
                null,
                0
            )
        )
    }

    override fun navigateDescriptionDetails() {
        startActivity(
            intentProviderFactory.create(
                ChallengeDescriptionDetailActivity::class.java,
                null,
                0
            )
        )
    }
}
