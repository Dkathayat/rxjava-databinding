package com.yewapp.ui.modules.addchallenge.additionalform

import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.yewapp.R
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.invitemember.InviteMember
import com.yewapp.databinding.ActivityAdditionalDetailFormBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.dialogs.challengepopupdialogs.ChallengeSingleSelectionPopUp
import com.yewapp.ui.modules.addchallenge.challengebanner.ChallengeBannerActivity
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.ui.modules.listner.Repository

//Todo:Add Challenge Step 7

class AdditionalFormActivity :
    BaseActivity<AdditionalFormNavigator, AdditionalFormViewModel, ActivityAdditionalDetailFormBinding>(),
    AdditionalFormNavigator {
    override fun getLayout(): Int {
        return R.layout.activity_additional_detail_form
    }

    override fun init() {
        bind(AdditionalFormViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: AdditionalFormViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityAdditionalDetailFormBinding) {
        Repository.getInstance().getChallengeProgress().observe(this) {
            viewModel.challengeExtras = it
        }
    }

    override fun navigateToBannerScreen() {
        val extras = ChallengeExtras.challengeExtras {
            challengeData = ChallengeModel(
                viewModel.challengeModel.isEdit,
                viewModel.challengeModel.challengeID ?: "",
                7,
                viewModel.challengeModel.selectedSportId,
                viewModel.challengeModel.selectedSportImage,
                viewModel.challengeModel.profileType,
                viewModel.challengeModel.sportsName,
                viewModel.challengeModel.startDate,
                viewModel.challengeModel.endDate,
                2,//for popular route
                viewModel.challengeModel.routeID,
                viewModel.challengeModel.challengeName,//challengeName
                viewModel.challengeModel.challengeVisibility,//challenge visibility
                viewModel.challengeModel.challengeStatus,//challenge visibility
                viewModel.challengeModel.selectedSportsLevel,//challenge selectedSportsLevel
                viewModel.challengeModel.ageGroup,//challenge selectedAgeGroupValue
                viewModel.challengeModel.challengeDescription,//challenge description
                viewModel.challengeModel.subSportTypeId,
                viewModel.challengeModel.sportsEquipments,
                //step-5 A (for radius reach)
                viewModel.challengeModel.challengeArea,//radius reach / Extended
                viewModel.challengeModel.location,
                viewModel.challengeModel.latitude,
                viewModel.challengeModel.longitude,
                viewModel.challengeModel.radius,

                //step-5 B (Extended reach)
                viewModel.challengeModel.country,
                viewModel.challengeModel.countryId,
                viewModel.challengeModel.state,
                viewModel.challengeModel.stateId,
                viewModel.challengeModel.cityId,
                viewModel.challengeModel.zipCode,
                //step-6
                viewModel.challengeModel.calories,
                viewModel.challengeModel.miles,
                viewModel.challengeModel.elevationGain,
                viewModel.challengeModel.avgWatt,
                viewModel.challengeModel.time,
                viewModel.challengeModel.maxMember,
                //Step 7
                viewModel.selectedWinnerPicked.get(),
                viewModel.selectedWinnerPrize.get() ?: "NO",
                viewModel.overViewValue.get(),
                viewModel.winnerValue.get(),
                viewModel.additionalInfoValue.get(),
                viewModel.rulesValue.get(),
                viewModel.guidelinesValue.get(),
                viewModel.qualificationValue.get(),
                "", "",
                arrayListOf<InviteMember>()
            )
        }
        startActivity(
            intentProviderFactory.create(
                ChallengeBannerActivity::class.java,
                extras,
                0
            )
        )
    }

    override fun navigateEditToBannerScreen() {
        val extras = ChallengeExtras.challengeExtras {
            challengeData = ChallengeModel(
                viewModel.challengeModel.isEdit,
                viewModel.challengeModel.challengeID ?: "",
                7,
                viewModel.challengeModel.selectedSportId,
                viewModel.challengeModel.selectedSportImage,
                viewModel.challengeModel.profileType,
                viewModel.challengeModel.sportsName,
                viewModel.challengeModel.startDate,
                viewModel.challengeModel.endDate,
                2,//for popular route
                viewModel.challengeModel.routeID,
                viewModel.challengeModel.challengeName,//challengeName
                viewModel.challengeModel.challengeVisibility,//challenge visibility
                viewModel.challengeModel.challengeStatus,//challenge visibility
                viewModel.challengeModel.selectedSportsLevel,//challenge selectedSportsLevel
                viewModel.challengeModel.ageGroup,//challenge selectedAgeGroupValue
                viewModel.challengeModel.challengeDescription,//challenge description
                viewModel.challengeModel.subSportTypeId,
                viewModel.challengeModel.sportsEquipments,
                //step-5 A (for radius reach)
                viewModel.challengeModel.challengeArea,//radius reach / Extended
                viewModel.challengeModel.location,
                viewModel.challengeModel.latitude,
                viewModel.challengeModel.longitude,
                viewModel.challengeModel.radius,

                //step-5 B (Extended reach)
                viewModel.challengeModel.country,
                viewModel.challengeModel.countryId,
                viewModel.challengeModel.state,
                viewModel.challengeModel.stateId,
                viewModel.challengeModel.cityId,
                viewModel.challengeModel.zipCode,
                //step-6
                viewModel.challengeModel.calories,
                viewModel.challengeModel.miles,
                viewModel.challengeModel.elevationGain,
                viewModel.challengeModel.avgWatt,
                viewModel.challengeModel.time,
                viewModel.challengeModel.maxMember,
                //Step 7
                viewModel.selectedWinnerPicked.get(),
                viewModel.selectedWinnerPrize.get() ?: "NO",
                viewModel.overViewValue.get(),
                viewModel.winnerValue.get(),
                viewModel.additionalInfoValue.get(),
                viewModel.rulesValue.get(),
                viewModel.guidelinesValue.get(),
                viewModel.qualificationValue.get(),
                viewModel.challengeModel.bannerImage,
                viewModel.challengeModel.challengeImage,
                viewModel.challengeModel.inviteMembers
            )
        }
        startActivity(
            intentProviderFactory.create(
                ChallengeBannerActivity::class.java,
                extras,
                0
            )
        )
    }

    override fun onWinnerPickedUpClick(view: View) {
        ChallengeSingleSelectionPopUp.Builder()
            .addList(viewModel.winnerPickedArray.toList())
            .setListener {
                viewModel.selectedWinnerPicked.set(it)
                viewModel.pickedWinnerError.set("")
            }
            .build()
            .show(view)
        // showWinnerPickedPopUp()
    }

    override fun onWinnerPrizeAwarded(view: View) {
        ChallengeSingleSelectionPopUp.Builder()
            .addList(viewModel.winnerPrizeAwardedArray.toList())
            .setListener {
                viewModel.selectedWinnerPrize.set(it)
                viewModel.selectedWinnerPrizeError.set("")


            }
            .build()
            .show(view)
        //showWinnerPrizeAwarded()
    }

    override fun onSaveAsDraftClick() {
    }

    private fun showWinnerPickedPopUp() {
        // Initialize alert dialog
        val builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
        // set title
        builder.setTitle("Select winner picked up from");
        // set dialog non cancelable
        builder.setCancelable(false)
        builder.setItems(viewModel.winnerPickedArray, object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                viewModel.selectedWinnerPicked.set(viewModel.winnerPickedArray[p1])
                if (viewModel.selectedWinnerPrize.get() == viewModel.winnerPickedArray[0]) {
                    viewModel.winnerPickedValue = "Leaderboard"
                } else {
                    viewModel.winnerPickedValue = "Random"
                }
            }
        })
        builder.show()
    }

    private fun showWinnerPrizeAwarded() {
        // Initialize alert dialog
        val builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
        // set title
        builder.setTitle("Select prize awarded");
        // set dialog non cancelable
        builder.setCancelable(false)
        builder.setItems(viewModel.winnerPrizeAwardedArray,
            object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    viewModel.selectedWinnerPrize.set(viewModel.winnerPrizeAwardedArray[p1])
                    if (viewModel.selectedWinnerPrize.get() == viewModel.winnerPrizeAwardedArray[0]) {
                        viewModel.winnerAwardedValue = 1
                    } else {
                        viewModel.winnerAwardedValue = 0
                    }
                }
            })
        builder.show()
    }
}