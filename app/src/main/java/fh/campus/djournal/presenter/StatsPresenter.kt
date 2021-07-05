package fh.campus.djournal.presenter

import fh.campus.djournal.interactor.ShapesInteractor
import fh.campus.djournal.utils.Shape
import java.io.Serializable

class StatsPresenter(private val shapesInteractor: ShapesInteractor) {



    fun deleteAllByShape(shapeType: Shape.Type) {
        shapesInteractor.getInstance()?.deleteAllByShape(shapeType)
    }
}