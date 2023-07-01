package com.yewapp.ui.modules.manageshortvideo.vm

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.google.common.base.Strings.isNullOrEmpty
import com.yewapp.data.network.api.report.ReportedPosts
import com.yewapp.data.network.api.report.ShortReportedVideo
import com.yewapp.ui.modules.managefeeds.vm.ReportedFeedsViewModel
import com.yewapp.utils.popup.PopUpDialog

class ReportedShortVideosItemViewModel(
    val reportedShort: ShortReportedVideo,
    val position: Int,
    val listener: OnReportedShortOptionClickListener
) : ViewModel() {
    val optionsVisibility = ObservableField<Boolean>(true)
    val profileImageVisibility = ObservableField<Boolean>(true)
    val profileImageName = ObservableField<String>("")
    val profileImage = ObservableField<String>("")
    var mainImage = ObservableField<String>("")


    init {
        for (i in reportedShort.files) {
            mainImage.set(i.url)
        }
        if (reportedShort.createdBy.profileImage.isNullOrEmpty()){
            profileImageVisibility.set(false)
            if (reportedShort.createdBy.name.isNotBlank()){
                val arr: Array<String> = reportedShort.createdBy.name.split(" ").toTypedArray()
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
                profileImage.set(reportedShort.createdBy.profileImage)
                profileImageVisibility.set(true)
            }
        } else {
            profileImageVisibility.set(true)
            profileImage.set(reportedShort.createdBy.profileImage)

        }
    }
    interface OnReportedShortOptionClickListener {
        fun onOptionMenuClick(commentId: ShortReportedVideo)
    }

    fun onOptionClick(view: View) {
        PopUpDialog.showRemoveReportPopUp(view, reportedShort.id) {
            listener.onOptionMenuClick(reportedShort)
        }
    }
}