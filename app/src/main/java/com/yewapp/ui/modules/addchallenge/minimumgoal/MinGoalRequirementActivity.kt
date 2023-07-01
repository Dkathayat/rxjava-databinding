package com.yewapp.ui.modules.addchallenge.minimumgoal

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.shawnlin.numberpicker.NumberPicker
import com.yewapp.R
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.invitemember.InviteMember
import com.yewapp.databinding.ActivityMinGoalRequirementBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.addchallenge.additionalform.AdditionalFormActivity
import com.yewapp.ui.modules.listner.ChallengeExtras

//Todo:Add Challenge Step 6
class MinGoalRequirementActivity :
    BaseActivity<MinGoalRequirementNavigator, MinGoalRequirementViewModel, ActivityMinGoalRequirementBinding>(),
    MinGoalRequirementNavigator {

    override fun getLayout(): Int {
        return R.layout.activity_min_goal_requirement
    }

    override fun init() {
        bind(MinGoalRequirementViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMaxMemberToJoinChallenge()
    }

    override fun onViewModelCreated(viewModel: MinGoalRequirementViewModel) {
        viewModel.setNavigator(this)

    }

    override fun onViewBound(viewDataBinding: ActivityMinGoalRequirementBinding) {
        setupFocusOutside(findViewById(android.R.id.content))

        viewDataBinding.timeEdt.setOnClickListener {
            val view: View = View.inflate(this, R.layout.time_picker, null)
            val numberPickerHour: NumberPicker = view.findViewById(R.id.numpicker_hours)
            numberPickerHour.maxValue = 23
            numberPickerHour.setFormatter(R.string.number_picker_formatter)

            //  numberPickerHour.value = sharedPreferences.getInt("Hours", 0)
            val numberPickerMinutes: NumberPicker = view.findViewById(R.id.numpicker_minutes)
            numberPickerMinutes.maxValue = 59
            numberPickerMinutes.setFormatter(R.string.number_picker_formatter)

            // numberPickerMinutes.value = sharedPreferences.getInt("Minutes", 0)
            val numberPickerSeconds: NumberPicker = view.findViewById(R.id.numpicker_seconds)
            numberPickerSeconds.maxValue = 59
            numberPickerSeconds.setFormatter(R.string.number_picker_formatter)
            //  numberPickerSeconds.value = sharedPreferences.getInt("Seconds", 0)
            //  val cancel: Button = view.findViewById(R.id.cancel)
            val ok: Button = view.findViewById(R.id.ok)
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setView(view)
            val alertDialog: AlertDialog = builder.create()
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//            cancel.setOnClickListener {
//
//                alertDialog.dismiss()
//
//            }
            ok.setOnClickListener {
                var hour = ""
                var min = ""
                var sec = ""
                hour = if (numberPickerHour.value.toString().length == 1) {
                    "0" + numberPickerHour.value.toString()
                } else {
                    numberPickerHour.value.toString()
                }
                min = if (numberPickerMinutes.value.toString().length == 1) {
                    "0" + numberPickerMinutes.value.toString()
                } else {
                    numberPickerMinutes.value.toString()
                }
                sec = if (numberPickerSeconds.value.toString().length == 1) {
                    "0" + numberPickerSeconds.value.toString()
                } else {
                    numberPickerSeconds.value.toString()
                }
//                viewDataBinding.timeEdt.setText("$hour : $min : $sec")
                viewModel.time.set("$hour : $min : $sec")
                viewModel.timeError.set("")
                //viewDataBinding.timeEdt.setText(hour + ":" + numberPickerMinutes.value + ":" + numberPickerSeconds.value)
//                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
//                        editor.putInt("Hours", numberPickerHour.value)
//                        editor.putInt("Minutes", numberPickerMinutes.value)
//                        editor.putInt("Seconds", numberPickerSeconds.value)
//                        editor.apply()
                alertDialog.dismiss()

            }
            alertDialog.show()
        }


    }

    override fun navigateToAdditionalFormScreen() {
        val maxMember = if (viewModel.maxMember.get() == "") "0"
        else viewModel.maxMember.get()

        val extras = ChallengeExtras.challengeExtras {
            challengeData = ChallengeModel(
                viewModel.challengeModel.isEdit,
                viewModel.challengeModel.challengeID ?: "",
                6,
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
                viewModel.challengeModel.challengeStatus,//challenge Status 1 or 0
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
                viewModel.calories.get(),
                viewModel.miles.get(),
                viewModel.elevationGain.get(),
                viewModel.avgWatt.get(),
                viewModel.time.get(),
                maxMember,
                "", "", "", "", "",
                "", "", "", "", "",
                arrayListOf<InviteMember>()
            )
        }

        startActivity(
            intentProviderFactory.create(
                AdditionalFormActivity::class.java,
                extras,
                0
            )
        )
    }

    override fun navigateToEditAdditionalFormScreen() {
        val maxMember = if (viewModel.maxMember.get() == "") "0"
        else viewModel.maxMember.get()

        val extras = ChallengeExtras.challengeExtras {
            challengeData = ChallengeModel(
                viewModel.challengeModel.isEdit,
                viewModel.challengeModel.challengeID ?: "",
                6,
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
                viewModel.challengeModel.challengeStatus,//challenge Status 1 or 0
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
                viewModel.calories.get(),
                viewModel.miles.get(),
                viewModel.elevationGain.get(),
                viewModel.avgWatt.get(),
                viewModel.time.get(),
                maxMember,
                viewModel.challengeModel.winnerPickedFrom,
                viewModel.challengeModel.selectedWinnerPrize,
                viewModel.challengeModel.overViewValue,
                viewModel.challengeModel.winnerValue,
                viewModel.challengeModel.additionalInfoValue,
                viewModel.challengeModel.rulesValue,
                viewModel.challengeModel.guidelinesValue,
                viewModel.challengeModel.qualificationValue,
                viewModel.challengeModel.bannerImage,
                viewModel.challengeModel.challengeImage,
                viewModel.challengeModel.inviteMembers
            )
        }

        startActivity(
            intentProviderFactory.create(
                AdditionalFormActivity::class.java,
                extras,
                0
            )
        )
    }

//    override fun onMaxMemberClicked(view: View) {
//        ChallengeSingleSelectionPopUp.Builder()
//            .addList(viewModel.maxMemberArray.toList())
//            .setListener {
//                viewModel.maxMember.set(it)
//            }
//            .build()
//            .show(view)
//
//    }


    //Removes the focus from text fields when the user click on the screen
    @SuppressLint("ClickableViewAccessibility")
    private fun setupFocusOutside(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideKeyboard()
                false
            }
        }

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupFocusOutside(innerView)
            }
        }
    }

}