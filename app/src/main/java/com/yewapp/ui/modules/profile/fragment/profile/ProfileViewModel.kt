package com.yewapp.ui.modules.profile.fragment.profile

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.profile.ProfileSettingsRequest
import com.yewapp.data.network.api.signup.SignUpResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.profile.navigator.ProfileNavigator
import com.yewapp.utils.BitmapImageRotator
import com.yewapp.utils.createNameWhenNoImage
import com.yewapp.utils.rx.SchedulerProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class ProfileViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ProfileNavigator>(dataManager, schedulerProvider) {

    val whoCanSeeStatsList = arrayListOf<String>()
    val allowUserToSeePointsList = arrayListOf<String>()


    var userId = ObservableField<String>("")
    var emailPhone = ObservableField<String>("")
    var userTypeSubscription = ObservableField<String>("Free User")  //subscribed or free user
    var fullName = ObservableField<String>("")
    var email = ObservableField<String>("")
    var profileImage = ObservableField<String>("")
    var profileCoverImage = ObservableField<String>("")
    var profileCoverImageBitmap = ObservableField<Bitmap>()
    var gender = ObservableField<String>("")


    var weight = ObservableField<String>("")
    var heartRate = ObservableField<String>("")

    var address = ObservableField<String>("")
    var pincode = ObservableField<String>("")
    var cityAndGender = ObservableField<String>("")
    var state = ObservableField<String>("")
    var country = ObservableField<String>("")
    var bio = ObservableField<String>("")

    var isNotificationEnabled = ObservableField<String>("")
    var mobileVerified = ObservableField<String>("")
    var followers = ObservableField<String>("")
    var following = ObservableField<String>("")

    var biking = ObservableField<String>("")
    var hiking = ObservableField<String>("")
    var running = ObservableField<String>("")
    var calories = ObservableField<String>("")
    var distance = ObservableField<String>("")
    var time = ObservableField<String>("")
    var elevationGain = ObservableField<String>("")
    var whoCanSeeYourStats = ObservableField<String>("")
    var isUserCanSeeYourPointsText = ObservableField<String>("")

    //used for unable to disable switches
    var allowCompareStatsPrivacy = ObservableField<Boolean>(true)
    var hideNumber = ObservableField<Boolean>(false)
    var hideEmail = ObservableField<Boolean>(false)
    var hideActivityStartLocation = ObservableField<Boolean>(false)

    // var statusVisibility = true
    var allowUserToSeeYourPoint = ObservableField<Boolean>(true)


    val isImageAvailable = ObservableField<Boolean>(false)
    val shortName = ObservableField<String>("")


    init {
        whoCanSeeStatsList.add("Followers and Following")
        whoCanSeeStatsList.add("Associate Accounts")
        whoCanSeeStatsList.add("Spectators")

        allowUserToSeePointsList.add("Yes")
        allowUserToSeePointsList.add("No")

        if (dataManager.getUser().profileImage.isNullOrEmpty()) {
            isImageAvailable.set(true)
        } else {
            isImageAvailable.set(false)
            shortName.set(
                createNameWhenNoImage(
                    "${dataManager.getUser().firstName} ${dataManager.getUser().lastName}" ?: "YW"
                )
            )
        }

    }

    override fun setData(extras: Bundle?) {

    }

    fun onActionItemClick(view: View) {
        when (view.id) {
            R.id.tv_edit -> {
                getNavigator()?.navigateToEditProfile()
            }
            R.id.followersLayout -> {
                getNavigator()?.navigateToFollower(0)
            }
            R.id.followingLayout -> {
                getNavigator()?.navigateToFollower(1)
            }
            R.id.compareStatistic -> {
                getNavigator()?.navigateCompareStatistic()
            }
            R.id.viewAllModelEquipments -> {
                getNavigator()?.navigateCompareStatistic()
            }
            R.id.whoCanSeeYourStats -> {
                getNavigator()?.chooseWhoCanSeeStats(view)
            }
            R.id.allowSeePoints -> {
                getNavigator()?.allowUsersCanSeePoints(view)
            }
            R.id.becameSponsor -> {
                getNavigator()?.becameSponsor()
            }
//            R.id.compareStatistic -> {
//                getNavigator()?.compareStatistic()
//            }
        }
    }


    fun getProfile() {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getProfile(
            ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::onSuccess, this::onError)
        )

    }

    private fun onSuccess(response: SignUpResponse) {
        isLoading.set(false)
        //save user details
        dataManager.saveUser(response.profile)
        userId.set(response.profile.userId.toString())
        gender.set(response.profile.gender)
//        if (!response.subscriptionDetail.name.equals("free", true)) {
//            userTypeSubscription.set("ASSOCIATE ACCOUNT")
//        }

        if (!response.profile.profileImage.isNullOrEmpty()) {
            isImageAvailable.set(true)
            profileImage.set(response.profile.profileImage)
        } else {
            isImageAvailable.set(false)
            shortName.set(
                createNameWhenNoImage(
                    "${response.profile.firstName} ${response.profile.lastName}" ?: "YW"
                )
            )
        }

        profileCoverImage.set(response.profile.profileCoverImage)
//        CoroutineScope(Dispatchers.IO).launch {
//            val imageBitmap = BitmapImageRotator.rotateImageBySize(
//                response.profile.profileCoverImage ?: return@launch
//            )
//            launch(Dispatchers.Main) {
//                profileCoverImageBitmap?.set(imageBitmap)
//
//            }
//        }

        weight.set(response.profile.weight)
        heartRate.set(response.profile.heartRate)
        address.set(response.profile.address)
        pincode.set(response.profile.pincode)
        address.set(response.profile.address)
        address.set(response.profile.address)
//        city.set("${response.profile.city}, ${response.profile.country}")
        cityAndGender.set(dataManager.getUser().city + ", " + dataManager.getUser().country + " • " + dataManager.getUser().gender)

        state.set(response.profile.state)
        country.set(response.profile.country)
        bio.set(response.profile.bio)
        whoCanSeeYourStats.set(response.profile.whoCanSeeYourStats)
        if (response.profile.allowUserToSeeYourPoint ?: return) {
            isUserCanSeeYourPointsText.set("Yes")
            allowUserToSeeYourPoint.set(true)
        } else {
            allowUserToSeeYourPoint.set(false)
            isUserCanSeeYourPointsText.set("No")
        }
        isNotificationEnabled.set(response.profile.isNotificationEnabled.toString())
        mobileVerified.set(response.profile.mobileVerified.toString())
        //Follower and following
        if ((response.profile.followers ?: return) > 1000) {
            followers.set("${response.profile.followers.div(1000)}K")
        } else {
            followers.set("${response.profile.followers}")
        }
        if ((response.profile.following ?: return) > 1000) {
            following.set("${response.profile.following.div(1000)}K")
        } else {
            following.set("${response.profile.following}")
        }

        if (response.profile.phone.isNullOrEmpty()) {
            emailPhone.set(response.profile.email.toString())//+ "/" + ""
        } else {
            emailPhone.set(response.profile.email.toString() + "/" + response.profile.phone)
        }
        //  emailPhone.set(response.profile.email.toString() + "/" + )
        fullName.set(response.profile.firstName.toString() + response.profile.lastName)
        biking.set("${response.activities.biking / 1000}K")
        hiking.set("${response.activities.hiking / 1000}K")
        running.set("${response.activities.running / 1000}K")
        calories.set(response.activities.calories.toString())
        val distanceFormatter = DecimalFormat("###,##0")
        distance.set("${distanceFormatter.format(response.activities.distance)} mi")
        time.set(response.activities.time.toString())
        elevationGain.set(response.activities.elevationGain.toString())
        // statusVisibility = (response.privacySetting.statusVisibility)


        allowCompareStatsPrivacy.set(response.profile.compareStatsPrivacy)
        allowUserToSeeYourPoint.set(response.profile.allowUserToSeeYourPoint)

        hideNumber.set(response.profile.hidePhoneNumber)
        hideEmail.set(response.profile.hideEmailId)
        hideActivityStartLocation.set(response.profile.hideActivityStartingLocation)


    }

    private fun onError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }


    fun updateProfileSettings() {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.updateProfileSettings(
                ProfileSettingsRequest(
                    allowUserToSeeYourPoint.get() ?: return,
                    allowCompareStatsPrivacy.get() ?: return,
                    hideActivityStartLocation.get() ?: return,
                    hideEmail.get() ?: return,
                    hideNumber.get() ?: return,
                    whoCanSeeYourStats.get() ?: return
                )
            ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::updateProfileSettingSuccess, this::onError)
        )
    }

    private fun updateProfileSettingSuccess(message: String) {
        getNavigator()?.onSuccess(message)
    }


    fun onCompareStatsPrivacyChanged(button: CompoundButton?, check: Boolean) {
        if (allowCompareStatsPrivacy.get() != check) {
            allowCompareStatsPrivacy.set(check)
            updateProfileSettings()
        }
    }

    fun onHideNumberChanged(button: CompoundButton?, check: Boolean) {
        if (hideNumber.get() != check) {
            hideNumber.set(check)
            updateProfileSettings()
        }

    }

    fun onHideEmailChanged(button: CompoundButton?, check: Boolean) {
        if (hideEmail.get() != check) {
            hideEmail.set(check)
            updateProfileSettings()
        }
    }

    fun onHideActivityStartLocationChanged(button: CompoundButton?, check: Boolean) {
        if (hideActivityStartLocation.get() != check) {
            hideActivityStartLocation.set(check)
            updateProfileSettings()
        }
    }

}