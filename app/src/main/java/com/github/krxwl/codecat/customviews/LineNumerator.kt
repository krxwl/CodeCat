package com.github.krxwl.codecat.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

// аннотация того что мы переопределяем все возможные конструкторы
class LineNumerator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /*private val gutterTextPaint = Paint() // Нумерация строк
    private val gutterDividerPaint = Paint() // Отделяющая линия
    val topVisibleLine = scrollY / lineHeight
    val bottomVisibleLine = topVisibleLine + height / lineHeight + 1 // height - высота View
    val lineStart = layout.getLineStart(topVisibleLine)
    val lineEnd = layout.getLineEnd(bottomVisibleLine)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // measurespec - класс для определения размеров view в андроиде
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var topVisibleLine = getTopVisibleLine()
        val bottomVisibleLine = getBottomVisibleLine()
        val textRight = (gutterWidth - gutterMargin / 2) + scrollX
        while (topVisibleLine <= bottomVisibleLine) {
            canvas?.drawText(
                (topVisibleLine + 1).toString(),
                textRight.toFloat(),
                (layout.getLineBaseline(topVisibleLine) + paddingTop).toFloat(),
                gutterTextPaint
            )
            topVisibleLine++
        }
        canvas?.drawLine(
            (gutterWidth + scrollX).toFloat(),
            scrollY.toFloat(),
            (gutterWidth + scrollX).toFloat(),
            (scrollY + height).toFloat(),
            gutterDividerPaint
        )
    }*/

}