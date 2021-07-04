package fh.campus.djournal.database

import androidx.lifecycle.LiveData
import androidx.room.*
import fh.campus.djournal.models.Drawing


@Dao
interface DrawingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createDrawing(vararg drawing: Drawing)

    @Query("SELECT * FROM drawing_table ORDER BY drawingId DESC")
    fun getAll(): LiveData<List<Drawing>>

    @Delete
    suspend fun delete(drawing: Drawing)

    @Query("DELETE FROM drawing_table")
    suspend fun clear()

    @Query("DELETE FROM drawing_table WHERE journalIdOfDrawing = :id")
    suspend fun clearDrawingFromJournal(id: Long)

    @Update
    suspend fun update(drawing: Drawing)

    @Query("SELECT * FROM drawing_table WHERE journalIdOfDrawing = :id")
    fun getDrawingFromJournal(id: Long): LiveData<List<Drawing>>

    @Query("SELECT * FROM drawing_table WHERE drawingId = :id")
    fun getDrawingById(id: Long): LiveData<Drawing>
}