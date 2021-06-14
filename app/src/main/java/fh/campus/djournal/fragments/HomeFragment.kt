package fh.campus.djournal.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fh.campus.djournal.R
import fh.campus.djournal.adapters.JournalListAdapter
import fh.campus.djournal.database.AppDatabase
import fh.campus.djournal.databinding.FragmentHomeBinding
import fh.campus.djournal.databinding.JournalItemBinding
import fh.campus.djournal.models.Journal
import fh.campus.djournal.repositories.JournalRepository
import fh.campus.djournal.viewmodels.JournalViewModel
import fh.campus.djournal.viewmodels.JournalViewModelFactory

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var journalViewModel: JournalViewModel
    private lateinit var viewModelFactory: JournalViewModelFactory
    private var newTitle = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        setHasOptionsMenu(true) // enable the options menu in the action bar

        val adapter = JournalListAdapter(
            dataSet = listOf(),     // start with empty list
            onJournalItemClicked = { journal -> journalOptionDialog(journal) }
        )    // instantiate a new MovieListAdapter for recyclerView
        binding.journalList.adapter = adapter // assign adapter to the recyclerView

        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getDatabase(application).journalDao
        val repository = JournalRepository.getInstance(dataSource)
        viewModelFactory = JournalViewModelFactory(repository)

        journalViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(JournalViewModel::class.java)

        binding.lifecycleOwner = this
        binding.journalTrackerViewModel = journalViewModel

        journalViewModel.journals.observe(
            viewLifecycleOwner,
            Observer { journals -> adapter.updateDataSet(journals) })

        binding.addNewJournal.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCreateNewJournalFragment())
        }

        return binding.root
    }

    private fun journalOptionDialog(journal: Journal) {
        val items = arrayOf("Rename", "New Description", "Delete")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Options")
            .setItems(items) { dialog, which ->
                when (which) {
                    0 -> {
                        updateNameDialog(journal)
                    }
                    1 -> {
                        Log.i("AAAA", "New Description")
                    }
                    2 -> {
                        deleteConfirmationDialog(journal)
                    }
                }
            }
            .setPositiveButton("CANCEL") { dialog, which ->
                dialog.cancel()
            }
            .show()
    }

    private fun deleteConfirmationDialog(journal: Journal) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("DANGER")
            .setMessage("Are you sure you want to delete this journal?")
            .setPositiveButton("CONFIRM") { dialog, which ->
                journalViewModel.deleteJournal(journal)
            }
            .setNegativeButton("CANCEL") { dialog, which ->
                dialog.cancel()
            }
            .show()
    }

    private fun updateNameDialog(journal: Journal) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Rename")
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton(
            "Save"
        ) { dialog, which ->
            newTitle = input.text.toString()
            journal.title = newTitle
            journalViewModel.updateJournal(journal)
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }

        builder.show()
    }
}