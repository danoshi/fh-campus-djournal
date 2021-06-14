package fh.campus.djournal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import fh.campus.djournal.R
import fh.campus.djournal.database.AppDatabase
import fh.campus.djournal.databinding.FragmentCreateNewJournalBinding
import fh.campus.djournal.models.Journal
import fh.campus.djournal.repositories.JournalRepository
import fh.campus.djournal.viewmodels.JournalViewModel
import fh.campus.djournal.viewmodels.JournalViewModelFactory

class CreateNewJournalFragment : Fragment() {
    private lateinit var binding: FragmentCreateNewJournalBinding
    private lateinit var journalViewModel: JournalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_new_journal,
            container,
            false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getDatabase(application).journalDao
        val repository = JournalRepository.getInstance(dataSource)
        val viewModelFactory = JournalViewModelFactory(repository)

        journalViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(JournalViewModel::class.java)

        binding.lifecycleOwner = this
        binding.journalTrackerViewModel = journalViewModel

        binding.btnCreate.setOnClickListener {
            val title = binding.editTextNewJournalName.text.toString()
            val description = binding.editTextNewJournalDescription.text.toString()
            val color = binding.editTextNewJournalColor.text.toString()
            if (title.isEmpty() && description.isEmpty()) {
                binding.editTextNewJournalName.setError("Please Enter a Name")
                binding.editTextNewJournalDescription.setError("Please Enter a Description")
            } else if (title.isEmpty()) {
                binding.editTextNewJournalName.setError("Please Enter a Name")
            } else if (description.isEmpty()) {
                binding.editTextNewJournalDescription.setError("Please Enter a Description")
            } else {
                val newJournal = Journal(title, description)
                journalViewModel.addJournal(newJournal)
                findNavController().navigate(
                    CreateNewJournalFragmentDirections.actionCreateNewJournalFragmentToHomeFragment(
                    )
                )
            }
        }

        binding.btnCancel.setOnClickListener {
            onCancel()
        }


        return binding.root
    }

    private fun onCancel() {
        findNavController().navigate(
            CreateNewJournalFragmentDirections.actionCreateNewJournalFragmentToHomeFragment(
            )
        )
    }

}