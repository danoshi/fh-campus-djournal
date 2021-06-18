package fh.campus.djournal.utils

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.widget.EditText
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fh.campus.djournal.models.Journal
import fh.campus.djournal.viewmodels.JournalViewModel

class JournalDialogs(private val context: Context, private val journalViewModel: JournalViewModel) {
    private var newTitle = ""
    private var newDescription = ""

    fun journalOptionDialog(journal: Journal) {
        val items = arrayOf("Rename", "New Description", "Delete")
        MaterialAlertDialogBuilder(context)
            .setTitle("Options")
            .setItems(items) { dialog, which ->
                when (which) {
                    0 -> {
                        updateNameDialog(journal)
                    }
                    1 -> {
                        updateDescriptionDialog(journal)
                    }
                    2 -> {
                        deleteConfirmationDialog(journal)
                    }
                }
            }
            .setPositiveButton("CANCEL") { dialog, which ->
                dialog.cancel()
            }
            .show()
    }

    private fun deleteConfirmationDialog(journal: Journal) {
        MaterialAlertDialogBuilder(context)
            .setTitle("DANGER")
            .setMessage("Are you sure you want to delete this journal?")
            .setPositiveButton("CONFIRM") { dialog, which ->
                journalViewModel.deleteJournal(journal)
            }
            .setNegativeButton("CANCEL") { dialog, which ->
                dialog.cancel()
            }
            .show()
    }

    private fun updateNameDialog(journal: Journal) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Rename")
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton(
            "Save"
        ) { dialog, which ->
            newTitle = input.text.toString()
            journal.title = newTitle
            journalViewModel.updateJournal(journal)
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }

        builder.show()
    }

    private fun updateDescriptionDialog(journal: Journal) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("New Description")
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton(
            "Save"
        ) { dialog, which ->
            newDescription = input.text.toString()
            journal.description = newDescription
            journalViewModel.updateJournal(journal)
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }

        builder.show()
    }
}