package fh.campus.djournal.utils

import android.os.Parcel
import android.os.Parcelable
import lombok.Getter
import lombok.Setter

@Getter
@Setter
class Shape() : Parcelable{

    private var xCordinate: Int = 0
    private var yCordinate: Int = 0
    var width: Int = 0
    lateinit var type: Type
    var isVisible = true
        private set
    var actionNumber = 0
    var lastTranformationIndex = Constants.ACTION_CREATE

    constructor(parcel: Parcel) : this() {
        xCordinate = parcel.readInt()
        yCordinate = parcel.readInt()
        width = parcel.readInt()
        actionNumber = parcel.readInt()
        lastTranformationIndex = parcel.readInt()
    }

    constructor(x: Int, y: Int, width: Int) : this() {
        this.xCordinate = x
        this.yCordinate = y
        this.width = width
    }
    enum class Type private constructor(val value: Int) {
        CIRCLE(0), SQUARE(1), TRIANGLE(2), ARROW(3), LINE(4)
    }

    fun getxCordinate(): Int {
        return xCordinate
    }

    fun setxCordinate(xCordinate: Int) {
        this.xCordinate = xCordinate
    }

    fun getyCordinate(): Int {
        return yCordinate
    }

    fun setyCordinate(yCordinate: Int) {
        this.yCordinate = yCordinate
    }

    fun setVisibility(visibility: Boolean) {
        this.isVisible = visibility
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(xCordinate)
        parcel.writeInt(yCordinate)
        parcel.writeInt(width)
        parcel.writeInt(actionNumber)
        parcel.writeInt(lastTranformationIndex)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Shape> {
        override fun createFromParcel(parcel: Parcel): Shape {
            return Shape(parcel)
        }

        override fun newArray(size: Int): Array<Shape?> {
            return arrayOfNulls(size)
        }
    }




}