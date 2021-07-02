package fh.campus.djournal.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import fh.campus.djournal.R
import fh.campus.djournal.databinding.RecordItemBinding
import fh.campus.djournal.models.AudioRecord

class AudioRecordListAdapter(
    private var dataSet: List<AudioRecord>,
    val onRecordItemShortClicked: (AudioRecord) -> Unit,
    val onRecordItemLongClicked: (AudioRecord) -> Unit,
) :
    RecyclerView.Adapter<AudioRecordListAdapter.ViewHolder>() {

    fun updateDataSet(records: List<AudioRecord>) {
        dataSet = records
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: RecordItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.recordItem.setOnLongClickListener {
                binding.record?.let { record ->
                    onRecordItemLongClicked(record)
                }
                true
            }
            binding.recordItem.setOnClickListener {
                binding.record?.let { record ->
                    onRecordItemShortClicked(record)
                }
            }
        }

        fun bind(recordItem: AudioRecord) {
            with(binding) {
                record = recordItem
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
                R.layout.record_item,
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
