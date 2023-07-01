package com.yewapp.ui.modules.managenotification.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.yewapp.R
import com.yewapp.databinding.SettingsFragmentBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.managenotification.navigator.SettingsFragmentNavigator
import com.yewapp.ui.modules.managenotification.vm.SettingsFragmentViewModel

class SettingsFragment(override val layoutId: Int = R.layout.settings_fragment) :
    BaseFragment<SettingsFragmentNavigator, SettingsFragmentViewModel, SettingsFragmentBinding>(),
    SettingsFragmentNavigator {

    override fun onViewModelCreated(viewModel: SettingsFragmentViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: SettingsFragmentBinding) {
        viewDataBinding.activityLikeSwitchCompat4.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.activityLike?.set(true)
            } else {
                viewModel?.activityLike?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.commentSwitchCompat.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.activityComment?.set(true)
            } else {
                viewModel?.activityComment?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.lostCrSwitchCompat.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.activityLostPosition?.set(true)
            } else {
                viewModel?.activityLostPosition?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.deviceSyncSwitchCompat.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.activityDataSync?.set(true)
            } else {
                viewModel?.activityDataSync?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.beaconSwitchCompat.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.activityBeacon?.set(true)
            } else {
                viewModel?.activityBeacon?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.becomeSpectorSwitchCompat4.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.becomeSpectator?.set(true)
            } else {
                viewModel?.becomeSpectator?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.spectorActivitySwitchCompat.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.spectatedUserActivity?.set(true)
            } else {
                viewModel?.spectatedUserActivity?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.friendsJoinSwitchCompat4.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.onFriendJoin?.set(true)
            } else {
                viewModel?.onFriendJoin?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.newFollowerSwitchCompat.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.newFollower?.set(true)
            } else {
                viewModel?.newFollower?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.friendActivitySwitchCompat.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.friendActivity?.set(true)
            } else {
                viewModel?.friendActivity?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.newChallengeSwitchCompat4.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.newPublicChallenge?.set(true)
            } else {
                viewModel?.newPublicChallenge?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.challengeProcessSwitchCompat4.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->

            if (isEnabled) {
                viewModel?.challengeProgress?.set(true)
            } else {
                viewModel?.challengeProgress?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.challengeRewardSwitchCompat4.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.challengeReward?.set(true)
            } else {
                viewModel?.challengeReward?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.challengeInviteSwitchCompat4.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.challengeInvite?.set(true)
            } else {
                viewModel?.challengeInvite?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.challengeCommentSwitchCompat4.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.challengeComment?.set(true)
            } else {
                viewModel?.challengeComment?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.challengeJoinedSwitchCompat4.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.challengeJoined?.set(true)
            } else {
                viewModel?.challengeJoined?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.challengeLeaderboardSwitchCompat4.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.challengeLeaderboardChange?.set(true)
            } else {
                viewModel?.challengeLeaderboardChange?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.feedLikeSwitchCompat4.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.feedLike?.set(true)
            } else {
                viewModel?.feedLike?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.feedCommentSwitchCompat.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.feedComment?.set(true)
            } else {
                viewModel?.feedComment?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.chatMessageSwitchCompat4.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.chatMessage?.set(true)
            } else {
                viewModel?.chatMessage?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.chatSubscriptionSwitchCompat.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            //viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.marketingSwitchCompat4.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.marketing?.set(true)
            } else {
                viewModel?.marketing?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.featureSubscriptionSwitchCompat.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.featureSubscriptionTips?.set(true)
            } else {
                viewModel?.featureSubscriptionTips?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
        viewDataBinding.emailSwitchCompat4.setOnCheckedChangeListener { compoundButton: CompoundButton, isEnabled: Boolean ->
            if (isEnabled) {
                viewModel?.emailAlerts?.set(true)
            } else {
                viewModel?.emailAlerts?.set(false)
            }
            viewModel?.onSetNotificationApiCall()
        }
    }

    override fun onBackPress() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bind(SettingsFragmentViewModel::class.java, inflater, container)
    }
}