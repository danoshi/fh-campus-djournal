package fh.campus.djournal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import fh.campus.djournal.R
import fh.campus.djournal.adapters.JournalListAdapter
import fh.campus.djournal.database.AppDatabase
import fh.campus.djournal.databinding.FragmentHomeBinding
import fh.campus.djournal.utils.Dialogs
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


        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getDatabase(application).journalDao
        val repository = JournalRepository.getInstance(dataSource)
        viewModelFactory = JournalViewModelFactory(repository)

        journalViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(JournalViewModel::class.java)

        val dialog = Dialogs(requireContext(), journalViewModel)

        val adapter = JournalListAdapter(
            dataSet = listOf(),     // start with empty list
            onJournalItemLongClicked = { journal -> dialog.journalOptionDialog(journal) },
            onJournalItemShortClicked = { journal ->  findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNotesFragment(journal.journalId))}
        )    // instantiate a new MovieListAdapter for recyclerView
        binding.journalList.adapter = adapter // assign adapter to the recyclerView

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


}