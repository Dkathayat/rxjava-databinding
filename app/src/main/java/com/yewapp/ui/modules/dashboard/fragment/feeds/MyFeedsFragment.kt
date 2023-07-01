package com.yewapp.ui.modules.dashboard.fragment.feeds

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.feed.FavoriteList
import com.yewapp.data.network.api.feed.Feed
import com.yewapp.data.network.api.feed.Files
import com.yewapp.databinding.FragmentMyFeedsBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.common.DatePicker
import com.yewapp.ui.common.GenericListBottomSheet
import com.yewapp.ui.modules.comments.CommentsActivity
import com.yewapp.ui.modules.dashboard.fragment.feeds.adapter.FavoriteUserAdapter
import com.yewapp.ui.modules.dashboard.fragment.feeds.adapter.FeedAdapter
import com.yewapp.ui.modules.dashboard.fragment.feeds.extras.CommentUpdateExtras
import com.yewapp.ui.modules.dashboard.fragment.feeds.extras.EditFeedExtra
import com.yewapp.ui.modules.dashboard.fragment.feeds.extras.FeedExtras
import com.yewapp.ui.modules.dashboard.fragment.feeds.navigator.MyFeedsNavigator
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.ItemFavoriteUserViewModel
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.ItemFeedsImageViewModel
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.MyFeedsViewModel
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.OnFeedOptionClickListener
import com.yewapp.ui.modules.feed.CreateFeedActivity
import com.yewapp.ui.modules.viewMedia.MediaListExtras
import com.yewapp.ui.modules.viewMedia.ViewImageVideoActivity
import com.yewapp.utils.filterList
import com.yewapp.utils.toJson
import java.util.*

class MyFeedsFragment(override val layoutId: Int = R.layout.fragment_my_feeds) :
    BaseFragment<MyFeedsNavigator, MyFeedsViewModel, FragmentMyFeedsBinding>(), MyFeedsNavigator,
    OnFeedOptionClickListener, ItemFeedsImageViewModel.OnItemClickListener,
    ItemFavoriteUserViewModel.OnItemClickListener {
    private lateinit var favUserAdapter: FavoriteUserAdapter
    var layoutManager = LinearLayoutManager(activity)
    private lateinit var adapter: FeedAdapter

    //  val adapter = FeedAdapter(viewModel?.dataManager?.getUser()?.userId,true,mutableListOf(), this,this)

    var timer: Timer? = null

    override fun onViewModelCreated(viewModel: MyFeedsViewModel) {
        viewModel.setNavigator(this)
        viewModel.allFeedList.observe(this) {
            viewDataBinding.swipeRefreshListener.isRefreshing = false
            adapter.addItems(it)
        }

        viewModel.favUserList.observe(this, Observer {
//            viewDataBinding.swipeRefreshListener.isRefreshing = false
            favUserAdapter.addItems(it)
        })
    }

    override fun onViewBound(viewDataBinding: FragmentMyFeedsBinding) {
        adapter = FeedAdapter(
            viewModel?.dataManager?.getUser()?.userId,
            false,
            mutableListOf(),
            this,
            this
        )

        viewDataBinding.rvFeeds.adapter = adapter
        viewDataBinding.rvFeeds.layoutManager = layoutManager
        viewDataBinding.rvFeeds.addOnScrollListener(scrollListener)
        favUserAdapter =
            FavoriteUserAdapter(viewModel?.dataManager?.getUser()?.userId, mutableListOf(), this)
        viewDataBinding.rvFavUserMyfeed.adapter = favUserAdapter.apply {
            setHasStableIds(true)
        }
        viewDataBinding.rvFavUserMyfeed.addOnScrollListener(favScrollListener)

        viewDataBinding.swipeRefreshListener.setOnRefreshListener {
            viewModel?.noFeedVisibility?.set(false)
            viewModel?.createFeedButtonVisibility?.set(false)
            viewModel?.clearList()
            adapter.clearItems()
            viewModel?.getFeeds()

//            viewModel?.clearFavUserList()
//            favUserAdapter.clearFavUserItems()
//            viewModel?.getFavoriteUserList()
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

    override fun onResume() {
        super.onResume()
        if (viewModel?.createFeedButtonVisibility?.get() == true) {
            viewModel?.getFeeds()
        }

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
                //  if (!viewModel.isLoading.get() && viewModel.currentPage <  viewModel.lastPage){
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
            .setTitleText("Filter Feed")
            .setDataList(filterList ?: mutableListOf())
            .setClickListener {
                adapter?.clearItems()
                viewModel?.onFilterSelected(it)
            }.build().show(requireActivity())
    }

    override fun showCalendar() {
        DatePicker.Builder()
            .isRangeCalendar(true)
            .setRangeListener {
                viewModel?.saveDateRange(it)
            }
            .build().show(childFragmentManager)
    }

    override fun onFeedLikeSuccess(feed: Feed) {
        adapter.updateLikedItem(feed.apply { isWaiting = false }, viewModel?.selectedItem ?: return)
    }

    override fun onCreateFeedClick() {
        startActivity(
            intentProviderFactory.create(
                CreateFeedActivity::class.java,
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
        savedInstanceState: Bundle?
    ): View {
        return bind(MyFeedsViewModel::class.java, inflater, container)
    }

    override fun onOptionMenuClick(option: String, feed: Feed, position: Int) {
        // Toast.makeText(activity, option, Toast.LENGTH_SHORT).show()
        viewModel?.onFeedOptionSelected(option, feed, position)
    }

    override fun onCommentClick(id: Int, position: Int, feed: Feed) {
/*        startActivity(
            intentProviderFactory.create(
                CommentsActivity::class.java,
                CommentExtras.commentExtras {
                    feedId = id
                },
                0
            )
        )*/

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
    @SuppressLint("NotifyDataSetChanged")
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == CommentsActivity.COMMENT_SCREEN) {
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
        viewModel?.likeFeed(
            id, feed
        )
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