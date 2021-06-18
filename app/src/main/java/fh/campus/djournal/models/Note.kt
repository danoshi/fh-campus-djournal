package fh.campus.djournal.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

//@Entity(
//    tableName = "note_table",
//    foreignKeys = [ForeignKey(
//        entity = Journal::class,
//        parentColumns = arrayOf("noteId"),
//        childColumns = arrayOf("journalId"),
//        onDelete = ForeignKey.CASCADE
//    )]
//)
@Entity(tableName = "note_table")
data class Note(
    @ColumnInfo(name = "title")
    var name: String = "",
    var journalIdOfNote: Long = 0L,
    @ColumnInfo(name = "text")
    var text: String = "",
    @ColumnInfo(name = "timestamp")
    val timestamp: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var noteId: Long = 0L
}