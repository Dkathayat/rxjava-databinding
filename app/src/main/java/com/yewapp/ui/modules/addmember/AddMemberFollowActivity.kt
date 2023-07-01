package com.yewapp.ui.modules.addmember

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.UserList
import com.yewapp.data.network.api.feed.SuggestedUserList
import com.yewapp.databinding.ActivityAddMembersForFeedsBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.addmember.adapter.AllUserAdapter
import com.yewapp.ui.modules.addmember.adapter.SuggestedUserAdapter
import com.yewapp.ui.modules.addmember.vm.ItemAllUserViewModel
import com.yewapp.ui.modules.addmember.vm.ItemSuggestedViewModel
import java.util.*

class AddMemberFollowActivity :
    BaseActivity<AddMemberNavigator, AddMemberViewModel, ActivityAddMembersForFeedsBinding>(),
    AddMemberNavigator, ItemSuggestedViewModel.OnItemClickListener,
    ItemAllUserViewModel.OnItemClickListener {
    private lateinit var allUserAdapter: AllUserAdapter

    private lateinit var suggestedUserAdapter: SuggestedUserAdapter
    var layoutManager = LinearLayoutManager(this)
    var layoutManagerAll = LinearLayoutManager(this)

    var timer: Timer? = null

    override fun getLayout(): Int {
        return R.layout.activity_add_members_for_feeds
    }

    override fun init() {
        bind(AddMemberViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: AddMemberViewModel) {
        viewModel.setNavigator(this)
        viewModel.userLiveList.observe(this, Observer {
//          viewDataBinding.swipeRefreshListener.isRefreshing = false
            suggestedUserAdapter.addItems(it)
        })

        viewModel.allUserLiveList.observe(this, Observer {
            viewDataBinding.swipeRefreshListener.isRefreshing = false
            allUserAdapter.addItems(it)
        })

    }

    override fun onViewBound(viewDataBinding: ActivityAddMembersForFeedsBinding) {
        suggestedUserAdapter =
            SuggestedUserAdapter(
                viewModel.dataManager.getUser().userId ?: return,
                mutableListOf(),
                this
            )
        viewDataBinding.rvSuggested.adapter = suggestedUserAdapter.apply {
            setHasStableIds(true)
        }
        viewDataBinding.rvSuggested.addOnScrollListener(favScrollListener)

        allUserAdapter =
            AllUserAdapter(viewModel.dataManager.getUser().userId ?: return, mutableListOf(), this)
        viewDataBinding.rvAllUser.adapter = allUserAdapter.apply {
            setHasStableIds(true)
        }
        viewDataBinding.rvAllUser.layoutManager = layoutManagerAll
        viewDataBinding.rvAllUser.addOnScrollListener(allScrollListener)
        viewDataBinding.swipeRefreshListener.setOnRefreshListener {
            viewModel.clearList()
            allUserAdapter.clearAllUserItems()
            viewModel.getAllUserList()
        }
        addSearchListener()
        viewModel.getSuggestedUserList()
        viewModel.getAllUserList()
    }

    override fun onClickItem(item: SuggestedUserList) {

        viewModel.followUser(item.userId!!)
    }

    private fun addSearchListener() {
        viewDataBinding.searchEditTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (timer != null) {
                    timer?.cancel()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.searchEdittextValue = p0.toString()
                viewModel.clearList()
                allUserAdapter.clearAllUserItems()
                timer = Timer()
                timer?.schedule(object : TimerTask() {
                    override fun run() {
                        viewModel.getAllUserList()
                    }

                }, 500)
            }

        })
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
            val isLastPage = viewModel.currentPage > viewModel.lastPage

            if (!viewModel.isLoading.get() && !isLastPage) {
                // if (!viewModel.isLoading.get() && viewModel.currentPage <  viewModel.lastPage){
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0 && totalItemCount >= viewModel.perPage
                ) {
                    viewModel.currentPage++
                    viewModel.getSuggestedUserList()
                }
            }
        }
    }

    private val allScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val viewModel = viewModel ?: return
            val visibleItemCount: Int = layoutManagerAll.childCount
            val totalItemCount: Int = layoutManagerAll.itemCount
            val firstVisibleItemPosition: Int = layoutManagerAll.findFirstVisibleItemPosition()
            val isLastPage = viewModel.currentPageAll > viewModel.lastPageAll

            if (!viewModel.isLoading.get() && !isLastPage) {
                // if (!viewModel.isLoading.get() && viewModel.currentPage <  viewModel.lastPage){
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0 && totalItemCount >= viewModel.perPageAll
                ) {
                    viewModel.currentPageAll++
                    viewModel.getAllUserList()
                }
            }
        }
    }

    override fun onClickItem(item: UserList) {
    }

    override fun onFollowClick(item: UserList, position: Int) {

        viewModel.selectedItem = position
        viewModel.followUser(item.userId!!)
        // viewModel.selectedUser=item
    }

    override fun onFollowSuccess(userList: List<UserList>, position: Int) {
        // val userdata=UserList(userList.userId,userList.fullName,userList.roleId,userList.first_name,userList.last_name,userList.email,userList.city,userList.state,userList.country,userList.profileImage,true) //val userId: Int?, val fullName: String?, val roleId: String?,
        // val first_name: String?, val last_name: String?, val email: String?, val city: String?, val state: String?, val country: String?, val profileImage: String?,val followStatus:Boolean=true
        allUserAdapter.updateLikedItem(userList[0], viewModel.selectedItem)
        // clear the suggested user list after follow unfollow the user list in all user listing
        viewModel.clearSuggestedUserList()
        suggestedUserAdapter.clearFavUserItems()
        viewModel.getSuggestedUserList()
    }

}