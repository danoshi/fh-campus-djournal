package fh.campus.djournal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.azeesoft.lib.colorpicker.ColorPickerDialog
import com.azeesoft.lib.colorpicker.ColorPickerDialog.OnColorPickedListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fh.campus.djournal.R
import fh.campus.djournal.database.AppDatabase
import fh.campus.djournal.databinding.FragmentNoteDetailBinding
import fh.campus.djournal.models.Note
import fh.campus.djournal.repositories.NoteRepository
import fh.campus.djournal.utils.Util
import fh.campus.djournal.viewmodels.NoteViewModel
import fh.campus.djournal.viewmodels.NoteViewModelFactory
import jp.wasabeef.richeditor.RichEditor


class NoteDetailFragment : Fragment() {
    private lateinit var binding: FragmentNoteDetailBinding
    private lateinit var viewModelFactory: NoteViewModelFactory
    private lateinit var noteViewModel: NoteViewModel
    private var journalId: Long = 0L
    private var noteId: Long = 0L
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
        noteId = args.noteId
        journalId = args.journalId

        val dataSource = AppDatabase.getDatabase(application).noteDao
        val repository = NoteRepository.getInstance(dataSource)
        viewModelFactory = NoteViewModelFactory(repository)

        noteViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(NoteViewModel::class.java)


        if (!noteId.equals(-1L)) {
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
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (noteId.equals(-1L)) {
                saveNewNoteDialog(
                    binding.noteDetailName.text.toString(),
                    mEditor.html!!,
                    journalId,
                )
            } else {
                saveNoteDialog(
                    noteObj,
                    binding.noteDetailName.text.toString(),
                    mEditor.html!!,
                    journalId,
                )
            }
        }


        val items = listOf("arial", "serif", "monospace", "cursive")
        val adapter = ArrayAdapter(requireContext(), R.layout.font_item, items)
        (binding.menu.editText as? AutoCompleteTextView)?.setAdapter(adapter)


        binding.fontOptions.setOnItemClickListener { parent, view, position, id ->
            when (id) {
                0L -> {
                    changeFont("arial", mEditor.html)
                }
                1L -> {
                    changeFont("serif", mEditor.html)
                }
                2L -> {
                    changeFont("monospace", mEditor.html)
                }
                3L -> {
                    changeFont("cursive", mEditor.html)
                }
            }
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

        val colorPickerDialog = ColorPickerDialog.createColorPickerDialog(requireContext())
        colorPickerDialog.setOnColorPickedListener(OnColorPickedListener { color, hexVal ->
            mEditor.setTextColor(color)
        })

        binding.actionTxtColor.setOnClickListener {
            colorPickerDialog.show()
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
                when {
                    mEditor.html == null -> {
                        findNavController().navigate(
                            NoteDetailFragmentDirections.actionNoteDetailFragmentToNotesFragment(
                                journalId
                            )
                        )
                    }
                    noteId.equals(-1L) -> {
                        saveNewNoteDialog(
                            binding.noteDetailName.text.toString(),
                            mEditor.html!!,
                            journalId,
                        )
                    }
                    else -> {
                        saveNoteDialog(
                            noteObj,
                            binding.noteDetailName.text.toString(),
                            mEditor.html!!,
                            journalId,
                        )
                    }
                }
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
                if (noteObj.timestamp.equals("")) {
                    noteViewModel.addNote(
                        Note(
                            newNoteName,
                            journalId,
                            newNoteText,
                            Util().getDateTime()
                        )
                    )
                } else {
                    noteObj.name = newNoteName
                    noteObj.text = newNoteText
                    noteViewModel.updateNote(noteObj)
                }
                findNavController().navigate(
                    NoteDetailFragmentDirections.actionNoteDetailFragmentToNotesFragment(
                        journalId
                    )
                )
            }
            .setNegativeButton("DON'T SAVE") { dialog, which ->
                findNavController().navigate(
                    NoteDetailFragmentDirections.actionNoteDetailFragmentToNotesFragment(
                        journalId
                    )
                )
            }
            .setNeutralButton("CANCEL") { dialog, which ->
                dialog.cancel()
            }
            .show()

    }

    private fun saveNewNoteDialog(
        newNoteName: String,
        newNoteText: String = "",
        journalId: Long,
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("SAVE NOTE")
            .setMessage("Do you want to save your changes")
            .setPositiveButton("CONFIRM") { dialog, which ->
                val newNote = Note(newNoteName, journalId, newNoteText, Util().getDateTime())
                noteViewModel.addNote(newNote)
                findNavController().navigate(
                    NoteDetailFragmentDirections.actionNoteDetailFragmentToNotesFragment(
                        journalId
                    )
                )
            }
            .setNegativeButton("DON'T SAVE") { dialog, which ->
                findNavController().navigate(
                    NoteDetailFragmentDirections.actionNoteDetailFragmentToNotesFragment(
                        journalId
                    )
                )
            }
            .setNeutralButton("CANCEL") { dialog, which ->
                dialog.cancel()
            }
            .show()

    }

    private fun trimmText(text: String): String {
        val endTag = "</font>"
        val textWithoutEndTag = text.dropLast(endTag.length)
        return textWithoutEndTag.substringAfter(">")
    }

    private fun changeFont(font: String, text: String) {
        val fontTag = "<font face='${font}'>"
        val endTag = "</font>"
        if (mEditor.html.contains(font)) {
            val newText = trimmText(mEditor.html)
            mEditor.html = newText
        } else if (mEditor.html.contains("font")) {
            val newText = trimmText(mEditor.html)
            mEditor.html = fontTag + newText + endTag
        } else {
            val newText = fontTag + text + endTag
            mEditor.html = newText
        }
    }

}