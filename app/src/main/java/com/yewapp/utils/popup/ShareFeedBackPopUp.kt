package com.yewapp.utils.popup

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.report.ReportReason
import com.yewapp.data.network.api.sports.Sport
import com.yewapp.utils.popup.adapter.ReasonsAdapter
import com.yewapp.utils.popup.adapter.ShareFeedBackReasonAdapter

class ShareFeedBackPopUp(
    private val reasonList: MutableList<Sport>,
    val listener: (Sport) -> Unit
) {
    fun show(view: View) {
        val inflater = LayoutInflater.from(view.context)
        val popupView = inflater.inflate(R.layout.pop_up_report_reason, null)

        //Specify the length and width through constants
        val width = ConstraintLayout.LayoutParams.MATCH_PARENT
        val height = ConstraintLayout.LayoutParams.WRAP_CONTENT

        //Make Inactive Items Outside Of PopupWindow
        val focusable = true

        //Create a window with our parameters
        val popupWindow = PopupWindow(popupView, width, height, focusable)
        popupWindow.elevation = 20f

        val recyclerView = popupView.findViewById<RecyclerView>(R.id.rv_reasons)

        recyclerView.adapter = ShareFeedBackReasonAdapter(reasonList) {
            popupWindow.dismiss()
            listener(it)
        }

        //Set the location of the window on the screen
        popupWindow.showAsDropDown(
            view,
            -100,
            0,
            Gravity.BOTTOM
        );

        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener { _, _ -> //Close the window when clicked
            popupWindow.dismiss()
            true
        }

    }

    class Builder() {
        var reasonList: List<Sport> = mutableListOf()
        var itemListener: (Sport) -> Unit = {}

        fun setListener(listener: (Sport) -> Unit): Builder {
            this.itemListener = listener
            return this
        }

        fun addReasonList(list: List<Sport>): Builder {
            this.reasonList = list
            return this
        }

        fun build(): ShareFeedBackPopUp {
            return ShareFeedBackPopUp(reasonList as MutableList<Sport>, itemListener)
        }
    }
}