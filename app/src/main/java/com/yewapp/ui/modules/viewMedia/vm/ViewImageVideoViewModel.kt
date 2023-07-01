package com.yewapp.ui.modules.viewMedia.vm

import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.api.feed.Files
import com.yewapp.ui.base.BaseViewModel
import com.yewapp.ui.modules.viewMedia.MediaListExtras
import com.yewapp.ui.modules.viewMedia.ViewImageVideoNavigator
import com.yewapp.utils.rx.SchedulerProvider

class ViewImageVideoViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ViewImageVideoNavigator>(dataManager, schedulerProvider) {
    var mutableImageList = mutableListOf<Files>()
    private var imageList = MutableLiveData<List<Files>>()
    val imageListLive: LiveData<List<Files>> get() = imageList

    var image = ObservableField<String>("")

    var imageslist = ArrayList<Files>()
    var videoIconVisibility = ObservableField<Boolean>(false)
    var isPlayingVideo = ObservableField<Boolean>(false)

    override fun setData(extras: Bundle?) {
        if (extras != null) {
            mutableImageList.addAll(extras.getParcelableArrayList(MediaListExtras.LIST)!!)
            imageList.value = mutableImageList
            image.set((imageList.value as MutableList<Files>)[0].url)
        } else {
            Log.d("error", "null received")
        }

    }

    fun onVideoPlayClick() {
        if (videoIconVisibility.get() == true)
            getNavigator()?.onVideoClick(true)
    }

    fun onCrossImageClick() {
        getNavigator()?.crossClick()
//        videoIconVisibility.set(true)
//        isPlayingVideo.set(false)
//        getNavigator()?.onStopPlayer()
    }

}