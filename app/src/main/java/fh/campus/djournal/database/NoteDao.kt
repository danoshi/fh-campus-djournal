package fh.campus.djournal.database

import androidx.lifecycle.LiveData
import androidx.room.*
import fh.campus.djournal.models.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNote(note: Note): Long

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM note_table")
    suspend fun clear()

    @Query("DELETE FROM note_table WHERE journalIdOfNote = :id")
    suspend fun clearNotesFromJournal(id: Long)

    @Query("SELECT * FROM note_table WHERE noteId = :id")
    fun getNoteById(id: Long): LiveData<Note>

    @Query("SELECT * FROM note_table ORDER BY noteId DESC")
    fun getAll(): LiveData<List<Note>>

    @Query("SELECT * FROM note_table WHERE journalIdOfNote = :id")
    fun getNotesFromJournal(id: Long): LiveData<List<Note>>
}