package com.yewapp.ui.modules.editProfile.vm

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amazonaws.auth.BasicAWSCredentials
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.profile.ProfileImageRequest
import com.yewapp.data.network.api.profile.SportType
import com.yewapp.data.network.api.sports.DeleteSportsRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.editProfile.extras.EditProfileExtras
import com.yewapp.ui.modules.editProfile.navigator.SportDetailNavigator
import com.yewapp.utils.rx.SchedulerProvider

class SportDetailViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<SportDetailNavigator>(dataManager, schedulerProvider) {

    lateinit var selectedSportsType: SportType
    var isNextActive = ObservableBoolean(false)
    var isSaveImages = ObservableBoolean(false)



    var mutableSportsList = mutableListOf<SportType>()
    var _sportTypeList = MutableLiveData<List<SportType>>()
    val sportTypeList: LiveData<List<SportType>>
        get() = _sportTypeList

    var isProfileImageClicked = ObservableField<Boolean>(false)
    val profileImageVisibility = ObservableField<Boolean>(true)
    var userName = ObservableField<String>("")
    var cityAndGender = ObservableField<String>("")
    var profileImageName = ObservableField<String>("")
    var profileImage = ObservableField<String>("")
    var profileCoverImageBitmap = ObservableField<Bitmap>()
    var profileCoverImageURL = ObservableField<String>("")
    var associateID = ""

    override fun setData(extras: Bundle?) {
        associateID = extras?.getString(EditProfileExtras.ASSOCIATE_ID) ?: ""
    }


    var awsCredentials: BasicAWSCredentials

    init {
        awsCredentials = BasicAWSCredentials(
            dataManager.getAwsCredential().bucket_key,
            dataManager.getAwsCredential().bucket_secret_key
        )
        userName.set(
            dataManager.getUser().firstName ?: ("" + " " + dataManager.getUser().lastName) ?: ""
        )
        cityAndGender.set(
            dataManager.getUser().city ?: ("" + ", " + dataManager.getUser().country)
            ?: ("" + " • " + dataManager.getUser().gender) ?: ""
        )
        if (dataManager.getUser().profileImage.isNullOrEmpty()) {
            profileImageVisibility.set(false)
            if (!dataManager.getUser().firstName.isNullOrBlank()) {
                val fname = dataManager.getUser().firstName!!.substring(0, 1).uppercase()
                val lname = dataManager.getUser().lastName!!.substring(0, 1).uppercase()
                profileImageName.set(fname + lname)

            } else {
                profileImage.set("")
                profileImageVisibility.set(true)
            }
        } else {
            profileImage.set(dataManager.getUser().profileImage)
            profileImageVisibility.set(true)
            // profileImage.set()
        }
        if (!dataManager.getUser().profileCoverImage.isNullOrEmpty()) {
            profileCoverImageURL.set(dataManager.getUser().profileCoverImage)
        }

    }

    /**
     * @author: Narbir Singh
     * @description: This method is fetch sports type
     */
    fun getSportType() {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.getSportTypes()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onGetSportSuccess, this::onGetSportFailure)
        )
    }

    private fun onGetSportSuccess(response: List<SportType>) {
        isLoading.set(false)
        mutableSportsList.clear()
        mutableSportsList.addAll(response)
        _sportTypeList.value = mutableSportsList
    }

    private fun onGetSportFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t)
    }

    fun onClick(view: View) {
        if (isLoading.get()) return
        when (view.id) {
            R.id.forUserImage -> {
                isProfileImageClicked.set(true)
                getNavigator()?.launchImageOptions()
            }
            R.id.forBannerImage -> {
                isProfileImageClicked.set(false)
                getNavigator()?.launchImageOptions()
            }
            R.id.btnNext -> {
                if (!isSaveImages.get()) {
                    saveImagesProfileApi()
                } else {
                    getNavigator()?.navigateToAddSubSportsType()
                }            }

        }
    }

    /**
     * @author: Narbir Singh
     * @description: This method is used to update profile and profile cover image
     */
    private fun saveImagesProfileApi() {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.updateProfileImage(
                ProfileImageRequest(
                    "",// only user can call from here that why we will always pass ""
                    profileImage.get() ?: "",
                    profileCoverImageURL.get() ?: ""
                )
            ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::onSaveImageSuccess, this::onSaveImageFailure)
        )
    }

    private fun onSaveImageSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.onSuccess(message)
    }

    private fun onSaveImageFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t)
    }


    /**
     * @author: Narbir Singh
     * @description: This method is used to delete sports data
     */
    fun deleteSportsData(sportsID: String) {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.deleteSportsEquipmentData(DeleteSportsRequest(associateID,sportsID))
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::deleteSportsDataSuccess, this::deleteSportsDataFailure)
        )
    }

    private fun deleteSportsDataSuccess(message: String) {
        isLoading.set(false)
        getSportType()
    }

    private fun deleteSportsDataFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t)
    }

}