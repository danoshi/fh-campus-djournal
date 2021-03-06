package fh.campus.djournal.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import fh.campus.djournal.R
import fh.campus.djournal.databinding.JournalItemBinding
import fh.campus.djournal.models.Journal

class JournalListAdapter(
    private var dataSet: List<Journal>,
    val onJournalItemLongClicked: (Journal) -> Unit,
    val onJournalItemShortClicked: (Journal) -> Unit,
) :
    RecyclerView.Adapter<JournalListAdapter.ViewHolder>() {

    fun updateDataSet(journals: List<Journal>) {
        dataSet = journals
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: JournalItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.journalItem.setOnLongClickListener {
                binding.journal?.let { journal ->
                    onJournalItemLongClicked(journal)
                }
                true
            }
            binding.journalItem.setOnClickListener {
                binding.journal?.let { journal ->
                    onJournalItemShortClicked(journal)
                }
            }
        }

        fun bind(journalItem: Journal) {
            with(binding) {
                journal = journalItem
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
                R.layout.journal_item,
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
