package com.yewapp.ui.modules.profile.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.yewapp.R
import com.yewapp.databinding.FragmentProfileBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.dialogs.profile.ProfileSingleSelectionPopUp
import com.yewapp.ui.modules.editProfile.EditProfileActivity
import com.yewapp.ui.modules.editProfile.extras.EditProfileExtras
import com.yewapp.ui.modules.follower.FollowerActivity
import com.yewapp.ui.modules.follower.FollowerExtras
import com.yewapp.ui.modules.profile.navigator.ProfileNavigator

class ProfileFragment(override val layoutId: Int = R.layout.fragment_profile) :
    BaseFragment<ProfileNavigator, ProfileViewModel, FragmentProfileBinding>(), ProfileNavigator {

    override fun onViewModelCreated(viewModel: ProfileViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: FragmentProfileBinding) {
    }

    override fun onResume() {
        super.onResume()
        if (isVisible && isAdded)
            viewModel?.getProfile()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return bind(ProfileViewModel::class.java, inflater, container)
    }

    override fun navigateToEditProfile() {
        val extras = EditProfileExtras.editProfileExtras {
            this.isSignUp = false
            this.assoicateID = ""
        }
        startActivity(
            intentProviderFactory.create(
                EditProfileActivity::class.java, extras, 0
            )
        )
    }

    override fun navigateToFollower(position: Int) {
        val extras = FollowerExtras.followerExtras {
            this.position = position
        }
        startActivity(
            intentProviderFactory.create(
                FollowerActivity::class.java, extras, 0
            )
        )
    }

    override fun navigateCompareStatistic() {
        Toast.makeText(requireContext(), "under dev", Toast.LENGTH_SHORT).show()
//        startActivity(
//            intentProviderFactory.create(
//                FollowerActivity::class.java,
//                null,
//                0
//            )
//        )
    }

    //Single Selection on click
    override fun chooseWhoCanSeeStats(view: View) {
        ProfileSingleSelectionPopUp.Builder().addList(viewModel?.whoCanSeeStatsList ?: return)
            .setListener {
                viewModel?.whoCanSeeYourStats?.set(it)
                viewModel?.updateProfileSettings()
            }.build().show(view)
    }

    //YES OR NO
    override fun allowUsersCanSeePoints(view: View) {
        ProfileSingleSelectionPopUp.Builder().addList(viewModel?.allowUserToSeePointsList ?: return)
            .setListener {
                if (viewModel?.isUserCanSeeYourPointsText?.get().equals(it)) {
                    return@setListener
                }

                if (viewModel?.allowUserToSeeYourPoint?.get() ?: return@setListener) {
                    viewModel?.allowUserToSeeYourPoint?.set(false)
                } else {
                    viewModel?.allowUserToSeeYourPoint?.set(true)
                }
                viewModel?.isUserCanSeeYourPointsText?.set(it)
                viewModel?.updateProfileSettings()
            }.build().show(view)

    }

    override fun becameSponsor() {
        Toast.makeText(requireContext(), "becameSponsor Clicked", Toast.LENGTH_SHORT).show()

    }

    override fun onBackPress() {

    }
}
