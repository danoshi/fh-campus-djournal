package fh.campus.djournal.models

import androidx.room.Embedded
import androidx.room.Relation

data class JournalWithNotes(
    @Embedded
    val journal: Journal,
    @Relation(
        parentColumn = "journalId",
        entityColumn = "journalIdOfNote"
    )
    val notes: List<Note>
)