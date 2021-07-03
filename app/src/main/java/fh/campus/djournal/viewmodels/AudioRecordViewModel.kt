package fh.campus.djournal.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fh.campus.djournal.models.AudioRecord
import fh.campus.djournal.models.Note
import fh.campus.djournal.repositories.AudioRecordRepository
import fh.campus.djournal.repositories.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AudioRecordViewModel(
    private val repository: AudioRecordRepository
) : ViewModel() {
    val records: LiveData<List<AudioRecord>> = repository.getAllRecords()

    fun addRecord(audioRecord: AudioRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createRecord(audioRecord)
        }
    }

    fun updateRecord(audioRecord: AudioRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateRecord(audioRecord)
        }
    }

    fun deleteRecord(audioRecord: AudioRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRecord(audioRecord)
        }
    }

    fun clearRecords() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearRecords()
        }
    }

    fun getRecordById(recordId: Long): LiveData<AudioRecord> {
        return repository.getRecordById(recordId)
    }

    fun getRecordsFromJournal(journalId: Long): LiveData<List<AudioRecord>> {
        return repository.getRecordsFromNote(journalId)
    }

    fun clearRecordsFromJournal(journalId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearRecordsFromNote(journalId)
        }
    }

}
