package fh.campus.djournal.interactor

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.annotation.NonNull
import fh.campus.djournal.utils.Constants
import fh.campus.djournal.utils.Shape
import fh.campus.djournal.view.CustomView
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.roundToInt
import kotlin.math.sqrt


class ShapesInteractor {
    private var mContext: Context? = null


    companion object {

        private var shapesInteractor: ShapesInteractor? = null

        fun getInstance(): Companion {
            synchronized(this) {
                var shapesInteractor = ShapesInteractor

                if (shapesInteractor == null) {
                    synchronized(ShapesInteractor::class.java) {
                        if (shapesInteractor == null) {
                            shapesInteractor = ShapesInteractor
                        }
                    }
                }
                return shapesInteractor
            }
        }
    }

    fun getInstance(): ShapesInteractor? {
        if (shapesInteractor == ShapesInteractor())
            ShapesInteractor()
        else shapesInteractor
        return shapesInteractor
    }

    var _canvas: CustomView? = null
    var max_X = 0
    var max_Y= 0

    /*
    Choose linkedlist (default doubly linkedlist in java ) as the data structure
     since we can add, transform, delete shapes very quickly in the same list without using extra memory
     */
    private var historyList: LinkedList<Shape> = LinkedList()
    private var actionSequence = 0

    /**
     *
     * @param oldShape
     * @param index
     * @param initialTouchX
     * @param initialTouchY
     */
    private fun askForDeleteShape(
            oldShape: Shape,
            index: Int,
            initialTouchX: Float,
            initialTouchY: Float
    ) {
        val builder= AlertDialog.Builder(mContext)
        builder.setMessage("Are you sure you want to delete ?")
            .setTitle("Delete Shape")
        builder.setPositiveButton("ok"
        ) { dialog, id -> deleteShape(oldShape, index) }
        builder.setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, id ->
            // User cancelled the dialog
        })
        // Create the AlertDialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun deleteShape(oldShape: Shape, i: Int) {
        oldShape.setVisibility(false)
        oldShape.actionNumber
        getHistoryList()[i] = oldShape
        _canvas!!.setHistoryList(getHistoryList())
        _canvas!!.invalidate()
    }

    fun changeShapeOnTouch(x: Float, y: Float, changeStatus: Int) {
        val touchX = x.roundToInt()
        val touchY = y.roundToInt()
        //   Toast.makeText(this.getContext(), " Touch at " + touchX + " y= " + touchY, Toast.LENGTH_SHORT).show();
        var oldX: Int
        var oldY: Int
        val list: LinkedList<Shape> = getHistoryList()
        val newShape: Shape? = null
        //Traverse from end so that we find the last performed action or shape first.
        for (i in list.size - 1 downTo 0) {
            val oldShape: Shape = list[i]
            if (oldShape.isVisible) {
                oldX = oldShape.getxCordinate()
                oldY = oldShape.getyCordinate()

                //Find an existing shape where the user has clicked on the canvas
                if (Constants.RADIUS >= calculateDistanceBetweenPoints(
                                oldX.toDouble(),
                                oldY.toDouble(),
                                touchX.toDouble(),
                                touchY.toDouble()
                        )
                ) {
                    if (changeStatus == Constants.ACTION_TRANSFORM) addTransformShape(
                            oldShape,
                            i,
                            oldX,
                            oldY
                    ) else if (changeStatus == Constants.ACTION_DELETE) askForDeleteShape(
                            oldShape,
                            i,
                            oldX.toFloat(),
                            oldY.toFloat()
                    )
                    break
                }
            }
        }
    }

    private fun addTransformShape(oldShape: Shape, index: Int, newX: Int, newY: Int) {
        oldShape.setVisibility(false)
        getHistoryList()?.set(index, oldShape)

        //transform object , rotate into available objects
        val newShapeType: Int =
            (oldShape.type?.value ?: + 1) % Constants.TOTAL_SHAPES
        val newshapeType: Shape.Type =
            Shape.Type.values()[newShapeType]
        val newShape: Shape = createShape(newshapeType, newX, newY)
        newShape.lastTranformationIndex
        upDateCanvas(newShape)
    }

    fun calculateDistanceBetweenPoints(
            x1: Double,
            y1: Double,
            x2: Double,
            y2: Double
    ): Double {
        return sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1))
    }

    /*
    Generate random x,y from 0,0 to screen max width and height
     */
    private fun generateRandomXAndY(): IntArray {
        var x = 0
        var y = 0
        var rn = Random()
        var diff: Int = max_X - Constants.RADIUS

        x += Constants.RADIUS
        rn = Random()
        diff = max_Y - Constants.RADIUS
        y = rn.nextInt(diff + 1)
        y += Constants.RADIUS
        return intArrayOf(x, y)
    }

    fun addShapeRandom(type: Shape.Type) {
        val randomCordinates = generateRandomXAndY()
        val shape: Shape = createShape(type, randomCordinates[0], randomCordinates[1])
        upDateCanvas(shape)
    }

    @NonNull
    private fun createShape(type: Shape.Type, x: Int, y: Int): Shape {
        val shape = Shape(x, y, Constants.RADIUS)
        shape.type
        return shape
    }

    fun undo() {
        if (getHistoryList().size > 0) {
            actionSequence--
            val toDeleteShape: Shape = getHistoryList().last
            if (toDeleteShape.lastTranformationIndex !== Constants.ACTION_CREATE) {
                val lastVisibleIndex: Int = toDeleteShape.lastTranformationIndex
                if (lastVisibleIndex < getHistoryList().size) {
                    val lastVisibleShape: Shape = getHistoryList()[lastVisibleIndex]
                    if (lastVisibleShape != null) {
                        lastVisibleShape.setVisibility(true)
                        getHistoryList()[lastVisibleIndex] = lastVisibleShape
                    }
                }
            }
            getHistoryList().removeLast()
            _canvas?.setHistoryList(getHistoryList())
            _canvas!!.invalidate()
        }
    }

    private fun upDateCanvas(shape: Shape) {
        shape.actionNumber
        getHistoryList().add(shape)
        _canvas!!.setHistoryList(getHistoryList())
        _canvas!!.invalidate()
    }

    private fun getHistoryList(): LinkedList<Shape> {
        return historyList
    }

    fun setHistoryList(historyList: LinkedList<Shape>) {
        this.historyList = historyList
    }

    /*
   Remove all items of a shape
    */
    fun deleteAllByShape(shapeType: Shape.Type) {
        val itr: MutableIterator<Shape> = getHistoryList().iterator()
        while (itr.hasNext()) {
            val shape: Shape = itr.next()
            if (shape.type === shapeType) {
                itr.remove()
            }
        }
    }

    /*
    Get all items in list , grouped by shape
     */
    fun getCountByGroup(): HashMap<Shape.Type, Int>? {
        val shapeTypeCountMap: HashMap<Shape.Type, Int> = HashMap()
        for (shape in getHistoryList()) {
            if (shape != null) {
                if (shape.isVisible) {
                    val shapeType: Shape.Type = shape.type
                    var existingCnt = 0
                    if (shapeTypeCountMap.containsKey(shape.type)) existingCnt =
                        shapeTypeCountMap[shape.type]!!
                    existingCnt++
                    shapeTypeCountMap[shapeType] = existingCnt
                }
            }
        }
        return shapeTypeCountMap
    }

    fun getCanvas(): CustomView? {
        return _canvas
    }

    fun setCanvas(canvas: CustomView?) {
        this._canvas = canvas
    }

    fun getmContext(): Context? {
        return mContext
    }

    fun getMaxX(): Int {
        return max_X
    }

    fun setMaxX(maxX: Int) {
        this.max_X = maxX
    }

    fun getMaxY(): Int {
        return max_Y
    }

    fun setMaxY(maxY: Int) {
        this.max_Y = maxY
    }


    fun setContext(mContext: Context?) {
        this.mContext = mContext
    }

}