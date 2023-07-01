package com.yewapp.ui.modules.dashboard.fragment.feeds

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.feed.FavoriteList
import com.yewapp.data.network.api.feed.Feed
import com.yewapp.data.network.api.feed.Files
import com.yewapp.databinding.FragmentAllFeedsBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.common.DatePicker
import com.yewapp.ui.common.GenericListBottomSheet
import com.yewapp.ui.dialogs.ConfirmationCommonDialog
import com.yewapp.ui.modules.addmember.AddMemberFollowActivity
import com.yewapp.ui.modules.comments.CommentsActivity
import com.yewapp.ui.modules.comments.CommentsActivity.Companion.COMMENT_SCREEN
import com.yewapp.ui.modules.dashboard.fragment.feeds.adapter.FavoriteUserAdapter
import com.yewapp.ui.modules.dashboard.fragment.feeds.adapter.FeedAdapter
import com.yewapp.ui.modules.dashboard.fragment.feeds.extras.CommentUpdateExtras
import com.yewapp.ui.modules.dashboard.fragment.feeds.extras.EditFeedExtra
import com.yewapp.ui.modules.dashboard.fragment.feeds.extras.FeedExtras
import com.yewapp.ui.modules.dashboard.fragment.feeds.navigator.AllFeedsNavigator
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.AllFeedsViewModel
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.ItemFavoriteUserViewModel
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.ItemFeedsImageViewModel
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.OnFeedOptionClickListener
import com.yewapp.ui.modules.feed.CreateFeedActivity
import com.yewapp.ui.modules.report.ReportExtras
import com.yewapp.ui.modules.report.SendReportActivity
import com.yewapp.ui.modules.viewMedia.MediaListExtras
import com.yewapp.ui.modules.viewMedia.ViewImageVideoActivity
import com.yewapp.utils.filterList
import java.util.*

enum class UserAction {
    BLOCK, MUTE, FOLLOW, FAVOURITE
}

