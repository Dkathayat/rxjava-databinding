package com.yewapp.ui.modules.addchallenge.challengepreview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.yewapp.R
import com.yewapp.data.network.api.invitemember.InviteMember
import com.yewapp.data.network.api.routes.Coordinate
import com.yewapp.data.network.api.routes.Coordinates
import com.yewapp.databinding.ActivityChallengePreviewBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.dialogs.SuccessDialog
import com.yewapp.ui.modules.addchallenge.challengepreview.extras.UpdateParticipantsExtras
import com.yewapp.ui.modules.createroute.CreateRouteActivity
import com.yewapp.ui.modules.createroute.extras.CreateRouteExtras
import com.yewapp.ui.modules.dashboard.DashboardActivity
import com.yewapp.ui.modules.login.LoginActivity
import com.yewapp.ui.modules.settings.setting.SettingsActivity
import com.yewapp.ui.modules.updateparticipants.UpdateParticipantsActivity
import com.yewapp.utils.ROUTE_DATA

//Todo:Add Challenge Step 10
class ChallengePreviewActivity :
    BaseActivity<ChallengePreviewNavigator, ChallengePreviewViewModel, ActivityChallengePreviewBinding>(),
    ChallengePreviewNavigator {

    @SuppressLint("LongLogTag", "LogNotTimber")
    var startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.data != null) {
                if (result.resultCode == 100108) {
                    Log.d(
                        "ChallengePreviewActivity",
                        "From UpdateParticipantsActivity:    RESULT_OK\n"
                    )
                    setReceivedData(result.data ?: return@registerForActivityResult)
                }
                if (result.resultCode == Activity.RESULT_CANCELED) {
                    Log.d(
                        "ChallengePreviewActivity",
                        "From UpdateParticipantsActivity:    CANCELLED\n"
                    )
                }
            } else {
                Log.d("ChallengePreviewActivity", "From UpdateParticipantsActivity:    NO DATA\n")
            }

        }

    private fun setReceivedData(data: Intent) {
        viewModel.resetImageViews()
        viewModel.challengeModel.inviteMembers.clear()
        val updatedList =
            data.getParcelableArrayListExtra<InviteMember>(UpdateParticipantsExtras.PARTICIPANTS_LIST)
                ?: return
        viewModel.challengeModel.inviteMembers.addAll(updatedList)
        viewModel.setChallengeDetails(viewModel.challengeModel)
    }


    override fun getLayout(): Int {
        return R.layout.activity_challenge_preview
    }

    override fun init() {
        bind(ChallengePreviewViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: ChallengePreviewViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityChallengePreviewBinding) {
//        Repository.getInstance().getChallengeProgress().observe(this) {
//            viewModel.challengeExtras = it
//        }
    }

    override fun onAthleteClicked() {
        if (viewModel.challengeModel.inviteMembers.isNotEmpty()) {
            val extras = UpdateParticipantsExtras.participantsListExtras {
                this.updateParticipantsList = viewModel.challengeModel.inviteMembers
            }
            val updateParticipants = intentProviderFactory.create(
                UpdateParticipantsActivity::class.java, extras, 0
            )
            startForResult.launch(updateParticipants)
        }

    }

    override fun createChallengeSuccess() {
        startActivity(
            intentProviderFactory.create(
                DashboardActivity::class.java, null, 0
            )
        )

    }

    override fun showAlertForInactiveChallenge() {
        SuccessDialog.Builder().setDescription(
            viewModel.dataManager.getResourceProvider()
                .getString(R.string.in_active_challenge_message)
        ).addPositiveListener {
            viewModel.publishChallenge(true)
        }.build().show(this)

    }


}