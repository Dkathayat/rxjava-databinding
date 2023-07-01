package com.yewapp.ui.modules.dashboard.listener

import androidx.lifecycle.MutableLiveData

class Repository {
    private var currentCommentProgress: MutableLiveData<Boolean> = MutableLiveData()

    fun getLiveCommentProgress(): MutableLiveData<Boolean> {
        return currentCommentProgress
    }

    companion object {

        private val mInstance: Repository =
            Repository()

        @Synchronized
        fun getInstance(): Repository {
            return mInstance
        }
    }
}