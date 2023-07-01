package com.yewapp.ui.modules.addchallenge.addchallengeselectrangedate

import android.annotation.SuppressLint
import android.os.Build
import android.text.format.DateUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.ObservableField
import com.shuhart.materialcalendarview.*
import com.shuhart.materialcalendarview.utils.CalendarUtils
import com.yewapp.R
import com.yewapp.data.network.api.challenges.ChallengeModel
import com.yewapp.data.network.api.invitemember.InviteMember
import com.yewapp.databinding.SelectDateRangeLayoutBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.listner.ChallengeExtras
import com.yewapp.ui.modules.listner.Repository
import com.yewapp.ui.modules.addchallenge.selectorcreateroute.SelectORCreateRouteActivity
import org.joda.time.LocalDate
import java.text.SimpleDateFormat
import java.util.*

//Todo:Add Challenge Step 2
class SelectRangeDateActivity :
    BaseActivity<SelectRangeDateNavigator, SelectRangeDateViewModel, SelectDateRangeLayoutBinding>(),
    SelectRangeDateNavigator, OnDateSelectedListener, OnMonthChangedListener,
    OnRangeSelectedListener {
    override fun getLayout(): Int {
        return R.layout.select_date_range_layout
    }


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
    var monthList = arrayListOf<String>()


    private var yearList = arrayListOf<String>()


    lateinit var monthAdapter: ArrayAdapter<String>
    lateinit var yearAdapter: ArrayAdapter<String>
    var currentMonthName = ""

    val month = ObservableField<String>("")

    //    val year = ObservableField<Int>(0)
    val year = ObservableField<String>("${Calendar.getInstance().get(Calendar.YEAR)}")

    private val formatter = SimpleDateFormat("MMMM yyyy")


    override fun init() {
        bind(SelectRangeDateViewModel::class.java)
    }

    override fun onViewModelCreated(viewModel: SelectRangeDateViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onViewBound(viewDataBinding: SelectDateRangeLayoutBinding) {
        Repository.getInstance().getChallengeProgress().observe(this) {
            viewModel.challengeExtras = it
        }
        viewDataBinding.calendarView.addOnDateChangedListener(this)
        viewDataBinding.calendarView.addOnMonthChangedListener(this)
        viewDataBinding.calendarView.addOnRangeSelectedListener(this)
        viewDataBinding.calendarView.selectionMode = MaterialCalendarView.SELECTION_MODE_RANGE
        viewDataBinding.calendarView.monthIndicatorVisible = false


        val cal = Calendar.getInstance()


        val instance: LocalDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        viewDataBinding.calendarView.state()?.edit()?.setMinimumDate(
            CalendarDay.from(
                instance.year, instance.monthOfYear - 1, instance.dayOfMonth
            )
        )?.commit()
        viewDataBinding.calendarView.showOtherDates = MaterialCalendarView.SHOW_OUT_OF_RANGE

        val monthDate = SimpleDateFormat("MMMM yyyy")
        viewModel.monthYear.set(monthDate.format(cal.time))
        viewModel.currentMonth.set(cal.time)

        initializeYearAdapter()
    }

    private fun initializeMonthAdapter() {
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
            ArrayAdapter(this, R.layout.spinner_item_date_time, monthList)

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        viewDataBinding.monthSpinner.adapter = arrayAdapter
        viewDataBinding.monthSpinner.setSelection(0)

        viewDataBinding.monthSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View, position: Int, id: Long
                ) {
//                    if (position != 0)
                    month.set(monthList[position])
                    //set data for next years

                    if (year.get()?.toInt()!! > Calendar.getInstance().get(Calendar.YEAR)) {
                        val monthIndex = allMonths.indexOf(month.get()) + 1
                        viewDataBinding.monthSpinner.setSelection(monthIndex - 1)
                        val cal = Calendar.getInstance()
                        cal.set(year.get()?.toInt() ?: return, monthIndex, 0)
                        val monthDate = SimpleDateFormat("MMMM yyyy")
                        viewModel.monthYear.set(monthDate.format(cal.time))
                        val calendarDay = CalendarDay.from(cal)
                        viewDataBinding.calendarView.setCurrentDate(calendarDay)
                    } else {
                        month.set(monthList[position])
                        val monthIndex = allMonths.indexOf(month.get())
                        //Set spinner month and year data into text view when year is same
                        viewDataBinding.monthSpinner.setSelection(monthList.indexOf(month.get()))
                        val cal = Calendar.getInstance()
                        cal.set(
                            year.get()!!.toInt(),
                            monthIndex,
                            Calendar.getInstance().get(Calendar.DATE)
                        )
                        val monthDate = SimpleDateFormat("MMMM yyyy")
                        viewModel.monthYear.set(monthDate.format(cal.time))
                        val calendarDay = CalendarDay.from(cal)
                        viewDataBinding.calendarView.setCurrentDate(calendarDay)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    Toast.makeText(this@SelectRangeDateActivity, "message", Toast.LENGTH_SHORT)
                        .show()
                }
            }
//        viewDataBinding.monthSpinner.setOnTouchListener(View.OnTouchListener { v, event -> false })
    }

    private fun initializeYearAdapter() {
        for (i in 0 until 5) {
            yearList.add("${Calendar.getInstance().get(Calendar.YEAR) + i}")
        }

        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.spinner_item_date_time, yearList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        viewDataBinding.yearSpinner.adapter = arrayAdapter
        viewDataBinding.yearSpinner.setSelection(0)
        viewDataBinding.yearSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View, position: Int, id: Long
                ) {
//                    if (position != 0)
                    year.set(yearList[position])
                    monthList.clear()

                    initializeMonthAdapter()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    Toast.makeText(this@SelectRangeDateActivity, "message", Toast.LENGTH_SHORT)
                        .show()
                }
            }
