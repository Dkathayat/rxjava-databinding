package com.yewapp.utils.popup

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.associate.Associate
import com.yewapp.utils.popup.adapter.OptionAssociateAdapter


class PopupAssociateOptions : View.OnClickListener {

    companion object {
        @SuppressLint("ClickableViewAccessibility")
        fun showPopUp(view: View, associate: Associate, userId: Int?, listener: (String) -> Unit) {
            var associateOptions = mutableListOf<String>()

            associateOptions = if (associate.status?.toInt() == 0) {
                mutableListOf<String>(
                    "Migrate Account", "Remove Account", "Edit Account", "Manage Permission",
                    "Activate Account"
                )
            } else {
                mutableListOf<String>(
                    "Migrate Account", "Remove Account", "Edit Account", "Manage Permission",
                    "Deactivate Account"
                )
            }

            val inflater = LayoutInflater.from(view.context)
            val popupView = inflater.inflate(R.layout.pop_up_associate_options, null)

            //Specify the length and width through constants
            val width = ConstraintLayout.LayoutParams.WRAP_CONTENT
            val height = ConstraintLayout.LayoutParams.WRAP_CONTENT

            //Make Inactive Items Outside Of PopupWindow
            val focusable = true

            val recyclerView = popupView.findViewById<RecyclerView>(R.id.rv_options)


            //Create a window with our parameters
            val popupWindow = PopupWindow(popupView, width, height, focusable)
            popupWindow.elevation = 20f

            if (userId == associate.userId) {
                recyclerView.adapter = OptionAssociateAdapter(associateOptions, associate) {
                    popupWindow.dismiss()
                    listener(it)
                }
            } else {
                recyclerView.adapter = OptionAssociateAdapter(feedOptions, associate) {
                    popupWindow.dismiss()
                    listener(it)
                }
            }

            val location = IntArray(2).apply {
                view.getLocationOnScreen(this)
            }

            //Set the location of the window on the screen
            popupWindow.showAsDropDown(
                view,
                5 * view.width - location[0],
                -100,
                Gravity.START
            );

            //Handler for clicking on the inactive zone of the window

            //Handler for clicking on the inactive zone of the window
            popupView.setOnTouchListener { v, event -> //Close the window when clicked
                popupWindow.dismiss()
                true
            }

        }

    }


    override fun onClick(v: View?) {
    }

}