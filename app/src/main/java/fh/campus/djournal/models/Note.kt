package fh.campus.djournal.models

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
    var text: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var noteId: Long = 0L

//    TODO: look how this works with room
//    @RequiresApi(Build.VERSION_CODES.O)
//    val timestamp = Timestamp.valueOf(LocalDateTime.now().toString())
}