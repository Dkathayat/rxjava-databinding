package com.yewapp.utils

import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout

val regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9-]+\\.[a-zA-Z]{2,6}\$".toRegex()
val regexPassword = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%&*(){}\\[\\]/?]).{8,}\$".toRegex()


fun EditText.showKeyboard() {
    val imm = ContextCompat.getSystemService(
        this.context,
        InputMethodManager::class.java
    ) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
}

fun TextInputLayout.showError(message: String) {
    this.showError(message)
}

fun String.isEmail(): Boolean = this.matches(regexEmail)
fun String.isPassword(): Boolean = this.matches(regexPassword)
