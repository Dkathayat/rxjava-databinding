package com.yewapp.ui.modules.addassociatememberdetails.fragment.profiledetails

import com.yewapp.ui.base.BaseNavigator

interface AssociateProfileDetailsNavigator : BaseNavigator {
    //    fun onCountryClicked()
//    fun onStateClicked()
//    fun onCityClicked()
//    fun onEmailClick()
//    fun onPhoneClick()
    fun onDobClicked()
    fun onGenderClicked()
    fun onStatusClicked()
    fun navigateToDashboard()
    fun navigateNextScreen()
    fun updateAssociateID(s: String)
    fun updateAssociateProfileSuccess(updateMessage: String)
//    fun onAddressFieldClick()
}