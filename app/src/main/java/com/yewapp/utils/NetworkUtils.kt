package com.yewapp.utils

import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.yewapp.R


class NoNetworkException(message: String?, @field:StringRes @param:StringRes val messageRes: Int) :
    Exception(message)

class ApiFailException : java.lang.Exception {
    var statusCode = 0
        private set

    @StringRes
    var messageRes = 0
        private set

    constructor() {}
    constructor(statusCode: Int, message: String?, @StringRes messageRes: Int) : super(
        message
    ) {
        this.statusCode = statusCode
        this.messageRes = messageRes
    }

}

fun Activity.showError(
    message: String,
    actionName: String = "Retry",
    showAction: Boolean = true,
    function: () -> Unit = {}
) {
    val duration = Snackbar.LENGTH_LONG
    Snackbar.make(findViewById(android.R.id.content), message, duration).apply {
        setBackgroundTint(Color.RED)
        if (showAction) {
            setAction(actionName) {
                function()
            }
        }
    }.show()
}

fun Activity.showSuccess(
    message: String,
    showAction: Boolean = true,
    function: () -> Unit = {}
) {
    val duration = Snackbar.LENGTH_LONG
    val snackbar = Snackbar.make(findViewById(android.R.id.content), message, duration).apply {
        setBackgroundTint(ContextCompat.getColor(this@showSuccess, R.color.grey_F4))
        setTextColor(ContextCompat.getColor(this@showSuccess, R.color.black))
        if (showAction) {
            setAction(" ") {
                function()
            }
        }
    }

    val customView =
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    val customAction =
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)

    customView.setPadding(8, 8, 8, 8)
    customAction.setPadding(0, 20, 8, 20)

    customView.gravity = Gravity.CENTER_VERTICAL

    customView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_success, 0, 0, 0);
    customAction.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cross, 0, 0, 0);

    customView.compoundDrawablePadding =
        resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding);
    customAction.compoundDrawablePadding =
        resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding);

    val params = snackbar.view.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.TOP
    snackbar.view.layoutParams = params

    snackbar.show()
}


val httpErrorMap = mutableMapOf<Int, String>().apply {
    put(500, "Something went wrong.")
    put(401, "Your session has expired. Please sign in again.")
//    put(405, "Invalid request URI or Method.")
//    put(404, "The requested resource was not found.")
    put(503, "The server is under maintenance mode, please try after few minutes later.")
//    put(400, "Please provide valid encryption string.")
//    put(400, "something went wrong in push notification.")
}
