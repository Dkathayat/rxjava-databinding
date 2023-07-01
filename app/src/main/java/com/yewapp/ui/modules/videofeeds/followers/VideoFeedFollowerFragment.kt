package com.yewapp.ui.modules.videofeeds.followers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.follower.MyFollowers
import com.yewapp.databinding.VideofeedFollowerFragmentBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.videofeeds.adapter.VideoFeedFollowerFollowingAdapter
import com.yewapp.ui.modules.videofeeds.extras.UserIdExtras
import com.yewapp.ui.modules.videofeeds.vm.ItemVideoFeedFollowerViewModel

class VideoFeedFollowerFragment(override val layoutId: Int = R.layout.videofeed_follower_fragment) :
    BaseFragment<VideoFeedFollowerNavigator, VideoFeedFollowerViewModel, VideofeedFollowerFragmentBinding>(),
    VideoFeedFollowerNavigator {
    var layoutManager = LinearLayoutManager(activity)

    private lateinit var adapter: VideoFeedFollowerFollowingAdapter

    companion object {
        fun newInstance(userID: Int, userName: String): VideoFeedFollowerFragment {
            //can not send without bundle else will give null pointer error
            val fragment = VideoFeedFollowerFragment()
            val args = Bundle()
//            val extras = UserIdExtras.userIdExtras {
//                userId = userID
//            }
            args.putInt(UserIdExtras.USER_ID, userID)
            args.putString(UserIdExtras.USER_Name, userName)

            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewModelCreated(viewModel: VideoFeedFollowerViewModel) {
        viewModel.setNavigator(this)
    }


    override fun onViewBound(viewDataBinding: VideofeedFollowerFragmentBinding) {
        // viewModel?.getLatestRoutesList()
        adapterInitialize()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bind(VideoFeedFollowerViewModel::class.java, inflater, container)
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
            adapter.setItems(it)
        })
    }

    private fun adapterInitialize() {
        var list = mutableListOf<MyFollowers>()

        adapter = VideoFeedFollowerFollowingAdapter(
            list,
            object : ItemVideoFeedFollowerViewModel.OnItemClickListener {

                override fun onClickItem(item: MyFollowers, position: Int) {
                    viewModel?.selectedItem = position
                    viewModel?.toggleFollowUser(item.userId)
                }
            })
        viewDataBinding.latestRecycler.adapter = adapter
        viewDataBinding.latestRecycler.addOnScrollListener(onScrollListener)
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
                    viewModel.getFollowerList()
                }
            }
        }
    }

    override fun onFollowSuccess(userList: List<MyFollowers>, position: Int) {
        // val userdata=UserList(userList.userId,userList.fullName,userList.roleId,userList.first_name,userList.last_name,userList.email,userList.city,userList.state,userList.country,userList.profileImage,true) //val userId: Int?, val fullName: String?, val roleId: String?,
        // val first_name: String?, val last_name: String?, val email: String?, val city: String?, val state: String?, val country: String?, val profileImage: String?,val followStatus:Boolean=true
        adapter.updateLikedItem(userList[0], viewModel?.selectedItem!!)
    }
}