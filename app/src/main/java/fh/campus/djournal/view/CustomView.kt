package fh.campus.djournal.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.Nullable
import fh.campus.djournal.presenter.CanvasTouch
import fh.campus.djournal.utils.Constants
import fh.campus.djournal.utils.Shape
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.sqrt


class CustomView(context: Context?, @Nullable attrs: AttributeSet?) :
    View(context, attrs) {
    val RADIUS: Int = Constants.RADIUS
    private var canvas: Canvas? = null
    var historyListofShapes: List<Shape> = ArrayList()
    var canvasTouchofShapes: CanvasTouch? = null
    private var longPressDone = false

    // defines paint and canvas
    private var drawPaint: Paint? = null

    // Setup paint with color and stroke styles
    private fun setupPaint() {
        drawPaint = Paint()
        drawPaint!!.color = Color.BLUE
        drawPaint!!.isAntiAlias = true
        drawPaint!!.strokeWidth = 5F
        drawPaint!!.style = Paint.Style.FILL_AND_STROKE
        drawPaint!!.strokeJoin = Paint.Join.ROUND
        drawPaint!!.strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas
        for (shape in getHistoryList()) {
            if (shape.isVisible) {
                when (shape.type) {
                    Shape.Type.CIRCLE -> {
                        drawPaint?.color = Color.BLUE
                        drawPaint?.let {
                            canvas.drawCircle(
                                shape.getxCordinate().toFloat(),
                                shape.getyCordinate().toFloat(),
                                RADIUS.toFloat(),
                                it
                            )
                        }
                    }
                    Shape.Type.SQUARE -> drawRectangle(shape.getxCordinate(), shape.getyCordinate())
                    Shape.Type.TRIANGLE -> drawTriangle(
                        shape.getxCordinate(), shape.getyCordinate(),
                        (2 * RADIUS)
                    )
                }
            }
        }
    }

    private var longClickActive = false
    var initialTouchX = 0f
    var initialTouchY = 0f
    private var startClickTime: Long = 0
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialTouchX = event.x
                initialTouchY = event.y
                longPressDone = false
                if (!longClickActive) {
                    longClickActive = true
                    startClickTime = Calendar.getInstance().getTimeInMillis()
                }
            }
            MotionEvent.ACTION_UP -> {
                val currentTime: Long = Calendar.getInstance().getTimeInMillis()
                val clickDuration = currentTime - startClickTime
                if (clickDuration <= MIN_CLICK_DURATION && !longPressDone) {
                    //normal click
                    canvasTouchofShapes?.onClickEvent(event)
                    longClickActive = false
                    startClickTime = Calendar.getInstance().getTimeInMillis()
                    return false
                }
            }
            MotionEvent.ACTION_MOVE -> {
                var currentTime = Calendar.getInstance().getTimeInMillis()
                var clickDuration = currentTime - startClickTime
                if (clickDuration >= MIN_CLICK_DURATION) {
                    canvasTouchofShapes?.onLongPressEvent(initialTouchX, initialTouchY)
                    longClickActive = false
                    longPressDone = true
                    startClickTime = Calendar.getInstance().getTimeInMillis()
                    return false
                }
            }
        }
        return true
    }

    var squareSideHalf = 1 / sqrt(2.0)

    //Consider pivot x,y as centroid.
    fun drawRectangle(x: Int, y: Int) {
        drawPaint?.color = Color.RED
        val rectangle = Rect(
            (x - squareSideHalf * RADIUS).toInt(),
            (y - squareSideHalf * RADIUS).toInt(),
            (x + squareSideHalf * RADIUS).toInt(),
            (y + squareSideHalf * RADIUS).toInt()
        )
        drawPaint?.let { canvas?.drawRect(rectangle, it) }
    }

    /*
    select three vertices of triangle. Draw 3 lines between them to form a traingle
     */
    fun drawTriangle(x: Int, y: Int, width: Int) {
        drawPaint?.color = Color.GREEN
        val halfWidth = width / 2
        val path = Path()
        path.moveTo(x.toFloat(), (y - halfWidth).toFloat()) // Top
        path.lineTo((x - halfWidth).toFloat(), (y + halfWidth).toFloat()) // Bottom left
        path.lineTo((x + halfWidth).toFloat(), (y + halfWidth).toFloat()) // Bottom right
        path.lineTo(x.toFloat(), (y - halfWidth).toFloat()) // Back to Top
        path.close()
        drawPaint?.let { canvas?.drawPath(path, it) }
    }

    fun getHistoryList(): List<Shape> {
        return historyListofShapes
    }


    fun setHistoryList(historyList: LinkedList<Shape>) {
        this.historyListofShapes = historyList
    }

    fun getCanvasTouch(): CanvasTouch? {
        return canvasTouchofShapes
    }

    fun setCanvasTouch(canvasTouch: CanvasTouch?) {
        this.canvasTouchofShapes = canvasTouch
    }

    companion object {
        private val LOG_TAG = CustomView::class.java.simpleName
        private const val MIN_CLICK_DURATION = 1000
    }

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        setupPaint()
    }
}