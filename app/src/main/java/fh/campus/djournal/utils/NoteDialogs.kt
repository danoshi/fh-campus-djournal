package fh.campus.djournal.utils

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.widget.EditText
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fh.campus.djournal.models.Note
import fh.campus.djournal.viewmodels.NoteViewModel

class NoteDialogs(private val context: Context, private val noteViewModel: NoteViewModel) {
    private var newTitle = ""

    fun noteOptionDialog(note: Note) {
        val items = arrayOf("Rename", "Delete")
        MaterialAlertDialogBuilder(context)
            .setTitle("Options")
            .setItems(items) { dialog, which ->
                when (which) {
                    0 -> {
                        updateNameDialog(note)
                    }
                    1 -> {
                        deleteConfirmationDialog(note)
                    }
                }
            }
            .setPositiveButton("CANCEL") { dialog, which ->
                dialog.cancel()
            }
            .show()
    }

    private fun deleteConfirmationDialog(note: Note) {
        MaterialAlertDialogBuilder(context)
            .setTitle("DANGER")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("CONFIRM") { dialog, which ->
                noteViewModel.deleteNote(note)
            }
            .setNegativeButton("CANCEL") { dialog, which ->
                dialog.cancel()
            }
            .show()
    }

    private fun updateNameDialog(note: Note) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Rename")
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton(
            "Save"
        ) { dialog, which ->
            newTitle = input.text.toString()
            note.name = newTitle
            noteViewModel.updateNote(note)
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }

        builder.show()
    }

}