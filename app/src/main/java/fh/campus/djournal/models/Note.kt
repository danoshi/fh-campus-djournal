package fh.campus.djournal.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "note_table",
    foreignKeys = [ForeignKey(
        entity = Journal::class,
        parentColumns = arrayOf("journalId"),
        childColumns = arrayOf("journalIdOfNote"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Note(
    @ColumnInfo(name = "title")
    var name: String = "",
    var journalIdOfNote: Long = 0L,
    @ColumnInfo(name = "text")
    var text: String = "",
    @ColumnInfo(name = "timestamp")
    val timestamp: String = "",
    @ColumnInfo(name = "layout")
    var layout: String = "",
    @ColumnInfo(name = "freehand_path")
    var freehandPath: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var noteId: Long = 0L
}