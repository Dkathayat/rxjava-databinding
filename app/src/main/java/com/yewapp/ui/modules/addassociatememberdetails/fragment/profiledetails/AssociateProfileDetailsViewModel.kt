package com.yewapp.ui.modules.addassociatememberdetails.fragment.profiledetails

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.associate.AddAssociateResponse
import com.yewapp.data.network.api.associate.AssociateMemberDetailsResponse
import com.yewapp.data.network.api.profile.AddAssociateRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.addassociatememberdetails.fragment.extras.AddMemberDetailsExtras
import com.yewapp.ui.modules.addassociatepermission.AddAssociatePermissionExtras
import com.yewapp.utils.DateUtils
import com.yewapp.utils.isEmail
import com.yewapp.utils.rx.SchedulerProvider
import java.util.*


/**
 * @description: This class is used to  add or update Associate Details
 * CASE 1 : false - add Associate
 *  CASE 2 : true - update Associate
 *  @author: Narbir Singh
 */

class AssociateProfileDetailsViewModel(
    dataManager: DataManager, schedulerProvider: SchedulerProvider
) : BaseViewModel<AssociateProfileDetailsNavigator>(dataManager, schedulerProvider) {

    /**
     * @description: This variable is used to check and validate user want to add or update Associate
     * CASE 1 : false - add Associate
     *  CASE 2 : true - update Associate
     *  @author: Narbir Singh
     */
    var isUpdate = ObservableField<Boolean>(false)


    var firstName = ObservableField<String>("")
    var firstNameError = ObservableField<String>("")
    var lastName = ObservableField<String>("")
    var lastNameError = ObservableField<String>("")
    var email = ObservableField<String>("")
    var emailError = ObservableField<String>("")
    var mobileNumber = ObservableField<String>("")
    var mobileNumberError = ObservableField<String>("")
    var gender = ObservableField<String>("")
    var genderError = ObservableField<String>("")
    var dobDisplayed = ObservableField<String>("")

    //    var dobSent = ObservableField<String>("")
    var dobError = ObservableField<String>("")
    var weight = ObservableField<String>("")
    var weightError = ObservableField<String>("")
    var heartRate = ObservableField<String>("")
    var heartRateError = ObservableField<String>("")
    var yewStatus = ObservableField<String>("Inactive")
    var yewStatusSent = ObservableField<String>("0")
    var password = ObservableField<String>("")

    //    var yewStatusError = ObservableField<String>("")
    var relation = ObservableField<String>("")
    var relationError = ObservableField<String>("")
    var bio = ObservableField<String>("")
    var bioError = ObservableField<String>("")
    var associateId = ObservableField<String>("")

    private lateinit var associateData: AddAssociateRequest
    private lateinit var updateAssociateData: AddAssociateRequest


    override fun setData(extras: Bundle?) {
        /**
         * @description: Check user received ASSOCIATE_ID and set data accordingly
         * CASE 1 :Not Having ASSOCIATE_ID : Used to Add ASSOCIATE Details
         * CASE 2 : Have ASSOCIATE_ID : Used to update ASSOCIATE Details
         *  @author: Narbir Singh
         */
        if (extras?.containsKey(AddAssociatePermissionExtras.ASSOCIATE_ID)
                ?: return
        ) {//UPDATE ASSOCIATE
            isUpdate.set(true)
            associateId.set(extras.getString(AddAssociatePermissionExtras.ASSOCIATE_ID))
            getAssociateMemberDetails(extras.getString(AddAssociatePermissionExtras.ASSOCIATE_ID))
            getNavigator()?.updateAssociateID(associateId.get() ?: return)
        } else {//ADD Associate
            isUpdate.set(false)
            email.set(extras.getString(AddMemberDetailsExtras.Member_EMAIL))
            dobDisplayed.set(extras.getString(AddMemberDetailsExtras.Member_DOB))
            password.set(extras.getString(AddMemberDetailsExtras.Member_PASSWORD))
        }
    }


    fun onDobClick(view: View) {
        getNavigator()?.onDobClicked()
    }

    fun onGenderClick(view: View) {
        getNavigator()?.onGenderClicked()
    }

    fun onStatusClicked(view: View) {
        getNavigator()?.onStatusClicked()
    }

    fun onNextClick(view: View) {
        if (!validate()) return

        val status = if (yewStatus.get()!!.equals("active", ignoreCase = true)) 1 else 0
        yewStatusSent.set("$status")

        if (associateId.get().isNullOrEmpty() || associateId.get()?.equals("0") ?: return) {
            addAssociateMemberDetails()
        } else {
            updateAssociateMemberDetails()
        }
    }

    private fun addAssociateMemberDetails() {
        if (isLoading.get()) return
        isLoading.set(true)

        associateData = AddAssociateRequest(
            firstName.get() ?: return,
            lastName.get() ?: return,
            mobileNumber.get() ?: return,
            gender.get() ?: return,
            DateUtils.convertDisplayDateToApiFormat(dobDisplayed.get() ?: return) ?: return,
            weight.get() ?: return,
            heartRate.get() ?: return,
            yewStatusSent.toString(),
            relation.get() ?: return,
            bio.get() ?: return,
            email.get()?.lowercase() ?: return,
            password.get() ?: return,
            "",
            "",
            "",
            "",
            ""
        )


        compositeDisposable.add(
            dataManager.addAssociateMember(
                associateData
            ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::addAssociateMemberSuccess, this::addAssociateMemberError)
        )

    }

    private fun addAssociateMemberSuccess(response: AddAssociateResponse) {
        isLoading.set(false)
        if (associateId.get().isNullOrEmpty()) {
            associateId.set(response.associateId.toString())
            getNavigator()?.navigateNextScreen()
        } else {
            getNavigator()?.updateAssociateProfileSuccess("Your associate profile updated sucessfully")
        }


    }

    private fun addAssociateMemberError(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error)
    }


    private fun updateAssociateMemberDetails() {
        updateAssociateData = AddAssociateRequest(
            firstName.get() ?: return,
            lastName.get() ?: return,
            mobileNumber.get() ?: return,
            gender.get() ?: return,
            DateUtils.convertDisplayDateToApiFormat(dobDisplayed.get() ?: return) ?: return,
            weight.get() ?: return,
            heartRate.get() ?: return,
            yewStatusSent.get() ?: return,
            relation.get() ?: return,
            bio.get() ?: return,
            email.get()?.lowercase() ?: return,
            "",
            "",
            "",
            "",
            "",
            ""
        )

        if (associateData.compareTo(updateAssociateData) == 0) {
            getNavigator()?.updateAssociateProfileSuccess("No changes are available to update")
            return
        } else {
            if (isLoading.get()) return
            isLoading.set(true)


            compositeDisposable.add(
                dataManager.updateAssociateMember(
                    associateId.get() ?: return, updateAssociateData
                ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                    .subscribe(this::updateAssociateMemberSuccess, this::updateAssociateMemberError)
            )
        }
    }


    private fun updateAssociateMemberSuccess(message: String) {
        isLoading.set(false)

        getNavigator()?.updateAssociateProfileSuccess(message)
    }

    private fun updateAssociateMemberError(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error)
    }


    private fun getAssociateMemberDetails(string: String?) {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getAssociateMemberDetails(
                associateId.get() ?: return
            ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::associateMemberDetailsSuccess, this::associateMemberDetailsError)
        )

    }

    private fun associateMemberDetailsSuccess(response: AssociateMemberDetailsResponse) {
        isLoading.set(false)
        associateData = AddAssociateRequest(
            response.firstName,
            response.lastName,
            response.mobileNo,
            response.gender,
            response.dob,
            response.weight,
            response.heartRate,
            response.status,
            response.associateRelation,
            response.bio,
            response.email.lowercase(),
            "",
            "",
            "",
            "",
            response.profileCoverImage,
            response.profileImage
        )


        firstName.set(response.firstName)
        lastName.set(response.lastName)
        email.set(response.email)
        mobileNumber.set(response.mobileNo)
        gender.set(response.gender.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
        weight.set(response.weight)
        heartRate.set(response.heartRate)
        if (response.status.toInt() == 1) {
            yewStatus.set("Active")
        } else {
            yewStatus.set("Inactive")
        }

        relation.set(response.associateRelation)
        bio.set(response.bio)
        dobDisplayed.set(DateUtils.convertApiDateToDisplayFormat(response.dob))
//        password.set(response.pass)
    }

    private fun associateMemberDetailsError(t: Throwable) {
        isLoading.set(false)
    }


    fun validate(): Boolean {
        return when {
            firstName.get()!!.isEmpty() -> {
                firstNameError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_first_name)
                )
                false
            }
            firstName.get()!!.length !in 4..32 -> {
                firstNameError.set(
                    dataManager.getResourceProvider().getString(R.string.invalid_first_name)
                )
                false
            }
            lastName.get()!!.isEmpty() -> {
                lastNameError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_last_name)
                )
                false
            }
            lastName.get()!!.length !in 4..32 -> {
                firstNameError.set(
                    dataManager.getResourceProvider().getString(R.string.invalid_last_name)
                )
                false
            }
            email.get()!!.isEmpty() -> {
                emailError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_email_error)
                )
                false
            }
            !email.get()!!.isEmail() -> {
                emailError.set(
                    dataManager.getResourceProvider().getString(R.string.invalid_email_error)
                )
                false
            }
            mobileNumber.get()!!.isNotEmpty() -> {
                return if (mobileNumber.get()!!.length !in 8..12) {
                    mobileNumberError.set(
                        dataManager.getResourceProvider()
                            .getString(R.string.invalid_phoneNumber_error)
                    )
                    false
                } else {
                    true
                }
            }

            dobDisplayed.get()!!.isEmpty() -> {
                getNavigator()?.onError(
                    Throwable(
                        dataManager.getResourceProvider().getString(R.string.empty_dob_error)
                    )
                )
                false
            }

            relation.get()!!.isNullOrEmpty() -> {
                getNavigator()?.onError(
                    Throwable(
                        dataManager.getResourceProvider().getString(R.string.empty_relation)
                    )
                )
                false
            }


            bio.get().isNullOrEmpty() -> {
                bioError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_address)
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

    fun onEmailChange(s: CharSequence, start: Int, before: Int, count: Int) {
        emailError.set("")
    }

    fun onMobileChange(s: CharSequence, start: Int, before: Int, count: Int) {
        mobileNumberError.set("")
    }


    fun onGenderChange(s: CharSequence, start: Int, before: Int, count: Int) {
        genderError.set("")
    }

    fun onDobChange(s: CharSequence, start: Int, before: Int, count: Int) {
        dobError.set("")
    }

    fun onWeightChange(s: CharSequence, start: Int, before: Int, count: Int) {
        weightError.set("")
    }

    fun onRelationChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        heartRateError.set("")
    }

    fun heartRateChange(s: CharSequence, start: Int, before: Int, count: Int) {
        heartRateError.set("")
    }

    fun bioChange(s: CharSequence, start: Int, before: Int, count: Int) {
        bioError.set("")
    }


//    fun setPlaceData(searchedAddress: Map<String, String>) {
//
//        zipCode.set(searchedAddress["pinCode"] ?: return)
//
//        countryList.forEach {
//            if (searchedAddress["country"].equals(it.name, true)) {
//                selectedCountry.set(it.name)
//                selectedCountryId = it.id
//                getStates()
//                return@forEach
//            }
//        }
//    }
}