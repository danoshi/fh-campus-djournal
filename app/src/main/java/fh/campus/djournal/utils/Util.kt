package fh.campus.djournal.utils

import java.text.SimpleDateFormat
import java.util.*

class Util {

    fun getDateTime(): String {
        val formatedDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
        val formatedTime = SimpleDateFormat("HH:mm").format(Date())
        val dateTime = "$formatedDate  $formatedTime"
        return dateTime
    }
}