package com.yewapp.ui.modules.comments.navigator

import com.yewapp.data.network.api.video.Comment
import com.yewapp.data.network.api.video.Reply
import com.yewapp.ui.base.BaseNavigator

interface CommentsNavigator : BaseNavigator {
    fun clearList()
    fun onOptionSelectedClick(comment: Comment, selectedOption: String, position: Int)
    fun onReplyOptionSelectedClick(reply: Reply, option: String, position: Int)
    fun onSuccessResult(message: String)
    fun onLikeSuccess(comment: Comment, position: Int)
    fun onBackClick()


}