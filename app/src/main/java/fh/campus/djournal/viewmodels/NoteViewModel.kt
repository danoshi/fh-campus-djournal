package fh.campus.djournal.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fh.campus.djournal.fragments.NewNoteFragmentDirections
import fh.campus.djournal.models.Note
import fh.campus.djournal.repositories.NoteRepository
import fh.campus.djournal.utils.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteViewModel(
    private val repository: NoteRepository,
) : ViewModel() {
    val notes: LiveData<List<Note>> = repository.getAllNotes()


    fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(note)
        }
    }

    fun getNoteById(noteId: Long): LiveData<Note> {
        return repository.getNoteById(noteId)
    }

    fun clearNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearNotes()
        }
    }

    fun getNotesFromJournal(journalId: Long): LiveData<List<Note>> {
        return repository.getNotesFromJournal(journalId)
    }

    fun clearNotesFromJournal(journalId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearNotesFromJournal(journalId)
        }
    }
/*
    private fun saveNoteDialog(
        noteTitle: String = "default title",
        noteText: String,
        journalId: Long,
        noteId: Long = -1,
        timestamp: String = ""
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("SAVE NOTE")
            .setMessage("Do you want to save your changes")
            .setPositiveButton("CONFIRM") { dialog, which ->
                if (noteId.equals(-1) && timestamp == "") {
                    val newNote = Note(noteTitle, journalId, noteText, Util().getDateTime())
                    noteViewModel.addNote(newNote)
                } else {
                    val newNote = Note(noteTitle, journalId, noteText, timestamp)
                    noteViewModel.updateNote(newNote)

                }
                findNavController().navigate(
                    NewNoteFragmentDirections.actionNewNoteFragmentToNotesFragment(
                        journalId
                    )
                )
            }
            .setNegativeButton("CANCEL") { dialog, which ->
                dialog.cancel()
            }
            .show()

    }
*/

}