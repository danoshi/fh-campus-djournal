package fh.campus.djournal.utils

import fh.campus.djournal.models.Note

//TODO: remove class later, only for testing purposes these notes will be in every Journal (foreign key will be done in other feature
class NoteStore(val myNotes: MutableList<Note> = mutableListOf()) {
    val defaultNotes = exampleNotes

    fun getNotes(): List<Note>? {
        return defaultNotes
    }

    companion object {
        val exampleNotes: MutableList<Note> = createStaticNoteList()

        private fun createStaticNoteList(): MutableList<Note> {
            val list: MutableList<Note> = mutableListOf()


            val note1 = Note(
                "Homework",
                1,
                "asd ffffffffffffff ffffffffffffffffffffff fffffffffffffff ffffffffffffff"
            )
            val note2 = Note(
                "Homework",
                2,
                "asd ffffffffffffff ffffffffffffffffffffff fffffffffffffff ffffffffffffff"
            )
            val note3 = Note(
                "Homework",
                3,
                "asd ffffffffffffff ffffffffffffffffffffff fffffffffffffff ffffffffffffff"
            )
            val note4 = Note(
                "Homework",
                4,
                "asd ffffffffffffff ffffffffffffffffffffff fffffffffffffff ffffffffffffff"
            )

            list.add(note1)
            list.add(note2)
            list.add(note3)
            list.add(note4)

            return list
        }
    }
}



