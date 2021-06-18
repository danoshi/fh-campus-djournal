package fh.campus.djournal.repositories

import androidx.lifecycle.LiveData
import fh.campus.djournal.models.Note
import fh.campus.djournal.database.NoteDao
import fh.campus.djournal.models.Journal
import fh.campus.djournal.models.JournalWithNotes

class NoteRepository(private val noteDao: NoteDao) {

    suspend fun createNote(note: Note) = noteDao.createNote(note)
    suspend fun updateNote(note: Note) = noteDao.update(note)
    suspend fun deleteNote(note: Note) = noteDao.delete(note)
    suspend fun clearNotes() = noteDao.clear()
    suspend fun clearNotesFromJournal(journalId: Long) = noteDao.clearNotesFromJournal(journalId)
    fun getAllNotes(): LiveData<List<Note>> = noteDao.getAll()
    fun getNotesFromJournal(journalId: Long): LiveData<List<Note>> = noteDao.getNotesFromJournal(journalId)


    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: NoteRepository? = null

        fun getInstance(dao: NoteDao) =
            instance ?: synchronized(this) {
                instance ?: NoteRepository(dao).also { instance = it }
            }
    }
}