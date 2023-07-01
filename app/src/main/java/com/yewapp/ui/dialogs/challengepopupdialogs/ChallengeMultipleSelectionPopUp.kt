package com.yewapp.ui.dialogs.challengepopupdialogs

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.yewapp.R
import com.yewapp.data.network.api.createchallenge.StaticMultipleSelection
import com.yewapp.ui.dialogs.challengepopupdialogs.adapter.ChallengeMultipleSelectionAdapter

class ChallengeMultipleSelectionPopUp(
    val list: MutableList<StaticMultipleSelection>,
    val listener: (Int, Boolean) -> Unit,
    val updatedListener: (MutableList<StaticMultipleSelection>) -> Unit
) {

    lateinit var challengeMultipleSelectionAdapter: ChallengeMultipleSelectionAdapter
    var count = 0
    lateinit var view: View

    init {
        for (i in 0 until list.size) {
            if (list[i].status)
                count++
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun show(view: View) {
        this.view = view

        val inflater = LayoutInflater.from(view.context)
        val popupView = inflater.inflate(R.layout.multiple_selection_pop_up, null)

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

        challengeMultipleSelectionAdapter =
            ChallengeMultipleSelectionAdapter(list) { position: Int, isItemSelected: Boolean ->
                list[position].status = isItemSelected
                challengeMultipleSelectionAdapter.notifyDataSetChanged()

                count = 0
                for (j in 0 until list.size) {
                    if (list[j].status)
                        count++
                }
            }

        challengeMultipleSelectionAdapter.setHasStableIds(true)
        recyclerView.adapter = challengeMultipleSelectionAdapter

        val setButton = popupView.findViewById<MaterialButton>(R.id.set_button)

        setButton.setOnClickListener {
            if (count == 0) {
                Toast.makeText(view.context, "Please select Sports level", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            popupWindow.dismiss()
            updatedListener(list)

//            if (list[0].status) {
//                for (i in 0 until list.size) {
//                    list[i].status = true
//                }
//                updatedListener(list)
//            } else {
//                updatedListener(list)
//            }

        }
        //Set the location of the window on the screen
        popupWindow.showAsDropDown(
            view,
            view.x.toInt(),
            view.y.toInt(),
            Gravity.BOTTOM
        );

        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener { _, _ -> //Close the window when clicked
            popupWindow.dismiss()
            true
        }

    }


    class Builder() {
        var sportList: List<StaticMultipleSelection> = mutableListOf()
        var itemListener: (Int, Boolean) -> Unit = { i: Int, b: Boolean -> }
        lateinit var updatedListener: (List<StaticMultipleSelection>) -> Unit

        fun setListener(listener: (Int, Boolean) -> Unit): Builder {
            this.itemListener = listener
            return this
        }

        fun setUpdatedListener(listener: (List<StaticMultipleSelection>) -> Unit): Builder {
            this.updatedListener = listener
            return this
        }

        fun addList(list: List<StaticMultipleSelection>): Builder {
            this.sportList = list
            return this
        }

        fun build(): ChallengeMultipleSelectionPopUp {
            return ChallengeMultipleSelectionPopUp(
                sportList as MutableList<StaticMultipleSelection>,
                itemListener,
                updatedListener
            )
        }
    }
}