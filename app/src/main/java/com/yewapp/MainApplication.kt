package com.yewapp

import android.content.ContentResolver
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.mapbox.mapboxsdk.Mapbox
import com.yewapp.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class MainApplication : MultiDexApplication(), HasAndroidInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Any>


    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent
            .builder()
            .application(this)
            .build()
            .inject(this)

        mContext = applicationContext
        mContentResolver = contentResolver

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))


    }

    override fun androidInjector(): AndroidInjector<Any> {
        return activityInjector
    }

    companion object {
        lateinit var mContext: Context
        lateinit var mContentResolver: ContentResolver
    }

}