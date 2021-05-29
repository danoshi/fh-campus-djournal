package fh.campus.djournal.models

//TODO: remove class later, only for testing purposes
data class JournalStore(val myJournals: MutableList<Journal> = mutableListOf()) {

    val defaultJournals = exampleJournals

    fun findJournalUUID(uuid: String): Journal? {
        return defaultJournals.find { journal -> journal.id.toString() == uuid }
    }

    companion object{
        val exampleJournals: MutableList<Journal> = createStaticJournalList()

        private fun createStaticJournalList(): MutableList<Journal> {
            val list: MutableList<Journal> = mutableListOf()


            val journal1 = Journal("Homework", "Homework notes for school")
            journal1.id = 1

            val journal2 = Journal("Ideas", "My Ideas that I think of")
            journal2.id = 2

            val journal3 = Journal("random", "Random thoughts are noted here")
            journal3.id = 3

            list.add(journal1)
            list.add(journal2)
            list.add(journal3)

            return list
        }
    }
}