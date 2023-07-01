package com.yewapp.ui.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<VH : RecyclerView.ViewHolder, M>(private val items: MutableList<M>) :
    RecyclerView.Adapter<VH>() {
    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<M>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setPersistentItems(items: List<M>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun getItems(): List<M> {
        return items
    }

}