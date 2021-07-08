package fh.campus.djournal.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "journal_table")
data class Journal(
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "description")
    var description: String = "",
    @PrimaryKey(autoGenerate = true)
    var journalId: Long = 0L,
)
