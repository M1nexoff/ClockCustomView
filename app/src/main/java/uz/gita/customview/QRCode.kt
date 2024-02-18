package uz.gita.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class QRCode(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private var matrix: Array<Array<Int>> = emptyArray()

    private var squareSize: Float = 0f

    private val paint = Paint()


    init {
        paint.style = Paint.Style.FILL
    }

    fun setMatrix(matrix: Array<Array<Int>>) {
        this.matrix = matrix
        calculateSquareSize()
        invalidate()
    }

    private fun calculateSquareSize() {
        val viewWidth = width.toFloat()
        val viewHeight = height.toFloat()
        val matrixRows = matrix.size
        val matrixCols = if (matrix.isNotEmpty()) matrix[0].size else 0

        squareSize = if (matrixRows > 0 && matrixCols > 0) {
            Math.min(viewWidth / matrixCols, viewHeight / matrixRows)
        } else {
            0f
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                val color = if (matrix[i][j] == 1) Color.BLACK else (if (matrix[i][j]==2)Color.BLUE else Color.WHITE)
                paint.color = color
                val left = j * squareSize
                val top = i * squareSize
                canvas.drawRect(
                    left,
                    top,
                    left + squareSize,
                    top + squareSize,
                    paint
                )
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = event.x.toInt() / squareSize.toInt()
            val y = event.y.toInt() / squareSize.toInt()
            if (matrix[y][x] == 0) {
                changeAdjacentSquares(x, y)
                invalidate()
            }
        }
        return true
    }

    private fun changeAdjacentSquares(x: Int, y: Int) {
        if (x < 0 || y < 0 || y >= matrix.size || x >= matrix[0].size || matrix[y][x] != 0 || matrix[y][x] == 2) {
            return
        }
        postDelayed({
            matrix[y][x] = 2
            changeAdjacentSquares(x - 1, y)
            changeAdjacentSquares(x + 1, y)
            changeAdjacentSquares(x, y - 1)
            changeAdjacentSquares(x, y + 1)
            invalidate()
        }, 300)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateSquareSize()
    }
}
