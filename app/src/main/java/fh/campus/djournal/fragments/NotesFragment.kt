package fh.campus.djournal.fragments

import android.os.Bundle
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
import fh.campus.djournal.utils.NoteStore
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

//
//        when(val movieEntry = NoteStore().findMovieByUUID(args.movieId)){
//            null -> {
//                Toast.makeText(requireContext(), "Could not load movie data", Toast.LENGTH_SHORT).show()
//                findNavController().navigateUp()
//            }
//            else -> binding.movie = movieEntry
//        }


        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getDatabase(application).noteDao
        val repository = NoteRepository.getInstance(dataSource)
        viewModelFactory = NoteViewModelFactory(repository)

        noteViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(NoteViewModel::class.java)


        val adapter = NoteListAdapter(
            dataSet = listOf(),
            onNoteItemClicked = { note ->  }
        )    // instantiate a new MovieListAdapter for recyclerView
        binding.noteList.adapter = adapter // assign adapter to the recyclerView

        binding.lifecycleOwner = this
        binding.noteTrackerViewModel = noteViewModel

        noteViewModel.notes.observe(
            viewLifecycleOwner,
            Observer { notes -> adapter.updateDataSet(notes) })

        binding.addNewNote.setOnClickListener {
            noteViewModel.addNote(NoteStore().defaultNotes[0])
            noteViewModel.addNote(NoteStore().defaultNotes[1])
            noteViewModel.addNote(NoteStore().defaultNotes[2])
            noteViewModel.addNote(NoteStore().defaultNotes[3])
        }

        return binding.root
    }

}