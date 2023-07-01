package com.yewapp.ui.modules.managedevices.extra

import android.net.Uri
import android.util.Log
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataRequest
import com.google.android.gms.wearable.Wearable
import com.yewapp.R
import com.yewapp.data.network.LOGIN
import com.yewapp.databinding.ActivityDeviceConnectInfoBinding
import com.yewapp.ui.base.BaseActivity
import com.yewapp.ui.modules.managedevices.garmin.MainManageDevicesActivity
import com.yewapp.utils.toJson

class DeviceConnectInfoActivity :
    BaseActivity<DeviceConnectInfoNavigator, DeviceConnectInfoViewModel, ActivityDeviceConnectInfoBinding>(),
    DeviceConnectInfoNavigator {
    override fun getLayout(): Int = R.layout.activity_device_connect_info
    override fun init() {
        bind(DeviceConnectInfoViewModel::class.java)
    }

    override fun onViewBound(viewDataBinding: ActivityDeviceConnectInfoBinding) {
    }

    override fun onViewModelCreated(viewModel: DeviceConnectInfoViewModel) {
        viewModel.setNavigator(this)
    }

    override fun navigateToGarmin() {
        startActivity(
            intentProviderFactory.create(
                MainManageDevicesActivity::class.java,
                null,
                0
            )
        )
    }

    override fun navigateToGoogleWearOs() {
        val client: DataClient = Wearable.getDataClient(this)
        client.dataItems
            .addOnSuccessListener { dataItems ->
                for (dataItem in dataItems) {
                    Log.d("wearOsGoogle",dataItem.toString())
                }
            }
        val uri: Uri = Uri.Builder()
            .scheme(PutDataRequest.WEAR_URI_SCHEME)
            .path("/path/to/data/item")
            .build()

        
    }
}