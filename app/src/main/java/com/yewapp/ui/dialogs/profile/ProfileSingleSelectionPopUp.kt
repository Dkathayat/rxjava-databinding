package com.yewapp.ui.dialogs.profile

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R

class ProfileSingleSelectionPopUp(
    val list: MutableList<String>,
    val listener: (String) -> Unit
) {

    @SuppressLint("ClickableViewAccessibility")
    fun show(view: View) {
        val inflater = LayoutInflater.from(view.context)
        val popupView = inflater.inflate(R.layout.pop_up_single_selection_layout, null)

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

        recyclerView.adapter = ProfileSingleSelectionAdapter(list) {
            popupWindow.dismiss()
            listener(it)
        }

        //Set the location of the window on the screen
        popupWindow.showAsDropDown(
            view
        );


        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener { _, _ -> //Close the window when clicked
            popupWindow.dismiss()
            true
        }

    }

    class Builder() {
        var sportList: List<String> = mutableListOf()
        var itemListener: (String) -> Unit = {}

        fun setListener(listener: (String) -> Unit): Builder {
            this.itemListener = listener
            return this
        }

        fun addList(list: List<String>): Builder {
            this.sportList = list
            return this
        }

        fun build(): ProfileSingleSelectionPopUp {
            return ProfileSingleSelectionPopUp(sportList as MutableList<String>, itemListener)
        }
    }
}