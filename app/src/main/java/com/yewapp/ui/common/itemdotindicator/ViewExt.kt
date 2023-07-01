package com.tbuonomo.viewpagerdotsindicator

import android.view.View

/**
 * @description: This class is used to add padding on circle
 *  @author: Narbir Singh
 */

fun View.setPaddingHorizontal(padding: Int) {
    setPadding(padding, paddingTop, padding, paddingBottom)
}

fun View.setPaddingVertical(padding: Int) {
    setPadding(paddingLeft, padding, paddingRight, padding)
}