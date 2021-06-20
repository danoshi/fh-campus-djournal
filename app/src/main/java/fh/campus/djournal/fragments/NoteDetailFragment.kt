package fh.campus.djournal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fh.campus.djournal.R
import fh.campus.djournal.database.AppDatabase
import fh.campus.djournal.databinding.FragmentNoteDetailBinding
import fh.campus.djournal.models.Note
import fh.campus.djournal.repositories.NoteRepository
import fh.campus.djournal.viewmodels.NoteViewModel
import fh.campus.djournal.viewmodels.NoteViewModelFactory

class NoteDetailFragment : Fragment() {
    private lateinit var binding: FragmentNoteDetailBinding
    private lateinit var viewModelFactory: NoteViewModelFactory
    private lateinit var noteViewModel: NoteViewModel
    private var journalId: Long = 0
    private lateinit var noteObj: Note

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_detail, container, false)

        setHasOptionsMenu(true)

        val application = requireNotNull(this.activity).application
        val args = NoteDetailFragmentArgs.fromBundle(requireArguments())

        val dataSource = AppDatabase.getDatabase(application).noteDao
        val repository = NoteRepository.getInstance(dataSource)
        viewModelFactory = NoteViewModelFactory(repository)

        noteViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(NoteViewModel::class.java)


        noteViewModel.getNoteById(args.noteId).observe(
            viewLifecycleOwner, Observer { note ->
                noteObj = note
                binding.noteDetailName.setText(note.name)
                binding.noteDetailDate.text = note.timestamp
                binding.noteDetailText.setText(note.text)
                journalId = note.journalIdOfNote

            }
        )


        requireActivity().onBackPressedDispatcher.addCallback(this) {
            saveNoteDialog(
                noteObj,
                binding.noteDetailName.text.toString(),
                binding.noteDetailText.text.toString(),
                journalId,
            )

        }

        return binding.root
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        Log.i("updateNote", binding.noteDetailName.text.toString())
//        Log.i("updateNote", binding.noteDetailText.text.toString())
//        saveNoteDialog(
//        )
//
//        return true
//        return NavigationUI.onNavDestinationSelected(
//            item!!,
//            requireView().findNavController()
//        )
//                || super.onOptionsItemSelected(item)
//    }


    private fun saveNoteDialog(
        noteObj: Note,
        newNoteName: String = "default title",
        newNoteText: String,
        journalId: Long,
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("SAVE NOTE")
            .setMessage("Do you want to save your changes")
            .setPositiveButton("CONFIRM") { dialog, which ->
                noteObj.name = newNoteName
                noteObj.text = newNoteText
                noteViewModel.updateNote(noteObj)
                findNavController().navigate(
                    NoteDetailFragmentDirections.actionNoteDetailFragmentToNotesFragment(journalId)
                )
            }
            .setNegativeButton("DON'T SAVE") { dialog, which ->
                findNavController().navigate(
                    NoteDetailFragmentDirections.actionNoteDetailFragmentToNotesFragment(journalId)
                )
            }
            .setNeutralButton("CANCEL") { dialog, which ->
                dialog.cancel()
            }
            .show()

    }
}