class AllFeedsFragment(override val layoutId: Int = R.layout.fragment_all_feeds) :
    BaseFragment<AllFeedsNavigator, AllFeedsViewModel, FragmentAllFeedsBinding>(),
    AllFeedsNavigator, OnFeedOptionClickListener, ItemFeedsImageViewModel.OnItemClickListener,
    ItemFavoriteUserViewModel.OnItemClickListener {

    var layoutManager = LinearLayoutManager(activity)
    private lateinit var adapter: FeedAdapter
    private lateinit var favUserAdapter: FavoriteUserAdapter

    //  val adapter = FeedAdapter(viewModel?.dataManager?.getUser()?.userId,false,mutableListOf(), this,this )
    var timer: Timer? = null
    private lateinit var filesAdapter: com.yewapp.ui.modules.dashboard.fragment.feeds.adapter.FeedsImageAdapter


    override fun onViewModelCreated(viewModel: AllFeedsViewModel) {
        viewModel.setNavigator(this)
        viewModel.allFeedList.observe(this, Observer {
            viewDataBinding.swipeRefreshListener.isRefreshing = false
            adapter.addItems(it)
        })

        viewModel.favUserList.observe(this, Observer {
//            viewDataBinding.swipeRefreshListener.isRefreshing = false
            favUserAdapter.addItems(it)
        })

    }

    override fun onViewBound(viewDataBinding: FragmentAllFeedsBinding) {
        adapter = viewModel?.feedFromActivityPoints?.get()?.let {
            FeedAdapter(
                viewModel?.dataManager?.getUser()?.userId,
                it,
                mutableListOf(),
                this,
                this
            )
        }!!
        viewDataBinding.rvFeeds.adapter = adapter.apply {
            setHasStableIds(true)
        }
        viewDataBinding.rvFeeds.layoutManager = layoutManager
        viewDataBinding.rvFeeds.addOnScrollListener(scrollListener)
        favUserAdapter =
            FavoriteUserAdapter(viewModel?.dataManager?.getUser()?.userId, mutableListOf(), this)
        viewDataBinding.rvFavUser.adapter = favUserAdapter.apply {
            setHasStableIds(true)
        }
        viewDataBinding.rvFavUser.addOnScrollListener(favScrollListener)

        viewDataBinding.swipeRefreshListener.setOnRefreshListener {
            viewModel?.clearList()
            adapter.clearItems()
            viewModel?.getFeeds()


        }
        addSearchListener()
        viewModel?.getFavoriteUserList()
        viewModel?.getFeeds()
    }


    private fun addSearchListener() {
        viewDataBinding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (timer != null) {
                    timer?.cancel()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel?.searchString = p0.toString()
                viewModel?.clearList()
                adapter.clearItems()
                timer = Timer()
                timer?.schedule(object : TimerTask() {
                    override fun run() {
                        viewModel?.getFeeds()
                    }
                }, 500)
            }

        })
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val viewModel = viewModel ?: return
            val visibleItemCount: Int = layoutManager.childCount
            val totalItemCount: Int = layoutManager.itemCount
            val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
            val isLastPage = viewModel.currentPage > viewModel.lastPage

            if (!viewModel.isLoading.get() && !isLastPage) {
                // if (!viewModel.isLoading.get() && viewModel.currentPage <  viewModel.lastPage){
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0 && totalItemCount >= viewModel.perPage
                ) {
                    viewModel.currentPage++
                    viewModel.getFeeds()
                }
            }
        }
    }

    private val favScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val viewModel = viewModel ?: return
            val visibleItemCount: Int = layoutManager.childCount
            val totalItemCount: Int = layoutManager.itemCount
            val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
            val isLastPage = viewModel.currentPageFav > viewModel.lastPageFav

            if (!viewModel.isLoading.get() && !isLastPage) {
                // if (!viewModel.isLoading.get() && viewModel.currentPage <  viewModel.lastPage){
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0 && totalItemCount >= viewModel.perPageFav
                ) {
                    viewModel.currentPageFav++
                    viewModel.getFavoriteUserList()
                }
            }
        }
    }

    override fun onFilterClick() {
        GenericListBottomSheet.Builder<String>()
            .setTitleText(getString(R.string.filter_feed))
            .setDataList(filterList)
            .setClickListener {
                adapter.clearItems()
                viewModel?.onFilterSelected(it)
            }
            .build()
            .show(requireActivity())
    }

    override fun onCalendarSelected() {
        DatePicker.Builder()
            .isRangeCalendar(true)
            .setRangeListener {
                viewModel?.saveDateRange(it)
                viewModel?.filterEnabled?.set(true)
            }
            .build().show(childFragmentManager)
    }

    override fun userBlockedSuccess() {
        adapter.updateItem(
            viewModel?.selectedFeed ?: return,
            viewModel?.selectedOptionFeedPosition ?: return,
            UserAction.BLOCK
        )
    }

    override fun userFavouriteSuccess() {
        adapter.updateItem(
            viewModel?.selectedFeed ?: return,
            viewModel?.selectedOptionFeedPosition ?: return,
            UserAction.FAVOURITE
        )
        viewModel?.clearFavUserList()
        favUserAdapter.clearFavUserItems()
        viewModel?.getFavoriteUserList()
    }

    override fun userMutedSuccess() {
        adapter.updateItem(
            viewModel?.selectedFeed ?: return,
            viewModel?.selectedOptionFeedPosition ?: return,
            UserAction.MUTE
        )
    }

    override fun userFollowedSuccess() {
        adapter.updateItem(
            viewModel?.selectedFeed ?: return,
            viewModel?.selectedOptionFeedPosition ?: return,
            UserAction.FOLLOW
        )
    }

    override fun launchReportActivity(id: Int) {
        ConfirmationCommonDialog.Builder()
            .setDescription(getString(R.string.do_you_want_to_report_this_feed))
            .setTitle(getString(R.string.report))
            .addPositiveListener {
                startActivity(
                    intentProviderFactory.create(
                        SendReportActivity::class.java,
                        ReportExtras.reportExtras {
                            feedId = id
                            option = ""
                        },
                        0
                    )
                )
            }.build().show(requireContext())

    }

    override fun onFeedLikeSuccess(feed: Feed) {
        adapter.updateLikedItem(feed, viewModel?.selectedItem ?: return)
    }

    override fun followClick(option: String, feed: Feed) {

    }

    override fun muteUnmuteClick(option: String, feed: Feed) {
        var message = getString(R.string.do_you_want_to_mute_this_feed)
        var title = ""
        if (feed.createdBy!!.muted) {
            message = "Do you want to mute the notifications?"
            title = "Mute"
        } else {
            message = "Do you want to un-mute the notifications?"
            title = "Un-mute"
        }

        ConfirmationCommonDialog.Builder()
            .setDescription(message)
            .setTitle(title)
            .addPositiveListener {
                viewModel?.toggleMuteUser(feed.createdBy.id)
            }.build().show(requireActivity())
    }

    override fun addToFavouriteClick(option: String, feed: Feed) {
        ConfirmationCommonDialog.Builder()
            .setDescription(getString(R.string.do_you_want_to_add_this_feed_to_favourite))
            .setTitle(option)
            .addPositiveListener {
                viewModel?.toggleFavouriteUser(feed.createdBy!!.id)
            }.build().show(requireActivity())
    }

    override fun blockClick(option: String, feed: Feed) {
        ConfirmationCommonDialog.Builder()
            .setDescription(getString(R.string.do_you_want_to_block_this_feed))
            .setTitle(option)
            .addPositiveListener {
                viewModel?.toggleBlockUser(feed.createdBy!!.id)
            }.build().show(requireActivity())
    }

    override fun onFollowButtonClick() {
        startActivity(
            intentProviderFactory.create(
                AddMemberFollowActivity::class.java,
                null,
                0
            )
        )
    }

    override fun onEditFeedClick(feed: Feed) {
        startActivity(
            intentProviderFactory.create(
                CreateFeedActivity::class.java,
                EditFeedExtra.editFeedExtra { editFeed = feed },  //
                0
            )
        )
    }

    override fun onBackPress() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return bind(AllFeedsViewModel::class.java, inflater, container)
    }

    override fun onOptionMenuClick(option: String, feed: Feed, position: Int) {
        viewModel?.onFeedOptionSelected(option, feed, position)
    }

    override fun onCommentClick(id: Int, position: Int, feed: Feed) {
//        startActivity(
//            intentProviderFactory.create(
//                CommentsActivity::class.java,
//                CommentExtras.commentExtras {
//                    feedId = id
//                },
//                0
//            )
//        )
//    }
        val intent = Intent(activity, CommentsActivity::class.java)
        intent.putExtras(CommentUpdateExtras.commentUpdateExtras {
            feedId = id
            this.position = position
            commentCount = feed.commentCount
        })
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        getResult.launch(intent)
    }

    // Receiver
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == COMMENT_SCREEN) {
                val commentCount = it.data?.getIntExtra("commentCount", 0)
                val position = it.data?.getIntExtra("index", 0)
                println("$commentCount  POSITIONNNNN?>>>>$position")
                viewModel!!.feedList[position!!].commentCount = commentCount!!
                adapter.notifyDataSetChanged()
            }
        }

    override fun onShareFeedClick(id: Int) {

        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_SUBJECT, "YewApp")
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "https://play.google.com/store/apps/details?id=com.wolomi"
        )
        intent.type = "text/plain"
        startActivity(intent)
    }

    override fun onLikeClick(feed: Feed) {
        if (feed.likeCount >= 1) {
            startActivity(
                intentProviderFactory.create(
                    FeedsLikedActivity::class.java,
                    FeedExtras.feedExtras {
                        feedId = feed.id
                    },
                    0
                )
            )
        }
    }

    override fun onLikeLongClick(id: String, feed: Feed, position: Int) {
        viewModel?.selectedItem = position
        adapter.updateLikedItem(feed.apply { isWaiting = true }, position)
        viewModel?.likeFeed(id, feed)

    }

    override fun checkProfileCompletion(): Boolean {
        return viewModel?.checkUserProfileCompletion(
            viewModel?.dataManager?.getResourceProvider()?.getString(
                R.string.complete_profile_use_feed
            ).toString()
        )!!
    }

    override fun onClickItem(item: Feed) {
        val extras = MediaListExtras.mediaListExtras {
            val imagelist = arrayListOf<Files>()
            imagelist.addAll(item.files!!)
            list = imagelist
        }

        startActivity(
            intentProviderFactory.create(
                ViewImageVideoActivity::class.java,
                extras,
                0
            )
        )
    }

    override fun onClickItem(item: FavoriteList) {
    }
}