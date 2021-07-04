package fh.campus.djournal.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import fh.campus.djournal.R
import fh.campus.djournal.databinding.DrawingItemBinding
import fh.campus.djournal.models.AudioRecord
import fh.campus.djournal.models.Drawing

class DrawingListAdapter(
    private var dataSet: List<Drawing>,
    val onDrawingItemShortClicked: (Drawing) -> Unit,
    val onDrawingItemLongClicked: (Drawing) -> Unit,
):
    RecyclerView.Adapter<DrawingListAdapter.ViewHolder>() {
    fun updateDataSet(drawing: List<Drawing>) {
        dataSet = drawing
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: DrawingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.drawingItem.setOnLongClickListener {
                binding.drawing?.let { drawing ->
                    onDrawingItemLongClicked(drawing)
                }
                true
            }
            binding.drawingItem.setOnClickListener {
                binding.drawing?.let { drawing ->
                    onDrawingItemShortClicked(drawing)
                }
            }
        }

        fun bind(drawingItem: Drawing) {
            with(binding) {
                drawing = drawingItem
                /**
                 * Evaluates the pending bindings, updating any Views that have expressions bound to
                 * modified variables.
                 */
                executePendingBindings()
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view binding, which defines the UI of the list item
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.drawing_item,
                viewGroup,
                false
            ),   //the binding
        )

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        holder.bind(dataSet[position])
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}