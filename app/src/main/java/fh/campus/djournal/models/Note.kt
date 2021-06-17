package fh.campus.djournal.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Note(
    var name: String = ""
) {
    var id: Long = 0L
    @RequiresApi(Build.VERSION_CODES.O)
    val timestamp = Timestamp.valueOf(LocalDateTime.now().toString())
}