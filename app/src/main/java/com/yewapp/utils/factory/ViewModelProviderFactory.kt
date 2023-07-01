package com.yewapp.utils.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yewapp.data.network.DataManager
import com.yewapp.utils.rx.SchedulerProvider
import javax.inject.Inject

class ViewModelProviderFactory @Inject constructor(
    var dataManager: DataManager,
    var schedulerProvider: SchedulerProvider
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        try {
            val constructor =
                modelClass.getConstructor(DataManager::class.java, SchedulerProvider::class.java)
            return constructor.newInstance(dataManager, schedulerProvider)

        } catch (e: Exception) {
            throw RuntimeException("Cannot create an instance of ${e.message}")
        }
    }
}