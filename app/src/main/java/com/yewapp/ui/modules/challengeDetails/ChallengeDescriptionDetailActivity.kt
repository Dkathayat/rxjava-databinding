package com.yewapp.ui.modules.challengeDetails

import com.yewapp.R
import com.yewapp.databinding.ActivityChallengeDescriptionDetailBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.challengeDetails.navigator.ChallengeDescriptionDetailNavigator
import com.yewapp.ui.modules.challengeDetails.vm.ChallengeDescriptionDetailViewModel

class ChallengeDescriptionDetailActivity :
    BaseActivity<ChallengeDescriptionDetailNavigator, ChallengeDescriptionDetailViewModel, ActivityChallengeDescriptionDetailBinding>(),
    ChallengeDescriptionDetailNavigator {
    override fun getLayout(): Int = R.layout.activity_challenge_description_detail

    override fun init() {
        bind(ChallengeDescriptionDetailViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: ChallengeDescriptionDetailViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityChallengeDescriptionDetailBinding) {
    }
}