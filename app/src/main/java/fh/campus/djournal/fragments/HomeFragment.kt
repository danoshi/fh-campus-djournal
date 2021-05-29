package fh.campus.djournal.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fh.campus.djournal.R
import fh.campus.djournal.adapters.JournalListAdapter
import fh.campus.djournal.databinding.FragmentHomeBinding
import fh.campus.djournal.databinding.NewJournalDialogBinding
import fh.campus.djournal.models.Journal
import fh.campus.djournal.models.JournalStore

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dialogBinding: NewJournalDialogBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        setHasOptionsMenu(true) // enable the options menu in the action bar

        val adapter = JournalListAdapter()    // instantiate a new MovieListAdapter for recyclerView
        binding.journalList.adapter = adapter // assign adapter to the recyclerView

        subscribeUI(adapter)

        binding.addNewJournal.setOnClickListener {
            addNewJournal()
        }

        return binding.root
    }

    private fun subscribeUI(adapter: JournalListAdapter) {
        //TODO remove later only for testing purposes, or change it so it fetches already created journals from user
        val journalList = JournalStore()
        adapter.submitList(journalList.defaultJournals)
    }

    private fun addNewJournal() {
        MaterialAlertDialogBuilder(requireContext())
            .setView(R.layout.new_journal_dialog)
            .setNeutralButton("Cancel") { dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton("Create") { dialog, which ->
                val journalTitle = dialogBinding.editTextJournalTitle
                val journalDescription = dialogBinding.editTextJournalDescription
                val journalColor = dialogBinding.editTextJournalColor
                toastMaker("New Journal was created")
            }
            .show()
    }

    private fun toastMaker(msg: String) {
        Toast.makeText(
            requireContext(),
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }
}