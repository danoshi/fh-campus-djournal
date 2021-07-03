package fh.campus.djournal.database

import androidx.lifecycle.LiveData
import androidx.room.*
import fh.campus.djournal.models.AudioRecord

@Dao
interface AudioRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createRecord(vararg audioRecord: AudioRecord)

    @Query("SELECT * FROM records_table ORDER BY recordId DESC")
    fun getAll(): LiveData<List<AudioRecord>>

    @Delete
    suspend fun delete(audioRecord: AudioRecord)

    @Query("DELETE FROM records_table")
    suspend fun clear()


    @Query("DELETE FROM records_table WHERE journalIdOfRecord = :id")
    suspend fun clearRecordsFromJournal(id: Long)

    @Update
    suspend fun update(audioRecord: AudioRecord)

    @Query("SELECT * FROM records_table WHERE journalIdOfRecord = :id")
    fun getRecordsFromJournal(id: Long): LiveData<List<AudioRecord>>

    @Query("SELECT * FROM records_table WHERE recordId = :id")
    fun getRecordById(id: Long): LiveData<AudioRecord>
}