package fh.campus.djournal.utils

import android.content.Context
import android.widget.Toast

class ToastMaker {
    fun toastMaker(context: Context, msg: String) {
        Toast.makeText(
            context,
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }
}