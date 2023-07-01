package com.yewapp.ui.modules.faqs.vm

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.yewapp.R
import com.yewapp.data.network.api.faqs.FaqData

class ItemsFaqsViewModel(val faq: FaqData) : ViewModel() {

    var isShow = ObservableField(false)


    init {

    }

    fun onActionClick(view: View) {
        when (view.id) {
            R.id.show_more -> {
                isShow.set(!(isShow.get()!!))
            }
        }
    }
}

