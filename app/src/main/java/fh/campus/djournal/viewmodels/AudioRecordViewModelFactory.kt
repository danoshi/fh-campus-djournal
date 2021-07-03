package fh.campus.djournal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fh.campus.djournal.repositories.AudioRecordRepository

class AudioRecordViewModelFactory(
    private val repository: AudioRecordRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AudioRecordViewModel::class.java)) {
            return AudioRecordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}