package fh.campus.djournal.models


data class Journal(
    var title: String = "",
    var description: String = "",
) {
    var id: Long = 0L
    var color: Int = 0
}