package com.yewapp.ui.modules.profile.fragment.bottomsheet

import android.content.Context
import android.os.Build
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.ObservableField
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shuhart.materialcalendarview.*
import com.shuhart.materialcalendarview.utils.CalendarUtils
import com.yewapp.R
import com.yewapp.data.network.api.profile.FilterData
import org.joda.time.LocalDate
import java.text.SimpleDateFormat
import java.util.*

class DateFilterActivitesBottomSheet(
    context: Context,
    var title: String,
    private var applyFilter: (startDate: String, endDate: String) -> Unit
) : BottomSheetDialogFragment(), OnDateSelectedListener, OnMonthChangedListener,
    OnRangeSelectedListener {
    private val view =
        LayoutInflater.from(context).inflate(R.layout.bottom_sheet_activity_datepicker_filter, null)
    val calendarView =
        view.findViewById<MaterialCalendarView>(R.id.calendarView2)
    val monthYear = view.findViewById<TextView>(R.id.tvMonthYear)
    val ivMonthYear = view.findViewById<ImageView>(R.id.ivMonthYear)
    val btnApplyFilter = view.findViewById<Button>(R.id.apply_filter_button)
    val monthSpinner = view.findViewById<AppCompatSpinner>(R.id.monthSpinner)
    val yearSpinner = view.findViewById<AppCompatSpinner>(R.id.yearSpinner)
    var isMonthYearSpinnerVisible = false
    val year = ObservableField<String>("${Calendar.getInstance().get(Calendar.YEAR)}")
    private var yearList = arrayListOf<String>()
    private val formatter = SimpleDateFormat("MMMM yyyy")
    val monthDate = SimpleDateFormat("MMMM yyyy")
    var monthList = arrayListOf<String>()
    val month = ObservableField<String>("")
    var startDate: String = ""
    var endDate: String = ""

    private var allMonths = arrayOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
    )

    private val selectedDatesString: String
        get() {
            val dates = calendarView.selectedDates
            if (dates.isNotEmpty()) {
                return formatted(start = dates.first(), end = dates.last())
            }
            val date =
                calendarView.selectedDate ?: return context?.getString(R.string.lifetime) ?: ""
            return formatted(date)
        }

    fun show(context: Context) {

        val dialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
        dialog.setCancelable(false)
        dialog.setContentView(view)

        val bottomSheetBehavior: BottomSheetBehavior<View> =
            BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        val tvTitle = view.findViewById<TextView>(R.id.tv_title_date)
        val crossIcon = view.findViewById<AppCompatImageView>(R.id.img_close_filter)

        val calender = Calendar.getInstance()
        btnApplyFilter.alpha = 0.5f
        btnApplyFilter.isEnabled = false

        calendarView.addOnDateChangedListener(this)
        calendarView.addOnMonthChangedListener(this)
        calendarView.addOnRangeSelectedListener(this)
        calendarView.selectionMode = MaterialCalendarView.SELECTION_MODE_RANGE

        calendarView.monthIndicatorVisible = false
        monthYear.text = monthDate.format(calender.time)

        initializeYearAdapter(context, yearSpinner)
        monthSpinner.visibility = View.GONE
        yearSpinner.visibility = View.GONE

        ivMonthYear.setOnClickListener {
            if (!isMonthYearSpinnerVisible) {
                ivMonthYear.animate().rotation(180f).setDuration(250).start()
                monthSpinner.visibility = View.VISIBLE
                yearSpinner.visibility = View.VISIBLE
                calendarView.visibility = View.INVISIBLE
            } else {
                ivMonthYear.animate().rotation(0f).setDuration(250).start()
                monthSpinner.visibility = View.GONE
                yearSpinner.visibility = View.GONE
                calendarView.visibility = View.VISIBLE
            }
            isMonthYearSpinnerVisible = !isMonthYearSpinnerVisible
        }


        val instance: LocalDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        monthYear.text = monthDate.format(calender.time)
        initializeYearAdapter(context, yearSpinner)

        calendarView.state()?.edit()?.setMaximumDate(
            CalendarDay.from(
                instance.year, instance.monthOfYear - 1, instance.dayOfMonth
            )
        )?.commit()
        calendarView.showOtherDates = MaterialCalendarView.SHOW_OUT_OF_RANGE

        tvTitle.text = title

        crossIcon.setOnClickListener {
            dialog.dismiss()
        }

        btnApplyFilter.setOnClickListener {
            applyFilter(startDate, endDate)
            dialog.dismiss()
        }


        dialog.show()
    }

    private fun initializeYearAdapter(context: Context, yearSpinner: AppCompatSpinner) {
        for (i in 0 until 5) {
            yearList.add("${Calendar.getInstance().get(Calendar.YEAR) + i}")
        }

        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(context, R.layout.spinner_item_date_time, yearList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner.adapter = arrayAdapter
        yearSpinner.setSelection(0)
        yearSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View, position: Int, id: Long
                ) {
                    year.set(yearList[position])
                    monthList.clear()

                    initializeMonthAdapter(context)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun initializeMonthAdapter(context: Context) {
        val monthSpinner = view.findViewById<AppCompatSpinner>(R.id.monthSpinner)
        val monthYear = view.findViewById<TextView>(R.id.tvMonthYear)
        val calendarView =
            view.findViewById<MaterialCalendarView>(R.id.calendarView2)
        monthList.clear()
        //Set month with respect to years
        for (i in allMonths.indices) {
            if (year.get()?.toInt()!! <= Calendar.getInstance().get(Calendar.YEAR)) {
                if (i >= Calendar.getInstance().get(Calendar.MONTH)) {
                    monthList.add(allMonths[i])
                }
            } else {
                monthList.add(allMonths[i])
            }
        }

        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(context, R.layout.spinner_item_date_time, monthList)

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        monthSpinner.adapter = arrayAdapter
        monthSpinner.setSelection(0)

        monthSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View, position: Int, id: Long
                ) {
//                    if (position != 0)
                    month.set(monthList[position])
                    //set data for next years

                    if (year.get()?.toInt()!! > Calendar.getInstance().get(Calendar.YEAR)) {
                        val monthIndex = allMonths.indexOf(month.get()) + 1
                        monthSpinner.setSelection(monthIndex - 1)
                        val cal = Calendar.getInstance()
                        cal.set(year.get()?.toInt() ?: return, monthIndex, 0)
                        val monthDate = SimpleDateFormat("MMMM yyyy")
                        monthYear.text = (monthDate.format(cal.time))
                        val calendarDay = CalendarDay.from(cal)
                        calendarView.setCurrentDate(calendarDay)
                    } else {
                        month.set(monthList[position])
                        val monthIndex = allMonths.indexOf(month.get())
                        //Set spinner month and year data into text view when year is same
                        monthSpinner.setSelection(monthList.indexOf(month.get()))
                        val cal = Calendar.getInstance()
                        cal.set(
                            year.get()!!.toInt(),
                            monthIndex,
                            Calendar.getInstance().get(Calendar.DATE)
                        )
                        val monthDate = SimpleDateFormat("MMMM yyyy")
                        monthYear.text = (monthDate.format(cal.time))
                        val calendarDay = CalendarDay.from(cal)
                        calendarView.setCurrentDate(calendarDay)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
//        viewDataBinding.monthSpinner.setOnTouchListener(View.OnTouchListener { v, event -> false })
    }


    class Builder(val context: Context) {
        private var title = ""
        private var filterList = mutableListOf<FilterData>()
        private var btnApplyFilter: (startDate: String, endDate: String) -> Unit =
            { s: String, s1: String ->

            }


        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setFilterList(list: MutableList<FilterData>): Builder {
            this.filterList = list
            return this
        }

        fun setFilterButton(listener: (startDate: String, endDate: String) -> Unit): Builder {
            this.btnApplyFilter = listener
            return this
        }

        fun build(): DateFilterActivitesBottomSheet {
            return DateFilterActivitesBottomSheet(context, title, btnApplyFilter)
        }

        fun dismiss() {
            this.dismiss()
        }
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
        when {
            startDate == selectedDatesString -> {
                startDate = ""
                endDate = ""
                btnApplyFilter.alpha = 0.5f
                btnApplyFilter.isEnabled = false
            }
            selectedDatesString == "No Selection" -> {
                startDate = ""
                endDate = ""
                btnApplyFilter.alpha = 0.5f
                btnApplyFilter.isEnabled = false
            }
            else -> {
                startDate = selectedDatesString
                endDate = selectedDatesString
//                viewModel.startDate = selectedDatesString
            }
        }

    }

    override fun onMonthChanged(widget: MaterialCalendarView, date: CalendarDay) {
        val updateDate: Date = date.date
        monthYear.text = (formatter.format(updateDate))


        year.set("${date.year}")
        if (date.year == Calendar.getInstance().get(Calendar.YEAR)) {
            val monthValue = allMonths[date.month]
            val monthIndex = monthList.indexOf(monthValue)
            monthSpinner.setSelection(monthIndex)
            month.set("${monthList.indexOf(monthValue)}")


            val yearIndex = yearList.indexOf("${date.year}")
//            val finalYear = yearList.indexOf(yearIndex)
            yearSpinner.setSelection(yearIndex)
        } else {
            val monthValue = allMonths[date.month]
//            val monthIndex = monthList.indexOf(monthValue)
            monthSpinner.setSelection(allMonths.indexOf(monthValue))
            month.set(monthValue)

            val yearIndex = yearList.indexOf("${date.year}")
            yearSpinner.setSelection(yearIndex)
        }
    }

    override fun onRangeSelected(widget: MaterialCalendarView, dates: List<CalendarDay>) {
        endDate = selectedDatesString


        startDate = convertDateForApi(
            dates[0].day.toString(),
            dates[0].month.toString(),
            dates[0].year.toString()
        )
        endDate = convertDateForApi(
            dates[dates.size - 1].day.toString(),
            dates[dates.size - 1].month.toString(),
            dates[dates.size - 1].year.toString()
        )

        btnApplyFilter.alpha = 1f
        btnApplyFilter.isEnabled = true
    }

    private fun convertDateForApi(day: String, month: String, year: String): String {
        return "$day-$month-$year"
    }

    private fun formatted(start: CalendarDay, end: CalendarDay? = null): String {
        if (end == null || start == end) {
            return DateUtils.formatDateTime(context, start.date.time, 0).toString()
        }
        if (CalendarUtils.isFirstDayOfMonth(start) && CalendarUtils.isLastDayOfMonth(end)) {
            return DateUtils.formatDateTime(
                context,
                start.date.time,
                DateUtils.FORMAT_NO_MONTH_DAY or DateUtils.FORMAT_SHOW_YEAR
            )
        }
        val correctedEnd = end.date.time + end.calendar.timeZone.rawOffset

        return DateUtils.formatDateRange(
            context, start.date.time, correctedEnd, DateUtils.FORMAT_SHOW_YEAR
        )
    }
}
