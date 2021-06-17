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
//        entity = Note::class,
//        parentColumns = arrayOf("id"),
//        childColumns = arrayOf("id"),
//        onDelete = ForeignKey.CASCADE
//    )]
//)
@Entity(tableName = "note_table")
data class Note(
    @ColumnInfo(name = "title")
    var name: String = "",
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "text")
    var text: String = ""
) {

//    TODO: look how this works with room
//    @RequiresApi(Build.VERSION_CODES.O)
//    val timestamp = Timestamp.valueOf(LocalDateTime.now().toString())
}