package com.yewapp.ui.modules.videofeeds.vm

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.R
import com.yewapp.data.network.api.video.VideoData
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.LIKES
import com.yewapp.utils.popup.VedioFeedCommentPopUpDialog

class ItemVideoFeedsViewModel(
//    val phoneContacts: String,
    val userId: Int,
    val item: VideoData,
    val position: Int,
    val listener: OnItemClickListener

) : ViewModel() {

    var number = ObservableField<String>("")
    var image = ObservableField<String>("")
    var likeCount = ObservableField<String>("")
    var commentcount = ObservableField<String>("")
    var sharecount = ObservableField<String>("")
    val profileImageVisibility = ObservableField<Boolean>(true)
    val profileImage = ObservableField<String>("")
    var emoId = ObservableField<Int>(0)
    val followVisibility = ObservableField<Boolean>(true)
    val profileImageName = ObservableField<String>("")

    init {
        if (item.likeCount != 0) {
            likeCount.set(item.likeCount.toString())
        }
        if (item.shareCount != 0) {
            sharecount.set(item.shareCount.toString())

        }
        if (item.commentCount != 0) {
            commentcount.set(item.commentCount.toString())
        }
        if (item.createdBy.profileImage.isNullOrEmpty()) {
            profileImageVisibility.set(false)
            if (!item.createdBy.name.isNullOrBlank()) {
                val arr: Array<String> = item.createdBy.name.split(" ").toTypedArray()
                val fname = arr[0].substring(0, 1).uppercase()
                var lname = ""
                if (arr[1].isNotEmpty()) {
                    lname = arr[1].substring(0, 1).uppercase()
                    profileImageName.set(fname + lname)
                } else {
                    lname = arr[0].substring(1, 2).uppercase()
                    profileImageName.set(fname + lname)
                }
            } else {
                profileImage.set("")
                profileImageVisibility.set(true)
            }
        } else {
            profileImageVisibility.set(true)
            // profileImage.set()
        }
        setEmoVisibility()
        if (item.createdBy.following) {
            followVisibility.set(false)
        } else {
            followVisibility.set(true)
        }
//        if (userId == item.createdBy.id) {
//            followVisibility.set(false)
//        } else {
//            followVisibility.set(true)
//        }
    }

    interface OnItemClickListener {
        fun onClickClose()
        fun onClickFind()
        fun onclickComment(item: VideoData, position: Int)
        fun onClickProfile(item: VideoData)
        fun onClickCamera()
        fun onLike(item: VideoData)
        fun onShare(item: VideoData)
        fun onFollow(item: VideoData)
        fun onOptionMenuClick(option: String, item: VideoData, position: Int)
    }

    fun onClickClose() {
        listener.onClickClose()
    }

    fun onClickFind() {
        listener.onClickFind()
    }

    fun onclickComment() {
        listener.onclickComment(item, position)
    }

    fun onClickProfile() {
        listener.onClickProfile(item)
    }

    fun onClickCamera() {
        listener.onClickCamera()
    }

    fun onLike() {
        listener.onLike(item)
    }

    fun onShare() {
        listener.onShare(item)
    }

    fun onFollowClick() {
        listener.onFollow(item)
    }

    fun onOptionClick(view: View) {
        VedioFeedCommentPopUpDialog.showPopUpForVideoFeeds(view, item) {
            listener.onOptionMenuClick(it, item, position)
        }
    }

    fun setEmoVisibility() {
        val uniCode = item.userLikeUnicode

        emoId.set(
            when (uniCode) {
                LIKES.HAPPY.type -> R.drawable.ic_emo_happy
                LIKES.SAD.type -> R.drawable.ic_emo_sad
                LIKES.SURPRISED.type -> R.drawable.ic_emo_surprised
                LIKES.SMILE.type -> R.drawable.ic_emo_smile
                LIKES.HEART.type -> R.drawable.ic_emo_heart
                LIKES.THUMBS.type -> R.drawable.ic_orange_thumbs_up
                else -> R.drawable.ic_icon_feather_thumbs_up
            }
        )

    }

}
