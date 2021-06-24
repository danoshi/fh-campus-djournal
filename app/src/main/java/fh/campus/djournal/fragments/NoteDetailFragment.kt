package fh.campus.djournal.fragments

import android.os.Bundle
import android.util.Log
import android.util.Log.INFO
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.text.toSpannable
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
import fh.campus.djournal.utils.ToastMaker
import fh.campus.djournal.viewmodels.NoteViewModel
import fh.campus.djournal.viewmodels.NoteViewModelFactory
import jp.wasabeef.richeditor.RichEditor
import java.util.logging.Level.INFO

class NoteDetailFragment : Fragment() {
    private lateinit var binding: FragmentNoteDetailBinding
    private lateinit var viewModelFactory: NoteViewModelFactory
    private lateinit var noteViewModel: NoteViewModel
    private var journalId: Long = 0
    private lateinit var noteObj: Note
    private lateinit var mEditor: RichEditor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_detail, container, false)

        setHasOptionsMenu(true)

        mEditor = binding.editor
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setPadding(10, 10, 10, 10);


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
                mEditor.html = note.text
//                binding.noteDetailText.setText(note.text)
                journalId = note.journalIdOfNote

            }
        )


        requireActivity().onBackPressedDispatcher.addCallback(this) {
            saveNoteDialog(
                noteObj,
                binding.noteDetailName.text.toString(),
                mEditor.html,
                journalId,
            )

        }

        val spannable = binding.noteDetailText
        val a = spannable.selectionStart
        val b = spannable.selectionEnd

        binding.btn.setOnClickListener {
            ToastMaker().toastMaker(requireContext(), spannable.length().toString())
        }

        binding.btn2.setOnClickListener {
            ToastMaker().toastMaker(requireContext(), spannable.selectionStart.toString())
        }

        binding.btn3.setOnClickListener {
            ToastMaker().toastMaker(requireContext(), spannable.selectionEnd.toString())
        }

        binding.actionBold.setOnClickListener {
            mEditor.setBold()
        }

        binding.actionItalic.setOnClickListener {
            mEditor.setItalic()
        }

        binding.actionUnderline.setOnClickListener {
            mEditor.setUnderline()
        }

        binding.actionAlignLeft.setOnClickListener {
            mEditor.setAlignLeft()
        }

        binding.actionAlignRight.setOnClickListener {
            mEditor.setAlignRight()
        }

        binding.actionAlignCenter.setOnClickListener {
            mEditor.setAlignCenter()
        }

        binding.actionHeading1.setOnClickListener {
            mEditor.setHeading(1)
        }

        binding.actionHeading2.setOnClickListener {
            mEditor.setHeading(2)
        }

        binding.actionHeading3.setOnClickListener {
            mEditor.setHeading(3)
        }

        binding.actionInsertBullets.setOnClickListener {
            mEditor.setBullets()
        }

        binding.actionInsertNumbers.setOnClickListener {
            mEditor.setNumbers()
        }

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //TODO: normaly it should be R.id.***
            16908332 -> {
                saveNoteDialog(
                    noteObj,
                    binding.noteDetailName.text.toString(),
                    mEditor.html,
                    journalId
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }


    private fun saveNoteDialog(
        noteObj: Note,
        newNoteName: String,
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