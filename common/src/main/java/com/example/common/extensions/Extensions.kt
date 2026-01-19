package com.example.common.extensions

import android.app.Activity
import android.app.Activity.OVERRIDE_TRANSITION_OPEN
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.common.R
import com.example.model.ApiResult
import com.example.model.BaseResponse
import common.decoration.EqualSpacingItemDecoration
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

val Any?.safeGetStr
    get() = this?.toString() ?: ""

fun Long.toDateTimeString(format: String = "yyyy-MM-dd HH:mm:ss"): String {
    val date = Date(this)
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    return sdf.format(date)
}


/**
 * 添加均匀间距（最常用）
 * @param spacingDp 间距大小（dp）
 * @param context Context 用于获取颜色资源
 * @param colorRes 颜色资源ID（可选）
 * @param includeEdge 是否包含边缘间距
 */
fun RecyclerView.addEqualSpacing(
    spacingDp: Int = 12,
    context: Context? = null,
    @ColorRes colorRes: Int? = null,
    includeEdge: Boolean = true
) {
    // 移除已有的相同装饰器
    val existing = (0 until itemDecorationCount)
        .map { getItemDecorationAt(it) }
        .firstOrNull { it is EqualSpacingItemDecoration }
    existing?.let { removeItemDecoration(it) }

    val color = if (colorRes != null && context != null) {
        ContextCompat.getColor(context, colorRes)
    } else {
        null
    }

    addItemDecoration(EqualSpacingItemDecoration(spacingDp.dp, color, includeEdge))
}

val Number.sp: Int
    get() = (TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ) + 0.5f).toInt()