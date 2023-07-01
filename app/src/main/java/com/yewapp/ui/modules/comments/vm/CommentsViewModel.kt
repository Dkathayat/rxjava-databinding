package com.yewapp.ui.modules.comments.vm

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.comment.CommentRequest
import com.yewapp.data.network.api.user.BlockUserRequest
import com.yewapp.data.network.api.video.CommentListResponse
import com.yewapp.data.network.api.video.LikeCommentRequest
import com.yewapp.data.network.api.video.Reply
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.comments.navigator.CommentsNavigator
import com.yewapp.ui.modules.dashboard.fragment.feeds.extras.CommentUpdateExtras
import com.yewapp.utils.getCurrentDate
import com.yewapp.utils.rx.SchedulerProvider

class CommentsViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<CommentsNavigator>(dataManager, schedulerProvider) {

    var commentMessage = ObservableField<String>("")
    var feedId = -1
    var parentId = 0
    var position = 0
    var comment = 0
    var commentError = ObservableField<String>("")
    var replyMessage = ObservableField<String>("")
    var replyError = ObservableField<String>("")
    var name = ObservableField<String>("")
    var profileName = ObservableField<String>("")
    var commentTime = ObservableField<String>("")
    val profileImageName = ObservableField<String>("")
    val profileImageVisibility = ObservableField<Boolean>(true)
    val userImage = ObservableField<String>("")
    var date = ObservableField<String>("")
    lateinit var selectedComment: com.yewapp.data.network.api.video.Comment
    var selectedItem = 0

    //    private val emptyCommentList = ObservableBoolean(false)
//    // values added that get from response
//    var _allCommentList = MutableLiveData<List<Comment>>()
//    val allCommentList: LiveData<List<Comment>>
//        get() = _allCommentList
    private val _commentFeedList =
        MutableLiveData<List<com.yewapp.data.network.api.video.Comment>>()
    val commentFeedLiveList: LiveData<List<com.yewapp.data.network.api.video.Comment>> get() = _commentFeedList

    // adding response in this list
    val commentFeedList = mutableListOf<com.yewapp.data.network.api.video.Comment>()

    init {
//        val fullName = "${dataManager.getUser().firstName} ${dataManager.getUser().lastName}"
//        name.set(fullName)
//        val charArray: CharArray = dataManager.getUser().firstName.toString().toCharArray()
//        val n = charArray.size
//        val first = charArray[0]
//        val charSecondArray: CharArray = dataManager.getUser().lastName.toString().toCharArray()
//        val lastNameFirstLetter = charSecondArray[0]
//        var final = first.toString() + lastNameFirstLetter.toString()
//        if (dataManager.getUser().profileImage.isNullOrEmpty()){
//            profileImageVisibility.set(false)
//        }
//        else{
//            profileImageVisibility.set(true)
//        }
//        profileName.set(final)
//        commentTime.set(
//            getCurrentDate()
//        )

        profileName.set(dataManager.getUser().firstName + " " + dataManager.getUser().lastName)
        date.set(getCurrentDate())
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
    }

    fun onCommentChange(s: CharSequence, start: Int, before: Int, count: Int) {
        commentError.set("")
    }

    override fun setData(extras: Bundle?) {
        if (extras != null) {
            feedId = extras.getInt(CommentUpdateExtras.FEED_ID)
            comment = extras.getInt(CommentUpdateExtras.COMMENT_COUNT)
            position = extras.getInt(CommentUpdateExtras.POSITION)
            getCommentList()
        }
    }

//    fun onSubmitClick(view: View) {
//        if (!validate()) return
//
//        if (isLoading.get()) return
//        isLoading.set(true)
//
//        compositeDisposable.add(
//            dataManager.comment(
//                CommentRequest(
//                    feedId, 0, commentMessage.get()!!
//                )
//            ).subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .subscribe(this::onSubmitSuccess, this::onSubmitError)
//        )
//    }
//
//    private fun onSubmitSuccess(message: String) {
//        isLoading.set(false)
//        commentMessage.set("")
//        getCommentList()
//    }
//
//    private fun onSubmitError(t: Throwable) {
//        isLoading.set(false)
//        getNavigator()?.onError(t, false)
//    }

    fun validate(): Boolean {
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

//    fun getCommentList() {
//        isLoading.set(true)
//        compositeDisposable.add(
//            dataManager.commentList(
//                feedId
//            )
//                .subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .subscribe(this::success, this::failure)
//        )
//    }
//
//    private fun clearLists() {
//        _allCommentList.value = mutableListOf()
//        getNavigator()?.clearList()
//    }
//
//    private fun failure(error: Throwable) {
//        isLoading.set(false)
//        getNavigator()?.onError(error, false)
//    }
//
//    private fun success(response: CommentResponse) {
//        isLoading.set(false)
//        clearLists()
//        if (response.list.isNotEmpty()!! && response.list.size!! > 0) {
//            _allCommentList.value = response.list
//
//            emptyCommentList.set(false)
//        } else {
//            emptyCommentList.set(true)
//        }
//    }


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
            comment = response.list.size
            for (i in 0 until response.list.size) {
                if (response.list[i].replyCount != 0) {
                    comment += response.list[i].replyCount!!
                }
            }

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
        selectedItem = commentIndex
        replyMessage.set(replyText)
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


    fun onLikeComment(commentId: Int, position: Int) {
        selectedItem = position
        //  if (isLoading.get()) return
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
        }
        getNavigator()?.onLikeSuccess(selectedComment, selectedItem)
    }

    private fun onLikeCommentError(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t, false)
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
    fun onFeedOptionSelected(
        selectedOption: String,
        comment: com.yewapp.data.network.api.video.Comment,
        position: Int
    ) {
//        selectedOptionFeedPosition = position
//        selectedFeed = feed
        when (selectedOption) {//replace(" ", "").lowercase()
            "Report Comment" -> {
                // getNavigator()?.onSuccess("Report Comment")
                getNavigator()?.onOptionSelectedClick(comment, selectedOption, position)
            }
            "Report User" -> {
                getNavigator()?.onOptionSelectedClick(comment, selectedOption, position)
            }
            "Block User" -> {
                getNavigator()?.onOptionSelectedClick(comment, selectedOption, position)
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

    fun onBackClick(view: View) {
        getNavigator()?.onBackClick()
    }

    // Block User
    fun toggleBlockUser(id: Int) {
        //  if (isLoading.get()) return
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