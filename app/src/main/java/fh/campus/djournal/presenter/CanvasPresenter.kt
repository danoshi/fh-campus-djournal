package fh.campus.djournal.presenter

import android.content.Context
import android.view.MotionEvent
import fh.campus.djournal.interactor.ShapesInteractor
import fh.campus.djournal.utils.Constants
import fh.campus.djournal.utils.Shape
import fh.campus.djournal.view.CustomView

class CanvasPresenter(
    private val canvas: CustomView,
    private val mContext: Context) {

    private val onTouchListener = object : CanvasTouch {
        override fun onClickEvent(event: MotionEvent) {

            ShapesInteractor.instance.changeShapeOnTouch(event.x, event.y, Constants.ACTION_TRANSFORM)

        }

        override fun onLongPressEvent(initialTouchX: Float, initialTouchY: Float) {
            ShapesInteractor.instance.changeShapeOnTouch(initialTouchX, initialTouchY, Constants.ACTION_DELETE)
        }
    }

    val countByGroup: HashMap<Shape.Type, Int>?
        get() = ShapesInteractor.instance.countByGroup

    init {
        canvas.canvasTouchofShapes = onTouchListener
        initializeUIComponents(canvas, mContext)
    }

    private fun initializeUIComponents(canvas: CustomView, mContext: Context) {
        ShapesInteractor.instance.canvas = canvas
        ShapesInteractor.instance.setContext(mContext)
    }


    fun setMaxX(maxX: Int) {
        ShapesInteractor.instance.maxX = maxX
    }

    fun setMaxY(maxY: Int) {
        ShapesInteractor.instance.maxY = maxY
    }

    fun addShapeRandom(type: Shape.Type) {
        ShapesInteractor.instance.addShapeRandom(type)
    }

    fun undo() {
        ShapesInteractor.instance.undo()
    }

    companion object {
        private val LOG_TAG = CanvasPresenter.javaClass.simpleName
    }

}