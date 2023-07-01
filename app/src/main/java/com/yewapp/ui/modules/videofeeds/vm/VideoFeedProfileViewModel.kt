package com.yewapp.ui.modules.videofeeds.vm

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.user.BlockUserRequest
import com.yewapp.data.network.api.video.VideoData
import com.yewapp.data.network.api.video.VideoListResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.videofeeds.extras.VideoFeedUserExtras
import com.yewapp.ui.modules.videofeeds.navigator.VideoFeedProfileNavigator
import com.yewapp.utils.popup.VedioFeedCommentPopUpDialog
import com.yewapp.utils.rx.SchedulerProvider

class VideoFeedProfileViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<VideoFeedProfileNavigator>(dataManager, schedulerProvider) {

    // values added that get from response
    private val _videoFeedList = MutableLiveData<List<VideoData>>()
    val videoFeedLiveList: LiveData<List<VideoData>> get() = _videoFeedList

    // adding response in this list
    val videoFeedList = mutableListOf<VideoData>()
    var userId = 0
    var followingVisibility = ObservableField<Boolean>(false)
    var followStatus = ObservableField<String>("")
    var userName = ObservableField<String>("")
    var userImage = ObservableField<String>("")
    var dateTime = ObservableField<String>("")
    val profileImageVisibility = ObservableField<Boolean>(true)
    val profileImage = ObservableField<String>("")
    val profileImageName = ObservableField<String>("")

    override fun setData(extras: Bundle?) {
        userId = extras?.getInt(VideoFeedUserExtras.USER_ID)!!
        userName.set(extras.getString(VideoFeedUserExtras.USER_NAME))
        userImage.set(extras.getString(VideoFeedUserExtras.USER_IMAGE))
        dateTime.set(extras.getString(VideoFeedUserExtras.DATETIME))
        if (userImage.get().isNullOrEmpty()) {
            profileImageVisibility.set(false)
            if (!userName.get().isNullOrBlank()) {
                val arr: Array<String> = userName.get()?.split(" ")?.toTypedArray()!!
                val fname = arr[0].substring(0, 1).uppercase()
                var lname = ""
                if (arr[1].isNotEmpty()) {
                    lname = arr[1].substring(0, 1).uppercase()
                    profileImageName.set(fname + lname)
                } else {
                    lname = arr[0].substring(1, 2).uppercase()
                    profileImageName.set(fname + lname)
                }
            }
//            else {
////                profileImage.set("")
//                profileImageVisibility.set(true)
//            }
        } else {
            profileImageVisibility.set(true)
//             profileImage.set(userImage.get())
        }

        if (extras.getBoolean(VideoFeedUserExtras.FOLLOW_STATUS)) {
            followStatus.set(dataManager.getResourceProvider().getString(R.string.unfollow))
        } else {
            followStatus.set(dataManager.getResourceProvider().getString(R.string.follow))
        }

        if (userId == dataManager.getUser().userId ?: return) {
            followingVisibility.set(true)
        } else {
            followingVisibility.set(false)
        }
        getVideoList()
    }

    private fun getVideoList() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getUserPublishVideoList(
                "", userId
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::success, this::error)
        )
    }

    fun success(response: VideoListResponse) {
        isLoading.set(false)
        if (response.list.isNotEmpty()) {
            _videoFeedList.value = response.list
            videoFeedList.addAll(response.list)
        }
    }

    fun error(throwable: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(Throwable(throwable))
    }

    fun onFollowClick(view: View) {
        when (view.id) {
            R.id.tvFollow -> {
                toggleFollowUser(userId)
            }
            R.id.tv_follower -> {
                getNavigator()?.onFollowerClick()
            }
            R.id.tv_following -> {
                getNavigator()?.onFollowingClick()
            }
        }

    }

    private fun toggleFollowUser(id: Int) {
//        if (isLoading.get()) return
//        isLoading.set(true)
//        compositeDisposable.add(
//            dataManager.followUser(
//                BlockUserRequest(
//                    id )).subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .subscribe(this::onFollowSuccess, this::onFollowFailure)
//        )
    }

    private fun onFollowSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.onSuccess(message)
    }

    private fun onFollowFailure(t: Throwable) {
        isLoading.set(false)
    }

    fun onOptionClick(view: View) {
        VedioFeedCommentPopUpDialog.showPopUpForProfileVideoFeeds(view, userId) {
            getNavigator()?.onOptionMenuClick(it, userId)
        }
    }

    fun onFeedOptionSelected(option: String, userId: Int) {
        when (option) {
            "Report User" -> {
                getNavigator()?.onOptionSelectedClick(option, userId)
            }
            "Block User" -> {
                getNavigator()?.onOptionSelectedClick(option, userId)
            }
        }
    }

    // Block User
    fun toggleBlockUser(id: Int) {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.blockUser(
                BlockUserRequest(id)
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onBlockSuccess, this::onUserOperationFailure)
        )
    }

    private fun onBlockSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.onSuccess(message)
    }

    private fun onUserOperationFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t)
    }

}