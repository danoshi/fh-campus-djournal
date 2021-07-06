package fh.campus.djournal.presenter

import android.content.Context
import android.view.MotionEvent
import fh.campus.djournal.interactor.ShapesInteractor
import fh.campus.djournal.utils.Constants
import fh.campus.djournal.utils.Shape
import fh.campus.djournal.view.CustomView
import java.io.Serializable

class CanvasPresenter(
    private val canvas: CustomView?,
    private val mContext: Context) {

    private val shapesInteractor: ShapesInteractor? = ShapesInteractor().getInstance()
    private val onTouchListener = object : CanvasTouch {
        override fun onClickEvent(event: MotionEvent) {

            shapesInteractor?.changeShapeOnTouch(event.x, event.y, Constants.ACTION_TRANSFORM)

        }

        override fun onLongPressEvent(initialTouchX: Float, initialTouchY: Float) {
            shapesInteractor?.changeShapeOnTouch(initialTouchX, initialTouchY, Constants.ACTION_DELETE)
        }
    }

    val countByGroup: HashMap<Shape.Type, Int>?
        get() = shapesInteractor?.getCountByGroup()

    init {
        if (canvas != null) {
            canvas.canvasTouchofShapes = onTouchListener
        }
        if (canvas != null) {
            initializeUIComponents(canvas, mContext)
        }
    }

    private fun initializeUIComponents(canvas: CustomView, mContext: Context) {
        shapesInteractor?._canvas = canvas
        shapesInteractor?.setContext(mContext)
    }


    fun setMaxX(maxX: Int) {
        shapesInteractor?.max_X
    }

    fun setMaxY(maxY: Int) {
        shapesInteractor?.max_Y
    }

    fun addShapeRandom(type: Shape.Type) {
        shapesInteractor?.addShapeRandom(type)
    }

    fun undo() {
        shapesInteractor?.undo()
    }

}