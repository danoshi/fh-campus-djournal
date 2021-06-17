package fh.campus.djournal.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import fh.campus.djournal.R
import fh.campus.djournal.databinding.NoteItemBinding
import fh.campus.djournal.models.Note

class NoteListAdapter (
    private var dataSet: List<Note>,
    val onNoteItemClicked: (Note) -> Unit,
) :
    RecyclerView.Adapter<NoteListAdapter.ViewHolder>(){

    fun updateDataSet(notes: List<Note>) {
        dataSet = notes
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
        }

        fun bind(noteItem: Note) {
            with(binding) {
                note = noteItem
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
                R.layout.note_item,
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