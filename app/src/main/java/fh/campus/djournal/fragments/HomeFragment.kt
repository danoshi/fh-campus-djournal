package fh.campus.djournal.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fh.campus.djournal.R
import fh.campus.djournal.adapters.JournalListAdapter
import fh.campus.djournal.database.AppDatabase
import fh.campus.djournal.databinding.FragmentHomeBinding
import fh.campus.djournal.models.Journal
import fh.campus.djournal.repositories.AudioRecordRepository
import fh.campus.djournal.repositories.JournalRepository
import fh.campus.djournal.utils.JournalDialogs
import fh.campus.djournal.viewmodels.AudioRecordViewModel
import fh.campus.djournal.viewmodels.AudioRecordViewModelFactory
import fh.campus.djournal.viewmodels.JournalViewModel
import fh.campus.djournal.viewmodels.JournalViewModelFactory

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var journalViewModel: JournalViewModel
    private lateinit var viewModelFactory: JournalViewModelFactory
    private lateinit var recordViewModel: AudioRecordViewModel
    private lateinit var recordViewModelFactory: AudioRecordViewModelFactory
    private lateinit var auth: FirebaseAuth
    private lateinit var journalsToLog: List<Journal>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        auth = Firebase.auth
        setHasOptionsMenu(true) // enable the options menu in the action bar


        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getDatabase(application).journalDao
        val repository = JournalRepository.getInstance(dataSource)
        viewModelFactory = JournalViewModelFactory(repository)

        journalViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(JournalViewModel::class.java)

        val recordDataSource = AppDatabase.getDatabase(application).recordDao
        val recordRepo = AudioRecordRepository.getInstance(recordDataSource)
        recordViewModelFactory = AudioRecordViewModelFactory(recordRepo)
        recordViewModel =
            ViewModelProvider(
                this, recordViewModelFactory
            ).get(AudioRecordViewModel::class.java)

        val dialog = JournalDialogs(requireContext(), journalViewModel, recordViewModel)

        val adapter = JournalListAdapter(
            dataSet = listOf(),     // start with empty list
            onJournalItemLongClicked = { journal -> dialog.journalOptionDialog(journal) },
            onJournalItemShortClicked = { journal ->
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToNotesFragment(journal.journalId)
                )
            }
        )    // instantiate a new MovieListAdapter for recyclerView
        binding.journalList.adapter = adapter // assign adapter to the recyclerView

        binding.lifecycleOwner = this
        binding.journalTrackerViewModel = journalViewModel

        journalViewModel.journals.observe(
            viewLifecycleOwner,
            Observer { journals ->
                adapter.updateDataSet(journals)
                journalsToLog = journals
            })

        binding.addNewJournal.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCreateNewJournalFragment())
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        val user = auth.uid
        Log.i("Users_UUID:", user.toString())
        if (currentUser != null) {
            reload()
        }
    }

    private fun reload() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)

    }
}