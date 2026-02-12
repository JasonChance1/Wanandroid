package common.utils

import android.content.res.Resources
import android.util.TypedValue

/**
 * @author wandervogel
 * @date 2026-02-12  星期四
 * @description
 */

val Float.dp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

val Int.dp: Int
    get() = (this.toFloat().dp + 0.5f).toInt()