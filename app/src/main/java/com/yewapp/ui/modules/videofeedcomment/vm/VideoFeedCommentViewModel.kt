package com.yewapp.ui.modules.videofeedcomment.vm

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.comment.CommentRequest
import com.yewapp.data.network.api.user.BlockUserRequest
import com.yewapp.data.network.api.video.Comment
import com.yewapp.data.network.api.video.CommentListResponse
import com.yewapp.data.network.api.video.LikeCommentRequest
import com.yewapp.data.network.api.video.Reply
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.videofeedcomment.extras.VideoFeedIdExtra
import com.yewapp.ui.modules.videofeedcomment.navigator.VideoFeedCommentNavigator
import com.yewapp.utils.rx.SchedulerProvider

class VideoFeedCommentViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<VideoFeedCommentNavigator>(dataManager, schedulerProvider) {
    private val _commentFeedList = MutableLiveData<List<Comment>>()
    val commentFeedLiveList: LiveData<List<Comment>> get() = _commentFeedList

    // adding response in this list
    val commentFeedList = mutableListOf<Comment>()
    var username = ObservableField<String>("")
    val profileImageVisibility = ObservableField<Boolean>(true)
    val userImage = ObservableField<String>("")
    val profileImageName = ObservableField<String>("")
    var feedId = -1
    var parentId = 0
    var position = 0
    var comment = 0
    var replyMessage = ObservableField<String>("")
    var replyError = ObservableField<String>("")
    var date = ObservableField<String>("")
    var commentMessage = ObservableField<String>("")
    var commentError = ObservableField<String>("")
    lateinit var selectedComment: Comment
    var selectedItem = 0

    override fun setData(extras: Bundle?) {
        username.set(dataManager.getUser().firstName + " " + dataManager.getUser().lastName)

        if (dataManager.getUser().profileImage.isNullOrEmpty()) {
            profileImageVisibility.set(false)
            if (!dataManager.getUser().firstName.isNullOrBlank()) {
                val fname = dataManager.getUser().firstName!!.substring(0, 1).uppercase()
                var lname = dataManager.getUser().lastName!!.substring(0, 1).uppercase()
                profileImageName.set(fname + lname)

            } else {
                userImage.set("")
                profileImageVisibility.set(true)
            }
        } else {
            profileImageVisibility.set(true)
            // profileImage.set()
        }
        //date.set(TimeUtils.getCurrentDate())
        feedId = extras?.getInt(VideoFeedIdExtra.FEED_ID)!!
//        added for comment count
        comment = extras.getInt(VideoFeedIdExtra.COMMENT_COUNT)
        position = extras.getInt(VideoFeedIdExtra.POSITION)

        getCommentList()
    }

    fun getCommentList() {
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

    fun onAddCommentClick(view: View) {
        if (!validate()) return
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.comment(CommentRequest(feedId, parentId, commentMessage.get().toString()))
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onSubmitSuccess, this::onSubmitError)
        )
    }

    private fun onSubmitSuccess(commentListResponse: CommentListResponse) {
        isLoading.set(false)
        comment += 1
        getNavigator()?.onSuccessResult(
            dataManager.getResourceProvider().getString(R.string.comment_added_successfull)
        )
        commentMessage.set("")
        // getCommentList()
    }

    private fun onSubmitError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }

    fun clearList() {
        _commentFeedList.value = mutableListOf()
        commentFeedList.clear()
    }

    fun onReply(replyText: String, commentIndex: Int) {
        replyMessage.set(replyText)
        selectedItem = commentIndex
        if (!validateReply()) return
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.comment(CommentRequest(feedId, parentId, replyMessage.get().toString()))
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onReplySubmitSuccess, this::onReplySubmitError)
        )
    }

    private fun onReplySubmitSuccess(commentListResponse: CommentListResponse) {
        isLoading.set(false)
        replyMessage.set("")
        parentId = 0
        comment += 1
        if (commentListResponse.list.isNotEmpty()) {
            selectedComment = commentListResponse.list[0]
        }
        getNavigator()?.onLikeSuccess(selectedComment, selectedItem)
        // getNavigator()?.onSuccessResult(message)

    }

    private fun onReplySubmitError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
    }

    fun onLikeComment(commentId: Int, position: Int, commentIndex: Int) {
        selectedItem = commentIndex
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.likeComment(LikeCommentRequest(commentId))
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onLikeCommentSuccess, this::onLikeCommentError)
        )
    }

    private fun onLikeCommentSuccess(commentListResponse: CommentListResponse) {
        isLoading.set(false)
        if (commentListResponse.list.isNotEmpty()) {
            selectedComment = commentListResponse.list[0]
            getNavigator()?.onLikeSuccess(selectedComment, selectedItem)

        }
        //getNavigator()?.onLikeSuccess(selectedComment,selectedItem)
    }

    private fun onLikeCommentError(t: Throwable) {
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

    private fun validateReply(): Boolean {
        return when {
            replyMessage.get().toString().isEmpty() -> {
                replyError.set(
                    dataManager.getResourceProvider().getString(R.string.empty_comment_error)
                )
                false
            }
            else -> true
        }
    }


    //Handles the Feed Options Menu for comment
    fun onFeedOptionSelected(option: String, comment: Comment, position: Int) {
//        selectedOptionFeedPosition = position
//        selectedFeed = feed
        when (option) {//replace(" ", "").lowercase()
            "Report Comment" -> {
                // getNavigator()?.onSuccess("Report Comment")
                getNavigator()?.onOptionSelectedClick(comment, option, position)
            }
            "Report User" -> {
                getNavigator()?.onOptionSelectedClick(comment, option, position)
            }
            "Block User" -> {
                getNavigator()?.onOptionSelectedClick(comment, option, position)
            }
        }
    }

    //Handles the Feed Options Menu for comment
    fun onReplyOptionSelected(option: String, reply: Reply, position: Int) {
//        selectedOptionFeedPosition = position
//        selectedFeed = feed
//
        when (option) {//replace(" ", "").lowercase()
            "Report Comment" -> {
                // getNavigator()?.onSuccess("Report Comment")
                getNavigator()?.onReplyOptionSelectedClick(reply, option, position)
            }
            "Report User" -> {
                getNavigator()?.onReplyOptionSelectedClick(reply, option, position)
            }
            "Block User" -> {
                getNavigator()?.onReplyOptionSelectedClick(reply, option, position)
            }
        }
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
        getNavigator()?.onSuccessResult(message)
        //  getNavigator()?.onSuccess(message)
    }

    private fun onUserOperationFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t)
    }


}