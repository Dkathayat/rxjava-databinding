package com.yewapp.utils.popup

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.R
import com.yewapp.data.network.api.video.Comment
import com.yewapp.data.network.api.video.Reply
import com.yewapp.data.network.api.video.VideoData
import com.yewapp.utils.popup.adapter.CommentOptionsAdapter
import com.yewapp.utils.popup.adapter.ReplyCommentOptionsAdapter
import com.yewapp.utils.popup.adapter.VideoFeedsOptionsAdapter
import com.yewapp.utils.popup.adapter.VideoFeedsProfileOptionsAdapter


val videoFeedCommentOptions = mutableListOf<String>("Report Comment", "Report User", "Block User")
val videoFeedOptions = mutableListOf<String>("Report Feed", "Report User", "Block User")
val videoFeedProfileOptions = mutableListOf<String>("Report User", "Block User")
val mapBoxMapStyleOptions =
    mutableListOf<String>("MAPBOX_STREETS", "OUTDOORS", "SATELLITE", "LIGHT", "DARK")


class VedioFeedCommentPopUpDialog : View.OnClickListener {

    companion object {
        fun showPopUp(view: View, comment: Comment, listener: (String) -> Unit) {// userId: Int?,
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

//            if (userId == feed.createdBy.id) {
//                recyclerView.adapter = OptionsAdapter(myFeedOptions, feed) {
//                    popupWindow.dismiss()
//                    listener(it)
//                }
//            } else {
            recyclerView.adapter = CommentOptionsAdapter(videoFeedCommentOptions, comment) {
                popupWindow.dismiss()
                listener(it)
                // }
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


        fun showPopUpForReply(
            view: View,
            reply: Reply,
            listener: (String) -> Unit
        ) {// userId: Int?,
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

//            if (userId == feed.createdBy.id) {
//                recyclerView.adapter = OptionsAdapter(myFeedOptions, feed) {
//                    popupWindow.dismiss()
//                    listener(it)
//                }
//            } else {
            recyclerView.adapter = ReplyCommentOptionsAdapter(videoFeedCommentOptions, reply) {
                popupWindow.dismiss()
                listener(it)
                // }
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

        fun showPopUpForVideoFeeds(
            view: View,
            videoData: VideoData,
            listener: (String) -> Unit
        ) {// userId: Int?,
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

//            if (userId == feed.createdBy.id) {
//                recyclerView.adapter = OptionsAdapter(myFeedOptions, feed) {
//                    popupWindow.dismiss()
//                    listener(it)
//                }
//            } else {
            recyclerView.adapter = VideoFeedsOptionsAdapter(videoFeedOptions, videoData) {
                popupWindow.dismiss()
                listener(it)
                // }
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

        // for user profile for video feeds option
        fun showPopUpForProfileVideoFeeds(
            view: View,
            id: Int,
            listener: (String) -> Unit
        ) {// userId: Int?,
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

//            if (userId == feed.createdBy.id) {
//                recyclerView.adapter = OptionsAdapter(myFeedOptions, feed) {
//                    popupWindow.dismiss()
//                    listener(it)
//                }
//            } else {
            recyclerView.adapter = VideoFeedsProfileOptionsAdapter(videoFeedProfileOptions, id) {
                popupWindow.dismiss()
                listener(it)
                // }
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

        fun showPopUpMapbox(view: View, id: Int, listener: (String) -> Unit) {// userId: Int?,
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

//            if (userId == feed.createdBy.id) {
//                recyclerView.adapter = OptionsAdapter(myFeedOptions, feed) {
//                    popupWindow.dismiss()
//                    listener(it)
//                }
//            } else {
            recyclerView.adapter = VideoFeedsProfileOptionsAdapter(mapBoxMapStyleOptions, id) {
                popupWindow.dismiss()
                listener(it)
                // }
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

    override fun onClick(p0: View?) {

    }
}