package com.yewapp.ui.modules.dashboard.fragment.feeds.vm

import androidx.lifecycle.ViewModel
import com.yewapp.R
import com.yewapp.data.network.api.feed.FeedLike

class ItemFeedsLikedViewModel(
    var feedsLiked: FeedLike,
    val position: Int,
) : ViewModel() {
    var imageUrl = ""
    var emoji = 0
    var address = ""

    init {
        imageUrl = feedsLiked.createdBy.profileImage
        address = "${feedsLiked.createdBy.city}, ${feedsLiked.createdBy.country}"
        setEmoVisibility()
    }

    fun setEmoVisibility() {

        emoji = when (feedsLiked.likeUnicode) {
            LIKES.HAPPY.type -> R.drawable.ic_emo_happy
            LIKES.SAD.type -> R.drawable.ic_emo_sad
            LIKES.SURPRISED.type -> R.drawable.ic_emo_surprised
            LIKES.SMILE.type -> R.drawable.ic_emo_smile
            LIKES.HEART.type -> R.drawable.ic_emo_heart
            LIKES.THUMBS.type -> R.drawable.ic_thumbs_up
            else -> throw IllegalArgumentException()
        }

    }
}
