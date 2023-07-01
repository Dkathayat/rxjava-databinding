package com.yewapp.di.app

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.yewapp.data.local.PreferencesHelper
import com.yewapp.data.local.PreferencesHelperImpl
import com.yewapp.data.network.ApiHelper
import com.yewapp.data.network.ApiHelperImpl
import com.yewapp.data.network.DataManager
import com.yewapp.data.network.DataManagerImpl
import com.yewapp.di.ListHorizontal
import com.yewapp.di.ListVertical
import com.yewapp.di.PrefName
import com.yewapp.di.SpanCount
import com.yewapp.utils.PREF_NAME
import com.yewapp.utils.factory.IntentProviderFactory
import com.yewapp.utils.factory.ViewModelProviderFactory
import com.yewapp.utils.resource.ResourceProvider
import com.yewapp.utils.resource.ResourceProviderImpl
import com.yewapp.utils.rx.SchedulerProvider
import com.yewapp.utils.rx.SchedulerProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun provideContext(application: Application): Context

    companion object {

        @JvmStatic
        @Provides
        @Singleton
        fun provideMoshi(): Gson = GsonBuilder().create()

        @JvmStatic
        @Provides
        @Singleton
        fun provideIntentFactory(application: Application): IntentProviderFactory {
            return IntentProviderFactory(application)
        }

        @JvmStatic
        @Provides
        @PrefName
        fun providePrefName(): String = PREF_NAME

        @JvmStatic
        @Provides
        @ListVertical
        fun provideLinearLayoutManagerVertical(context: Context): LinearLayoutManager {
            return LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        @JvmStatic
        @Provides
        @ListHorizontal
        fun provideLinearLayoutManagerHorizontal(context: Context): LinearLayoutManager {
            return LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }

        @JvmStatic
        @Provides
        @SpanCount
        fun provideGridLayoutManager(context: Context): GridLayoutManager {
            return GridLayoutManager(context, 2)
        }
    }


    @Binds
    @Singleton
    abstract fun providePreferencesHelper(preferencesHelper: PreferencesHelperImpl): PreferencesHelper

    @Binds
    @Singleton
    abstract fun provideViewModelFactory(viewModelProviderFactory: ViewModelProviderFactory): ViewModelProvider.Factory

    @Binds
    @Singleton
    abstract fun provideDataManager(dataManager: DataManagerImpl): DataManager

    @Binds
    @Singleton
    abstract fun provideSchedulerProvider(schedulerProvider: SchedulerProviderImpl): SchedulerProvider

    @Binds
    @Singleton
    abstract fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper

    @Binds
    @Singleton
    abstract fun provideResourceProvider(resourceProvider: ResourceProviderImpl): ResourceProvider


}