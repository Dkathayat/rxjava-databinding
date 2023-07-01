package com.yewapp.ui.modules.addchallenge.addchallengedetails

import android.view.View
import com.yewapp.ui.base.BaseNavigator

interface ChallengeLocationDetailNavigator : BaseNavigator {
    fun navigateToAdditionalScreen()
    fun onChallengeVisibilityClick(view: View)
    fun onSportsLevelClick(view: View)
    fun onAgeGroupClick(view: View)
    fun onSubSportClick(view: View)
    fun onModelClick(view: View)
    fun onStatusClick(view: View)
//    fun saveAsDraft()
//    fun onAddIconClick()
//    fun addModelLayout()
    fun filterDataRequestToSend()
    fun addSportsLevelChips(sportsName:String)
    fun addChips(sportsName:String)


}