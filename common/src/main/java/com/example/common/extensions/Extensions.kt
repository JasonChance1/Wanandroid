package com.example.common.extensions

import android.app.Activity
import android.app.Activity.OVERRIDE_TRANSITION_OPEN
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import android.view.View
import androidx.core.view.WindowCompat
import com.example.common.R
import com.example.model.ApiResult
import com.example.model.BaseResponse
import kotlin.coroutines.cancellation.CancellationException

fun View?.setVisible(isVisible: Boolean) {
    this?.visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.fadeVisible(visible: Boolean, duration: Long = 200) {
    if (visible && this.visibility != View.VISIBLE) {
        alpha = 0f
        visibility = View.VISIBLE
        animate().alpha(1f).setDuration(duration).start()
    } else if (!visible && this.visibility == View.VISIBLE) {
        animate().alpha(0f).setDuration(duration).withEndAction {
            visibility = View.GONE
        }.start()
    }
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

fun Activity.startActivity(cls: Class<*>) {
    val intent = Intent(this, cls)
    startActivity(intent)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        overrideActivityTransition(
            OVERRIDE_TRANSITION_OPEN,
            R.anim.slide_in,
            R.anim.slide_out,
            Color.TRANSPARENT
        )
    } else {
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }
}
suspend inline fun <T> safeApiCall(
    crossinline block: suspend () -> BaseResponse<T>
): ApiResult<T?> {
    return try {
        val resp = block()
        if (resp.isSuccess()) {
            ApiResult.Success(resp.data)
        } else {
            ApiResult.Error(
                message = resp.errorMsg.ifBlank { "Unknown error" },
                throwable = null
            )
        }
    } catch (e: CancellationException) {
        throw e
    } catch (t: Throwable) {
        ApiResult.Error(t.message ?: "Network error", t)
    }
}