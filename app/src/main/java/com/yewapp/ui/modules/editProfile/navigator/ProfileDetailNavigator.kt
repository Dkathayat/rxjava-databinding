package com.yewapp.ui.modules.editProfile.navigator

import com.yewapp.ui.base.BaseNavigator

interface ProfileDetailNavigator : BaseNavigator {
    fun onCountryClicked()
    fun onStateClicked()
    fun onCityClicked()
    fun onEmailClick()
    fun onPhoneClick()
    fun onDobClicked()
    fun onGenderClicked()
    fun navigateToDashboard()
    fun onAddressFieldClick()
}