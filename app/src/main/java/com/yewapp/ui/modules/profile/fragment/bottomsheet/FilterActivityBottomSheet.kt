package com.yewapp.ui.modules.profile.fragment.bottomsheet

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mapbox.maps.extension.style.expressions.dsl.generated.indexOf
import com.yewapp.R
import com.yewapp.data.network.api.profile.FilterData
import com.yewapp.ui.modules.profile.adapter.ApplyFilterBottomSheetAdapter

class FilterActivityBottomSheet(
    var title: String,
    var list: MutableList<FilterData>,
    private var applyFilter: (MutableList<FilterData>) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var adapter: ApplyFilterBottomSheetAdapter
    var filterList = mutableListOf<FilterData>()
    fun show(context: Context) {
        val view =
            LayoutInflater.from(context).inflate(R.layout.bottom_sheet_activities_filter, null)
        val dialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
        dialog.setCancelable(false)
        dialog.setContentView(view)

        val bottomSheetBehavior: BottomSheetBehavior<View> =
            BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        val tvTitle = view.findViewById<TextView>(R.id.tv_title_filter)
        val crossIcon = view.findViewById<AppCompatImageView>(R.id.img_close_filter)
        val btnApplyFilter = view.findViewById<Button>(R.id.apply_filter_button)
        val recylerView = view.findViewById<RecyclerView>(R.id.rv_list_filter_profile)

        btnApplyFilter.isEnabled = false
        btnApplyFilter.alpha = 0.5f
        tvTitle.text = title

        val mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = ApplyFilterBottomSheetAdapter(
            list,
            object : ItemBottomSheetFilterViewModel.OnItemClickListener {
                override fun onClickItem(item: FilterData) {
                    filterList.add(item)
                    if (filterList.isEmpty()) {
                        btnApplyFilter.isEnabled = false
                        btnApplyFilter.alpha = 0.5f
                    } else {
                        btnApplyFilter.isEnabled = true
                        btnApplyFilter.alpha = 1f
                    }
                    if (item.filterDatatype.contains("all")) {
                        for (i in list) {
                            i.isSelected = true
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onRemoveItem(removeItem: FilterData) {
                    filterList.remove(removeItem)
                    if (filterList.isEmpty()) {
                        btnApplyFilter.isEnabled = false
                        btnApplyFilter.alpha = 0.5f
                    } else {
                        btnApplyFilter.isEnabled = true
                        btnApplyFilter.alpha = 1f
                    }
                    if (!removeItem.filterDatatype.contains("all")){
                        list[0].isSelected = false
                        adapter.notifyDataSetChanged()
                    }
                }

            })
        recylerView?.isNestedScrollingEnabled = true
        recylerView?.layoutManager = mLayoutManager
        recylerView?.setItemViewCacheSize(50)
        recylerView?.adapter = adapter


        crossIcon.setOnClickListener {
            adapter.clearItem()
            dialog.dismiss()
        }
        btnApplyFilter.setOnClickListener {
            applyFilter(filterList)
            dialog.dismiss()
            adapter.clearItem()
        }

        dialog.show()
    }

    class Builder {
        private var title = ""
        private var filterList = mutableListOf<FilterData>()
        private var btnApplyFilter: (MutableList<FilterData>) -> Unit = {

        }


        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setFilterList(list: MutableList<FilterData>): Builder {
            this.filterList = list
            return this
        }

        fun setFilterButton(listener: (MutableList<FilterData>) -> Unit): Builder {
            this.btnApplyFilter = listener
            return this
        }

        fun build(): FilterActivityBottomSheet {
            return FilterActivityBottomSheet(title, filterList, btnApplyFilter)
        }

        fun dismiss() {
            this.dismiss()
        }

    }
}