package com.yewapp.ui.modules.managefeeds.vm

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.comment.ReportedComments
import com.yewapp.data.network.api.feed.Files
import com.yewapp.data.network.api.report.ReportedFeedFile
import com.yewapp.data.network.api.report.ReportedPosts
import com.yewapp.utils.getFeedTime
import com.yewapp.utils.popup.PopUpDialog
import java.text.DecimalFormat
import kotlin.math.log10

//interface OnReportedFeedOptionClickListener {
//    fun onOptionMenuClick(option: String, feed: ReportedPosts, position: Int)
//}

class ReportedFeedsViewModel(
    val reportedFeeds: ReportedPosts,
    val position: Int,
    val listener: OnReportedFeedOptionClickListener
) : ViewModel() {
    val createdOn = ObservableField<String>("")
    val likeCount = ObservableField<String>("")
    val commentCount = ObservableField<String>("")
    val optionsVisibility = ObservableField<Boolean>(true)
    val profileImageVisibility = ObservableField<Boolean>(true)
    val profileImage = ObservableField<String>("")
    var feedImage = ObservableField<String>("")
    val profileImageName = ObservableField<String>("")


    init {
        createdOn.set(getFeedTime(reportedFeeds.createdAt))
//         likeCount.set(if (reportedFeeds.likeCount == 0) "" else "${prettyCount(reportedFeeds.likeCount)}")
//         commentCount.set(if (reportedFeeds.commentCount == 0) "" else "${prettyCount(reportedFeeds.commentCount)}")

            if (reportedFeeds.createdBy.profileImage.isNullOrEmpty()) {
                profileImageVisibility.set(false)
                if (reportedFeeds.createdBy.name.isNotBlank()) {
                    val arr: Array<String> = reportedFeeds.createdBy.name.split(" ").toTypedArray()
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
                    profileImage.set(reportedFeeds.createdBy.profileImage)
                    profileImageVisibility.set(true)
                }
            } else {
                profileImageVisibility.set(true)
                profileImage.set(reportedFeeds.createdBy.profileImage)
            }


    }

    interface OnReportedFeedOptionClickListener {
        fun onOptionMenuClick(commentId: ReportedPosts)
    }

    fun onOptionClick(view: View) {
        PopUpDialog.showRemoveReportPopUp(view, reportedFeeds.id) {
            listener.onOptionMenuClick(reportedFeeds)
        }
    }
}