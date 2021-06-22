package fh.campus.djournal.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import fh.campus.djournal.R
import fh.campus.djournal.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration)
        checkFields()
        switchToSignIn()
    }

    private fun checkFields(){
        var emailAddress = binding.editTextTextEmailAddress
        var password = binding.editTextTextPassword
        var register = binding.buttonSignUp
        register.setOnClickListener {
            if (emailAddress.text.toString().isEmpty() && password.text.toString().isEmpty()){
                Toast.makeText(baseContext, "Fields are empty", Toast.LENGTH_SHORT).show()
            }
            else if (emailAddress.text.toString().isEmpty()){
                binding.editTextTextPassword.setError("Please enter your email")
                binding.editTextTextPassword.requestFocus()
            }
            else if (password.text.toString().isEmpty()){
                binding.editTextTextEmailAddress.setError("Please enter your password")
                binding.editTextTextEmailAddress.requestFocus()
            }

            else if (!(emailAddress.text.toString().isEmpty() && password.text.toString().isEmpty())) {
                createAccount(emailAddress.text.toString(), password.text.toString())
                sendEmailVerification()
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("CreateUser", "createUserWithEmail:success")
                    Toast.makeText(baseContext, "Sign up was successful", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)
                    val userID = auth.uid
                    val documentReference = firestore.collection("users").document()
                    val hashMap: HashMap<String, String> = HashMap<String, String>()
                    hashMap["email"] = email
                    documentReference.set(hashMap).addOnSuccessListener {
                        Log.d("UploadToDb", "email is stored in db $userID")
                    }
                    startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("CreateUser", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }
    private fun sendEmailVerification() {
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnSuccessListener {
                Toast.makeText(
                    this@RegistrationActivity,
                    "Verification email has been sent",
                    Toast.LENGTH_SHORT
                ).show() }
            .addOnFailureListener {
                Log.d(
                    "Email",
                    "onFailure: Email not sent" + Toast.makeText(
                        this@RegistrationActivity,
                        "Verfication email have could not be sent",
                        Toast.LENGTH_SHORT
                    ).show()) }
    }

    private fun switchToSignIn(){
        var signInLabel = binding.textViewSignIn
        signInLabel.setOnClickListener {
            startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
        }
    }

    private fun updateUI(user: FirebaseUser?) {

    }
}