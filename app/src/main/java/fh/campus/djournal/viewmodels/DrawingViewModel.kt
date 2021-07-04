package fh.campus.djournal.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fh.campus.djournal.models.Drawing
import fh.campus.djournal.repositories.DrawingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DrawingViewModel(
    private val repository: DrawingRepository
) : ViewModel() {
    val drawing: LiveData<List<Drawing>> = repository.getAllDrawings()

    fun addDrawing(drawing: Drawing){
        viewModelScope.launch(Dispatchers.IO) {
            repository.createDrawing(drawing)
        }
    }
    fun updateDrawing(drawing: Drawing){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateDrawing(drawing)
        }
    }
    fun deleteDrawing(drawing: Drawing){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDrawing(drawing)
        }
    }
    fun clearDrawing(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearDrawing()
        }
    }
    fun getDrawingById(drawingId: Long): LiveData<Drawing>{
        return repository.getDrawingById(drawingId)
    }
    fun getDrawingFromJournal(journalId: Long): LiveData<List<Drawing>>{
        return repository.getDrawingFromNote(journalId)
    }
    fun clearDrawingFromJournal(journalId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearDrawingFromNote(journalId)
        }
    }
}