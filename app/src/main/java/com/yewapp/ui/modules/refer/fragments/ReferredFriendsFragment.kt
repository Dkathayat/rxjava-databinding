package com.yewapp.ui.modules.refer.fragments

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.refer.ReferResponse
import com.yewapp.databinding.FragmentReferredFriendsBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.refer.adapter.ReferredFriendsAdapter
import com.yewapp.ui.modules.refer.navigator.ReferredFriendsNavigator
import com.yewapp.ui.modules.refer.vm.ReferredFriendsViewModel


class ReferredFriendsFragment(override val layoutId: Int = R.layout.fragment_referred_friends) :
    BaseFragment<ReferredFriendsNavigator, ReferredFriendsViewModel, FragmentReferredFriendsBinding>(),
    ReferredFriendsNavigator {

    private lateinit var adapter: ReferredFriendsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return bind(ReferredFriendsViewModel::class.java, inflater, container)
    }


    override fun onBackPress() {

    }

    override fun onViewModelCreated(viewModel: ReferredFriendsViewModel) {
        viewModel.setNavigator(this)
        addObserver()
    }

    override fun onViewBound(viewDataBinding: FragmentReferredFriendsBinding) {

        val list = ArrayList<ReferResponse>()
        adapter = ReferredFriendsAdapter(list)
        adapter.setHasStableIds(true)
        viewDataBinding.recyclerRefer.adapter = adapter
        viewDataBinding.recyclerRefer.layoutManager = LinearLayoutManager(activity)

        // item swipe
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val achieveContacts = list[viewHolder.adapterPosition]
                val position = viewHolder.adapterPosition
                list.removeAt(viewHolder.adapterPosition)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }).attachToRecyclerView(viewDataBinding.recyclerRefer)
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel.referredList.observe(viewLifecycleOwner, Observer {
            this.adapter.setItems(it)
        })
    }
}

