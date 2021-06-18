package fh.campus.djournal.database

import androidx.lifecycle.LiveData
import androidx.room.*
import fh.campus.djournal.models.Journal
import fh.campus.djournal.models.JournalWithNotes
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

    @Query("SELECT * FROM journal_table ORDER BY journalId DESC LIMIT 1")
    fun getJournal(): Journal

    @Query("SELECT * FROM journal_table ORDER BY journalId DESC")
    fun getAll(): LiveData<List<Journal>>

}