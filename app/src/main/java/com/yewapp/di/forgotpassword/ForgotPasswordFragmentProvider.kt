package com.yewapp.di.forgotpassword

import com.yewapp.ui.modules.forgotPassword.fragments.EnterPasswordFragment
import com.yewapp.ui.modules.forgotPassword.fragments.ResetPasswordFragment
import com.yewapp.ui.modules.forgotPassword.fragments.VerifyCodeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
public abstract class ForgotPasswordFragmentProvider {
    @ContributesAndroidInjector
    abstract fun provideVerifyEmailFragment(): ResetPasswordFragment

    @ContributesAndroidInjector
    abstract fun provideVerifyCodeFragment(): VerifyCodeFragment

    @ContributesAndroidInjector
    abstract fun provideEnterPasswordFragment(): EnterPasswordFragment
}