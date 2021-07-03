package fh.campus.djournal.repositories

import androidx.lifecycle.LiveData
import fh.campus.djournal.database.AudioRecordDao
import fh.campus.djournal.models.AudioRecord

class AudioRecordRepository(private val recordDao: AudioRecordDao) {
    suspend fun createRecord(audioRecord: AudioRecord) = recordDao.createRecord(audioRecord)
    suspend fun updateRecord(audioRecord: AudioRecord) = recordDao.update(audioRecord)
    suspend fun deleteRecord(audioRecord: AudioRecord) = recordDao.delete(audioRecord)
    suspend fun clearRecords() = recordDao.clear()
    suspend fun clearRecordsFromNote(journalId: Long) = recordDao.clearRecordsFromJournal(journalId)
    fun getAllRecords(): LiveData<List<AudioRecord>> = recordDao.getAll()
    fun getRecordById(recordId: Long): LiveData<AudioRecord> = recordDao.getRecordById(recordId)
    fun getRecordsFromNote(journalId: Long): LiveData<List<AudioRecord>> =
        recordDao.getRecordsFromJournal(journalId)

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: AudioRecordRepository? = null

        fun getInstance(dao: AudioRecordDao) =
            instance ?: synchronized(this) {
                instance ?: AudioRecordRepository(dao).also { instance = it }
            }
    }
}
