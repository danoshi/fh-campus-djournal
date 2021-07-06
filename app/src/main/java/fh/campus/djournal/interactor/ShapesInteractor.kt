package fh.campus.djournal.interactor

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import fh.campus.djournal.utils.Constants.ACTION_CREATE
import fh.campus.djournal.utils.Constants.ACTION_DELETE
import fh.campus.djournal.utils.Constants.ACTION_TRANSFORM
import fh.campus.djournal.utils.Constants.RADIUS
import fh.campus.djournal.utils.Constants.TOTAL_SHAPES
import fh.campus.djournal.utils.Shape
import fh.campus.djournal.view.CustomView
import java.util.*

class ShapesInteractor private constructor() {
    private var mContext: Context? = null
    var canvas: CustomView? = null
    var maxX = 0
    var maxY = 0
    private var actionSequence = 0

    /**
     *
     * @param oldShape
     * @param index
     * @param initialTouchX
     * @param initialTouchY
     */
    private fun askForDeleteShape(oldShape: Shape, index: Int, initialTouchX: Float, initialTouchY: Float) {
        val builder = AlertDialog.Builder(mContext)
        builder.setMessage("Are you sure you want to delete ?")
                .setTitle("Delete Shape")
        builder.setPositiveButton("ok") { dialog, id -> deleteShape(oldShape, index) }
        builder.setNegativeButton("cancel") { dialog, id ->
            // User cancelled the dialog
        }
        // Create the AlertDialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteShape(oldShape: Shape, i: Int) {
        oldShape.setVisibility(false)
        oldShape.actionNumber = actionSequence++
        historyList[i] = oldShape
        Log.d(LOG_TAG, "askForDeleteShape =  " + oldShape.type)
        canvas!!.setHistoryList(historyList)
        canvas!!.invalidate()
    }

    fun changeShapeOnTouch(x: Float, y: Float, changeStatus: Int) {
        val touchX = Math.round(x)
        val touchY = Math.round(y)
        //   Toast.makeText(this.getContext(), " Touch at " + touchX + " y= " + touchY, Toast.LENGTH_SHORT).show();
        var oldX: Int
        var oldY: Int
        val list = historyList
        val newShape: Shape? = null
        //Traverse from end so that we find the last performed action or shape first.
        for (i in list.indices.reversed()) {
            val oldShape = list[i]
            if (oldShape.isVisible) {
                oldX = oldShape.getxCordinate()
                oldY = oldShape.getyCordinate()

                //Find an existing shape where the user has clicked on the canvas
                if (RADIUS >= calculateDistanceBetweenPoints(oldX.toDouble(), oldY.toDouble(), touchX.toDouble(), touchY.toDouble())) {
                    if (changeStatus == ACTION_TRANSFORM) addTransformShape(oldShape, i, oldX, oldY) else if (changeStatus == ACTION_DELETE) askForDeleteShape(oldShape, i, oldX.toFloat(), oldY.toFloat())
                    break
                }
            }
        }
    }

    private fun addTransformShape(oldShape: Shape, index: Int, newX: Int, newY: Int) {
        Log.d(LOG_TAG, " oldShape =  " + oldShape.type)
        oldShape.setVisibility(false)
        historyList[index] = oldShape
        Log.d(LOG_TAG, " HIDE oldShape =  " + oldShape.type)

        //transform object , rotate into available objects
        val newShapeType = (oldShape.type.value + 1) % TOTAL_SHAPES
        val newshapeType = Shape.Type.values()[newShapeType]
        Log.d(LOG_TAG, " newshape =  $newshapeType")
        val newShape = createShape(newshapeType, newX, newY)
        newShape.lastTranformationIndex = index
        upDateCanvas(newShape)
    }

    fun calculateDistanceBetweenPoints(
            x1: Double,
            y1: Double,
            x2: Double,
            y2: Double): Double {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1))
    }

    /*
    Generate random x,y from 0,0 to screen max width and height
     */
    private fun generateRandomXAndY(): IntArray {
        var x: Int
        var y: Int
        var rn = Random()
        var diff = maxX - RADIUS
        x = rn.nextInt(diff + 1)
        x += RADIUS
        rn = Random()
        diff = maxY - RADIUS
        y = rn.nextInt(diff + 1)
        y += RADIUS
        Log.d(LOG_TAG, " Random x= " + x + "y" + y)
        return intArrayOf(x, y)
    }

    fun addShapeRandom(type: Shape.Type) {
        val randomCordinates = generateRandomXAndY()
        val shape = createShape(type, randomCordinates[0], randomCordinates[1])
        upDateCanvas(shape)
    }

    private fun createShape(type: Shape.Type, x: Int, y: Int): Shape {
        val shape = Shape(x, y, RADIUS)
        shape.type = type
        return shape
    }

    fun undo() {
        if (historyList.size > 0) {
            actionSequence--
            val toDeleteShape = historyList.last
            if (toDeleteShape.lastTranformationIndex != ACTION_CREATE) {
                val lastVisibleIndex = toDeleteShape.lastTranformationIndex
                if (lastVisibleIndex < historyList.size) {
                    val lastVisibleShape = historyList[lastVisibleIndex]
                    if (lastVisibleShape != null) {
                        lastVisibleShape.setVisibility(true)
                        historyList[lastVisibleIndex] = lastVisibleShape
                    }
                }
            }
            historyList.removeLast()
            canvas!!.setHistoryList(historyList)
            canvas!!.invalidate()
        }
    }

    private fun upDateCanvas(shape: Shape) {
        Log.d(LOG_TAG, " upDateCanvas " + shape.type + " actiontype = " + actionSequence)
        shape.actionNumber = actionSequence++
        historyList.add(shape)
        canvas!!.setHistoryList(historyList)
        canvas!!.invalidate()
    }

    private val historyList: LinkedList<Shape>
        private get() = Companion.historyList

   // fun setHistoryList(historyList: LinkedList<Shape>) {
    //     this.historyList = historyList
   // }

    /*
   Remove all items of a shape
    */
    fun deleteAllByShape(shapeType: Shape.Type) {
        val itr = historyList.iterator()
        while (itr.hasNext()) {
            val shape = itr.next()
            if (shape.type === shapeType) {
                itr.remove()
            }
        }
    }

    /*
    Get all items in list , grouped by shape
     */
    val countByGroup: HashMap<Shape.Type, Int>
        get() {
            val shapeTypeCountMap = HashMap<Shape.Type, Int>()
            for (shape in historyList) {
                if (shape.isVisible) {
                    val shapeType = shape.type
                    var existingCnt = 0
                    if (shapeTypeCountMap.containsKey(shape.type)) existingCnt = shapeTypeCountMap[shape.type]!!
                    existingCnt++
                    shapeTypeCountMap[shapeType] = existingCnt
                }
            }
            return shapeTypeCountMap
        }

    fun getmContext(): Context? {
        return mContext
    }

    fun setContext(mContext: Context?) {
        this.mContext = mContext
    }

    companion object {
        val instance = ShapesInteractor()
        private const val LOG_TAG = ""

        /*
    Choose linkedlist (default doubly linkedlist in java ) as the data structure
     since we can add, transform, delete shapes very quickly in the same list without using extra memory
     */
        private val historyList = LinkedList<Shape>()
    }
}