package com.yewapp.ui.modules.managefeeds.extras

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.report.ReportedFeedFile
import com.yewapp.data.network.api.report.ReportedPosts

class ReportedImageViewModel(
    val items: ReportedFeedFile,
    val reportedPosts: ReportedPosts,
    var index: Int
):ViewModel() {
    var image = ObservableField<String>()
    val reportedImageVisibility = ObservableBoolean(true)

    init {
        image.set(items.url)
        if (items.type == "image") {
            reportedImageVisibility.set(false)
        } else {
            reportedImageVisibility.set(true)
        }
    }
}