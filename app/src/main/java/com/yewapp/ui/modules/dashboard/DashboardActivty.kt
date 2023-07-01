package com.yewapp.ui.modules.dashboard

import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.yewapp.R
import com.yewapp.databinding.ActivityDashboardBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.dialogs.CompleteProfileDialog
import com.yewapp.ui.modules.addchallenge.addchallengesportstype.AddChallengeActivity
import com.yewapp.ui.modules.addmember.AddMemberFollowActivity
import com.yewapp.ui.modules.dashboard.fragment.feeds.FeedsFragment
import com.yewapp.ui.modules.dashboard.navigator.DashboardNavigator
import com.yewapp.ui.modules.dashboard.vm.DashboardViewModel
import com.yewapp.ui.modules.editProfile.EditProfileActivity
import com.yewapp.ui.modules.editProfile.extras.EditProfileExtras
import com.yewapp.ui.modules.feed.CreateFeedActivity
import com.yewapp.ui.modules.settings.setting.SettingsActivity
import com.yewapp.ui.modules.videofeeds.VideoFeedsActivity

class DashboardActivity :
    BaseActivity<DashboardNavigator, DashboardViewModel, ActivityDashboardBinding>(),
    DashboardNavigator {
    override fun getLayout(): Int = R.layout.activity_dashboard


    override fun init() {
        bind(DashboardViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: DashboardViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityDashboardBinding) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<FeedsFragment>(R.id.vp_fragment_container)
        }
        //  viewModel.checkUserProfileCompletion(viewModel.dataManager.getResourceProvider().getString(R.string.complete_profile))
    }

    override fun scrollToPosition(fragment: Fragment) {
//        if (viewModel.scrollPosition != 0) {
//            Toast.makeText(this, "Not available at the moment", Toast.LENGTH_LONG).show()
//            return
//        }
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            replace(R.id.vp_fragment_container, fragment)
        }
//        viewDataBinding.vpFragmentContainer.currentItem = viewModel.scrollPosition
    }

    override fun navigateToFeed() {
        startActivity(
            intentProviderFactory.create(
                CreateFeedActivity::class.java,
                null,
                0
            )
        )
    }

    override fun navigateToProfileSetting() {
        startActivity(
            intentProviderFactory.create(
                SettingsActivity::class.java,
                null,
                0
            )
        )

    }

    override fun navigateToAddChallenge() {
        startActivity(
            intentProviderFactory.create(
                AddChallengeActivity::class.java,
                null,
                0
            )
        )
    }

    override fun navigateToVideoFeeds() {
        startActivity(
            intentProviderFactory.create(
                VideoFeedsActivity::class.java,
                null,
                0
            )
        )
    }

    override fun navigateToAddMember() {
        startActivity(
            intentProviderFactory.create(
                AddMemberFollowActivity::class.java,
                null,
                0
            )
        )
    }


    override fun showSportsCompletionAlert(message: String) {
        CompleteProfileDialog.Builder()
            .setDescription(message)
            .setClickListener {
                startActivity(
                    intentProviderFactory.create(
                        EditProfileActivity::class.java,
                        EditProfileExtras.editProfileExtras {
                            isSignUp = false
                        },
                        0
                    )
                )
            }
            .build()
            .show(this)
    }

}