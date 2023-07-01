package com.yewapp.ui.modules.managenotification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.notification.Notification
import com.yewapp.databinding.ItemNotificationBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.managenotification.vm.ItemNotificationViewModel

class NotificationListAdapter(
    private val notificationList: MutableList<Notification>,
    val onItemClickListener: ItemNotificationViewModel.OnItemClickListener
) :
    BaseRecyclerAdapter<NotificationListAdapter.ViewHolder, Notification>(notificationList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            ItemNotificationViewModel(
                notificationList[position],
                onItemClickListener,
                position
            )
        )

    }


    class ViewHolder(val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemNotificationViewModel) {
            binding.viewModel = viewModel
        }
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
}