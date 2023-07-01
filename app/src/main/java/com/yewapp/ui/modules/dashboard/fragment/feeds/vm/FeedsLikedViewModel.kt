package com.yewapp.ui.modules.dashboard.fragment.feeds.vm

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.feed.FeedLike
import com.yewapp.data.network.api.feed.FeedLikeResponse
import com.yewapp.data.network.api.feed.LikeCount
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.dashboard.fragment.feeds.extras.FeedExtras
import com.yewapp.ui.modules.dashboard.fragment.feeds.navigator.FeedsLikedNavigator
import com.yewapp.utils.rx.SchedulerProvider


class FeedsLikedViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<FeedsLikedNavigator>(dataManager, schedulerProvider) {

    var feedId: Int? = null

    // values added that get from response
    private var _likedFeedList = MutableLiveData<List<FeedLike>>()
    val likedFeedList: LiveData<List<FeedLike>>
        get() = _likedFeedList

    val isThumbsUpVisible = ObservableBoolean(false)
    val isHappyVisible = ObservableBoolean(false)
    val isSurpriseVisible = ObservableBoolean(false)
    val isSmileVisible = ObservableBoolean(false)
    val isSadVisible = ObservableBoolean(false)
    val isHeartVisible = ObservableBoolean(false)

    var thumbsCount = ObservableField<String>("0")
    var happyCount = ObservableField<String>("0")
    var surpriseCount = ObservableField<String>("0")
    var smileCount = ObservableField<String>("0")
    var sadCount = ObservableField<String>("0")
    var heartCount = ObservableField<String>("0")

    override fun setData(extras: Bundle?) {
        if (extras != null) {
            feedId = extras.getInt(FeedExtras.FEED_ID)
            getFeedLikes()
        }
    }



    fun getFeedLikes() {
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.feedsLikedList(
                feedId ?: return
            )
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::success, this::failure)
        )
    }

    private fun failure(error: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(error, false)
    }

    private fun success(response: FeedLikeResponse) {
        isLoading.set(false)
        _likedFeedList.value = response.list
        manageLikeCounts(response.countSummary)


    }

    private fun manageLikeCounts(countSummary: LikeCount) {
        thumbsCount.set(
            if (countSummary.thumbsUp == null) {
                "0"
            } else {
                isThumbsUpVisible.set(true)
                countSummary.thumbsUp
            }
        )

        happyCount.set(
            if (countSummary.happy == null) {
                "0"
            } else {
                isHappyVisible.set(true)
                countSummary.happy
            }
        )

        surpriseCount.set(
            if (countSummary.surprised == null) {
                "0"
            } else {
                isSurpriseVisible.set(true)
                countSummary.surprised
            }
        )

        smileCount.set(
            if (countSummary.smile == null) {
                "0"
            } else {
                isSmileVisible.set(true)
                countSummary.smile
            }
        )

        sadCount.set(
            if (countSummary.sad == null) {
                "0"
            } else {
                isSadVisible.set(true)
                countSummary.sad
            }
        )

        heartCount.set(
            if (countSummary.heart == null) {
                "0"
            } else {
                isHeartVisible.set(true)
                countSummary.heart
            }
        )
    }
}