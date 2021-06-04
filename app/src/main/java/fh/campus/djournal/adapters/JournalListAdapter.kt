package fh.campus.djournal.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fh.campus.djournal.R
import fh.campus.djournal.databinding.JournalItemBinding
import fh.campus.djournal.models.Journal

class JournalListAdapter :
    ListAdapter<Journal, RecyclerView.ViewHolder>(
        JournalDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.journal_item,
                parent,
                false
            ),   //the binding
        )
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [ViewHolder] to reflect the item at the given
     * position.
     * */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //call the custom bind function from ViewHolder class
        if (holder is ViewHolder) {
            holder.bind(getItem(position))
        }
    }

    class ViewHolder(
        private val binding: JournalItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

//        init {
//            binding.goToDetailBtn.setOnClickListener{ btnView ->
//                binding.movie?.id?.let { itemId->
//                    btnView.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment(itemId.toString()))
//                }
//            }
//        }

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
}

private class JournalDiffCallback : DiffUtil.ItemCallback<Journal>() {
    override fun areItemsTheSame(
        oldItem: Journal,
        newItem: Journal
    ): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(
        oldItem: Journal,
        newItem: Journal
    ): Boolean {
        return oldItem == newItem
    }
}
