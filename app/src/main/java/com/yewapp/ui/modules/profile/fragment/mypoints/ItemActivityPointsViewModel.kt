package com.yewapp.ui.modules.profile.fragment.mypoints

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.yewapp.R
import com.yewapp.data.network.api.mypoint.UserPointSummaryData
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.LIKES
import com.yewapp.utils.DateUtils

class ItemActivityPointsViewModel(
    val position: Int,
    val pointsData: UserPointSummaryData,
    val listener: OnActivityPointOptionClickListener
) {
    var drawableStart = R.drawable.ic_walk
    val points = ObservableField<String>("")

    val createdOn = ObservableField<String>("")
    val likeCount = ObservableField<String>("")
    val commentCount = ObservableField<String>("")
    val optionsVisibility = ObservableField<Boolean>(true)
    val profileImageVisibility = ObservableField<Boolean>(true)
    val profileImage = ObservableField<String>("")
    var feedLike = ""
    val sportActivityIcon = ObservableField<String>("")
    var startEndDateText = ObservableField<String>("")
    val profileImageName = ObservableField<String>("")
    val happyVisibility = ObservableBoolean(false)
    val smileVisibility = ObservableBoolean(false)
    val surprisedVisibility = ObservableBoolean(false)
    val sadVisibility = ObservableBoolean(false)
    val heartVisibility = ObservableBoolean(false)
    var emoId = ObservableField<Int>()


    init {
        drawableStart = if (pointsData.sport_name == "running") R.drawable.ic_walk else R.drawable.ic_run
        startEndDateText.set(
            "${DateUtils.getDateFromTimeStamp(pointsData.start_date.toLong())}  -   ${
                DateUtils.getDateFromTimeStamp(pointsData.end_date.toLong())}"
        )
        likeCount.set(pointsData.likeCount)
        commentCount.set(pointsData.commentCount)
        setEmoVisibility()
    }

    private fun setEmoVisibility() {
        val uniCode = pointsData.userLikeUnicode
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

    fun onCommentClick(view: View) {
        if (listener.checkProfileCompletion())
            listener.onCommentClick(pointsData.user_id.toInt(), position, pointsData)

    }
    fun onShareFeedClick(view: View) {
        listener.onOptionMenuClick(
            "",
            pointsData,
            position
        )
    }

    fun onLikeClick(view: View) {
//        listener.onLikeClick(pointsData)

    }

    fun onLikeLongClick(view: View): Boolean {
//        if (listener.checkProfileCompletion())
//            PopUpDialog.showLikesPopUp(view) {
//                listener.onLikeLongClick(it, feed , position)
//            }
        return true
    }
}

interface OnActivityPointOptionClickListener {
    fun onOptionMenuClick(
        option: String,
        filterResponse: UserPointSummaryData,
        position: Int
    )

    fun onCommentClick(id: Int, position: Int, filterResponse: UserPointSummaryData)
    fun onShareFeedClick(id: Int)
    fun onLikeClick(filterResponse: UserPointSummaryData)

    //    fun onLikeClick(id: Int)
    fun onLikeLongClick(id: String, filterResponse: UserPointSummaryData, position: Int)
    fun checkProfileCompletion(): Boolean
}