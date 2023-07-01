package com.yewapp.ui.modules.dashboard.fragment.feeds.vm

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.feed.*
import com.yewapp.data.network.api.user.BlockUserRequest
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.dashboard.fragment.feeds.navigator.AllFeedsNavigator
import com.yewapp.utils.DateRange
import com.yewapp.utils.getDateRange
import com.yewapp.utils.parseToUTC
import com.yewapp.utils.rx.SchedulerProvider

class AllFeedsViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<AllFeedsNavigator>(dataManager, schedulerProvider) {

    var lastPage: Int = 1
    var currentPage: Int = 1
    var perPage: Int = 10
    var lastPageFav: Int = 1
    var currentPageFav: Int = 1
    var perPageFav: Int = 10
    val feedFromActivityPoints= ObservableField<Boolean>(false)
    val createFeedButtonVisibility = ObservableField<Boolean>(false)

    // values added that get from response
    private val _allFeedList = MutableLiveData<List<Feed>>()
    val allFeedList: LiveData<List<Feed>>
        get() = _allFeedList

    // adding response in this list
    var feedList = mutableListOf<Feed>()

    // values added that get from response
    private val _favUserList = MutableLiveData<List<FavoriteList>>()
    val favUserList: LiveData<List<FavoriteList>>
        get() = _favUserList

    // adding response in this list
    val favList = mutableListOf<FavoriteList>()

    // adding response in this list
    var selectedItem = 0

    var startDate = ""
    var endDate = ""
    var searchString = ""

    var loadMore = ObservableBoolean(false)
    var filterEnabled = ObservableBoolean(false)
    var noFeedVisibility = ObservableBoolean(false)
    var noFavUserVisibility = ObservableBoolean(false)

    var selectedOptionFeedPosition = 0
    var selectedFeed: Feed? = null


    init {
        getFavoriteUserList()
        getFeeds()
    }

    fun onFollowFeedClick(view: View) {
        getNavigator()?.onFollowButtonClick()
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

    //Fetch all the feeds from the Server
    fun getFeeds() {
        if (isLoading.get()) return

        if (feedList.size > 0) {
            loadMore.set(true)
        } else {
            isLoading.set(true)
        }
        compositeDisposable.add(
            dataManager.getFeed(
                searchString, null, startDate, endDate, currentPage
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onFeedSuccess, this::onFeedFailure)
        )
    }

    private fun onFeedSuccess(response: FeedResponse ) {
        isLoading.set(false)
        loadMore.set(false)
        if (response.list.isEmpty() && response.pageData.current_page == 1) {
            noFeedVisibility.set(false)
            createFeedButtonVisibility.set(true)
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
    }

    private fun onFeedFailure(t: Throwable) {
        isLoading.set(false)
        loadMore.set(false)
        getNavigator()?.onError(t)
    }

    fun onFilterClick(view: View) {
        getNavigator()?.onFilterClick()
    }

    //Handles the filter selected event. When the filter is selected, the recycler view is cleared
    //and populated with new list.
    fun onFilterSelected(it: String) {
        clearList()
        noFeedVisibility.set(false)
        filterEnabled.set(true)
        val dateRange = fetchDateRange(it) ?: return
        startDate = dateRange.first
        endDate = dateRange.second
        getFeeds()
    }

    //Clears the existing list from the recycler view
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
                getNavigator()?.onCalendarSelected()
                return null
            }
            else -> {
                filterEnabled.set(false)
                Pair("", "")
            }
        }
    }

    override fun setData(extras: Bundle?) {

    }

    fun saveDateRange(it: androidx.core.util.Pair<Long, Long>) {
        if (it.first == 0L || it.second == 0L) {
            startDate = ""
            endDate = ""
            filterEnabled.set(false)
        } else {
            startDate = it.first.toString().parseToUTC("yyyy-MM-dd")
            endDate = it.second.toString().parseToUTC("yyyy-MM-dd")
            filterEnabled.set(false)
        }

        getFeeds()
    }

    //Handles the Feed Options Menu
    fun onFeedOptionSelected(option: String, feed: Feed, position: Int) {
        selectedOptionFeedPosition = position
        selectedFeed = feed
        if (feed.createdBy!!.id == dataManager.getUser().userId?:return) {
            when (option) {
                "Edit Feed" -> {

                    getNavigator()?.onEditFeedClick(feed)
                }

            }

        } else {
            when (option.replace(" ", "").lowercase()) {
                "follow" -> {

                    toggleFollowUser(feed.createdBy.id)
                }
                "chat" -> {

                }
                "mute" -> {
                    getNavigator()?.muteUnmuteClick(option, feed)
                    // toggleMuteUser(feed.createdBy.id)
                }
                "report" -> {
                    getNavigator()?.launchReportActivity(feed.id)
                }
                "addtofavourite" -> {
                    getNavigator()?.addToFavouriteClick(option, feed)
                    // toggleFavouriteUser(feed.createdBy.id)
                }
                "blockuser" -> {
                    getNavigator()?.blockClick(option, feed)
                    //   toggleBlockUser(feed.createdBy.id)
                }
            }

        }
    }

    // Block User
    fun toggleBlockUser(id: Int) {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.blockUser(
                BlockUserRequest(
                    id
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onBlockSuccess, this::onUserOperationFailure)
        )
    }

    // Favourite User
    fun toggleFavouriteUser(id: Int) {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.favouriteUser(
                BlockUserRequest(
                    id
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onFavouriteSuccess, this::onUserOperationFailure)
        )
    }

    // MuteUser
    fun toggleMuteUser(id: Int) {
        if (isLoading.get()) return
        isLoading.set(true)

        compositeDisposable.add(
            dataManager.muteUser(
                BlockUserRequest(
                    id
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onMuteSuccess, this::onUserOperationFailure)
        )
    }

    // FollowUser
    fun toggleFollowUser(id: Int) {
        if (isLoading.get()) return
        isLoading.set(true)
        compositeDisposable.add(
            dataManager.followUserFeed(
                BlockUserRequest(
                    id
                )
            ).subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::onFollowSuccess, this::onUserOperationFailure)
        )
    }

    fun onFollowSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.userFollowedSuccess()
        getNavigator()?.onSuccess(message)
    }

    fun onMuteSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.userMutedSuccess()
        getNavigator()?.onSuccess(message)
    }

    private fun onBlockSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.userBlockedSuccess()
        getNavigator()?.onSuccess(message)
    }

    private fun onFavouriteSuccess(message: String) {
        isLoading.set(false)
        getNavigator()?.userFavouriteSuccess()
        getNavigator()?.onSuccess(message)
    }

    private fun onUserOperationFailure(t: Throwable) {
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