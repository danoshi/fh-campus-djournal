package fh.campus.djournal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fh.campus.djournal.R
import fh.campus.djournal.database.AppDatabase
import fh.campus.djournal.databinding.FragmentNewNoteBinding
import fh.campus.djournal.models.Note
import fh.campus.djournal.repositories.NoteRepository
import fh.campus.djournal.utils.Util
import fh.campus.djournal.viewmodels.NoteViewModel
import fh.campus.djournal.viewmodels.NoteViewModelFactory


class NewNoteFragment : Fragment() {
    private lateinit var binding: FragmentNewNoteBinding
    private lateinit var viewModelFactory: NoteViewModelFactory
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var args: NewNoteFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_note, container, false)

        setHasOptionsMenu(true)

        args = NewNoteFragmentArgs.fromBundle(requireArguments())

        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getDatabase(application).noteDao
        val repository = NoteRepository.getInstance(dataSource)
        viewModelFactory = NoteViewModelFactory(repository)

        noteViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(NoteViewModel::class.java)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            saveNoteDialog(
                binding.newNoteName.text.toString(),
                binding.newNoteText.text.toString(),
                args.journalId
            )

        }

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        saveNoteDialog(
            binding.newNoteName.text.toString(),
            binding.newNoteText.text.toString(),
            args.journalId
        )

        return true
//        return NavigationUI.onNavDestinationSelected(
//            item!!,
//            requireView().findNavController()
//        )
//                || super.onOptionsItemSelected(item)
    }


    // TODO: refactor
    private fun saveNoteDialog(
        noteTitle: String = "default title",
        noteText: String,
        journalId: Long,
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("SAVE NOTE")
            .setMessage("Do you want to save your changes")
            .setPositiveButton("CONFIRM") { dialog, which ->
                val newNote = Note(noteTitle, journalId, noteText, Util().getDateTime())
                noteViewModel.addNote(newNote)
                findNavController().navigate(
                    NewNoteFragmentDirections.actionNewNoteFragmentToNotesFragment(
                        journalId
                    )
                )
            }
            .setNegativeButton("DON'T SAVE") { dialog, which ->
                findNavController().navigate(
                    NewNoteFragmentDirections.actionNewNoteFragmentToNotesFragment(
                        journalId
                    )
                )
            }
            .setNeutralButton("CANCEL") { dialog, which ->
                dialog.cancel()
            }
            .show()

    }
}