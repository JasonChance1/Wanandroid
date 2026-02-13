package common.widiget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import common.utils.dp
import kotlin.math.max

/**
 * @author wandervogel
 * @date 2026-02-12  星期四
 * @description
 */
class FlowFlexLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {

    var horizontalSpacing = 8.dp
    var verticalSpacing = 8.dp

    var maxLines: Int = 0
        // 0为不限制行数
        set(value) {
            field = value;invalidate()
        }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p is MarginLayoutParams
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val maxWidth = if (widthMode == MeasureSpec.UNSPECIFIED) Int.MAX_VALUE
        else widthSize - paddingLeft - paddingRight

        var lineWidth = 0
        var lineHeight = 0
        var totalHeight = 0
        var usedWidth = 0
        var lines = 1

        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) continue

            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
            val lp = child.layoutParams as MarginLayoutParams

            val childW = child.measuredWidth + lp.leftMargin + lp.rightMargin
            val childH = child.measuredHeight + lp.topMargin + lp.bottomMargin

            val nextWidth =
                if (lineWidth == 0) childW else lineWidth + horizontalSpacing + childW

            if (nextWidth > maxWidth) {
                // 换行
                totalHeight += (lineHeight + verticalSpacing)
//                if (lines > 1) totalHeight += verticalSpacing

                lines++
                if (maxLines in 1..<lines) {
                    // 超出最大行数,后面的child仍会被 measure，但不布局显示
                    break
                }

                lineWidth = childW
                lineHeight = childH
                usedWidth = max(usedWidth, nextWidth - (horizontalSpacing + childW)) // 上一行宽度
            } else {
                lineWidth = nextWidth
                lineHeight = max(lineHeight, childH)
                usedWidth = max(usedWidth, lineWidth)
            }
        }

        // 最后一行
        totalHeight += lineHeight

        val finalW = usedWidth + paddingLeft + paddingRight
        val finalH = totalHeight + paddingTop + paddingBottom

        val measuredW = resolveSize(finalW, widthMeasureSpec)
        val measuredH = resolveSize(finalH, heightMeasureSpec)

        setMeasuredDimension(
            if (widthMode == MeasureSpec.EXACTLY) widthSize else measuredW,
            if (heightMode == MeasureSpec.EXACTLY) heightSize else measuredH
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val maxWidth = width - paddingLeft - paddingRight

        var x = paddingLeft
        var y = paddingTop
        var lineHeight = 0
        var lines = 1

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) continue

            val lp = child.layoutParams as MarginLayoutParams
            val childW = child.measuredWidth
            val childH = child.measuredHeight

            val needW = childW + lp.leftMargin + lp.rightMargin
            val needH = childH + lp.topMargin + lp.bottomMargin

            val nextX = if (x == paddingLeft) x + needW else x + horizontalSpacing + needW
            if (nextX - paddingLeft > maxWidth) {
                // 换行
                y += lineHeight + verticalSpacing
                x = paddingLeft
                lineHeight = 0
                lines++
            }

            if (maxLines in 1..<lines) {
                child.layout(0, 0, 0, 0)
                child.visibility = View.GONE
                continue
            } else if (child.visibility != View.VISIBLE) {
                child.visibility = View.VISIBLE
            }

            val left = x + lp.leftMargin
            val top = y + lp.topMargin
            val right = left + childW
            val bottom = top + childH

            child.layout(left, top, right, bottom)

            x += horizontalSpacing + needW
            lineHeight = max(lineHeight, needH)
        }
    }

}
