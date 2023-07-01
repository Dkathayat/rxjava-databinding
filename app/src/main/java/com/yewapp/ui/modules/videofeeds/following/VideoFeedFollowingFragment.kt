package com.yewapp.ui.modules.videofeeds.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.follower.MyFollowers
import com.yewapp.databinding.VideofeedFollowingFragmentBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.videofeeds.adapter.VideoFeedFollowerFollowingAdapter
import com.yewapp.ui.modules.videofeeds.extras.UserIdExtras
import com.yewapp.ui.modules.videofeeds.vm.ItemVideoFeedFollowerViewModel

class VideoFeedFollowingFragment(override val layoutId: Int = R.layout.videofeed_following_fragment) :
    BaseFragment<VideoFeedFollowingNavigator, VideoFeedFollowingViewModel, VideofeedFollowingFragmentBinding>(),
    VideoFeedFollowingNavigator {

    private lateinit var videoFeedFollowerFollowingAdapter: VideoFeedFollowerFollowingAdapter
    var layoutManager = LinearLayoutManager(activity)

    companion object {
        fun newInstance(userID: Int, userName: String): VideoFeedFollowingFragment {
            //can not send without bundle else will give null pointer error
            val fragment = VideoFeedFollowingFragment()
            val args = Bundle()
            args.putInt(UserIdExtras.USER_ID, userID)
            args.putString(UserIdExtras.USER_Name, userName)

//            args.putAll(extras)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewModelCreated(viewModel: VideoFeedFollowingViewModel) {
        viewModel.setNavigator(this)
    }


    override fun onViewBound(viewDataBinding: VideofeedFollowingFragmentBinding) {
//        viewModel?.getLatestRoutesList()
        adapterInitialize()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bind(VideoFeedFollowingViewModel::class.java, inflater, container)
    }

    override fun onBackPress() {

    }

    override fun onStart() {
        super.onStart()
        addObserver()
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.latestRouteList.observe(this, Observer {
            videoFeedFollowerFollowingAdapter.setItems(it)
        })
    }

    private fun adapterInitialize() {
        var list = mutableListOf<MyFollowers>()

        videoFeedFollowerFollowingAdapter = VideoFeedFollowerFollowingAdapter(
            list,
            object : ItemVideoFeedFollowerViewModel.OnItemClickListener {

                override fun onClickItem(item: MyFollowers, position: Int) {
                    viewModel?.selectedItem = position
                    viewModel?.followUser(item.userId)
                }
            })
        viewDataBinding.latestRecycler.adapter = videoFeedFollowerFollowingAdapter
        viewDataBinding.latestRecycler!!.addOnScrollListener(onScrollListener)
    }

    val onScrollListener = object : RecyclerView.OnScrollListener() {
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
                    viewModel.getFollowingUserList()
                }
            }
        }
    }

    override fun onFollowSuccess(userList: List<MyFollowers>, position: Int) {
        // val userdata=UserList(userList.userId,userList.fullName,userList.roleId,userList.first_name,userList.last_name,userList.email,userList.city,userList.state,userList.country,userList.profileImage,true) //val userId: Int?, val fullName: String?, val roleId: String?,
        // val first_name: String?, val last_name: String?, val email: String?, val city: String?, val state: String?, val country: String?, val profileImage: String?,val followStatus:Boolean=true
        videoFeedFollowerFollowingAdapter.updateLikedItem(userList[0], viewModel?.selectedItem!!)
    }
}