package com.yewapp.ui.dialogs.challengepopupdialogs

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.sports.Sport
import com.yewapp.ui.dialogs.challengepopupdialogs.adapter.ChallengePopUpAdapter

class ChallengePopUp(
    val sportList: MutableList<Sport>,
    val listener: (Sport) -> Unit
) {




    fun show(view: View) {
        val inflater = LayoutInflater.from(view.context)
        val popupView = inflater.inflate(R.layout.pop_up_report_reason, null)

        //Specify the length and width through constants
        val width = view.width

//        val width = ConstraintLayout.LayoutParams.MATCH_PARENT
        val height = ConstraintLayout.LayoutParams.WRAP_CONTENT

        //Make Inactive Items Outside Of PopupWindow
        val focusable = true

        //Create a window with our parameters
        val popupWindow = PopupWindow(popupView, width, height, focusable)
        popupWindow.elevation = 10f

        val recyclerView = popupView.findViewById<RecyclerView>(R.id.rv_reasons)

        recyclerView.adapter = ChallengePopUpAdapter(sportList) {
            popupWindow.dismiss()
            listener(it)
        }

        //Set the location of the window on the screen
        popupWindow.showAsDropDown(
            view,
            view.x.toInt(),
            view.y.toInt(),
            Gravity.BOTTOM
        )
        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener { _, _ -> //Close the window when clicked
            popupWindow.dismiss()
            true
        }

    }

    class Builder() {
        var sportList: List<Sport> = mutableListOf()
        var itemListener: (Sport) -> Unit = {}

        fun setListener(listener: (Sport) -> Unit): Builder {
            this.itemListener = listener
            return this
        }

        fun addList(list: List<Sport>): Builder {
            this.sportList = list
            return this
        }

        fun build(): ChallengePopUp {
            return ChallengePopUp(sportList as MutableList<Sport>, itemListener)
        }
    }
}