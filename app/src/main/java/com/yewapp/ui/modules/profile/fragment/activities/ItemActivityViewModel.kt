package com.yewapp.ui.modules.profile.fragment.activities

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.R
import com.yewapp.data.network.api.profile.ActivitiesData
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.LIKES
import com.yewapp.utils.DateUtils
import com.yewapp.utils.popup.PopUpDialog

class ItemActivityViewModel(
    val position: Int,
    val activitiesData: ActivitiesData,
    val listener: OnActivityOptionClickListener
) :
    ViewModel() {

    var drawableStart = R.drawable.ic_walk
    val points = ObservableField<String>("")

    val createdOn = ObservableField<String>("")
    val likeCount = ObservableField<String>("")
    val commentCount = ObservableField<String>("")
    val optionsVisibility = ObservableField<Boolean>(true)
    val profileImageVisibility = ObservableField<Boolean>(true)
    val profileImage = ObservableField<String>("")
    var feedLike = ""
    var activityStartEndDate = ObservableField<String>("")

    val profileImageName = ObservableField<String>("")
    val happyVisibility = ObservableBoolean(false)
    val smileVisibility = ObservableBoolean(false)
    val surprisedVisibility = ObservableBoolean(false)
    val sadVisibility = ObservableBoolean(false)
    val heartVisibility = ObservableBoolean(false)
    var emoId = ObservableField<Int>()

    init {
        drawableStart =
            if (!activitiesData.activityRunning) R.drawable.ic_walk else R.drawable.ic_run

        activityStartEndDate.set(
            "${DateUtils.getDateFromTimeStamp(activitiesData.start_date.toLong())}  -   ${
                DateUtils.getDateFromTimeStamp(activitiesData.end_date.toLong())}"
                )

                likeCount.set(activitiesData.likeCount)
                        commentCount . set (activitiesData.commentCount)

                //  points.set("${activity.points.pointGained}/${activity.points.maxPoints}")
                }

    fun onCommentClick(view: View) {
//            listener.onCommentClick(activitiesData.categoryId.toInt(), position, activitiesData)

    }

    fun onShareFeedClick(view: View) {
        listener.onShareFeedClick(activitiesData.categoryId.toInt())
    }

    fun onLikeClick(view: View) {
       // listener.onLikeClick(activitiesData)

    }

    fun onLikeLongClick(view: View): Boolean {
//            PopUpDialog.showLikesPopUp(view) {
//                listener.onLikeLongClick(it, activitiesData , position)
//            }
        return true
    }

    private fun setEmoVisibility() {
        val uniCode = activitiesData.activityName
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

    fun sharedOnClick(view: View){
        listener.onOptionMenuClick(
            "",
            activitiesData,
            position
        )
    }

}

interface OnActivityOptionClickListener {
    fun onOptionMenuClick(
        option: String,
        filterResponse: ActivitiesData,
        position: Int
    )

    fun onCommentClick(id: Int, position: Int, filterResponse: ActivitiesData)
    fun onShareFeedClick(id: Int)
    fun onLikeClick(filterResponse: ActivitiesData)

    //    fun onLikeClick(id: Int)
    fun onLikeLongClick(id: String, filterResponse: ActivitiesData, position: Int)

}