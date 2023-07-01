package com.yewapp.ui.common

import androidx.core.util.Pair
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.INPUT_MODE_CALENDAR
import com.yewapp.R

val constraintsBuilder =
    CalendarConstraints.Builder()
        .setValidator(DateValidatorPointBackward.now())

fun getMonthConstraint(): CalendarConstraints.Builder {
    val today = MaterialDatePicker.todayInUtcMilliseconds()

    return CalendarConstraints.Builder()
        .setOpenAt(today)
}

class DatePicker(
    val confirmListener: (String) -> Unit,
    val confirmRangeListener: (Pair<Long, Long>) -> Unit,
    val selectRange: Boolean
) {

    fun show(fm: FragmentManager) {


        val datePicker = if (selectRange)
            getDatePicker(selectRange) as MaterialDatePicker<Pair<Long, Long>>
        else
            getDatePicker(selectRange) as MaterialDatePicker<Long>


        datePicker.addOnPositiveButtonClickListener {
            if (selectRange)
                confirmRangeListener(datePicker.selection as Pair<Long, Long>)
            else
                confirmListener(datePicker.selection.toString())
        }

        datePicker.addOnCancelListener {
            if (selectRange)
                confirmRangeListener(Pair(0, 0))
            else
                confirmListener("")
        }

        datePicker.addOnNegativeButtonClickListener {
            if (selectRange)
                confirmRangeListener(Pair(0, 0))
            else
                confirmListener("")
        }
        datePicker.addOnDismissListener {
            if (selectRange)
                confirmRangeListener(Pair(0, 0))
            else
                confirmListener("")
        }
        datePicker.show(fm, "datePicker")
    }


    private fun getDatePicker(isRange: Boolean): DialogFragment {
        return when {
            !isRange -> MaterialDatePicker.Builder.datePicker()
                .setTitleText("Date of Birth")
                .setInputMode(INPUT_MODE_CALENDAR)
                .setCalendarConstraints(constraintsBuilder.build())
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
                .build()

            isRange -> MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select Date Range")
                .setInputMode(INPUT_MODE_CALENDAR)
                .setCalendarConstraints(getMonthConstraint().build())
                .setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .build()

            else -> MaterialDatePicker.Builder.datePicker()
                .setTitleText("Date of Birth")
                .setInputMode(INPUT_MODE_CALENDAR)
                .setCalendarConstraints(constraintsBuilder.build())
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        }


    }

    class Builder() {
        var confirmListener = { date: String -> }
        var confirmRangeListener = { dateRange: Pair<Long, Long> -> }
        var isRange = false

        fun setmConfirmListener(confirmListener: (date: String) -> Unit): Builder {
            this.confirmListener = confirmListener
            return this
        }

        fun setRangeListener(rangeListener: (Pair<Long, Long>) -> Unit): Builder {
            this.confirmRangeListener = rangeListener
            return this
        }

        fun isRangeCalendar(isRange: Boolean): Builder {
            this.isRange = isRange
            return this
        }

        fun build(): DatePicker {
            return DatePicker(
                confirmListener,
                confirmRangeListener,
                isRange
            )
        }
    }
}