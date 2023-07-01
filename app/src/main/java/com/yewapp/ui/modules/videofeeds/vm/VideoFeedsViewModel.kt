package com.yewapp.ui.modules.videofeeds.vm

import android.os.Bundle
import android.text.Editable
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.FollowUpdateResponse
import com.yewapp.data.network.api.comment.CommentRequest
import com.yewapp.data.network.api.feed.LikeFeedRequest
import com.yewapp.data.network.api.user.BlockUserRequest
import com.yewapp.data.network.api.video.Comment
import com.yewapp.data.network.api.video.CommentListResponse
import com.yewapp.data.network.api.video.VideoData
import com.yewapp.data.network.api.video.VideoListResponse
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.videofeeds.navigator.VideoFeedsNavigator
import com.yewapp.utils.rx.SchedulerProvider

class VideoFeedsViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<VideoFeedsNavigator>(dataManager, schedulerProvider) {

    var list = mutableListOf<VideoData>()
    var _list = MutableLiveData<List<VideoData>>()
    val listLive: LiveData<List<VideoData>> get() = _list

    // values added that get from response
    private val _videoFeedList = MutableLiveData<List<VideoData>>()
    val videoFeedLiveList: LiveData<List<VideoData>> get() = _videoFeedList

    // adding response in this list
    val videoFeedList = mutableListOf<VideoData>()
    private val _commentFeedList = MutableLiveData<List<Comment>>()
    val commentFeedLiveList: LiveData<List<Comment>> get() = _commentFeedList

    // adding response in this list
    val commentFeedList = mutableListOf<Comment>()
    var username = ""
    val profileImageVisibility = ObservableField<Boolean>(true)
    val userImage = ObservableField<String>("")
    val profileImageName = ObservableField<String>("")
    var feedId = -1
    var parentId = 0
    var commentMessage = ObservableField<String>("")
    var commentError = ObservableField<String>("")
    lateinit var selectedComment: com.yewapp.data.network.api.video.Comment
    var selectedItem = 0
    override fun setData(extras: Bundle?) {

    }

    init {
        getPublishVideoList()
    }

    fun getPublishVideoList() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getPublishVideoList(
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

    fun getCommentList(feedId: Int) {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.getCommentList(
                feedId
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::success, this::error)
        )
    }

    fun success(response: CommentListResponse) {
        isLoading.set(false)
        if (response.list.isNotEmpty()) {
            _commentFeedList.value = response.list
            commentFeedList.addAll(response.list)
        }
    }

    fun error(throwable: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(Throwable(throwable))
    }

    fun clearList() {
        _videoFeedList.value = mutableListOf()
        videoFeedList.clear()
    }

    fun onAddCommentClick() {
        // if (!validate()) return
//        if (isLoading.get()) return
//        isLoading.set(true)
        compositeDisposable.add(
            dataManager.comment(CommentRequest(feedId, parentId, commentMessage.get().toString()))
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSubmitSuccess, this::onSubmitError)
        )
    }

    private fun onSubmitSuccess(commentListResponse: CommentListResponse) {
        isLoading.set(false)
        commentMessage.set("")
        getCommentList(feedId)
    }

    private fun onSubmitError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }

    fun onReply(replyMessage: String, commentIndex: Int) {
        selectedItem = commentIndex
//        if (!validate()) return
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.comment(CommentRequest(feedId, parentId, replyMessage))
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onReplySubmitSuccess, this::onReplySubmitError)
        )
    }

    private fun onReplySubmitSuccess(commentListResponse: CommentListResponse) {
        isLoading.set(false)
        commentMessage.set("")
        getCommentList(feedId)
    }

    private fun onReplySubmitError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }

    fun validate(): Boolean {
        return when {
            commentMessage.get().toString().isEmpty() -> {
                commentError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_comment_error)
                )
                false
            }
            else -> true
        }
    }

    fun validateReply(): Boolean {
        return when {
            commentMessage.get()!!.trim().isEmpty() -> {
                commentError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_comment_error)
                )
                false
            }
            else -> true
        }
    }

    //Handles the Video Feed Options Menu for comment
    fun onFeedOptionSelected(option: String, videoData: VideoData, position: Int) {
        when (option) {//replace(" ", "").lowercase()
            "Report Feed" -> {
                getNavigator()?.onOptionSelectedClick(videoData, option, position)
            }
            "Report User" -> {
                getNavigator()?.onOptionSelectedClick(videoData, option, position)
            }
            "Block User" -> {
                getNavigator()?.onOptionSelectedClick(videoData, option, position)
            }
        }
    }

    fun likeFeed(likeType: String, feed: VideoData) {
        compositeDisposable.add(
            dataManager.likeVideFeed(
                LikeFeedRequest(
                    feed.id,
                    likeType
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::likeSuccess, this::likeFailure)
        )
    }

    private fun likeSuccess(feed: VideoData) {
        isLoading.set(false)
        getNavigator()?.onFeedLikeSuccess(feed, selectedItem)
    }

    private fun likeFailure(t: Throwable) {
        isLoading.set(false)
    }

    // FollowUser
    fun toggleFollowUser(id: Int) {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.followUser(
                BlockUserRequest(
                    id
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onFollowSuccess, this::onFolllowFailure)
        )
    }

    fun onFollowSuccess(followUpdateResponse: FollowUpdateResponse) {
        isLoading.set(false)
        getNavigator()?.onFollowSuccess(followUpdateResponse.details!!, selectedItem)
        // getNavigator()?.onSuccess(message)
    }

    fun onFolllowFailure(t: Throwable) {
        isLoading.set(false)
    }

    fun afterCommentChange(s: Editable) {
        commentError.set("")
    }

    // Block User
    fun toggleBlockUser(id: Int) {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.blockUser(
                BlockUserRequest(
                    id
                )
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