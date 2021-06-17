package fh.campus.djournal.database

import androidx.lifecycle.LiveData
import androidx.room.*
import fh.campus.djournal.models.Journal

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

    @Query("SELECT * FROM journal_table ORDER BY id DESC")
    fun getAll(): LiveData<List<Journal>>
}