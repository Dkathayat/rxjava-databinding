package com.yewapp.di

import android.app.Application
import com.yewapp.MainApplication
import com.yewapp.di.app.ActivityBuilderModule
import com.yewapp.di.app.AppModule
import com.yewapp.di.app.retrofit.RetrofitModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, RetrofitModule::class, ActivityBuilderModule::class])
interface AppComponent {
    fun inject(application: MainApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}