package com.yewapp.ui.modules.dashboard.fragment.feeds.vm

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.feed.Feed
import com.yewapp.data.network.api.feed.FeedLike
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.utils.getFeedTime
import com.yewapp.utils.popup.PopUpDialog
import com.yewapp.utils.toJson
import java.text.DecimalFormat
import kotlin.math.log10

interface OnFeedOptionClickListener {
    fun onOptionMenuClick(option: String, feed: Feed, position: Int)
    fun onCommentClick(id: Int, position: Int, feed: Feed)
    fun onShareFeedClick(id: Int)
    fun onLikeClick(feed: Feed)

    //    fun onLikeClick(id: Int)
    fun onLikeLongClick(id: String, feed: Feed, position: Int)
    fun checkProfileCompletion(): Boolean
}

class ItemFeedViewModel(
    val userId: Int?,
    val myFeed: Boolean,
    val feed: Feed,
    val position: Int,
    val listener: OnFeedOptionClickListener
) : ViewModel() {

    val createdOn = ObservableField<String>("")
    val likeCount = ObservableField<String>("")
    val commentCount = ObservableField<String>("")
    val optionsVisibility = ObservableField<Boolean>(true)
    val profileImageVisibility = ObservableField<Boolean>(true)
    val profileImage = ObservableField<String>("")
    var feedLike =  ""

    val profileImageName = ObservableField<String>("")
    val happyVisibility = ObservableBoolean(false)
    val smileVisibility = ObservableBoolean(false)
    val surprisedVisibility = ObservableBoolean(false)
    val sadVisibility = ObservableBoolean(false)
    val heartVisibility = ObservableBoolean(false)
    var emoId = ObservableField<Int>()

    init {
        createdOn.set(getFeedTime(feed.createdAt))
        likeCount.set(if (feed.likeCount == 0) "" else "${prettyCount(feed.likeCount)}")
        commentCount.set(if (feed.commentCount == 0) "" else "${prettyCount(feed.commentCount)}")
        setEmoVisibility()

        if (feed.createdBy!!.profileImage.isNullOrEmpty()) {
            profileImageVisibility.set(false)
            if (feed.createdBy.name.isNotBlank()) {
                val arr: Array<String> = feed.createdBy.name.split(" ").toTypedArray()
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
                profileImage.set(feed.createdBy.profileImage)
                profileImageVisibility.set(true)
            }
        } else {
            profileImageVisibility.set(true)
            profileImage.set(feed.createdBy.profileImage)
        }
        if (feed.activityId.isNullOrEmpty()) {
            optionsVisibility.set(true)
        } else {
            optionsVisibility.set(false)
        }
    }

    fun onCommentClick(view: View) {
        if (listener.checkProfileCompletion())
            listener.onCommentClick(feed.id, position, feed)

    }

    fun onShareFeedClick(view: View) {
        listener.onShareFeedClick(feed.id)
    }

    fun onOptionClick(view: View) {
        PopUpDialog.showPopUp(view, feed, userId) {
            listener.onOptionMenuClick(it, feed, position)
        }
    }

    fun onLikeClick(view: View) {
        listener.onLikeClick(feed)

    }
    fun onLikeLongClick(view: View): Boolean {
        if (listener.checkProfileCompletion())
            PopUpDialog.showLikesPopUp(view) {
                listener.onLikeLongClick(it, feed , position)
            }
        return true
    }
    private fun prettyCount(number: Number): String? {
        val suffix = charArrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
        val numValue = number.toLong()
        val value = Math.floor(log10(numValue.toDouble())).toInt()
        val base = value / 3
        return if (value >= 3 && base < suffix.size) {
            DecimalFormat("#0.0").format(
                numValue / Math.pow(
                    10.0,
                    (base * 3).toDouble()
                )
            ) + suffix[base]
        } else {
            DecimalFormat("#,##0").format(numValue)
        }
    }
    private fun setEmoVisibility() {
        val uniCode = feed.userLikeUnicode
        emoId.set(
            when (uniCode) {
                LIKES.HAPPY.type -> R.drawable.ic_emo_happy
                LIKES.SAD.type -> R.drawable.ic_emo_sad
                LIKES.SURPRISED.type -> R.drawable.ic_emo_surprised
                LIKES.SMILE.type -> R.drawable.ic_emo_smile
                LIKES.HEART.type -> R.drawable.ic_emo_heart
                LIKES.THUMBS.type -> R.drawable.ic_thumbs_up
                else -> R.drawable.ic_like_feed
            }
        )
    }

}