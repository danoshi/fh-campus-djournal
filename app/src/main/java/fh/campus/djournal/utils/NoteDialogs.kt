package fh.campus.djournal.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Environment
import android.print.PrintAttributes
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.uttampanchasara.pdfgenerator.CreatePdf
import fh.campus.djournal.activities.MainActivity
import fh.campus.djournal.models.Note
import fh.campus.djournal.viewmodels.NoteViewModel

class NoteDialogs(private val context: Context, private val noteViewModel: NoteViewModel) {
    private var newTitle = ""

    fun noteOptionDialog(note: Note) {
        val items = arrayOf("Rename", "Delete", "Convert To PDF")
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
                    2 -> {
                        createPdf(note)
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

    fun createPdf(note: Note){
        CreatePdf(context)
            .setPdfName(note.name)
            .openPrintDialog(true)
            .setPageSize(PrintAttributes.MediaSize.ISO_A4)
            .setContent(note.text)
            .setFilePath("${Activity().externalCacheDir?.absolutePath}/MyPdf")
            .setCallbackListener(object : CreatePdf.PdfCallbackListener {
                override fun onFailure(errorMsg: String) {
                    Toast.makeText(context, "This note can not be converted into PDF", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(filePath: String) {
                    Toast.makeText(context, "Pdf Saved at: $filePath", Toast.LENGTH_SHORT).show()
                }
            })
            .create()
    }


}