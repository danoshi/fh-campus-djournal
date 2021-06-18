package fh.campus.djournal.utils

import fh.campus.djournal.models.Journal

//TODO: remove class later, only for testing purposes
class JournalStore(val myJournals: MutableList<Journal> = mutableListOf()) {

    val defaultJournals = exampleJournals

    fun findJournalUUID(uuid: String): Journal? {
        return defaultJournals.find { journal -> journal.journalId.toString() == uuid }
    }

    companion object {
        val exampleJournals: MutableList<Journal> = createStaticJournalList()

        private fun createStaticJournalList(): MutableList<Journal> {
            val list: MutableList<Journal> = mutableListOf()


            val journal1 = Journal("Homework", "Homework notes for school")
            journal1.journalId = 1

            val journal2 = Journal("Ideas", "My Ideas that I think of")
            journal2.journalId = 2

            val journal3 = Journal("random", "Random thoughts are noted here")
            journal3.journalId = 3

            list.add(journal1)
            list.add(journal2)
            list.add(journal3)

            return list
        }
    }
}