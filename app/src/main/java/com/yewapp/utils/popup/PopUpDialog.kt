package com.yewapp.utils.popup

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.feed.Feed
import com.yewapp.data.network.api.feed.FeedLike
import com.yewapp.data.network.api.follower.MyFollowers
import com.yewapp.ui.modules.dashboard.fragment.feeds.vm.LIKES
import com.yewapp.utils.popup.adapter.OptionsAdapter
import com.yewapp.utils.popup.adapter.OptionsAdapterMyFollowers

val feedOptions =
    mutableListOf<String>("Follow", "Chat", "Mute", "Report", "Add to Favourite", "Block User")

val myFeedOptions =
    mutableListOf<String>("Edit Feed")

class PopUpDialog : View.OnClickListener {


    companion object {
        @SuppressLint("ClickableViewAccessibility")
        fun showPopUp(view: View, feed: Feed, userId: Int?, listener: (String) -> Unit) {
            val inflater = LayoutInflater.from(view.context)
            val popupView = inflater.inflate(R.layout.pop_up_feed_options, null)

            //Specify the length and width through constants
            val width = ConstraintLayout.LayoutParams.WRAP_CONTENT
            val height = ConstraintLayout.LayoutParams.WRAP_CONTENT

            //Make Inactive Items Outside Of PopupWindow
            val focusable = true

            val recyclerView = popupView.findViewById<RecyclerView>(R.id.rv_options)


            //Create a window with our parameters
            val popupWindow = PopupWindow(popupView, width, height, focusable)
            popupWindow.elevation = 20f

            if (userId == feed.createdBy!!.id) {
                recyclerView.adapter = OptionsAdapter(myFeedOptions, feed) {
                    popupWindow.dismiss()
                    listener(it)
                }
            } else {
                recyclerView.adapter = OptionsAdapter(feedOptions, feed) {
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

        @SuppressLint("MissingInflatedId")
        fun showRemoveReportPopUp(view: View, commentId: Int, listener: (String) -> Unit) {

            val inflater = LayoutInflater.from(view.context)
            val popupView = inflater.inflate(R.layout.remove_reported_comment_dialog, null)

            val width = ConstraintLayout.LayoutParams.WRAP_CONTENT
            val height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            val focusable = true

            val popupWindow = PopupWindow(popupView, width, height, focusable)
            popupWindow.elevation = 20f

            val location = IntArray(2).apply {
                view.getLocationOnScreen(this)
            }

            popupWindow.showAsDropDown(
                view,
                7 * view.width - location[0],
                0,
                Gravity.RIGHT
            )



            popupView.findViewById<TextView>(R.id.remove_reported).setOnClickListener {
                listener.invoke(it.toString())
                popupWindow.dismiss()
            }

            popupView.setOnTouchListener { _, _ -> //Close the window when clicked
                popupWindow.dismiss()
                true
            }

        }

        @SuppressLint("ClickableViewAccessibility")
        fun showFollowerOptionsPopUp(
            view: View,
            myFollowers: MyFollowers,
            listener: (String) -> Unit
        ) {
            val inflater = LayoutInflater.from(view.context)
            val popupView = inflater.inflate(R.layout.pop_up_feed_options, null)

            //Specify the length and width through constants
            val width = ConstraintLayout.LayoutParams.WRAP_CONTENT
            val height = ConstraintLayout.LayoutParams.WRAP_CONTENT

            //Make Inactive Items Outside Of PopupWindow
            val focusable = true

            val recyclerView = popupView.findViewById<RecyclerView>(R.id.rv_options)


            //Create a window with our parameters
            val popupWindow = PopupWindow(popupView, width, height, focusable)
            popupWindow.elevation = 20f

            recyclerView.adapter = OptionsAdapterMyFollowers(feedOptions, myFollowers) {
                popupWindow.dismiss()
                listener(it)
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
            popupView.setOnTouchListener { _, _ -> //Close the window when clicked
                popupWindow.dismiss()
                true
            }

        }

        @SuppressLint("ClickableViewAccessibility", "InflateParams")
        fun showLikesPopUp(view: View, listener: (String) -> Unit) {
            val inflater = LayoutInflater.from(view.context)
            val popupView = inflater.inflate(R.layout.layout_likes_option, null)

            //Specify the length and width through constants
            val width = ConstraintLayout.LayoutParams.WRAP_CONTENT
            val height = ConstraintLayout.LayoutParams.WRAP_CONTENT

            //Make Inactive Items Outside Of PopupWindow
            val focusable = true

            //Create a window with our parameters
            val popupWindow = PopupWindow(popupView, width, height, focusable)
            popupWindow.elevation = 20f

            popupView.findViewById<ImageView>(R.id.img_thumbs_up).setOnClickListener {
                listener(LIKES.THUMBS.type)
                popupWindow.dismiss()

            }

            popupView.findViewById<ImageView>(R.id.img_smile).setOnClickListener {
                listener(LIKES.SMILE.type)
                popupWindow.dismiss()
            }
            popupView.findViewById<ImageView>(R.id.img_sad).setOnClickListener {
                listener(LIKES.SAD.type)
                popupWindow.dismiss()
            }
            popupView.findViewById<ImageView>(R.id.img_happy).setOnClickListener {
                listener(LIKES.HAPPY.type)
                popupWindow.dismiss()
            }
            popupView.findViewById<ImageView>(R.id.img_surprised).setOnClickListener {
                listener(LIKES.SURPRISED.type)
                popupWindow.dismiss()
            }
            popupView.findViewById<ImageView>(R.id.img_heart).setOnClickListener {
                listener(LIKES.HEART.type)
                popupWindow.dismiss()
            }

            //Set the location of the window on the screen
            popupWindow.showAsDropDown(
                view,
                0,
                3 * (-view.height + popupView.height),
                Gravity.TOP
            );

            //Handler for clicking on the inactive zone of the window

            //Handler for clicking on the inactive zone of the window
            popupView.setOnTouchListener { _, _ -> //Close the window when clicked
                popupWindow.dismiss()
                true
            }
        }
    }


    fun showSubSportPopUp(view: View, feed: Feed, listener: (String) -> Unit) {
        val inflater = LayoutInflater.from(view.context)
        val popupView = inflater.inflate(R.layout.pop_up_feed_options, null)

        //Specify the length and width through constants
        val width = ConstraintLayout.LayoutParams.WRAP_CONTENT
        val height = ConstraintLayout.LayoutParams.WRAP_CONTENT

        //Make Inactive Items Outside Of PopupWindow
        val focusable = true

        val recyclerView = popupView.findViewById<RecyclerView>(R.id.rv_options)


        //Create a window with our parameters
        val popupWindow = PopupWindow(popupView, width, height, focusable)
        popupWindow.elevation = 20f

        recyclerView.adapter = OptionsAdapter(feedOptions, feed) {
            popupWindow.dismiss()
            listener(it)
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


    override fun onClick(p0: View?) {

    }
}