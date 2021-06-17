package fh.campus.djournal.database

import androidx.lifecycle.LiveData
import androidx.room.*
import fh.campus.djournal.models.Journal
import fh.campus.djournal.models.Note

@Dao
interface JournalDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createJournal(journal: Journal): Long

    @Update
    suspend fun update(journal: Journal)

    @Delete
    suspend fun delete(journal: Journal)

    @Query("DELETE FROM journal_table")
    suspend fun clear()

    @Query("SELECT * FROM journal_table ORDER BY id DESC LIMIT 1")
    fun getJournal(): Journal

//    TODO: query to get alls notes belonging to the journal
//    @Query("SELECT * FROM note_table ORDER BY id DESC LIMIT 1")
//    fun getNotes(): Note

    @Query("SELECT * FROM journal_table ORDER BY id DESC")
    fun getAll(): LiveData<List<Journal>>
}