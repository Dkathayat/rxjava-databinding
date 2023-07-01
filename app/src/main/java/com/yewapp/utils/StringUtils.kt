package com.yewapp.utils

import android.text.SpannableString
import android.text.style.UnderlineSpan


fun String.mask(maskEnd: Int, maskChar: Char = '*'): String {
    val numberArray = arrayListOf<Char>()
    for (i in this.indices) {
        if (i > maskEnd)
            numberArray.add(i, this[i])
        else
            numberArray.add(i, maskChar)

    }

    return String(numberArray.toCharArray())
}

fun String.drawUnderline(): SpannableString {
    val content = SpannableString(this)
    content.setSpan(UnderlineSpan(), 0, content.length, 0)
    return content
}

fun createNameWhenNoImage(name: String): String {
    val tempName = name.split(" ")
    return if (tempName.size == 2)
        tempName[0].substring(0, 1) + tempName[1].substring(0, 1)
    else
        name.substring(0, 1)
}
