package com.yewapp.ui.modules.videofeeds.vm

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.R
import com.yewapp.data.network.api.video.Comment
import com.yewapp.ui.modules.videofeedcomment.vm.ItemReplyViewModel
import com.yewapp.utils.getFeedTime
import com.yewapp.utils.popup.VedioFeedCommentPopUpDialog
import java.text.DecimalFormat

class ItemVideoFeedsCommentViewModel(
    val commentItem: Comment,
    val listener: OnItemClickListener,
    var userId: Int,
    var index: Int,
    val replyListener: ItemReplyViewModel.OnReplyItemClickListener//
) : ViewModel() {

    var userName = ObservableField<String>("")
    var number = ObservableField<String>("")
    var profileImage = ObservableField<String>("")
    var replyTextError = ObservableField("")
    var replyText = ObservableField("")
    var date = ObservableField<String>("")
    var replyLayoutVisibility = ObservableField(false)
    val profileImageVisibility = ObservableField<Boolean>(true)
    val profileImageName = ObservableField<String>("")
    val commentCount = ObservableField<String>("")
    val replyCount = ObservableField<String>("")
    val likeCount = ObservableField<String>("")
    var threeDotOptionVisibility = ObservableField(true)

    init {
        if (userId == commentItem.createdBy.id) {
            threeDotOptionVisibility.set(false)
        } else {
            threeDotOptionVisibility.set(true)
        }
        if (commentItem.createdBy.profileImage.isNullOrEmpty()) {
            profileImageVisibility.set(false)
            if (!commentItem.createdBy.name.isNullOrBlank()) {
                val arr: Array<String> = commentItem.createdBy.name.split(" ").toTypedArray()
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
        replyCount.set(if (commentItem.replyCount == 0) "" else "${prettyCount(commentItem.replyCount!!.toInt())}")
        likeCount.set(if (commentItem.likeCount == 0) "" else "${prettyCount(commentItem.likeCount!!.toInt())}")//
        // likeCount.set(prettyCount(commentItem.likeCount!!.toInt()))
        date.set(getFeedTime(commentItem.createdAt.toLong()))
    }

    interface OnItemClickListener {
        fun replyComment(commentItem: Comment, replyText: String, commentIndex: Int)
        fun likeComment(commentItem: Comment, position: Int)
        fun onOptionMenuClick(option: String, commentItem: Comment, position: Int)

    }

    fun onReplyCommentClick() {
        listener.replyComment(commentItem, replyText.get().toString(), index)
    }

    fun likeCommentClick() {
        listener.likeComment(commentItem, index)
    }

    fun onReplyTextChange(s: CharSequence, start: Int, before: Int, count: Int) {
        replyTextError.set("")
    }

    fun isReplyLayoutVisibility() {
        if (replyLayoutVisibility.get() == true) {
            replyLayoutVisibility.set(false)
        } else {
            replyLayoutVisibility.set(true)
        }
    }

    fun onOptionClick(view: View) {
        VedioFeedCommentPopUpDialog.showPopUp(view, commentItem) {
            listener.onOptionMenuClick(it, commentItem, index)
        }
    }

    fun clickView(view: View) {
        var id = view.id

        if (id == R.id.ivComment) {
            var a = 1;
        }

        if (id == R.id.ivLike) {
            var b = 1;
        }

        if (id == R.id.ivShare) {
            var c = 1;
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

    fun countviews(count: Long): String {
        val array = arrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
        val value = Math.floor(Math.log10(count.toDouble())).toInt()
        val base = value / 3
        if (value >= 3 && base < array.size) {
            return DecimalFormat("#0.0").format(
                count / Math.pow(
                    10.0,
                    (base * 3).toDouble()
                )
            ) + array[base]
        } else {
            return DecimalFormat("#,##0").format(count)
        }
    }
}
