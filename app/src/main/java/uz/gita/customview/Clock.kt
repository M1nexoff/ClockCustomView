package uz.gita.customview

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.util.Calendar
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Clock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    styleAttr: Int = 0,
    defStyle: Int = 0,
) : View(context, attrs, styleAttr, defStyle) {
    var currentSecund = 5
    var currentMinute = 25
    var currentHour = 48
    val maxSpeed = 60
    val startAngle = 450
    val endAngle = 90
    private val smallLine = 20
    private val bigLine = 50
    private val radius by lazy { width / 2 }

    private val largeLinePaint = Paint().apply {
        isAntiAlias = true
        strokeWidth = 10f
        style = Paint.Style.FILL
        color = Color.BLACK
    }
    private val seconds = Paint().apply {
        isAntiAlias = true
        strokeWidth = 10f
        style = Paint.Style.FILL
        color = Color.parseColor("#FF0000")
    }
    private val minute = Paint().apply {
        isAntiAlias = true
        strokeWidth = 10f
        style = Paint.Style.FILL
        color = Color.parseColor("#000000")
    }
    private val hour = Paint().apply {
        isAntiAlias = true
        strokeWidth = 10f
        style = Paint.Style.FILL
        color = Color.parseColor("#000000")
    }

    private val smallLinePaint = Paint().apply {
        isAntiAlias = true
        strokeWidth = 4f
        style = Paint.Style.FILL
        color = Color.BLACK
    }

    private val circleInnerPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.parseColor("#000000")
    }

    private val paintText = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.BLACK
        textSize = 48f
    }

    private val circleOuterPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.parseColor("#000000")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val calendar: Calendar = Calendar.getInstance()
        currentHour = (calendar.get(Calendar.HOUR_OF_DAY)%12)*5 // 24 hours format yesli chto
        currentMinute = calendar.get(Calendar.MINUTE)
        currentSecund = calendar.get(Calendar.SECOND)
        drawSmallLines(canvas)
        drawBigLines(canvas)
        drawIndicators(canvas)
        drawCircle(canvas)
        postDelayed({
            invalidate()
        }, 1000)
    }

    private fun drawSmallLines(canvas: Canvas) {
        val extraRadius = (radius - (bigLine - smallLine) / 2)
        for (i in 0..maxSpeed) {
            val alphaRadian = calculateAlpha(i).toRadian()
            canvas.drawLine(
                radius + (extraRadius - smallLine) * cos(alphaRadian).toFloat(),
                radius - (extraRadius - smallLine) * sin(alphaRadian).toFloat(),
                radius + extraRadius * cos(alphaRadian).toFloat(),
                radius - extraRadius * sin(alphaRadian).toFloat(),
                smallLinePaint
            )
        }
    }

    private fun drawCircle(canvas: Canvas) {
        canvas.drawCircle(radius.toFloat(), radius.toFloat(), radius / 15f, circleInnerPaint)
    }

    private fun drawBigLines(canvas: Canvas) {
        for (i in 0..maxSpeed step 5) {
            val alphaRadian = calculateAlpha(i).toRadian()
            canvas.drawLine(
                radius + (radius - bigLine) * cos(alphaRadian).toFloat(),
                radius - (radius - bigLine) * sin(alphaRadian).toFloat(),
                radius * (1 + cos(alphaRadian).toFloat()),
                radius * (1 - sin(alphaRadian).toFloat()),
                largeLinePaint
            )
            if (i == 0) {
                continue
            }
            canvas.drawCenterText(
                "${i / 5}",
                radius + (radius - 100) * cos(alphaRadian).toFloat(),
                radius - (radius - 100) * sin(alphaRadian).toFloat(),
                paintText
            )
        }
    }

    private fun calculateAlpha(speed: Int): Int {
        val betta = (speed * (startAngle - endAngle) / maxSpeed)
        return startAngle - betta
    }

    private fun Int.toRadian(): Double = this * PI / 180

    fun Int.toPx(): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()

    private fun drawIndicators(canvas: Canvas) {
        val alphaRadianSeconds = calculateAlpha(currentSecund).toRadian()
        canvas.drawLine(
            radius.toFloat(),
            radius.toFloat(),
            radius + radius * 0.7f * cos(alphaRadianSeconds).toFloat(),
            radius - radius * 0.7f * sin(alphaRadianSeconds).toFloat(),
            seconds
        )
        val alphaRadianMinutes = calculateAlpha(currentMinute).toRadian()
        canvas.drawLine(
            radius.toFloat(),
            radius.toFloat(),
            radius + radius * 0.65f * cos(alphaRadianMinutes).toFloat(),
            radius - radius * 0.65f * sin(alphaRadianMinutes).toFloat(),
            minute
        )
        val alphaRadianHours = calculateAlpha(currentHour).toRadian()
        canvas.drawLine(
            radius.toFloat(),
            radius.toFloat(),
            radius + radius * 0.55f * cos(alphaRadianHours).toFloat(),
            radius - radius * 0.55f * sin(alphaRadianHours).toFloat(),
            hour
        )
    }

    private fun Canvas.drawCenterText(text: String, x: Float, y: Float, paint: Paint) {
        val rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)
        this.drawText(
            text,
            x - rect.exactCenterX(),
            y - rect.exactCenterY(),
            paint
        )
    }
}