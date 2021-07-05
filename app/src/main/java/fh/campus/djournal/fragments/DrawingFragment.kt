package fh.campus.djournal.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fh.campus.djournal.R
import fh.campus.djournal.database.AppDatabase
import fh.campus.djournal.databinding.FragmentDrawingBinding
import fh.campus.djournal.interactor.ShapesInteractor
import fh.campus.djournal.models.Drawing
import fh.campus.djournal.presenter.CanvasPresenter
import fh.campus.djournal.repositories.DrawingRepository
import fh.campus.djournal.utils.Constants
import fh.campus.djournal.view.CustomView
import fh.campus.djournal.viewmodels.DrawingViewModel
import fh.campus.djournal.viewmodels.DrawingViewModelFactory
import kotlinx.android.synthetic.main.content_canvas_draw.view.*

class DrawingFragment : Fragment() {
    private lateinit var binding: FragmentDrawingBinding
    private lateinit var drawingViewModel: DrawingViewModel
    private lateinit var viewModelFactory: DrawingViewModelFactory
    private lateinit var drawing: List<Drawing>
    private lateinit var canvasPresenter: CanvasPresenter
    private var canvas: CustomView? = null
    private lateinit var mContext: Context
    private lateinit var shapesInteractor: ShapesInteractor
    var max_Y = 800
    var max_X = 600

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_drawing, container, false)

        canvas = binding.include.canvasDrawView
        mContext = this.requireContext()
        canvasPresenter = CanvasPresenter(canvas, mContext)


        setupActionButton()
        getCanvasWidthAndHeight()
        return binding.root
    }

    private fun setupActionButton(){
        binding.fabCircle.setOnClickListener {
            canvasPresenter.addShapeRandom(type = fh.campus.djournal.utils.Shape.Type.CIRCLE)
        }
        binding.fabRect.setOnClickListener {
            canvasPresenter.addShapeRandom(type = fh.campus.djournal.utils.Shape.Type.SQUARE)
        }
        binding.fabTriangle.setOnClickListener {
            canvasPresenter.addShapeRandom(type = fh.campus.djournal.utils.Shape.Type.SQUARE)
        }
        binding.fabUndo.setOnClickListener {
            canvasPresenter.undo()
        }
    }
    private fun getCanvasWidthAndHeight(){
        val viewTreeObserver = canvas!!.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    canvas!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    max_Y = canvas!!.height
                    max_X = canvas!!.width
                    //Reduce radius so that shape isn't left incomplete at the edge
                    canvasPresenter.setMaxX(max_X - Constants.RADIUS)
                    val bottomButtonHeight = 100
                    canvasPresenter.setMaxY(max_Y - Constants.RADIUS - bottomButtonHeight)
                    removeOnGlobalLayoutListener(canvas!!, this)
                }
            })
        }
    }
    fun removeOnGlobalLayoutListener(v: View, listener: OnGlobalLayoutListener?) {
        v.viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }

}
