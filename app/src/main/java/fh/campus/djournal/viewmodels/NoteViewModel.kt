package fh.campus.djournal.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fh.campus.djournal.models.Note
import fh.campus.djournal.repositories.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

}