package fh.campus.djournal.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        setHasOptionsMenu(true) // enable the options menu in the action bar

        val adapter = JournalListAdapter(
            dataSet = listOf(),     // start with empty list
            onJournalItemClicked = {journal -> journalOptionDialog(journal) }
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
                if (which == 0) {
                    Log.i("AAAA", "RENAME")
                } else if (which == 1) {
                    Log.i("AAAA", "New Description")
                } else if (which == 2) {
                    journalViewModel.deleteJournal(journal)
                }
            }
            .setPositiveButton("CANCEL") { dialog, which ->
                dialog.cancel()
            }
            .show()
    }
}