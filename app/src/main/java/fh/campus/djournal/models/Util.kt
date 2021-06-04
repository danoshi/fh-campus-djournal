package fh.campus.djournal.models

import android.content.Context
import android.widget.Toast

class Util {
    fun toastMaker(context: Context, msg: String) {
        Toast.makeText(
            context,
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }
}