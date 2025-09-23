package com.example.common

import android.app.Activity
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import androidx.core.view.WindowCompat

fun View?.setVisible(isVisible: Boolean) {
    this?.visibility = if (isVisible) View.VISIBLE else View.GONE
}

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.fdp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

val Int.dp: Int
    get() = this.toFloat().dp
val Int.fdp: Float
    get() = this.toFloat().fdp

fun String?.safeToLong(): Result<Long> {
    return runCatching { this?.toLong() ?: 0L }
}

fun Activity.transparentStatusBar() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
}