package com.yewapp.ui.modules.videofeedcomment.navigator

import com.yewapp.data.network.api.video.Comment
import com.yewapp.data.network.api.video.Reply
import com.yewapp.ui.base.BaseNavigator

interface VideoFeedCommentNavigator : BaseNavigator {
    fun onSuccessResult(message: String)
    fun onOptionSelectedClick(comment: Comment, option: String, position: Int)
    fun onReplyOptionSelectedClick(reply: Reply, option: String, position: Int)
    fun onLikeSuccess(comment: Comment, position: Int)
}