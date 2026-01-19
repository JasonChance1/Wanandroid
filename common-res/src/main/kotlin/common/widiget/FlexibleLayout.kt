package common.widiget

import android.content.Context
import android.util.AttributeSet
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import kotlin.math.max

/**
 * @author wandervogel
 * @date 2026-01-19  星期一
 * @description
 */
class FlexibleLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    /** 横向间距(px) */
    var horizontalSpacing: Int = dp(8)

    /** 纵向间距(px) */
    var verticalSpacing: Int = dp(8)

    /** 最大行数，<=0 表示不限 */
    var maxLines: Int = 0

    /** 是否把最后一行也强制贴满宽度（一般不需要，默认 false） */
    var stretchLastLine: Boolean = false

    private val childBounds = ArrayList<Rect>()

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams =
        MarginLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams =
        MarginLayoutParams(p)

    override fun generateDefaultLayoutParams(): LayoutParams =
        MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

    override fun checkLayoutParams(p: LayoutParams?): Boolean = p is MarginLayoutParams

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        childBounds.clear()

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val paddingH = paddingLeft + paddingRight
        val paddingV = paddingTop + paddingBottom

        val contentMaxWidth = if (widthMode == MeasureSpec.UNSPECIFIED) {
            Int.MAX_VALUE
        } else {
            max(0, widthSize - paddingH)
        }

        var x = 0
        var y = 0
        var lineHeight = 0
        var lineCount = 1

        var usedWidth = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) {
                childBounds.add(Rect())
                continue
            }

            val lp = child.layoutParams as MarginLayoutParams

            measureChildWithMargins(
                child,
                widthMeasureSpec,
                paddingH,
                heightMeasureSpec,
                paddingV
            )

            val childW = child.measuredWidth + lp.leftMargin + lp.rightMargin
            val childH = child.measuredHeight + lp.topMargin + lp.bottomMargin

            val needWrap = x > 0 && x + horizontalSpacing + childW > contentMaxWidth

            if (needWrap) {
                // 换行
                if (maxLines in 1..lineCount) {
                    // 超出最大行数：后面的 child 直接不给位置（测量照常，布局时放到 0,0）
                    childBounds.add(Rect())
                    continue
                }

                y += lineHeight + verticalSpacing
                x = 0
                lineHeight = 0
                lineCount++
            }

            val left = paddingLeft + x + lp.leftMargin
            val top = paddingTop + y + lp.topMargin
            val right = left + child.measuredWidth
            val bottom = top + child.measuredHeight

            childBounds.add(Rect(left, top, right, bottom))

            x += if (x == 0) childW else (horizontalSpacing + childW)
            lineHeight = max(lineHeight, childH)
            usedWidth = max(usedWidth, x)
        }

        val measuredW = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            else -> usedWidth + paddingH
        }

        val contentH = y + lineHeight
        val measuredH = resolveSize(contentH + paddingV, heightMeasureSpec)

        setMeasuredDimension(measuredW, measuredH)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = minOf(childCount, childBounds.size)
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) continue

            val rect = childBounds[i]
            if (rect.isEmpty) {
                // 超出 maxLines 等情况：不摆放
                child.layout(0, 0, 0, 0)
            } else {
                child.layout(rect.left, rect.top, rect.right, rect.bottom)
            }
        }
    }

    /** 一句话渲染搜索记录（默认创建 TextView Tag） */
    fun setData(
        items: List<String>,
        tagFactory: ((String) -> View)? = null,
        onClick: ((String) -> Unit)? = null,
        onLongClick: ((String) -> Unit)? = null
    ) {
        removeAllViews()
        for (s in items) {
            val v = tagFactory?.invoke(s) ?: DefaultTagViewFactory.create(context, s)
            v.setOnClickListener { onClick?.invoke(s) }
            v.setOnLongClickListener {
                onLongClick?.invoke(s)
                onLongClick != null
            }
            addView(v)
        }
        requestLayout()
    }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density + 0.5f).toInt()
}

/** 默认 Tag 的创建（你可以换成 Chip） */
private object DefaultTagViewFactory {
    fun create(context: Context, text: String): View {
        return androidx.appcompat.widget.AppCompatTextView(context).apply {
            this.text = text
            textSize = 13f
            setPadding(dp(context, 12), dp(context, 8), dp(context, 12), dp(context, 8))
            // 简单背景（不依赖 xml）
            background = android.graphics.drawable.GradientDrawable().apply {
                cornerRadius = dp(context, 16).toFloat()
                setColor(0xFFF3F4F6.toInt()) // 浅灰背景
            }
            setTextColor(0xFF111827.toInt())
            isSingleLine = true
            ellipsize = android.text.TextUtils.TruncateAt.END
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                // 也可以用 margin 控制 item 间距；这里交给 layout 的 spacing 控制即可
            }
        }
    }

    private fun dp(context: Context, v: Int): Int =
        (v * context.resources.displayMetrics.density + 0.5f).toInt()
}
