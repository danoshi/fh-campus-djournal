package fh.campus.djournal.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "drawing_table")
data class Drawing(
    @ColumnInfo(name = "timestamp")
    val timestamp: String = "",
    @ColumnInfo(name = "layout")
    var layout: String = "",
    var journalIdOfDrawing: Long = 0L,
)
{
    @PrimaryKey(autoGenerate = true)
    var drawingId: Long = 0L
}
