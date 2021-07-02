package fh.campus.djournal.models

import androidx.room.*

@Entity(tableName = "records_table")
data class AudioRecord(
    @ColumnInfo(name = "filename")
    var filename: String,
    @ColumnInfo(name = "filePatch")
    var filePath: String,
    @ColumnInfo(name = "timestamp")
    var timestamp: String,
    @ColumnInfo(name = "duration")
    var duration: String,
    @ColumnInfo(name = "Amplitude Path")
    var ampsPath: String,
    var journalIdOfRecord: Long = 0L,
) {

    @PrimaryKey(autoGenerate = true)
    var recordId: Long = 0L
}