package com.yewapp.ui.modules.addassociatememberdetails.fragment.sportsimages

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amazonaws.auth.BasicAWSCredentials
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.associate.*
import com.yewapp.data.network.api.profile.ProfileImageRequest
import com.yewapp.data.network.api.profile.SportType
import com.yewapp.data.network.api.sports.DeleteSportsRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.addassociatepermission.AddAssociatePermissionExtras
import com.yewapp.utils.rx.SchedulerProvider

class AssociateSportsImagesViewModel(
    dataManager: DataManager, schedulerProvider: SchedulerProvider
) : BaseViewModel<AssociateSportsImagesNavigator>(dataManager, schedulerProvider) {

    lateinit var selectedSportsType: SportType

    var isNextActive = ObservableBoolean(false)
    var isSaveImages = ObservableBoolean(false)

    var associateName = ObservableField<String>("")
    var cityCountryGender = ObservableField<String>("Oakwill, USA • Male")
    var profileImage = ObservableField<String>("")
    var profileCoverImageURL = ObservableField<String>("")
    var isUploading = ObservableField<Boolean>(false)
    var isProfileImageClicked = ObservableField<Boolean>(false)
    var haveAssociateID = ObservableField<Boolean>(false)
    var associateId = ObservableField<String>("")

    var associateSportTypeList = mutableListOf<SportType>()
    var associateSportTypeMutableList = MutableLiveData<List<SportType>>()
    val associateSportTypeLiveDataList: LiveData<List<SportType>> get() = associateSportTypeMutableList


    var awsCredentials: BasicAWSCredentials

    init {
        awsCredentials = BasicAWSCredentials(
            dataManager.getAwsCredential().bucket_key,
            dataManager.getAwsCredential().bucket_secret_key
        )
    }

    override fun setData(extras: Bundle?) {
        if (extras?.containsKey(AddAssociatePermissionExtras.ASSOCIATE_ID) ?: return) {
            associateId.set(extras.getString(AddAssociatePermissionExtras.ASSOCIATE_ID))
            haveAssociateID.set(true)
            getAssociateSportType(associateId.get() ?: return)
        }
    }

    fun getAssociateSportType(associateId: String) {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.getAssociateSportsType(
                RemoveAssociateAccountRequest(
                    associateId
                )
            ).subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::onGetSportSuccess, this::onGetSportFailure)
        )
    }

    private fun onGetSportSuccess(response: AssociateSportsTypeResponse) {
        isLoading.set(false)

        associateSportTypeList.clear()
        associateSportTypeList.addAll(response.sportsType)
        associateSportTypeMutableList.value = associateSportTypeList

        associateName.set(response.fullName)
        profileImage.set(response.profileImage)
        profileCoverImageURL.set(response.profileCoverImage)

        if (response.city.isNotEmpty() && response.country.isNotEmpty()) {
            cityCountryGender.set("${response.city}, ${response.country} • ${response.gender}")
        } else {
            cityCountryGender.set(response.gender)
        }

    }

    private fun onGetSportFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t)
    }

    fun onClick(view: View) {
        if (isLoading.get()) return
        when (view.id) {
            R.id.profileImage -> {
                isProfileImageClicked.set(true)
                getNavigator()?.launchImageOptions()
            }
            R.id.bannerCoverImage -> {
                isProfileImageClicked.set(false)
                getNavigator()?.launchImageOptions()
            }
            R.id.btnNext -> {
                if (!isSaveImages.get()) {
                    saveImagesProfileApi()
                } else {
                    getNavigator()?.navigateToAddSubSportsType()
                }
            }

        }
    }

    private fun saveImagesProfileApi() {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.updateProfileImage(
                ProfileImageRequest(
                    associateId.get() ?: "",
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

            dataManager.deleteSportsEquipmentData(
                DeleteSportsRequest(
                    associateId.get() ?: "",
                    sportsID
                )
            )
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::deleteSportsDataSuccess, this::deleteSportsDataFailure)
        )
    }

    private fun deleteSportsDataSuccess(message: String) {
        isLoading.set(false)
        getAssociateSportType(associateId.get() ?: "")
    }

    private fun deleteSportsDataFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t)
    }


}