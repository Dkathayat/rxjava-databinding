package com.yewapp.ui.modules.editProfile.vm

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.profile.*
import com.yewapp.data.network.api.signup.SignUpResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.editProfile.extras.EditProfileExtras
import com.yewapp.ui.modules.editProfile.navigator.ProfileDetailNavigator
import com.yewapp.utils.convertDate
import com.yewapp.utils.isEmail
import com.yewapp.utils.rx.SchedulerProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileDetailViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ProfileDetailNavigator>(dataManager, schedulerProvider) {

    var countryList: List<Country> = mutableListOf()
    var stateList: List<State> = mutableListOf()
    var cityList: List<City> = mutableListOf()

    var firstName = ""
    var lastName = ""
    var address = ObservableField<String>("")
    var zipCode = ObservableField<String>("")
    var email = ""
    var country = ObservableField<String>("")
    var state = ""
    var city = ""
    var mobile = ""
    var weight = ""
    var maxHeartRate = ""
    var bio = ""
    var latitude = ""
    var longitude = ""

    val firstNameError = ObservableField<String>("")
    val lastNameError = ObservableField<String>("")
    val emailError = ObservableField<String>("")
    val addressError = ObservableField<String>("")
    val zipCodeError = ObservableField<String>("")
    val mobileError = ObservableField<String>("")
    val genderError = ObservableField<String>("")
    val weightError = ObservableField<String>("")
    val heartRateError = ObservableField<String>("")
    val bioError = ObservableField<String>("")

    var selectedCountryId = ""
    var selectedStateId = ""
    var selectedCityId = ""

    var selectedCountry = ObservableField<String>("")
    var countryError = ObservableField<String>("")
    var selectedState = ObservableField<String>("")
    var stateError = ObservableField<String>("")
    var selectedCity = ObservableField<String>("")
    var cityError = ObservableField<String>("")
    var dobDisplayed = ObservableField<String>("")
    var associateID = ""
    var dobSent = ""

    var gender = ObservableField<String>("")


    val isCountryLoading = ObservableBoolean(false)
    val isStateLoading = ObservableBoolean(false)
    val isCityLoading = ObservableBoolean(false)

    init {
        getCountry()
        populateExistingUserFields()
    }

    private fun populateExistingUserFields() {
        val user = dataManager.getUser() ?: return
        firstName = user.firstName ?: ""
        lastName = user.lastName ?: ""
        firstName = user.firstName ?: ""
        lastName = user.lastName ?: ""
        address.set(user.address ?: "")
        zipCode.set(user.pincode ?: "")
        email = user.email ?: ""
        country.set(user.country ?: "")
        selectedCountry.set(user.country ?: "")
        selectedCountryId = user.countryId.toString()
        city = user.city ?: ""
        selectedCity.set(user.city ?: "")
        selectedCityId = user.cityId.toString()
        state = user.state ?: ""
        selectedState.set(user.state)
        selectedStateId = user.stateId.toString()
        mobile = user.phone ?: ""
        gender.set(user.gender ?: "")
        dobDisplayed.set(user.dob ?: "")
        dobSent = if (!dobDisplayed.get().isNullOrEmpty()) {
            dobDisplayed.get()!!.convertDate()
        } else {
            ""
        }
        selectedCityId = "${user.cityId}"
        weight = user.weight ?: ""
        maxHeartRate = user.heartRate ?: ""
        bio = user.bio ?: ""
        if (selectedCountryId.isNotEmpty()) {
            getStates()
        }
        if (selectedStateId.isNotEmpty()) {
            getCities()
        }
    }

    override fun setData(extras: Bundle?) {
        associateID = extras?.getString(EditProfileExtras.ASSOCIATE_ID) ?: ""
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

    fun onCountryClick(view: View) {
        getNavigator()?.onCountryClicked()
    }

    fun onStateClick(view: View) {
        if (countryList.isNotEmpty()) {
            getNavigator()?.onStateClicked()
        } else {
            getNavigator()?.onError(
                Throwable(
                    dataManager.getResourceProvider().getString(R.string.country_id_error)
                )
            )
        }
    }

    fun onCityClick(view: View) {
        if (stateList.isNotEmpty()) {
            getNavigator()?.onCityClicked()
        } else {
            getNavigator()?.onError(
                Throwable(
                    dataManager.getResourceProvider().getString(R.string.state_id_error)
                )
            )
        }
    }

    fun onEmailClick(view: View) {
        getNavigator()?.onEmailClick()
    }

    fun onPhoneClick(view: View) {
        getNavigator()?.onPhoneClick()
    }

    fun onDobClick(view: View) {
        getNavigator()?.onDobClicked()
    }

    fun onGenderClick(view: View) {
        getNavigator()?.onGenderClicked()
    }

    fun onSubmitClick(view: View) {
        if (!validate()) return

        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.updateProfile(
                ProfileRequest(
                    firstName,
                    lastName,
                    mobile,
                    selectedCityId,
                    address.get() ?: return,
                    zipCode.get() ?: return,
                    gender.get() ?: return,
                    dobSent ?: "",
                    weight,
                    maxHeartRate,
                    bio,
                    "",
                    "",
                    latitude,
                    longitude
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onUpdateSuccess, this::onUpdateError)
        )
    }

    private fun onUpdateSuccess(response: SignUpResponse) {
        isLoading.set(false)
        dataManager.saveUser(response.profile)
        dataManager.saveSubscription(response.subscriptionDetail)
        getNavigator()?.onSuccess(
            dataManager.getResourceProvider().getString(R.string.user_profile_updated), false
        )
        viewModelScope.launch {
            delay(1000)
            getNavigator()?.navigateToDashboard()
        }

    }

    private fun onUpdateError(t: Throwable) {
        isLoading.set(false)
    }

    fun validate(): Boolean {
        return when {
            firstName.isEmpty() -> {
                firstNameError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_first_name)
                )
                false
            }
            firstName.length !in 4..32 -> {
                firstNameError.set(
                    dataManager.getResourceProvider().getString(R.string.invalid_first_name)
                )
                false
            }
            lastName.isEmpty() -> {
                lastNameError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_last_name)
                )
                false
            }
            lastName.length !in 4..32 -> {
                lastNameError.set(
                    dataManager.getResourceProvider().getString(R.string.invalid_last_name)
                )
                false
            }
            email.isEmpty() -> {
                emailError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_email_error)
                )
                false
            }
            !email.isEmail() -> {
                emailError.set(
                    dataManager.getResourceProvider().getString(R.string.invalid_email_error)
                )
                false
            }
            mobile.isNotEmpty() -> {
                return if (mobile.length !in 8..12) {
                    mobileError.set(
                        dataManager.getResourceProvider()
                            .getString(R.string.invalid_phoneNumber_error)
                    )
                    false
                } else {
                    true
                }
            }

