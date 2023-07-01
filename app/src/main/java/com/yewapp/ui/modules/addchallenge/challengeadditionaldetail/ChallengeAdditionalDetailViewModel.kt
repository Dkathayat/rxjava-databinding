package com.yewapp.ui.modules.addchallenge.challengeadditionaldetail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.challenges.CreateChallengeResponse
import com.yewapp.data.network.api.createchallenge.CreateChallengeRequest
import com.yewapp.data.network.api.profile.City
import com.yewapp.data.network.api.profile.Country
import com.yewapp.data.network.api.profile.CountryListResponse
import com.yewapp.data.network.api.profile.State
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.utils.DateUtils
import com.yewapp.utils.rx.SchedulerProvider

class ChallengeAdditionalDetailViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) :
    BaseViewModel<ChallengeAdditionalDetailNavigator>(dataManager, schedulerProvider) {

    var countryList: List<Country> = mutableListOf()
    var stateList: List<State> = mutableListOf()
    var cityList: List<City> = mutableListOf()

    //    var country = ""
//    var state = ""
//    var city = ""
    var zipCodeList = arrayListOf<Int>()
    var zipCode = ObservableField<String>("")
    var zipCodeError = ObservableField<String>("")

    var selectedCountryId = "0"
    var selectedStateId = "0"
    var selectedCityId = arrayListOf<Int>()
    var selectedChallengeArea = ObservableField<String>("Radius Reach")
    var selectedLocation = ObservableField<String>("")
    var selectedRadius = ObservableField<String>("")

    var selectedCountry = ObservableField<String>("")
    var countryError = ObservableField<String>("")
    var stateError = ObservableField<String>("")
    var cityError = ObservableField<String>("")
    var selectedLocationError = ObservableField<String>("")
    var selectedRadiusError = ObservableField<String>("")

    var selectedState = ObservableField<String>("")
    var selectedCity = ObservableField<String>("")
    val isCountryLoading = ObservableBoolean(false)
    val isStateLoading = ObservableBoolean(false)
    val isCityLoading = ObservableBoolean(false)
    val isZipCodeApplied = ObservableBoolean(true)
    var challengeAreaArray = arrayOf("Radius Reach", "Extended Reach")
    var locationRadiusArray =
        arrayOf("5 miles", "10 miles", "15 miles", "20 miles", "25 miles", "30 miles")
    var countryLayoutVisibility = ObservableBoolean(false)
//    var challengeExtras: ChallengeExtras? = null


    var latitude = ObservableField<Double>(0.0)
    var longitude = ObservableField<Double>(0.0)
    var isContinueActive = ObservableField<Boolean>(false)
    lateinit var challengeModel: ChallengeModel


//    val nameError = ObservableField<String>("")
//    val descriptionError = ObservableField<String>("")


    override fun setData(extras: Bundle?) {
        challengeModel =
            extras?.getParcelable(ChallengeExtras.CHALLENGE_DATA) ?: return
        Log.d("STEP 5:", "${challengeModel}")


        getCountry()

        if (!challengeModel.isEdit) {
            selectedCountryId = "${dataManager.getUser().countryId}"
            selectedCountry.set(dataManager.getUser().country)
            selectedStateId = "${dataManager.getUser().stateId}"
            selectedState.set(dataManager.getUser().state)
            getStates()
            getCities()
        } else {
            initializeDataForEdit()
        }
    }


    fun getCountry() {
        if (isCountryLoading.get()) return
        isCountryLoading.set(true)

        compositeDisposable.add(
            dataManager.getCountries()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onCountrySuccess, this::onCountryFailure)
        )

    }

    fun getStates() {
        if (isStateLoading.get()) return
        isStateLoading.set(true)

        compositeDisposable.add(
            dataManager.getStates(
                selectedCountryId
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onStateFetchSuccess, this::onStateFetchFailure)
        )
    }

    fun getCities() {
        if (isCityLoading.get()) return
        isCityLoading.set(true)

        compositeDisposable.add(
            dataManager.getCities(
                selectedStateId
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onCityFetchSuccess, this::onCityFetchFailure)
        )
    }

    private fun onCityFetchSuccess(list: List<City>) {
        isCityLoading.set(false)
        cityList = list
    }

    private fun onCityFetchFailure(t: Throwable) {
        isCityLoading.set(false)
        getNavigator()?.onError(t)
    }

    private fun onStateFetchSuccess(list: List<State>) {
        isStateLoading.set(false)
        stateList = list
    }

    private fun onStateFetchFailure(t: Throwable) {
        isStateLoading.set(false)
        getNavigator()?.onError(t)
    }

    private fun onCountryFailure(t: Throwable) {
        isCountryLoading.set(false)
        getNavigator()?.onError(t)
    }

    private fun onCountrySuccess(response: CountryListResponse) {
        isCountryLoading.set(false)
        countryList = response.countryList
    }


    fun onActionClick(view: View) {
        when (view.id) {
            R.id.challenge_area_edt -> getNavigator()?.onChallengeAreaClick(view)
            R.id.for_loctaion_edt -> getNavigator()?.onLocationClick()
            R.id.radius_edt -> getNavigator()?.onRadiusClick(view)
            R.id.tvSaveAsDraft -> saveAsDraft()
            R.id.tvSkip -> getNavigator()?.skipScreen()
        }
    }

    fun onCountryClick(view: View) {
        getNavigator()?.onCountryClicked()
    }

    fun onStateClick(view: View) {
        getNavigator()?.onStateClicked()
    }

    fun onCityClick(view: View) {
        getNavigator()?.onCityClicked()
    }

    fun onContinueClick(view: View) {

        getNavigator()?.navigateToMinGoalRequirementActivity()
    }


    fun onCountryChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        countryError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }
    }

    fun onStateChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        stateError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }
    }

    fun onCityChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        cityError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }
    }

    fun onZipCodeChange(s: CharSequence, start: Int, before: Int, count: Int) {
        zipCodeError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }
    }

    fun onForLocationChange(s: CharSequence, start: Int, before: Int, count: Int) {
        selectedLocationError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }
    }

    fun onRadiusChange(s: CharSequence, start: Int, before: Int, count: Int) {
        selectedRadiusError.set("")
        if (validate()) {
            isContinueActive.set(true)
        }
    }

    fun validate(): Boolean {
        if (!selectedChallengeArea.get().equals("Radius Reach", ignoreCase = true))
            return when {
                selectedCountry.get().toString().trim().isEmpty() -> {
                    countryError.set(
                        dataManager.getResourceProvider().getString(R.string.empty_country_error)
                    )
                    false
                }
                selectedState.get().toString().trim().isEmpty() -> {
                    stateError.set(
                        dataManager.getResourceProvider().getString(R.string.empty_state_error)
                    )
                    false
                }
                selectedCity.get().toString().trim().isEmpty() -> {
                    cityError.set(
                        dataManager.getResourceProvider().getString(R.string.empty_city_error)
                    )
                    false
                }
                zipCode.get().toString().trim().isEmpty() -> {
                    if (isZipCodeApplied.get())
                        true
                    else {
                        zipCodeError.set(
                            dataManager.getResourceProvider()
                                .getString(R.string.empty_country_error)
                        )
                        false
                    }
                }
                else -> true
            }
        else
            return when {
                selectedLocation.get().toString().trim().isEmpty() -> {
                    selectedLocationError.set(
                        dataManager.getResourceProvider().getString(R.string.empty_location_error)
                    )
                    false
                }
                selectedRadius.get().toString().trim().isEmpty() -> {
                    selectedRadiusError.set(
                        dataManager.getResourceProvider().getString(R.string.empty_radius_error)
                    )
                    false
                }
                else -> true
            }

    }


    fun saveAsDraft() {
        if (isLoading.get()) return
        isLoading.set(true)

        if (selectedRadius.get().equals("Radius Reach", ignoreCase = true)) {
            callWithRadiusReach()
        } else {
            callWithExtendedReach()
        }
    }

    private fun callWithRadiusReach() {
        compositeDisposable.add(
            dataManager.createChallenge(
                CreateChallengeRequest(
                    challengeModel.challengeID ?: "",
                    true,
                    challengeModel.selectedSportId,
                    challengeModel.subSportTypeId ?: "",
                    challengeModel.challengeName,
                    challengeModel.selectedSportsLevel,
                    challengeModel.sportsEquipments,
                    challengeModel.challengeVisibility,
                    challengeModel.challengeDescription,
                    "",
                    challengeModel.miles ?: "",
                    challengeModel.elevationGain ?: "",
                    "",
                    challengeModel.time ?: "",
                    0,
                    dataManager.getUser().countryId,//challengeModel.countryId?.toInt(),
                    dataManager.getUser().stateId,// challengeModel.stateId?.toInt(),
                    arrayListOf(),
                    arrayListOf(),
                    challengeModel.ageGroup,
                    arrayListOf(),
                    selectedLocation.get() ?: "",
                    "${latitude.get() ?: "0.0"}",
                    "${longitude.get() ?: "0.0"}",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    if (challengeModel.selectedWinnerPrize.equals(
                            "yes", ignoreCase = true
                        )
                    ) 1 else 0,
                    "",
                    "Goal",
                    DateUtils.convertCreateChallengeDatesToApiFormat(
                        challengeModel.startDate ?: return
                    ),
                    DateUtils.convertCreateChallengeDatesToApiFormat(
                        challengeModel.endDate ?: return
                    ),
                    "",
                    "",
                    "",
                    "",
                    challengeModel.challengeStatus,
                    arrayListOf(),
                    "5",
                    selectedChallengeArea.get()?.replace(" reach", "") ?: "",
                    selectedRadius.get()?.replace(" miles", "") ?: "", ""
                )
            ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::saveAsDraftSuccess, this::saveAsDraftFailure)
        )

    }

    private fun callWithExtendedReach() {
        compositeDisposable.add(
            dataManager.createChallenge(
                CreateChallengeRequest(
                    challengeModel.challengeID ?: "",
                    true,
                    challengeModel.selectedSportId,
                    challengeModel.subSportTypeId ?: "",
                    challengeModel.challengeName,
                    challengeModel.selectedSportsLevel,
                    challengeModel.sportsEquipments,
                    challengeModel.challengeVisibility,
                    challengeModel.challengeDescription,
                    "",
                    challengeModel.miles ?: "",
                    challengeModel.elevationGain ?: "",
                    "",
                    challengeModel.time ?: "",
                    0,
                    dataManager.getUser().countryId,//challengeModel.countryId?.toInt(),
                    dataManager.getUser().stateId,// challengeModel.stateId?.toInt(),
                    selectedCityId,
                    zipCodeList,
                    challengeModel.ageGroup,
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
                    if (challengeModel.selectedWinnerPrize.equals(
                            "yes", ignoreCase = true
                        )
                    ) 1 else 0,
                    "",
                    "Goal",
                    DateUtils.convertCreateChallengeDatesToApiFormat(
                        challengeModel.startDate ?: return
                    ),
                    DateUtils.convertCreateChallengeDatesToApiFormat(
                        challengeModel.endDate ?: return
                    ),
                    "",
                    "",
                    "",
                    "",
                    challengeModel.challengeStatus,
                    arrayListOf(),
                    "4",
                    "", "", ""
                )
            ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::saveAsDraftSuccess, this::saveAsDraftFailure)
        )

    }

    private fun saveAsDraftSuccess(response: CreateChallengeResponse) {
        isLoading.set(false)
        challengeModel.challengeID = response.id.toString()
        getNavigator()?.onSuccess("challenge saved")
    }

    private fun saveAsDraftFailure(error: Throwable) {
        isLoading.set(false)
    }


    private fun initializeDataForEdit() {
        isContinueActive.set(true)
        if (challengeModel.challengeArea.equals("Radius Reach", ignoreCase = true)) {
            selectedChallengeArea.set(challengeAreaArray.get(0))
            selectedLocation.set(challengeModel.location)
            selectedRadius.set("${challengeModel.radius} miles")
            latitude.set(challengeModel.latitude)
            longitude.set(challengeModel.longitude)
            countryLayoutVisibility.set(false)
        } else {
            countryLayoutVisibility.set(true)
            selectedChallengeArea.set(challengeAreaArray.get(1))
            selectedCountryId = challengeModel.countryId.toString()
            selectedCountry.set(challengeModel.country)
            selectedStateId = challengeModel.stateId.toString()
            selectedState.set(challengeModel.state)

            var citySelected = ""
            for (i in 0 until cityList.size) {
                for (j in 0 until challengeModel.cityId.size) {
                    if (cityList[i].id == challengeModel.cityId[j].toString()) {
                        if (citySelected.equals("")) {
                            citySelected = cityList[i].id
                        } else {
                            citySelected = "$citySelected ,${cityList[i].id}"
                        }
                    }
                }
            }
            selectedCity.set(citySelected)


            var zipCodeTemp = ""
            zipCodeList.addAll(challengeModel.zipCode)
            for (i in 0 until challengeModel.zipCode.size) {
                if (i == 0) {
                    zipCodeTemp = challengeModel.zipCode[i].toString()
                } else
                    zipCodeTemp = "$zipCode,${challengeModel.zipCode}"
            }
            zipCode.set(zipCodeTemp)


            if (challengeModel.countryId != 0)
                getCities()
            if (challengeModel.stateId != 0)
                getStates()
        }


    }


}