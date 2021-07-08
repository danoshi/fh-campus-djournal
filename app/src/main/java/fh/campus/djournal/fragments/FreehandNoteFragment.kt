package fh.campus.djournal.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.outsbook.libs.canvaseditor.CanvasEditorView
import com.outsbook.libs.canvaseditor.listeners.CanvasEditorListener
import fh.campus.djournal.R
import fh.campus.djournal.database.AppDatabase
import fh.campus.djournal.databinding.FragmentFreehandNoteBinding
import fh.campus.djournal.models.Note
import fh.campus.djournal.repositories.NoteRepository
import fh.campus.djournal.utils.Util
import fh.campus.djournal.viewmodels.NoteViewModel
import fh.campus.djournal.viewmodels.NoteViewModelFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class FreehandNoteFragment : Fragment() {
    private lateinit var binding: FragmentFreehandNoteBinding
    private lateinit var canvasEditor: CanvasEditorView
    private lateinit var viewModelFactory: NoteViewModelFactory
    private lateinit var noteViewModel: NoteViewModel
    private var dirPath = ""
    private var fileName = ""
    private var filePath = ""
    private var noteId = 0L
    private var journalId = 0L
    private lateinit var noteObj: Note
    private var strokeWidth: Float = 10f


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_freehand_note, container, false)

        setHasOptionsMenu(true)

        canvasEditor = binding.canvasEditor
        initValue()
        initClickListener()
        initCanvasEditorListener()

        val application = requireNotNull(this.activity).application
        val args = FreehandNoteFragmentArgs.fromBundle(requireArguments())
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
                    binding.freehandNoteName.setText(note.name)
                    binding.freehandNoteDate.text = note.timestamp
                    canvasEditor.addBitmapSticker(BitmapFactory.decodeFile(note.freehandPath))
                })
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            when {
                noteId.equals(-1L) -> {
                    val freehandNote = canvasEditor.downloadBitmap()
                    saveNewFreehandNoteDialog(
                        binding.freehandNoteName.text.toString(),
                        journalId,
                        freehandNote
                    )
                }
                else -> {
                    val freehandNote = canvasEditor.downloadBitmap()
                    saveFreehandNoteDialog(
                        noteObj,
                        journalId,
                        freehandNote
                    )
                }
            }
        }

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //TODO: normaly it should be R.id.***
            16908332 -> {
                when {
                    noteId.equals(-1L) -> {
                        val freehandNote = canvasEditor.downloadBitmap()
                        saveNewFreehandNoteDialog(
                            binding.freehandNoteName.text.toString(),
                            journalId,
                            freehandNote
                        )
                    }
                    else -> {
                        val freehandNote = canvasEditor.downloadBitmap()
                        saveFreehandNoteDialog(
                            noteObj,
                            journalId,
                            freehandNote
                        )
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun initValue() {
        binding.buttonUndo.imageAlpha = 50
        binding.buttonRedo.imageAlpha = 50
        canvasEditor.setStrokeWidth(strokeWidth)
        canvasEditor.setPaintColor(ContextCompat.getColor(requireContext(), R.color.black))
    }

    private fun initClickListener() {
        binding.btnArrow.setOnClickListener {
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow)
            drawable?.let {
                canvasEditor.addDrawableSticker(drawable)
            }
        }
        binding.btnRectangle.setOnClickListener {
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_rectangle)
            drawable?.let {
                canvasEditor.addDrawableSticker(drawable)
            }
        }
        binding.btnCircle.setOnClickListener {
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_black_circle)
            drawable?.let {
                canvasEditor.addDrawableSticker(drawable)
            }
        }
        binding.btnLine.setOnClickListener {
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_line)
            drawable?.let {
                canvasEditor.addDrawableSticker(drawable)
            }
        }

        binding.buttonSave.setOnClickListener {
            val freehandNote = canvasEditor.downloadBitmap()
            when {
                noteId.equals(-1L) -> {
                    saveNewFreehandNoteDialog(
                        binding.freehandNoteName.text.toString(),
                        journalId,
                        freehandNote
                    )
                }
                else -> {
                    saveFreehandNoteDialog(
                        noteObj,
                        journalId,
                        freehandNote
                    )
                }
            }
        }


        binding.buttonUndo.setOnClickListener {
            canvasEditor.undo()
        }

        binding.buttonDelete.setOnClickListener {
            canvasEditor.removeAll()
        }

        binding.buttonRedo.setOnClickListener {
            canvasEditor.redo()
        }
    }

    private fun initCanvasEditorListener() {
        canvasEditor.setListener(object : CanvasEditorListener {
            override fun onEnableUndo(isEnable: Boolean) {
                binding.buttonUndo.imageAlpha = if (isEnable) 255 else 50
            }

            override fun onEnableRedo(isEnable: Boolean) {
                binding.buttonRedo.imageAlpha = if (isEnable) 255 else 50
            }

        })
    }

    private fun saveNewFreehandNoteDialog(
        newNoteName: String,
        journalId: Long,
        bitmap: Bitmap
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("SAVE NOTE")
            .setMessage("Do you want to save your changes")
            .setPositiveButton("CONFIRM") { dialog, which ->
                val filePath = saveBitmap(bitmap)
                val newNote =
                    Note(newNoteName, journalId, "", Util().getDateTime(), "", filePath)
                noteViewModel.addNote(newNote)
                findNavController().navigate(
                    FreehandNoteFragmentDirections.actionFreehandNoteFragmentToNotesFragment(
                        journalId
                    )
                )
            }
            .setNegativeButton("DON'T SAVE") { dialog, which ->
                findNavController().navigate(
                    FreehandNoteFragmentDirections.actionFreehandNoteFragmentToNotesFragment(
                        journalId
                    )
                )
            }
            .setNeutralButton("CANCEL") { dialog, which ->
                dialog.cancel()
            }
            .show()

    }

    private fun saveFreehandNoteDialog(
        noteObj: Note,
        journalId: Long,
        bitmap: Bitmap
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("SAVE NOTE")
            .setMessage("Do you want to save your changes")
            .setPositiveButton("CONFIRM") { dialog, which ->
                noteObj.name = binding.freehandNoteName.text.toString()
                noteObj.freehandPath = saveBitmap(bitmap)
                noteViewModel.updateNote(noteObj)
                findNavController().navigate(
                    FreehandNoteFragmentDirections.actionFreehandNoteFragmentToNotesFragment(
                        journalId
                    )
                )
            }
            .setNegativeButton("DON'T SAVE") { dialog, which ->
                findNavController().navigate(
                    FreehandNoteFragmentDirections.actionFreehandNoteFragmentToNotesFragment(
                        journalId
                    )
                )
            }
            .setNeutralButton("CANCEL") { dialog, which ->
                dialog.cancel()
            }
            .show()

    }

    private fun saveBitmap(freehandNote: Bitmap): String {
        dirPath = "${activity?.externalCacheDir?.absolutePath}/"

        val simpleDateFormat = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss", Locale.GERMAN)
        val date = simpleDateFormat.format(Date())
        fileName = "freehand_note_$date"

        try {
            val bytes = ByteArrayOutputStream()
            freehandNote.compress(Bitmap.CompressFormat.PNG, 90, bytes)
            filePath = "$dirPath$fileName.png"
            val file = File(filePath)
            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(bytes.toByteArray())
            fileOutputStream.close()

        } catch (e: IOException) {
        }

        return filePath

    }

}