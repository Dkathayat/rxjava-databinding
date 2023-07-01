package com.yewapp.ui.modules.addassociatepermission

import android.os.Bundle
import android.widget.CompoundButton
import androidx.databinding.ObservableField
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.associate.AssociatePermission
import com.yewapp.data.network.api.associate.AssociatePermissionRequest
import com.yewapp.data.network.api.associate.AssociatePermissionResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.rx.SchedulerProvider


/**
 * @author: Narbir Singh
 * @description:This model class is used to add or update associate member permission
 */

class AddAssociatePermissionViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider
) : BaseViewModel<AddAssociatePermissionNavigator>(dataManager, schedulerProvider) {

    var isUpdate = ObservableField<Boolean>(false)

    var feedFirstGranted = ObservableField<Boolean>(false)
    var feedSecondGranted = ObservableField<Boolean>(false)
    var feedThirdGranted = ObservableField<Boolean>(false)
    var feedFourthGranted = ObservableField<Boolean>(false)

    var videoFeedFirstGranted = ObservableField<Boolean>(false)
    var videoFeedSecondGranted = ObservableField<Boolean>(false)
    var videoFeedThirdGranted = ObservableField<Boolean>(false)
    var videoFeedFourthGranted = ObservableField<Boolean>(false)

    var challengeFirstGranted = ObservableField<Boolean>(false)
    var challengeSecondGranted = ObservableField<Boolean>(false)
    var challengeThirdGranted = ObservableField<Boolean>(false)

    var routeGranted = ObservableField<Boolean>(false)
    var associateId = ObservableField<String>("")
    var permissionList = arrayListOf<AssociatePermission>()


    override fun setData(extras: Bundle?) {
        associateId.set(extras?.getString(AddAssociatePermissionExtras.ASSOCIATE_ID) ?: return)
        isUpdate.set(extras?.getBoolean(AddAssociatePermissionExtras.IS_UPDATE) ?: return)

        getAssociatePermissions(
            associateId.get() ?: return
        )

    }

    /**
     * @author: Narbir Singh
     * @description: Get Associate member permissions details
     */
    private fun getAssociatePermissions(associateID: String) {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getAssociatePermissions(associateID)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::associatePermissionSuccess, this::failure)
        )
    }

    private fun associatePermissionSuccess(response: AssociatePermissionResponse) {
        isLoading.set(false)
        val feedList = response.feeds
        for (i in feedList.indices) {
            permissionList.add(AssociatePermission(feedList[i].id, feedList[i].isGranted))
            when (i) {
                0 -> feedFirstGranted.set(feedList[i].isGranted)
                1 -> feedSecondGranted.set(feedList[i].isGranted)
                2 -> feedThirdGranted.set(feedList[i].isGranted)
                3 -> feedFourthGranted.set(feedList[i].isGranted)
            }
        }
        val videoFeed = response.video_feeds
        for (i in videoFeed.indices) {
            permissionList.add(AssociatePermission(videoFeed[i].id, videoFeed[i].isGranted))
            when (i) {
                0 -> videoFeedFirstGranted.set(feedList[i].isGranted)
                1 -> videoFeedSecondGranted.set(feedList[i].isGranted)
                2 -> videoFeedThirdGranted.set(feedList[i].isGranted)
                3 -> videoFeedFourthGranted.set(feedList[i].isGranted)
            }
        }

        val challengePermission = response.challenges
        for (i in challengePermission.indices) {
            permissionList.add(
                AssociatePermission(
                    challengePermission[i].id,
                    challengePermission[i].isGranted
                )
            )
            when (i) {
                0 -> challengeFirstGranted.set(feedList[i].isGranted)
                1 -> challengeSecondGranted.set(feedList[i].isGranted)
                2 -> challengeThirdGranted.set(feedList[i].isGranted)
            }
        }
        val routePermission = response.routes
        for (i in routePermission.indices) {
            permissionList.add(
                AssociatePermission(
                    routePermission[i].id,
                    routePermission[i].isGranted
                )
            )
            when (i) {
                0 -> routeGranted.set(feedList[i].isGranted)
            }
        }
    }

    private fun failure(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error, false)
    }

    fun onClickAddAssociate() {
        updateAssociatePermission()
    }


    /**
     * @author: Narbir Singh
     * @description: UPDATE Associate member permissions details
     */
    private fun updateAssociatePermission() {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.updateAssociatePermission(
                AssociatePermissionRequest(
                    associateId.get()?.toInt() ?: return, permissionList
                )
            )
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::addPermissionSuccess, this::addPermissionFailure)
        )
    }

    private fun addPermissionSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.navigateToProfile(message)
    }

    private fun addPermissionFailure(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error, false)
    }


    //    Feeds permission switch
    fun onFeedFirstCheckedChange(button: CompoundButton?, check: Boolean) {
        permissionList[0] = AssociatePermission(permissionList[0].id, check)
    }

    fun onFeedSecondCheckedChange(button: CompoundButton?, check: Boolean) {
        permissionList[1] = AssociatePermission(permissionList[1].id, check)

    }

    fun onFeedThirdCheckedChange(button: CompoundButton?, check: Boolean) {
        permissionList[2] = AssociatePermission(permissionList[2].id, check)

    }

    fun onFeedFourthCheckedChange(button: CompoundButton?, check: Boolean) {
        permissionList[3] = AssociatePermission(permissionList[3].id, check)
    }


    //Video Feeds permission switch
    fun onVideoFeedFirstCheckedChange(button: CompoundButton?, check: Boolean) {
        permissionList[4] = AssociatePermission(permissionList[4].id, check)
    }

    fun onVideoFeedSecondCheckedChange(button: CompoundButton?, check: Boolean) {
        permissionList[5] = AssociatePermission(permissionList[5].id, check)

    }

    fun onVideoFeedThirdCheckedChange(button: CompoundButton?, check: Boolean) {
        permissionList[6] = AssociatePermission(permissionList[6].id, check)

    }

    fun onVideoFeedFourthCheckedChange(button: CompoundButton?, check: Boolean) {
        permissionList[7] = AssociatePermission(permissionList[7].id, check)
    }


    //Challenge permission switch
    fun onChallengeFirstCheckedChange(button: CompoundButton?, check: Boolean) {
        permissionList[8] = AssociatePermission(permissionList[8].id, check)

    }

    fun onChallengeSecondCheckedChange(button: CompoundButton?, check: Boolean) {
        permissionList[9] = AssociatePermission(permissionList[9].id, check)
    }

    fun onChallengeThirdCheckedChange(button: CompoundButton?, check: Boolean) {
        permissionList[10] = AssociatePermission(permissionList[10].id, check)
    }

    //Route permission switch
    fun onRouteCheckedChange(button: CompoundButton?, check: Boolean) {
        permissionList[11] = AssociatePermission(permissionList[11].id, check)
    }


}