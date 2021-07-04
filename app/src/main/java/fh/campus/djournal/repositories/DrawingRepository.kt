package fh.campus.djournal.repositories

import androidx.lifecycle.LiveData
import fh.campus.djournal.database.DrawingDao
import fh.campus.djournal.models.Drawing

class DrawingRepository(private val drawingDao: DrawingDao) {
    suspend fun createDrawing(drawing: Drawing) = drawingDao.createDrawing(drawing)
    suspend fun updateDrawing(drawing: Drawing) = drawingDao.update(drawing)
    suspend fun deleteDrawing(drawing: Drawing) = drawingDao.delete(drawing)
    suspend fun clearDrawing() = drawingDao.clear()
    suspend fun clearDrawingFromNote(journalId: Long) = drawingDao.clearDrawingFromJournal(journalId)
    fun getAllDrawings(): LiveData<List<Drawing>> = drawingDao.getAll()
    fun getDrawingById(drawingId: Long): LiveData<Drawing> = drawingDao.getDrawingById(drawingId)
    fun getDrawingFromNote(journalId: Long): LiveData<List<Drawing>> = drawingDao.getDrawingFromJournal(journalId)

    companion object{
        @Volatile
        private var instance: DrawingRepository? = null

        fun getInstance(dao: DrawingDao) =
            instance ?: synchronized(this){
                instance ?: DrawingRepository(dao).also { instance = it }
            }
    }
}