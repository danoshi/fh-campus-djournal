package fh.campus.djournal.database

import androidx.lifecycle.LiveData
import androidx.room.*
import fh.campus.djournal.models.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createNote(note: Note): Long

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM note_table")
    suspend fun clear()

    @Query("SELECT * FROM note_table ORDER BY id DESC LIMIT 1")
    fun getNote(): Note

    @Query("SELECT * FROM note_table ORDER BY id DESC")
    fun getAll(): LiveData<List<Note>>
}