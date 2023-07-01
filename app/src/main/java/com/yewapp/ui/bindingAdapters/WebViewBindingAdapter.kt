package com.yewapp.ui.bindingAdapters

import android.os.Build
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.BindingAdapter
import com.yewapp.ui.modules.about.vm.OnWebPageError
import com.yewapp.ui.modules.about.vm.OnWebPageLoad


@BindingAdapter("app:loadPage", "app:onPageLoaded", "app:onPageError")
fun loadWebPage(
    view: WebView,
    url: String,
    listener: OnWebPageLoad,
    listenerError: OnWebPageError
) {
    if (url.isEmpty()) return

    view.webViewClient = WebViewClient()
    view.loadUrl(url)

    view.webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            listener.onWebPageLoaded()
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                listenerError.onWebPageError(error!!.description.toString())
            } else {
                listenerError.onWebPageError(null)
            }
        }
    }
}
