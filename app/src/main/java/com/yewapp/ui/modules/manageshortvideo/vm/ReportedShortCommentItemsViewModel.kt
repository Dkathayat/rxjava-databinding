package com.yewapp.ui.modules.manageshortvideo.vm

import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.report.ReportedComment
import com.yewapp.utils.getFeedTime
import com.yewapp.utils.popup.PopUpDialog

class ReportedShortCommentItemsViewModel(
    val reportedShort: ReportedComment,
    val position: Int,
    val listener: OnReportedShortOptionClickListener
) : ViewModel() {
    val optionsVisibility = ObservableField<Boolean>(true)
    val profileImageVisibility = ObservableField<Boolean>(true)
    val profileImageVisibilityComments = ObservableField<Boolean>(true)
    val profileImageName = ObservableField<String>("")
    val profileCommentName = ObservableField<String>("")
    val profileImage = ObservableField<String>("")
    val profileCommentImage = ObservableField<String>("")
    var mainImage = ObservableField<String>("")
    val createdOn = ObservableField<String>("")


    init {
        if (!reportedShort.feedUrl.isNullOrEmpty()) {
            mainImage.set(reportedShort.feedUrl)
        }


        createdOn.set(getFeedTime(reportedShort.updateDate))
        if (reportedShort.profileImage.isNullOrEmpty()) {
            profileImageVisibility.set(false)
            if (reportedShort.createdByName.isNotBlank()) {
                val arr: Array<String> = reportedShort.createdByName.split(" ").toTypedArray()
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
                profileImage.set(reportedShort.profileImage)
                profileImageVisibility.set(true)
            }
        } else {
            profileImageVisibility.set(true)
            profileImage.set(reportedShort.profileImage)

        }
        getCommentedUserInfo()
    }

    private fun getCommentedUserInfo() {
        try {
            if (reportedShort.profileImage.isNullOrEmpty()) {
                profileImageVisibilityComments.set(false)
                if (reportedShort.commentBy.fullName.isNotBlank()) {
                    val arr: Array<String> =
                        reportedShort.commentBy.fullName.split(" ").toTypedArray()
                    val fname = arr[0].substring(0, 1).uppercase()
                    var lname = ""
                    if (arr[1].isNotEmpty()) {
                        lname = arr[1].substring(0, 1).uppercase()
                        profileCommentName.set(fname + lname)
                    } else {
                        lname = arr[0].substring(1, 2).uppercase()
                        profileCommentName.set(fname + lname)
                    }
                } else {
                    profileCommentImage.set(reportedShort.commentBy.profileImage)
                    profileImageVisibilityComments.set(true)
                }
            } else {
                profileImageVisibilityComments.set(true)
                profileCommentImage.set(reportedShort.commentBy.profileImage)
            }
        } catch (e: ArrayIndexOutOfBoundsException){

        }
    }

    interface OnReportedShortOptionClickListener {
        fun onOptionMenuClick(commentId: ReportedComment)
    }

    fun onOptionClick(view: View) {
        PopUpDialog.showRemoveReportPopUp(view, reportedShort.id) {
            listener.onOptionMenuClick(reportedShort)
        }
    }
}