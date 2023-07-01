package com.yewapp.ui.modules.listner

import androidx.lifecycle.MutableLiveData

class Repository {
    private var currentChallengeProgress: MutableLiveData<ChallengeExtras> = MutableLiveData()

    fun getChallengeProgress(): MutableLiveData<ChallengeExtras> {
        return currentChallengeProgress
    }

    companion object {

        private val mInstance: Repository = Repository()

        @Synchronized
        fun getInstance(): Repository {
            return mInstance
        }
    }
}