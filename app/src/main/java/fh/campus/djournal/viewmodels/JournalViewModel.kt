package fh.campus.djournal.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fh.campus.djournal.models.Journal
import fh.campus.djournal.repositories.JournalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JournalViewModel(
    private val repository: JournalRepository,
) : ViewModel() {
    val journals: LiveData<List<Journal>> = repository.getAllJournals()

    fun addJournal(journal: Journal) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createJournal(journal)
        }
    }

    fun updateJournal(journal: Journal) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateJournal(journal)
        }
    }

    fun deleteJournal(journal: Journal) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteJournal(journal)
        }
    }

    fun clearJournal() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearJournals()
        }
    }

}