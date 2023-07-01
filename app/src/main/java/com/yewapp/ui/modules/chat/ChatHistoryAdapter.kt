package com.yewapp.ui.modules.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.local.PreferencesHelper
import com.yewapp.data.network.api.chat.TempMessage
import com.yewapp.databinding.ItemChatHistoryBinding
import com.yewapp.ui.base.BaseRecyclerAdapter

class ChatHistoryAdapter(
    val chatHistory: MutableList<TempMessage>,
    val preferencesHelper: PreferencesHelper
) :
    BaseRecyclerAdapter<ChatHistoryAdapter.ChatViewHolder, TempMessage>(chatHistory) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder =
        ChatViewHolder(
            ItemChatHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(ItemChatViewModel(chatHistory[position], position, preferencesHelper))
    }


    class ChatViewHolder(private val binding: ItemChatHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ItemChatViewModel) {
            binding.viewModel = viewModel
        }
    }

    fun addItems(list: List<TempMessage>) {
        chatHistory.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    class DiffCallback(
        private val newMessages: List<TempMessage>,
        private val oldMessages: List<TempMessage>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldMessages[oldItemPosition].timeToken == newMessages[newItemPosition].timeToken
        }

        override fun getOldListSize(): Int = oldMessages.size

        override fun getNewListSize(): Int = newMessages.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            val type =
//                oldMessages[oldItemPosition].getMessageViewType() == newMessages[newItemPosition].getMessageViewType()
//            val sent =
//                oldMessages[oldItemPosition].isUpdated == newMessages[newItemPosition].isUpdated
//            return type && sent
            return false
        }

    }


    fun update(newData: MutableList<TempMessage>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallback(newData, chatHistory))
        diffResult.dispatchUpdatesTo(this)
        chatHistory.clear()
        chatHistory.addAll(newData)
        notifyDataSetChanged()
    }

}
