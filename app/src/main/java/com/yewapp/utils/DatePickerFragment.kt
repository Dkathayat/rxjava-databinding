package com.yewapp.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment(
    val listener: OnDateSelectedListener,
    val dateString: String = ""
) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        var dialog = DatePickerDialog(context!!, this, year, month, day)

        if (dateString.isNotEmpty()) {
            val splitDate = getDecomposedDate(dateString)
            if (splitDate.isNotEmpty()) {
                dialog = DatePickerDialog(
                    context!!, this, splitDate[2].toInt(),
                    splitDate[1].toInt() - 1, splitDate[0].toInt()
                )
            }
        }

        c.set(year, month, day + 1)
        dialog.datePicker.minDate = c.timeInMillis
        return dialog
    }

    fun getDecomposedDate(dateString: String): List<String> {
        if (dateString.contains("/"))
            return dateString.split("/")
        else if (dateString.contains("-")) {
            return dateString.split("-")
        }

        return mutableListOf()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val dateString = getDateString(dayOfMonth, month + 1, year)
        listener.onDateSelected(dateString)
    }
}

fun getDateString(day: Int, month: Int, year: Int): String {
    return if (month < 10) {
        if (day < 10)
            "0$day/0${month}/$year"
        else
            "$day/0${month}/$year"
    } else {
        if (day < 10)
            "0$day/${month}/$year"
        else
            "$day/${month}/$year"
    }
}

fun getDateString() {
    val cal = Calendar.getInstance()
    val seconds = cal.get(Calendar.SECOND)
    val minutes = cal.get(Calendar.MINUTE)
    val hours = cal.get(Calendar.HOUR)


}

interface OnDateSelectedListener {
    fun onDateSelected(date: String)
}