//            address.get()?.isNotEmpty()!! -> {
//                addressError.set(
//                    dataManager.getResourceProvider()
//                        .getString(R.string.empty_address)
//                )
//                return false
//            }


            selectedCountryId.isEmpty() -> {
                getNavigator()?.onError(
                    Throwable(
                        dataManager.getResourceProvider().getString(R.string.country_id_error)
                    )
                )
                false
            }
            selectedStateId.isEmpty() -> {
                getNavigator()?.onError(
                    Throwable(
                        dataManager.getResourceProvider().getString(R.string.state_id_error)
                    )
                )
                false
            }
            selectedCityId.isEmpty() -> {
                getNavigator()?.onError(
                    Throwable(
                        dataManager.getResourceProvider().getString(R.string.city_id_error)
                    )
                )
                false
            }

            zipCode.get().isNullOrEmpty() -> {
                getNavigator()?.onError(
                    Throwable(
                        dataManager.getResourceProvider().getString(R.string.empty_zip_code)
                    )
                )
                false
            }

            zipCode.get()?.length !in 5..10 -> {   // zipCode.length !in 4..7
                getNavigator()?.onError(
                    Throwable(
                        dataManager.getResourceProvider().getString(R.string.invalid_zipcode)
                    )
                )
                false
            }

            weight.isNotEmpty() -> {
                if (weight.toDouble() == 0.0) {
                    weightError.set(
                        dataManager.getResourceProvider().getString(R.string.invalid_weight)
                    )
                    false
                } else {
                    true
                }
            }

            maxHeartRate.isNotEmpty() -> {
                return if (maxHeartRate.toInt() == 0) {
                    heartRateError.set(
                        dataManager.getResourceProvider().getString(R.string.invalid_heart_rate)
                    )
                    false
                } else
                    true
            }
            address.get().isNullOrEmpty() -> {
                addressError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_address)
                )
                false
            }

            else -> {
                true
            }

        }
    }

    fun validateOnBackPress(): Boolean {
        return when {

            email.isEmpty() -> {
                emailError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_email_error)
                )
                false
            }
            !email.isEmail() -> {
                emailError.set(
                    dataManager.getResourceProvider().getString(R.string.invalid_email_error)
                )
                false
            }

            selectedCountryId.isEmpty() -> {
                getNavigator()?.onError(
                    Throwable(
                        dataManager.getResourceProvider().getString(R.string.country_id_error)
                    )
                )
                false
            }
            selectedStateId.isEmpty() -> {
                getNavigator()?.onError(
                    Throwable(
                        dataManager.getResourceProvider().getString(R.string.state_id_error)
                    )
                )
                false
            }
            selectedCityId.isEmpty() -> {
                getNavigator()?.onError(
                    Throwable(
                        dataManager.getResourceProvider().getString(R.string.city_id_error)
                    )
                )
                false
            }

            zipCode.get().isNullOrEmpty() -> {
                getNavigator()?.onError(
                    Throwable(
                        dataManager.getResourceProvider().getString(R.string.empty_zip_code)
                    )
                )
                false
            }

            zipCode.get()?.length !in 4..7 -> {
                getNavigator()?.onError(
                    Throwable(
                        dataManager.getResourceProvider().getString(R.string.invalid_zipcode)
                    )
                )
                false
            }
            else -> {
                true
            }

        }
    }

    fun onFirstNameChange(s: CharSequence, start: Int, before: Int, count: Int) {
        firstNameError.set("")
    }

    fun onLastNameChange(s: CharSequence, start: Int, before: Int, count: Int) {
        lastNameError.set("")
    }

    fun onAddressChange(s: CharSequence, start: Int, before: Int, count: Int) {
        addressError.set("")
    }

    fun onCountryChange(s: CharSequence, start: Int, before: Int, count: Int) {
        countryError.set("")
    }

    fun onStateChange(s: CharSequence, start: Int, before: Int, count: Int) {
        stateError.set("")
    }

    fun onCityChange(s: CharSequence, start: Int, before: Int, count: Int) {
        stateError.set("")
    }

    fun onZipCodeChange(s: CharSequence, start: Int, before: Int, count: Int) {
        zipCodeError.set("")
    }

    fun onEmailChange(s: CharSequence, start: Int, before: Int, count: Int) {
        emailError.set("")
    }

    fun onMobileChange(s: CharSequence, start: Int, before: Int, count: Int) {
        mobileError.set("")
    }

    fun onGenderChange(s: CharSequence, start: Int, before: Int, count: Int) {
        genderError.set("")
    }

    fun onWeightChange(s: CharSequence, start: Int, before: Int, count: Int) {
        weightError.set("")
    }

    fun heartRateChange(s: CharSequence, start: Int, before: Int, count: Int) {
        heartRateError.set("")
    }

    fun bioChange(s: CharSequence, start: Int, before: Int, count: Int) {
        bioError.set("")
    }

    fun onAddressClick(view: View) {
        getNavigator()?.onAddressFieldClick()
    }

    fun setPlaceData(searchedAddress: Map<String, String>) {

        zipCode.set(searchedAddress["pinCode"] ?: return)

        countryList.forEach {
            if (searchedAddress["country"].equals(it.name, true)) {
                selectedCountry.set(it.name)
                selectedCountryId = it.id
                getStates()
                return@forEach
            }
        }
    }

}