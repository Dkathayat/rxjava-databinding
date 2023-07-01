package com.yewapp.ui.modules.managenotification.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.yewapp.R
import com.yewapp.data.network.api.notification.Notification
import com.yewapp.databinding.NotificationFragmentBinding
import com.yewapp.ui.base.BaseFragment
import com.yewapp.ui.modules.managenotification.adapter.NotificationListAdapter
import com.yewapp.ui.modules.managenotification.navigator.NotificationFragmentNavigator
import com.yewapp.ui.modules.managenotification.vm.ItemNotificationViewModel
import com.yewapp.ui.modules.managenotification.vm.NotificationFragmentViewModel

class NotificationFragment(override val layoutId: Int = R.layout.notification_fragment) :
    BaseFragment<NotificationFragmentNavigator, NotificationFragmentViewModel, NotificationFragmentBinding>(),
    NotificationFragmentNavigator {

    private lateinit var notificationListAdapter: NotificationListAdapter

    override fun onViewModelCreated(viewModel: NotificationFragmentViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: NotificationFragmentBinding) {
        viewModel?.notificationList?.add(
            Notification(
                1,
                "Admin Created a new challenge with hidden trails. 12 Sep 2021, 11:30 AM",
                ""
            )
        )
        viewModel?.notificationList?.add(
            Notification(
                2,
                "Jio Brandon started a challenge. 12 Sep 2021, 11:30 AM",
                ""
            )
        )
        viewModel?.notificationList?.add(
            Notification(
                3,
                "Jio Brandon started a challenge. 12 Sep 2021, 11:30 AM",
                ""
            )
        )
        viewModel?.notificationList?.add(
            Notification(
                4,
                "Admin Created a new challenge with hidden trails. 12 Sep 2021, 11:30 AM",
                ""
            )
        )
        viewModel?.notificationList?.add(
            Notification(
                5,
                "Admin Created a new challenge with hidden trails. 12 Sep 2021, 11:30 AM",
                ""
            )
        )
        viewModel?._notificationList?.value = viewModel?.notificationList

        adapterInitialize()
    }

    override fun onBackPress() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bind(NotificationFragmentViewModel::class.java, inflater, container)
    }

    override fun onStart() {
        super.onStart()
        addObserver()
    }

    private fun addObserver() {
        val viewModel = viewModel ?: return
        viewModel._notificationList.observe(this, Observer {
            notificationListAdapter.setItems(it)
        })
    }

    private fun adapterInitialize() {
        var list = mutableListOf<Notification>()
        notificationListAdapter =
            NotificationListAdapter(list, object : ItemNotificationViewModel.OnItemClickListener {

                override fun onClickItem(item: Notification) {

                }
            })
        viewDataBinding.rvNotification.adapter = notificationListAdapter
    }
}