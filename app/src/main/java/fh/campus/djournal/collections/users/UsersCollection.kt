package fh.campus.djournal.collections.users

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class UsersCollection {

    private val COLLECTION_NAME = "users"
    val EMAIL_FIELD = "email"


    fun getCurrentUser(onCompleteListener: OnCompleteListener<DocumentSnapshot?>?) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        getById(userId, onCompleteListener)
    }


    fun getById(userId: String?, onCompleteListener: OnCompleteListener<DocumentSnapshot?>?) {
        val firestore = FirebaseFirestore.getInstance()
        val documentReference = firestore.collection(COLLECTION_NAME).document(
            userId!!
        )
        documentReference.get().addOnCompleteListener(onCompleteListener!!)
    }

    fun getUserDocumentOf(documentSnapshot: DocumentSnapshot): UsersDocument? {
        val firestore = FirebaseFirestore.getInstance()
        val documentReference = firestore.collection(COLLECTION_NAME).document(EMAIL_FIELD)
        return UsersDocument(email = documentReference.toString())
    }

}
