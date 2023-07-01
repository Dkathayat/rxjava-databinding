package com.yewapp.ui.modules.manageuser.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.yewapp.R
import com.yewapp.data.network.api.UserList
import com.yewapp.databinding.FavoriteManageUserFragmentBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.manageuser.adapter.FavoriteUserListAdapter
import com.yewapp.ui.modules.manageuser.navigator.FavouriteFragmentNavigator
import com.yewapp.ui.modules.manageuser.vm.FavoriteManageUserFragmentViewModel
import com.yewapp.ui.modules.manageuser.vm.ItemManageUserViewModel
import com.yewapp.ui.modules.addchallenge.challengeroutes.ManageUserEnum

class FavoriteManageUserFragment(override val layoutId: Int = R.layout.favorite_manage_user_fragment) :
    BaseFragment<FavouriteFragmentNavigator, FavoriteManageUserFragmentViewModel, FavoriteManageUserFragmentBinding>(),
    FavouriteFragmentNavigator {

    private lateinit var favoriteListAdapter: FavoriteUserListAdapter

    override fun onViewModelCreated(viewModel: FavoriteManageUserFragmentViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: FavoriteManageUserFragmentBinding) {
        viewModel?.getFavoriteUserList()
        adapterInitialize()
    }

    override fun onBackPress() {
        TODO("Not yet implemented")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return bind(FavoriteManageUserFragmentViewModel::class.java,inflater,container)
    }

    override fun onStart() {
        super.onStart()
        addObserver()
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel._userList.observe(this, Observer {
            viewDataBinding.swipeRefreshListener.isRefreshing = false
            favoriteListAdapter.setItems(it!!)
        })
    }

    private fun adapterInitialize() {
        val list = mutableListOf<UserList>()
        favoriteListAdapter =
            FavoriteUserListAdapter(list, object : ItemManageUserViewModel.OnItemClickListener {

                override fun onClickItem(item: UserList) {
                    viewModel?.unFavoriteUser(item.userId!!)

                }
            }, ManageUserEnum.FAVORITE.Type)
        viewDataBinding.rvFavorite.adapter = favoriteListAdapter.apply {
            setHasStableIds(true)

        }
        viewDataBinding.swipeRefreshListener.setOnRefreshListener {
            viewModel?.clearList()
            favoriteListAdapter.clearItems()
            viewModel?.getFavoriteUserList()
        }
    }


}