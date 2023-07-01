package com.yewapp.utils

import android.app.Activity
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

class LocationHelper(val activity: Activity) {
    private lateinit var permissionsManager: PermissionsManager

    fun checkPermissions(onMapReady: () -> Unit) {
        if (PermissionsManager.areLocationPermissionsGranted(activity)) {
            onMapReady()
        } else {
            permissionsManager = PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: List<String>) {
                    Toast.makeText(
                        activity, "You need to accept location permissions.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionResult(granted: Boolean) {
                    if (granted) {
                        onMapReady()
                    } else {
                        activity?.finish()
                    }
                }
            })
            permissionsManager.requestLocationPermissions(activity)
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    fun getAddressFromLatLng(latitude: Double, longitude: Double): Map<String, String> {
        val geocoder = Geocoder(activity, Locale.getDefault())
        var addressMap = mutableMapOf<String, String>()
        try {
            val addressList = geocoder.getFromLocation(latitude, longitude, 1)
            if (addressList != null && addressList.size > 0) {
                val address = addressList[0]
                addressMap["country"] = address.countryName
                addressMap["city"] = address.adminArea
                addressMap["pinCode"] = address.postalCode
            }
        } catch (e: IOException) {
            Log.e("Location Address Loader", "Unable connect to Geocoder", e)
        }
        return addressMap
    }
}