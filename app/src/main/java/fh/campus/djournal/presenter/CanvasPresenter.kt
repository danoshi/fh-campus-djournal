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

    private var shapesInteractor: ShapesInteractor = ShapesInteractor()

    private val onTouchListener = object : CanvasTouch {
        override fun onClickEvent(event: MotionEvent) {
            shapesInteractor.getInstance()?.changeShapeOnTouch(event.x, event.y, Constants.ACTION_TRANSFORM)

        }

        override fun onLongPressEvent(initialTouchX: Float, initialTouchY: Float) {
            shapesInteractor.getInstance()
                ?.changeShapeOnTouch(initialTouchX, initialTouchY, Constants.ACTION_DELETE)
        }
    }

    val countByGroup: HashMap<Shape.Type, Int>?
        get() = shapesInteractor.getInstance()?.getCountByGroup()

    init {
        if (canvas != null) {
            canvas.canvasTouchofShapes = onTouchListener
        }
        if (canvas != null) {
            initializeUIComponents(canvas, mContext)
        }
    }

    private fun initializeUIComponents(canvas: CustomView, mContext: Context) {
        shapesInteractor.getInstance()?._canvas = canvas
        shapesInteractor.getInstance()?.setContext(mContext)
    }


    fun setMaxX(maxX: Int) {
        shapesInteractor.getInstance()?.max_X
    }

    fun setMaxY(maxY: Int) {
        shapesInteractor.getInstance()?.max_Y
    }

    fun addShapeRandom(type: Shape.Type) {
        shapesInteractor.getInstance()?.addShapeRandom(type)
    }

    fun undo() {
        shapesInteractor.getInstance()?.undo()
    }

}