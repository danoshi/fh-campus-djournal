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
import fh.campus.djournal.R
import fh.campus.djournal.adapters.NoteListAdapter
import fh.campus.djournal.database.AppDatabase
import fh.campus.djournal.databinding.FragmentNotesBinding
import fh.campus.djournal.models.Note
import fh.campus.djournal.repositories.NoteRepository
import fh.campus.djournal.utils.NoteDialogs
import fh.campus.djournal.utils.ToastMaker
import fh.campus.djournal.utils.Util
import fh.campus.djournal.viewmodels.NoteViewModel
import fh.campus.djournal.viewmodels.NoteViewModelFactory

class NotesFragment : Fragment() {
    private lateinit var binding: FragmentNotesBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var viewModelFactory: NoteViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notes, container, false)

        setHasOptionsMenu(true) // enable the options menu in the action bar

        val args = NotesFragmentArgs.fromBundle(requireArguments())

        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getDatabase(application).noteDao
        val repository = NoteRepository.getInstance(dataSource)
        viewModelFactory = NoteViewModelFactory(repository)

        noteViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(NoteViewModel::class.java)

        val dialog = NoteDialogs(requireContext(), noteViewModel)

        val adapter = NoteListAdapter(
            dataSet = listOf(),
            onNoteItemShortClicked = { note -> },
            onNoteItemLongClicked = { note -> dialog.noteOptionDialog(note)},
        )    // instantiate a new MovieListAdapter for recyclerView
        binding.noteList.adapter = adapter // assign adapter to the recyclerView

        binding.lifecycleOwner = this
        binding.noteTrackerViewModel = noteViewModel

        noteViewModel.getNotesFromJournal(args.journalId).observe(
            viewLifecycleOwner, Observer { note ->
                adapter.updateDataSet(note)
            }
        )

        binding.addNewNote.setOnClickListener {
            val note = Note(Math.random().toString(), args.journalId, "this is my testitest note", Util().getDateTime())
            noteViewModel.addNote(note)
        }

        //TODO: remove later
        binding.addNewNote.setOnLongClickListener {
            noteViewModel.clearNotesFromJournal(args.journalId)
            true
        }

        noteViewModel.notes.observe(
            viewLifecycleOwner, Observer { note ->
                Log.i("NOTES", note.toString())
            }
        )



        return binding.root
    }


}