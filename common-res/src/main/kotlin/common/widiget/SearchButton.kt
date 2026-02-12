package common.widiget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.example.common_res.R
import kotlin.math.min

/**
 * @author wandervogel
 * @date 2026-02-12  星期四
 * @description
 */
class SearchButton(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    var btnColor: Int = Color.WHITE
        set(value) {
            field = value;invalidate()
        }
    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 2.dp
    }

    private val path by lazy { Path() }
    private val pathDst by lazy { Path() }
    private val pathMeasure by lazy { PathMeasure() }
    private val rectF = RectF()
    private lateinit var animator: ValueAnimator

    private var progress = 0f

    private var radius = 10f

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.SearchButton)
        btnColor = ta.getColor(R.styleable.SearchButton_btnColor, Color.WHITE)
        paint.color = btnColor
        ta.recycle()

        initAnimator()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val mW = MeasureSpec.getSize(widthMeasureSpec)
        val mH = MeasureSpec.getSize(heightMeasureSpec)
        val size = min(mW, mH)

        radius = size * 0.3f
        setMeasuredDimension(size, size)
        rectF.set(-radius, -radius, radius, radius)
        path.reset()
        path.addArc(rectF, 45f, 350f)
        path.lineTo(size*0.4f, size*0.4f)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(width / 2f, height / 2f)
        drawPath(canvas)
    }

    private fun drawPath(canvas: Canvas) {
        pathMeasure.setPath(path, false)
        pathDst.reset()
        val length = pathMeasure.length
        val drawProgress = mapProgressWithHold(progress)
        pathMeasure.getSegment(drawProgress * length, length, pathDst, true)
        canvas.drawPath(pathDst, paint)
    }

    private fun initAnimator() {
        animator = ObjectAnimator.ofFloat(0f, 1f).apply {
            addUpdateListener {
                progress = it.animatedValue as Float
                invalidate()
            }
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            duration = 2000
        }
        animator.start()
    }

    // 出现后停顿的时间占整段动画的比例
    private val holdFraction = 0.25f
    private fun mapProgressWithHold(raw: Float): Float {
        return if (raw <= holdFraction) {
            0f
        } else {
            // 剩余时间再把 0-1 跑完
            (raw - holdFraction) / (1f - holdFraction)
        }
    }

    private val Int.dp
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        )
}