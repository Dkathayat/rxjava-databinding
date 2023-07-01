package com.yewapp.ui.modules.managefeeds.vm

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.comment.ReportedComments
import com.yewapp.data.network.api.report.DeleteComment
import com.yewapp.data.network.api.report.GetReportedPost
import com.yewapp.utils.getFeedTime
import com.yewapp.utils.popup.PopUpDialog



class ReportedCommentViewModel(
    val reportedComments: ReportedComments,
    val position: Int,
    val listener: OnReportedFeedCommentsOptionClickListener
) : ViewModel() {

    val createdOn = ObservableField<String>("")
    val likeCount = ObservableField<String>("")
    val commentCount = ObservableField<String>("")
    val optionsVisibility = ObservableField<Boolean>(true)
    val profileImageVisibility = ObservableField<Boolean>(true)
    val profileImageVisibilityComments = ObservableField<Boolean>(true)
    val profileImage = ObservableField<String>("")
    val profileImageName = ObservableField<String>("")
    val commentedByImage = ObservableField<String>("")
    val commentedByImageName = ObservableField<String>("")
    var image = ObservableField<String>()


    init {

//        if (reportedComments.statustype == "image") {
//            reportedImageVisibility.set(false)
//        } else {
//            reportedImageVisibility.set(true)
//        }

        createdOn.set(getFeedTime(reportedComments.createdAt))
        if (reportedComments.createdBy.profileImage.isNullOrEmpty()) {
            profileImageVisibility.set(false)
            if (reportedComments.createdBy.name.isNotBlank()) {
                val arr: Array<String> = reportedComments.createdBy.name.split(" ").toTypedArray()
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
                profileImage.set(reportedComments.createdBy.profileImage)
                profileImageVisibility.set(true)
            }
        } else {
            profileImageVisibility.set(true)
            profileImage.set(reportedComments.createdBy.profileImage)
        }
        getCommentedUserInfo()
    }
    private fun getCommentedUserInfo() {
        if (reportedComments.commentBy.profileImage.isNullOrEmpty()){
            profileImageVisibilityComments.set(false)
            if (reportedComments.commentBy.name.isNotBlank()){
                val nameParts = reportedComments.commentBy.name.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                val firstName = nameParts[0]
                val firstNameChar = firstName[0]
                if (nameParts.size > 1) {
                    val lastName = nameParts[nameParts.size - 1]
                    commentedByImageName.set(firstNameChar.toString()+lastName[0])
                }
            } else {
                commentedByImage.set(reportedComments.commentBy.profileImage)
                profileImageVisibilityComments.set(true)
            }
        } else {
            profileImageVisibilityComments.set(true)
            commentedByImage.set(reportedComments.commentBy.profileImage)
        }
    }

    interface OnReportedFeedCommentsOptionClickListener {
        fun onOptionMenuClick(commentId:ReportedComments)
    }
    fun onOptionClick(view: View) {
        PopUpDialog.showRemoveReportPopUp(view,reportedComments.id) {
            listener.onOptionMenuClick(reportedComments)
        }


    }
}