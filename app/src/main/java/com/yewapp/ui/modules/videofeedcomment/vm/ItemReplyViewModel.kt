package com.yewapp.ui.modules.videofeedcomment.vm

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.data.network.api.video.Reply
import com.yewapp.utils.getFeedTime
import com.yewapp.utils.popup.VedioFeedCommentPopUpDialog
import java.text.DecimalFormat

class ItemReplyViewModel(
    val actualCommentId: Int,
    var userId: Int,
    val commentItemIndex: Int,
    val item: Reply,
    var index: Int,
    val listener: OnReplyItemClickListener
) ://
    ViewModel() {
    var replyImageUrl = ObservableField("")
    var nameAlbhabet = ObservableField<String>("")
    var replyLayoutVisibility = ObservableField(false)
    var replyTextError = ObservableField("")
    var replyText = ObservableField("")
    val profileImageVisibility = ObservableField<Boolean>(true)
    val profileImageName = ObservableField<String>("")
    var profileImage = ObservableField("")
    val replySpanStart = 0
    var replySpanEnd = 0
    var date = ObservableField("")
    val likeCount = ObservableField<String>("")
    var threeDotOptionVisibility = ObservableField(true)

    init {
        date.set(getFeedTime(item.createdAt.toLong()))
        likeCount.set(if (item.likeCount == 0) "" else "${prettyCount(item.likeCount!!.toInt())}")//
        if (userId == item.createdBy.id) {
            threeDotOptionVisibility.set(false)
        } else {
            threeDotOptionVisibility.set(true)
        }
        if (item.createdBy.profileImage.isNullOrEmpty()) {
            profileImageVisibility.set(false)
            if (!item.createdBy.name.isNullOrBlank()) {
                val arr: Array<String> = item.createdBy.name.split(" ").toTypedArray()
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
                profileImage.set("")
                profileImageVisibility.set(true)
            }
        } else {
            profileImageVisibility.set(true)
            // profileImage.set()
        }

    }

    private fun prettyCount(number: Number): String? {
        val suffix = charArrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
        val numValue = number.toLong()
        val value = Math.floor(Math.log10(numValue.toDouble())).toInt()
        val base = value / 3
        return if (value >= 3 && base < suffix.size) {
            DecimalFormat("#0.0").format(
                numValue / Math.pow(
                    10.0,
                    (base * 3).toDouble()
                )
            ) + suffix[base]
        } else {
            DecimalFormat("#,##0").format(numValue)
        }
    }

    fun isReplyLayoutVisibility() {
        if (replyLayoutVisibility.get() == true) {
            replyLayoutVisibility.set(false)
            replyText.set("")
        } else {

            //   replyText.set("@"+item.profile?.name+" ")//"@"+item.profile?.name
            replyLayoutVisibility.set(true)
        }
    }

    fun onReplyTextChange(s: CharSequence, start: Int, before: Int, count: Int) {
        replyTextError.set("")
    }

    interface OnReplyItemClickListener {
        fun report(commentId: Int, item: Reply, index: Int)
        fun likeReply(commentId: Int, item: Reply, index: Int, commentIndex: Int)
        fun onOptionMenuClick(option: String, item: Reply, position: Int)

    }

    fun onOptionClick(view: View) {
        VedioFeedCommentPopUpDialog.showPopUpForReply(view, item) {
            listener.onOptionMenuClick(it, item, index)
        }
    }

    fun onLikeReply() {
        listener.likeReply(actualCommentId, item, index, commentItemIndex)
    }

    fun onReport() {
        listener.report(actualCommentId, item, index)
    }
}