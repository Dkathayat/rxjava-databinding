package com.yewapp.ui.modules.profile.fragment.mypoints.filteractivity

import android.content.Context
import android.os.Build
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import com.shuhart.materialcalendarview.*
import com.shuhart.materialcalendarview.utils.CalendarUtils
import com.yewapp.R
import com.yewapp.data.network.api.mypoint.MyPointsHistoryDetails
import com.yewapp.data.network.api.profile.FilterData
import com.yewapp.databinding.ActivityMypointsFilterBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.profile.adapter.MyPointsHistoryAdapter
import com.yewapp.ui.modules.profile.fragment.mypoints.MyPointsViewModel
import com.yewapp.ui.modules.profile.navigator.MyPointsNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MyPointsFilterActivity :
    BaseActivity<MyPointsNavigator, MyPointsViewModel, ActivityMypointsFilterBinding>(),
    MyPointsNavigator, ItemMyPointsFilterViewModel.OnFilterItemClickListener,
    OnDateSelectedListener, OnMonthChangedListener,
    OnRangeSelectedListener {
    val calendarView by lazy { viewDataBinding.calendarView2 }
    val monthYear by lazy { viewDataBinding.tvMonthYear }
    val ivMonthYear by lazy { viewDataBinding.ivMonthYear }
    private val btnApplyFilter by lazy { viewDataBinding.applyFilterButton }
    val monthSpinner by lazy { viewDataBinding.monthSpinner }
    private val yearSpinner by lazy { viewDataBinding.yearSpinner }
    val year = ObservableField<String>("${Calendar.getInstance().get(Calendar.YEAR)}")
    private var yearList = arrayListOf<String>()
    private val formatter = SimpleDateFormat("MMMM yyyy")
    val monthDate = SimpleDateFormat("MMMM yyyy")
    var monthList = arrayListOf<String>()
    var myPointsHistoryDetails:MyPointsHistoryDetails?=null
    val month = ObservableField<String>("")
    var startDate: String = ""
    var endDate: String = ""
    var isMonthYearSpinnerVisible = false
    var isCalanderVisible = false
    val userSportID = ArrayList<String>()
    val userSubSportID = ArrayList<String>()
    private lateinit var adapter: UserPointsFilterAdapter
    lateinit var pointsHistoryAdapter: MyPointsHistoryAdapter

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
                calendarView.selectedDate ?: return this.getString(R.string.lifetime)
            return formatted(date)
        }
    override fun getLayout(): Int {
        return R.layout.activity_mypoints_filter
    }

    override fun init() {
        bind(MyPointsViewModel::class.java)

    }

    override fun onViewBound(viewDataBinding: ActivityMypointsFilterBinding) {
        calendarView.visibility = View.INVISIBLE
        monthSpinner.visibility = View.GONE
        yearSpinner.visibility = View.GONE
        viewModel.getUserPointHistoryData("","", listOf(), listOf())
        addObserver()
        initializeAdapter()
        setUpCalanderView()

        viewDataBinding.checkStartEndDate.setOnClickListener {
            setCheckUnCheck()
        }
        btnApplyFilter.setOnClickListener {
            viewModel.getUserPointHistoryData(
                startDate,endDate,userSportID,userSubSportID
            )
            viewModel.getUserSummary(startDate,endDate)
            finish()

        }
        viewDataBinding.imgCloseFilter.setOnClickListener {
            finish()
        }

        viewDataBinding.tvTitleReset.setOnClickListener {
            startDate = ""
            endDate = ""
            viewDataBinding.checkStartEndDate.setImageResource(R.drawable.ic_unchecked__checkbox)
            viewModel.startDate.set("")
            viewModel.endDate.set("")
            adapter.clearItem()
            setUpCalanderView()
            calendarView.visibility = View.INVISIBLE

        }

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

    }

    private fun setUpCalanderView() {
        val calender = Calendar.getInstance()

        calendarView.addOnDateChangedListener(this)
        calendarView.addOnMonthChangedListener(this)
        calendarView.addOnRangeSelectedListener(this)
        calendarView.selectionMode = MaterialCalendarView.SELECTION_MODE_RANGE
        calendarView.monthIndicatorVisible = false

        val instance: LocalDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        monthYear.text = monthDate.format(calender.time)
        initializeYearAdapter(this, yearSpinner)

        calendarView.state()?.edit()?.setMaximumDate(
            CalendarDay.from(
                instance.year, instance.monthOfYear - 1, instance.dayOfMonth
            )
        )?.commit()
        calendarView.showOtherDates = MaterialCalendarView.SHOW_OUT_OF_RANGE
    }

    private fun formatted(start: CalendarDay, end: CalendarDay? = null): String {
        if (end == null || start == end) {
            return DateUtils.formatDateTime(this, start.date.time, 0).toString()
        }
        if (CalendarUtils.isFirstDayOfMonth(start) && CalendarUtils.isLastDayOfMonth(end)) {
            return DateUtils.formatDateTime(
                this,
                start.date.time,
                DateUtils.FORMAT_NO_MONTH_DAY or DateUtils.FORMAT_SHOW_YEAR
            )
        }
        val correctedEnd = end.date.time + end.calendar.timeZone.rawOffset

        return DateUtils.formatDateRange(
            this, start.date.time, correctedEnd, DateUtils.FORMAT_SHOW_YEAR
        )
    }

    private fun initializeYearAdapter(context: Context, yearSpinner: AppCompatSpinner) {
        for (i in 0 until 10) {
            yearList.add("${Calendar.getInstance().get(Calendar.YEAR) - i}")
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
        monthList.clear()
            for (i in allMonths.indices) {
                if (year.get()?.toInt()!! >= Calendar.getInstance().get(Calendar.YEAR)) {
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
                        val monthIndex = allMonths.indexOf(month.get()) - 1
                        monthSpinner.setSelection(monthIndex + 1)
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
    }

    private fun setCheckUnCheck() {
        if (isCalanderVisible) {
            viewDataBinding.checkStartEndDate.setImageResource(R.drawable.ic_unchecked__checkbox)
            viewModel.startDate.set("")
            viewModel.endDate.set("")
            calendarView.visibility = View.INVISIBLE

        } else {
            viewDataBinding.checkStartEndDate.setImageResource(R.drawable.ic_check_enabled)
            calendarView.visibility = View.VISIBLE

        }
        isCalanderVisible = !isCalanderVisible
    }

    private fun initializeAdapter() {
        val list = mutableListOf<MyPointsHistoryDetails>()
        adapter = UserPointsFilterAdapter(list, this)
        viewDataBinding.rvListFilter.adapter = adapter

    }

    private fun addObserver() {
        viewModel.userPointsHistory.observe(this, Observer {
            adapter.setItems(it)
        })
    }

    override fun onViewModelCreated(viewModel: MyPointsViewModel) {
        viewModel.setNavigator(this)
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {

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

        Log.d("ThisTag", dates.first().toString() + dates.last().date)

        viewModel.startDate.set(SimpleDateFormat("yyyy-MM-dd").format(dates.first().date))
        viewModel.endDate.set(SimpleDateFormat("yyyy-MM-dd").format(dates.last().date))
        startDate = SimpleDateFormat("yyyy-MM-dd").format(dates.first().date)
        endDate = SimpleDateFormat("yyyy-MM-dd").format(dates.last().date)
    }

    override fun navigateToFilterActivity() {

    }

    override fun onClickItem(item: MyPointsHistoryDetails) {
        userSportID.add(item.sport_id)
        userSubSportID.add(item.sub_sport_id)

    }

    override fun onRemoveItem(removeItem: MyPointsHistoryDetails) {
        userSportID.remove(removeItem.sport_id)
        userSubSportID.remove(removeItem.sub_sport_id)

    }
}