package com.yewapp.ui.modules.dashboard.fragment.feeds.vm

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.R
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.feed.*
import com.yewapp.data.network.api.user.BlockUserRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.dashboard.fragment.feeds.navigator.MyFeedsNavigator
import com.yewapp.utils.DateRange
import com.yewapp.utils.getDateRange
import com.yewapp.utils.parseToUTC
import com.yewapp.utils.rx.SchedulerProvider

enum class LIKES(val type: String) {
    THUMBS("U+1245"),
    SURPRISED("U+1F62E"),
    SMILE("U+1F642"),
    HAPPY("U+1F603"),
    SAD("U+1F622"),
    HEART("U+2764")
}

class MyFeedsViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<MyFeedsNavigator>(dataManager, schedulerProvider) {

    var lastPage: Int = 1
    var currentPage: Int = 1
    var perPage: Int = 10
    var lastPageFav: Int = 1
    var currentPageFav: Int = 1
    var perPageFav: Int = 10
    val feedList = mutableListOf<Feed>()
    val createFeedButtonVisibility = ObservableField<Boolean>(false)
    var selectedItem = 0
    var noFeedVisibility = ObservableBoolean(false)

    private val _allFeedList = MutableLiveData<List<Feed>>()
    val allFeedList: LiveData<List<Feed>>
        get() = _allFeedList

    var userId: Int? = null
    var startDate = ""
    var endDate = ""
    var searchString = ""

    // values added that get from response
    private val _favUserList = MutableLiveData<List<FavoriteList>>()
    val favUserList: LiveData<List<FavoriteList>>
        get() = _favUserList

    // adding response in this list
    val favList = mutableListOf<FavoriteList>()

    var noFavUserVisibility = ObservableBoolean(false)
    var loadMore = ObservableBoolean(false)
    var filterEnabled = ObservableBoolean(false)

    override fun setData(extras: Bundle?) {
    }

    init {
        userId = dataManager.getUser().userId
    }

    //Fetch all the feeds from the Server
    fun getFavoriteUserList() {
        if (isLoading.get()) return

//        if (favList.size > 0) {
//            loadMore.set(true)
//        } else {
//            isLoading.set(true)
//        }
        compositeDisposable.add(
            dataManager.getFavoritesUserList(
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onFavListSuccess, this::onFavListFailure)
        )
    }

    private fun onFavListSuccess(response: FavoritesUserResponse) {
        isLoading.set(false)

        if (response.list.isEmpty()) {
            noFavUserVisibility.set(true)
        } else {
            noFavUserVisibility.set(false)
            _favUserList.value = response.list
            favList.addAll(response.list)

            currentPageFav = response.pageData.current_page
            perPageFav = response.pageData.per_page
            lastPageFav = response.pageData.last_page
        }
    }

    private fun onFavListFailure(t: Throwable) {
        isLoading.set(false)
        //loadMore.set(false)
        getNavigator()?.onError(t)
    }

    fun getFeeds() {
        if (isLoading.get()) return

        if (feedList.size > 0) {
            loadMore.set(true)
        } else {
            isLoading.set(true)
        }
        compositeDisposable.add(
            dataManager.getFeed(
                searchString, userId, startDate, endDate, currentPage
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onFeedSuccess, this::onFeedFailure)
        )
    }

    private fun onFeedSuccess(response: FeedResponse) {
        isLoading.set(false)
        loadMore.set(false)
        if (response.list.isEmpty() && filterEnabled.get() && currentPage == 1) {
            noFeedVisibility.set(true)
            createFeedButtonVisibility.set(false)
        } else {
            currentPage++
            noFeedVisibility.set(false)
            createFeedButtonVisibility.set(false)
            _allFeedList.value = response.list
            feedList.addAll(response.list)
            currentPage = response.pageData.current_page
            perPage = response.pageData.per_page
            lastPage = response.pageData.last_page

        }
        if (response.list.isEmpty()) {
            createFeedButtonVisibility.set(true)
            noFeedVisibility.set(false)
        }
    }


    private fun onFeedFailure(t: Throwable) {
        isLoading.set(false)
        loadMore.set(false)
        getNavigator()?.onError(t)
    }

    fun onFilterClick(view: View) {
        getNavigator()?.onFilterClick()
    }

    fun onAddFeedClick(view: View) {
        if (checkUserProfileCompletion(
                dataManager.getResourceProvider()
                    .getString(R.string.complete_profile_use_feed)
            )
        ) getNavigator()?.onCreateFeedClick()
    }

    fun onFilterSelected(it: String) {
        clearList()
        noFeedVisibility.set(false)
        filterEnabled.set(true)
        val dateRange = fetchDateRange(it) ?: return
        startDate = dateRange.first
        endDate = dateRange.second
        getFeeds()
    }

    fun clearList() {
        _allFeedList.value = mutableListOf()
        feedList.clear()
        currentPage = 1
    }

    fun clearFavUserList() {
        _favUserList.value = mutableListOf()
        favList.clear()
        currentPageFav = 1
    }

    private fun fetchDateRange(it: String): Pair<String, String>? {

        return when (it.lowercase().replace(" ", "")) {
            "allfeeds" -> {
                filterEnabled.set(false)
                Pair("", "")
            }
            "today" -> {
                getDateRange(DateRange.CURRENT)
            }
            "lastweek" -> {
                getDateRange(DateRange.LAST_WEEK)
            }
            "lastmonth" -> {
                getDateRange(DateRange.LAST_MONTH)
            }
            "lastyear" -> {
                getDateRange(DateRange.LAST_YEAR)
            }
            "selectdaterange" -> {
                getNavigator()?.showCalendar()
                null
            }
            else -> {
                filterEnabled.set(false)
                Pair("", "")
            }
        }
    }

    fun saveDateRange(it: androidx.core.util.Pair<Long, Long>) {
        if (it.first == 0L || it.second == 0L) {
            startDate = ""
            endDate = ""
            filterEnabled.set(false)
        } else {
            startDate = it.first.toString().parseToUTC("yyyy-MM-dd")
            endDate = it.second.toString().parseToUTC("yyyy-MM-dd")
        }

        getFeeds()
    }

    fun onFeedOptionSelected(option: String, feed: Feed, position:Int) {
        when (option) {
            "Edit Feed" -> {
                getNavigator()?.onEditFeedClick(feed)
            }
        }
    }

    private fun blockUser(id: Int) {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.blockUser(
                BlockUserRequest(
                    id
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onUserOperationSuccess, this::onUserOperationFailure)
        )
    }

    fun onUserOperationSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.onSuccess(message, false)
    }

    fun onUserOperationFailure(t: Throwable) {
        isLoading.set(false)
        getNavigator()?.onError(t)
    }

    fun likeFeed(likeType: String, feed: Feed) {

        compositeDisposable.add(
            dataManager.likeFeed(
                LikeFeedRequest(
                    feed.id,
                    likeType
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::likeSuccess, this::onUserOperationFailure)
        )
    }

    private fun likeSuccess(feed: Feed) {
        isLoading.set(false)
        getNavigator()?.onFeedLikeSuccess(feed)
    }
}