package fh.campus.djournal.presenter

import fh.campus.djournal.interactor.ShapesInteractor
import fh.campus.djournal.utils.Shape

class StatsPresenter {

    private lateinit var shapesInteractor: ShapesInteractor

    fun deleteAllByShape(shapeType: Shape.Type) {
        shapesInteractor.deleteAllByShape(shapeType)
    }
}