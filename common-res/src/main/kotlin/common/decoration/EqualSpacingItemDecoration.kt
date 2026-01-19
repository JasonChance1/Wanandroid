package common.decoration

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView

public class EqualSpacingItemDecoration(
    private val spacingPx: Int,
    @ColorInt private val color: Int? = null,
    private val includeEdge: Boolean = true
) : RecyclerView.ItemDecoration() {
    
    private val paint = Paint().apply {
        color?.let { this.color = it }
        style = Paint.Style.FILL
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0
        
        if (position == RecyclerView.NO_POSITION) return
        
        // 默认所有边都设置相同间距
        val halfSpacing = spacingPx / 2
        
        if (includeEdge) {
            outRect.left = spacingPx
            outRect.right = spacingPx
            outRect.top = if (position == 0) spacingPx else halfSpacing
            outRect.bottom = if (position == itemCount - 1) spacingPx else halfSpacing
        } else {
            outRect.left = halfSpacing
            outRect.right = halfSpacing
            outRect.top = halfSpacing
            outRect.bottom = halfSpacing
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (color == null) return
        
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            
            // 绘制底部间距线
            val bottom = child.bottom + spacingPx / 2
            c.drawRect(
                child.left.toFloat(),
                bottom.toFloat(),
                child.right.toFloat(),
                bottom.toFloat() + spacingPx / 2,
                paint
            )
            
            // 绘制右侧间距线
            val right = child.right + spacingPx / 2
            c.drawRect(
                right.toFloat(),
                child.top.toFloat(),
                right.toFloat() + spacingPx / 2,
                child.bottom.toFloat(),
                paint
            )
        }
    }
}