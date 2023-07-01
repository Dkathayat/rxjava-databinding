package com.yewapp.ui.modules.comments.vm

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.comment.Comment
import com.yewapp.utils.getFeedTime

class ItemCommentsViewModel(
    val comment: Comment,
    val position: Int
) : ViewModel() {
    var createdAt = ObservableField<String>("")
    var comments = ObservableField<String>("")
    var createdBy = ObservableField<String>("")
    val profileImage = ObservableField<String>("")

    init {
        createdAt.set(getFeedTime(comment.createdAt.toLong()))
    }

}