//        viewDataBinding.yearSpinner.setOnTouchListener(View.OnTouchListener { v, event -> false })
    }

    override fun onDateSelected(
        widget: MaterialCalendarView, date: CalendarDay, selected: Boolean
    ) {
        //   Toast.makeText(this, "$selectedDatesString is selected", Toast.LENGTH_LONG).show()
        when {
            viewModel.startDate == selectedDatesString -> {
                viewModel.startDate = ""
                viewModel.endDate = ""
                viewModel.isBtnActive.set(false)
            }
            selectedDatesString == "No Selection" -> {
                viewModel.startDate = ""
                viewModel.endDate = ""
                viewModel.isBtnActive.set(false)
            }
            else -> {
                viewModel.startDate =
                    FORMATTER.format(viewDataBinding.calendarView.selectedDate?.date ?: return)
                viewModel.endDate =
                    FORMATTER.format(viewDataBinding.calendarView.selectedDate?.date ?: return)
                viewModel.isBtnActive.set(true)
//                viewModel.startDate = selectedDatesString
            }
        }

    }

    override fun onMonthChanged(widget: MaterialCalendarView, date: CalendarDay) {
        //  Toast.makeText(this, "${date.month} is current month", Toast.LENGTH_LONG).show()
        // Toast.makeText(this, "$selectedDatesString is selected", Toast.LENGTH_LONG).show()
        val updateDate: Date = date.date
        viewModel.monthYear.set(formatter.format(updateDate))


        year.set("${date.year}")
        if (date.year == Calendar.getInstance().get(Calendar.YEAR)) {
            val monthValue = allMonths[date.month]
            val monthIndex = monthList.indexOf(monthValue)
            viewDataBinding.monthSpinner.setSelection(monthIndex)
            month.set("${monthList.indexOf(monthValue)}")


            val yearIndex = yearList.indexOf("${date.year}")
//            val finalYear = yearList.indexOf(yearIndex)
            viewDataBinding.yearSpinner.setSelection(yearIndex)
        } else {
            val monthValue = allMonths[date.month]
//            val monthIndex = monthList.indexOf(monthValue)
            viewDataBinding.monthSpinner.setSelection(allMonths.indexOf(monthValue))
            month.set(monthValue)

            val yearIndex = yearList.indexOf("${date.year}")
            viewDataBinding.yearSpinner.setSelection(yearIndex)
        }

    }

    override fun onRangeSelected(widget: MaterialCalendarView, dates: List<CalendarDay>) {
        viewModel.endDate = selectedDatesString

        viewModel.startDate = FORMATTER.format(dates[0].date)
        viewModel.endDate = FORMATTER.format(dates[dates.size - 1].date)
        viewModel.isBtnActive.set(true)
    }

    private val selectedDatesString: String
        get() {
            val dates = viewDataBinding.calendarView.selectedDates
            if (dates.isNotEmpty()) {
                return format(start = dates.first(), end = dates.last())
            }
            val date =
                viewDataBinding.calendarView.selectedDate ?: return getString(R.string.lifetime)
            return format(date)
        }

    private fun format(start: CalendarDay, end: CalendarDay? = null): String {
        if (end == null || start == end) {
            return DateUtils.formatDateTime(this, start.date.time, 0).toString()
        }
        if (CalendarUtils.isFirstDayOfMonth(start) && CalendarUtils.isLastDayOfMonth(end)) {
            return DateUtils.formatDateTime(
                this, start.date.time, DateUtils.FORMAT_NO_MONTH_DAY or DateUtils.FORMAT_SHOW_YEAR
            )
        }
        val correctedEnd = end.date.time + end.calendar.timeZone.rawOffset

        return DateUtils.formatDateRange(
            this, start.date.time, correctedEnd, DateUtils.FORMAT_SHOW_YEAR
        )
    }

    companion object {

        @SuppressLint("SimpleDateFormat")
        val FORMATTER = SimpleDateFormat("dd MMM yyyy")
//        val FORMATTER = SimpleDateFormat.getDateInstance()
    }

    private fun setSpinerOfMonth(value: String) {
        for (i in monthList.indices) {

//            if (monthList[i].contains(value+" "+Calendar.getInstance().get(Calendar.YEAR).toString())){
//                viewDataBinding.monthspinner.setSelection(i)
//            }

        }
    }

    override fun clickOnContinue() {
        val extras = ChallengeExtras.challengeExtras {
            challengeData = ChallengeModel(
                viewModel.challengeModel.isEdit,
                viewModel.challengeModel.challengeID ?: "",
                2,
                viewModel.challengeModel.selectedSportId,
                viewModel.challengeModel.selectedSportImage,
                viewModel.challengeModel.profileType,
                viewModel.challengeModel.sportsName,
                viewModel.startDate,
                viewModel.endDate,
                0,
                viewModel.challengeModel.routeID,
                "",
                "",
                "InActive",
                arrayListOf(),
                arrayListOf(),
                "",
                "",
                arrayListOf(),
                "",
                "",
                0.0,
                0.0,
                "",
                viewModel.challengeModel.country,
                viewModel.challengeModel.countryId,
                viewModel.challengeModel.state,
                viewModel.challengeModel.stateId,
                arrayListOf(),
                arrayListOf(),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                arrayListOf<InviteMember>()
            )
        }
        startActivity(
            intentProviderFactory.create(
                SelectORCreateRouteActivity::class.java, extras, 0
            )
        )
    }

    private fun convertDateForApi(date: String): String {
        val newDate = FORMATTER.parse(date)
        return SimpleDateFormat("yyyy-MM-dd").format(newDate)
    }

    override fun animateLayout(animStatus: Boolean) {
        viewModel.isMonthYearSpinnerVisible.set(animStatus)
        if (animStatus) {
            viewDataBinding.ivMonthYear.animate().rotation(180f).setDuration(250).start()
        } else {
            viewDataBinding.ivMonthYear.animate().rotation(0f).setDuration(250).start()
        }
    }
}