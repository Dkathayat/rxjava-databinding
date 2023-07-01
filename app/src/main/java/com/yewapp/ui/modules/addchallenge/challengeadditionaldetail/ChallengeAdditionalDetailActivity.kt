package com.yewapp.ui.modules.addchallenge.challengeadditionaldetail

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.yewapp.R
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.invitemember.InviteMember
import com.yewapp.data.network.api.profile.City
import com.yewapp.data.network.api.profile.Country
import com.yewapp.data.network.api.profile.State
import com.yewapp.databinding.ActivityChallengeAdditionalDetailBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.common.GenericListBottomSheet
import com.yewapp.ui.dialogs.challengepopupdialogs.ChallengeSingleSelectionPopUp
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.ui.modules.addchallenge.minimumgoal.MinGoalRequirementActivity

//Todo:Add Challenge Step 5
class ChallengeAdditionalDetailActivity :
    BaseActivity<ChallengeAdditionalDetailNavigator, ChallengeAdditionalDetailViewModel, ActivityChallengeAdditionalDetailBinding>(),
    ChallengeAdditionalDetailNavigator {
    private var REQUEST_CODE_AUTOCOMPLETE = 700

    override fun getLayout(): Int {
        return R.layout.activity_challenge_additional_detail
    }

    override fun init() {
        bind(ChallengeAdditionalDetailViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: ChallengeAdditionalDetailViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: ActivityChallengeAdditionalDetailBinding) {
//        Repository.getInstance().getChallengeProgress().observe(this) {
//            viewModel.challengeExtras = it
//        }
    }


    @SuppressLint("LongLogTag", "LogNotTimber")
    var searchLocationResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.data != null) {
                setSearchData(result.data)
            } else {
                Log.d("ChallengeAdditionalDetailActivity", "From Activity2:    NO DATA\n")
            }

        }


    private fun setSearchData(data: Intent?) {
        val selectedCarmenFeature = PlaceAutocomplete.getPlace(data)
        viewModel.selectedLocation.set("${selectedCarmenFeature.placeName()}")
        viewModel.latitude.set((selectedCarmenFeature.geometry() as Point?)?.latitude())
        viewModel.longitude.set((selectedCarmenFeature.geometry() as Point?)?.longitude())
    }


    override fun onCountryClicked() {
        showBottomSheet(
            getString(R.string.choose_country), viewModel.countryList as MutableList<Country>
        ) {
            viewModel.selectedCountryId = it.id
            viewModel.selectedCountry.set(it.name)
            viewModel.getStates()
        }
    }

    override fun onStateClicked() {
        showBottomSheet(
            getString(R.string.choose_state), viewModel.stateList as MutableList<State>
        ) {
            viewModel.selectedStateId = it.id
            viewModel.selectedState.set(it.name)
            viewModel.getCities()
        }
    }

    override fun onCityClicked() {
        viewModel.selectedCity.set("")
        viewModel.selectedCityId.clear()
        showBottomSheetCity(
            getString(R.string.choose_city), viewModel.cityList as MutableList<City>
        ) {
            var cities = ""
            var count = 0
            for (i in 0 until it.size) {
                if (it[i].isSelected) {
                    count++
                    viewModel.selectedCityId.add(it[i].cityId.toInt())
                    cities += if (cities != "") {
                        ",${it[i].name}"
                    } else {
                        it[i].name
                    }

                }
            }
            if (count > 1) {
                viewModel.isContinueActive.set(true)
                viewModel.isZipCodeApplied.set(false)
            } else {
                viewModel.isZipCodeApplied.set(true)
                viewModel.isContinueActive.set(false)
            }
            viewModel.selectedCity.set(cities)
        }
    }

    private fun showBottomSheetCity(
        title: String, list: MutableList<City>, listener: (MutableList<City>) -> Unit
    ) {
        GenericListBottomSheetMultipleSelection.Builder().setTitleText(title).setDataList(list)
            .setUpdatedListener {
                listener(it as MutableList<City>)
            }.build().show(this)

    }

    override fun navigateToMinGoalRequirementActivity() {
        if (!viewModel.zipCode.get().equals("")) {
            val splitZipCode = viewModel.zipCode.get()?.split(",")
            for (i in 0 until splitZipCode?.size!!) {
                if (splitZipCode.equals("")) return
                viewModel.zipCodeList.add(splitZipCode[i].toInt())
            }
        }


        if (viewModel.challengeModel.isEdit) {
            val extras = ChallengeExtras.challengeExtras {
                challengeData =
                    if (viewModel.selectedChallengeArea.get()?.equals(
                            "Radius Reach",
                            ignoreCase = true
                        ) ?: return || viewModel.selectedChallengeArea.get()?.equals("") ?: return
                    ) {
                        ChallengeModel(
                            viewModel.challengeModel.isEdit,
                            viewModel.challengeModel.challengeID ?: "",
                            5,
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
                            viewModel.challengeModel.subSportTypeId,//challenge sub Sport
                            viewModel.challengeModel.sportsEquipments,
                            //step-5 A (for radius reach)
                            viewModel.selectedChallengeArea.get(),//radius reach / Extended
                            viewModel.selectedLocation.get() ?: "",
                            viewModel.latitude.get(),
                            viewModel.longitude.get(),
                            viewModel.selectedRadius.get(),
                            //step-5 B (Extended reach)
                            "",
                            0,
                            "",
                            0,
                            arrayListOf(),
                            viewModel.zipCodeList,
                            viewModel.challengeModel.calories,
                            viewModel.challengeModel.miles,
                            viewModel.challengeModel.elevationGain,
                            viewModel.challengeModel.avgWatt,
                            viewModel.challengeModel.time,
                            viewModel.challengeModel.maxMember,
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
                    } else {
                        ChallengeModel(
                            viewModel.challengeModel.isEdit,
                            viewModel.challengeModel.challengeID ?: "",
                            5,
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
                            viewModel.challengeModel.subSportTypeId,//challenge sub Sport
                            viewModel.challengeModel.sportsEquipments,
                            //step-5 A (for radius reach)
                            viewModel.selectedChallengeArea.get(),//radius reach / Extended
                            "",
                            0.0,
                            0.0,
                            "",
                            //step-5 B (Extended reach)
                            viewModel.selectedCountry.get() ?: "",
                            viewModel.selectedCountryId.toInt(),
                            viewModel.selectedState.get() ?: "",
                            viewModel.selectedStateId.toInt(),
                            viewModel.selectedCityId,
                            viewModel.zipCodeList,
                            viewModel.challengeModel.calories,
                            viewModel.challengeModel.miles,
                            viewModel.challengeModel.elevationGain,
                            viewModel.challengeModel.avgWatt,
                            viewModel.challengeModel.time,
                            viewModel.challengeModel.maxMember,
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
            }
            startActivity(
                intentProviderFactory.create(
                    MinGoalRequirementActivity::class.java, extras, 0
                )
            )
        } else {
            //TODO:When Add
            val extras = ChallengeExtras.challengeExtras {
                challengeData = if (viewModel.selectedChallengeArea.get()
                        .equals("Radius Reach", ignoreCase = true)
                ) {
                    ChallengeModel(
                        viewModel.challengeModel.isEdit,
                        viewModel.challengeModel.challengeID ?: "",
                        5,
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
                        viewModel.challengeModel.subSportTypeId,//challenge sub Sport
                        viewModel.challengeModel.sportsEquipments,
                        //step-5 A (for radius reach)
                        viewModel.selectedChallengeArea.get(),//radius reach / Extended
                        viewModel.selectedLocation.get() ?: "",
                        viewModel.latitude.get(),
                        viewModel.longitude.get(),
                        viewModel.selectedRadius.get(),
                        //step-5 B (Extended reach)
                        "",
                        0,
                        "",
                        0,
                        arrayListOf(),
                        arrayListOf(),
                        "",
                        viewModel.challengeModel.miles,
                        viewModel.challengeModel.elevationGain,
                        "",
                        viewModel.challengeModel.time,
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        arrayListOf<InviteMember>()
                    )

                } else {
                    ChallengeModel(
                        viewModel.challengeModel.isEdit,
                        viewModel.challengeModel.challengeID ?: "",
                        5,
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
                        viewModel.challengeModel.subSportTypeId,//challenge sub Sport
                        viewModel.challengeModel.sportsEquipments,
                        //step-5 A (for radius reach)
                        viewModel.selectedChallengeArea.get(),//radius reach / Extended
                        viewModel.selectedLocation.get() ?: "",
                        0.0,
                        0.0,
                        "",
                        //step-5 B (Extended reach)
                        viewModel.selectedCountry.get() ?: "",
                        viewModel.selectedCountryId.toInt(),
                        viewModel.selectedState.get() ?: "",
                        viewModel.selectedStateId.toInt(),
                        viewModel.selectedCityId,
                        viewModel.zipCodeList,
                        "",
                        viewModel.challengeModel.miles,
                        viewModel.challengeModel.elevationGain,
                        "",
                        viewModel.challengeModel.time,
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        arrayListOf<InviteMember>()
                    )

                }
            }

            startActivity(
                intentProviderFactory.create(
                    MinGoalRequirementActivity::class.java, extras, 0
                )
            )
        }

    }

    override fun onChallengeAreaClick(view: View) {
        // showChallengeAreaPopUp()
        ChallengeSingleSelectionPopUp.Builder().addList(viewModel.challengeAreaArray.toList())
            .setListener {
                viewModel.selectedChallengeArea.set(it)
                if (viewModel.selectedChallengeArea.get() == viewModel.challengeAreaArray[0]) {
                    viewModel.countryLayoutVisibility.set(false)
                } else {
                    viewModel.countryLayoutVisibility.set(true)
                }
            }.build().show(view)
    }

    override fun onLocationClick() {
        val intent = PlaceAutocomplete.IntentBuilder().accessToken(
            ((if (Mapbox.getAccessToken() != null) Mapbox.getAccessToken() else getString(R.string.mapbox_access_token))!!)
        ).placeOptions(
            PlaceOptions.builder().backgroundColor(Color.parseColor("#EEEEEE")).limit(10)
//                    .addInjectedFeature(home)
//                    .addInjectedFeature(work)
                .build(PlaceOptions.MODE_CARDS)
        ).build(this@ChallengeAdditionalDetailActivity)

        searchLocationResult.launch(intent)

//        startActivityForResult(
//            intent,
//            REQUEST_CODE_AUTOCOMPLETE
//        )

//        val intent = PlaceAutocomplete.IntentBuilder()
//            .accessToken(MAPBOX_ACCESS_TOKEN)
//            .placeOptions(
//                PlaceOptions.builder()
//              //  .backgroundColor(Color.parseColor("#EEEEEE"))
//                .limit(10)
//                .build(PlaceOptions.MODE_CARDS))
//            .build(this)
//        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
    }

    override fun onRadiusClick(view: View) {
        ChallengeSingleSelectionPopUp.Builder().addList(viewModel.locationRadiusArray.toList())
            .setListener {
                viewModel.selectedRadius.set(it)

            }.build().show(view)
        //showRadiusPopUp()
    }

    override fun skipScreen() {
        val extras = ChallengeExtras.challengeExtras {
            challengeData = ChallengeModel(
                viewModel.challengeModel.isEdit,
                viewModel.challengeModel.challengeID ?: "",
                5,
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
                viewModel.challengeModel.challengeStatus,//challenge Status value 1 or 0
                viewModel.challengeModel.selectedSportsLevel,
                viewModel.challengeModel.ageGroup,
                viewModel.challengeModel.challengeDescription,
                viewModel.challengeModel.subSportTypeId,
                arrayListOf(),
                viewModel.selectedChallengeArea.get(),
                "",
                0.0,
                0.0,
                "",
                viewModel.challengeModel.country,
                viewModel.challengeModel.countryId,
                viewModel.challengeModel.state,
                viewModel.challengeModel.stateId,
                arrayListOf(),
                arrayListOf(),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                arrayListOf()
            )
        }
        startActivity(
            intentProviderFactory.create(
                MinGoalRequirementActivity::class.java, extras, 0
            )
        )
    }

//    private fun showRadiusPopUp() {
//        // Initialize alert dialog
//        val builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
//        // set title
//        builder.setTitle("Select Location Radius");
//        // set dialog non cancelable
//        builder.setCancelable(false)
//        builder.setItems(
//            viewModel.locationRadiusArray,
//            object : DialogInterface.OnClickListener {
//                override fun onClick(p0: DialogInterface?, p1: Int) {
//                    viewModel.selectedRadius.set(viewModel.locationRadiusArray[p1])
//                }
//            })
//        builder.show()
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
//            val feature = PlaceAutocomplete.getPlace(data)
//            Toast.makeText(this, feature.text(), Toast.LENGTH_LONG).show()
//        }
//
//    }

    private fun showChallengeAreaPopUp() {
        // Initialize alert dialog
        val builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
        // set title
        builder.setTitle("Select Challenge Area");
        // set dialog non cancelable
        builder.setCancelable(false)
        builder.setItems(
            viewModel.challengeAreaArray,
            object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    viewModel.selectedChallengeArea.set(viewModel.challengeAreaArray[p1])

                    if (viewModel.selectedChallengeArea.get()
                            .toString() == viewModel.challengeAreaArray[0]
                    ) {
                        viewModel.countryLayoutVisibility.set(false)
                    } else {
                        viewModel.countryLayoutVisibility.set(true)
                    }
                }
            })
        builder.show()
    }

    private fun <T> showBottomSheet(
        title: String,
        list: MutableList<T>,
        listener: (T) -> Unit
    ) {
        GenericListBottomSheet.Builder<T>().setTitleText(title).setDataList(list)
            .setClickListener {
                listener(it)
            }.build().show(this)
    }

}