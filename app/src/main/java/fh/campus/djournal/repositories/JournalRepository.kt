package fh.campus.djournal.repositories

import androidx.lifecycle.LiveData
import fh.campus.djournal.database.JournalDao
import fh.campus.djournal.models.Journal

class JournalRepository(private val journalDao: JournalDao) {

    suspend fun createJournal(journal: Journal) = journalDao.createJournal(journal)
    suspend fun updateJournal(journal: Journal) = journalDao.update(journal)
    suspend fun deleteJournal(journal: Journal) = journalDao.delete(journal)
    suspend fun clearJournals() = journalDao.clear()
    fun getAllJournals(): LiveData<List<Journal>> = journalDao.getAll()
//    fun getNotesFromJournal(): LiveData<List<Note>> = journalDao.getNotesFromJournal()


    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: JournalRepository? = null

        fun getInstance(dao: JournalDao) =
            instance ?: synchronized(this) {
                instance ?: JournalRepository(dao).also { instance = it }
            }
    }
}