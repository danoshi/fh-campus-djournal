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
import com.google.firebase.ktx.Firebase
import fh.campus.djournal.R
import fh.campus.djournal.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration)
        checkFields()
    }

    fun checkFields(){
        var emailAddress = binding.editTextTextEmailAddress
        var password = binding.editTextTextPassword
        var register = binding.buttonSignUp
        register.setOnClickListener {
            createAccount(emailAddress.text.toString(), password.text.toString())
            sendEmailVerification()
            startActivity(Intent(this@RegistrationActivity,LoginActivity::class.java))

            if (emailAddress != null && password == null){
                binding.editTextTextPassword.setError("Please enter your password")
                binding.editTextTextPassword.requestFocus()
            }
            else if (emailAddress == null && password != null){
                binding.editTextTextEmailAddress.setError("Please enter your email")
                binding.editTextTextEmailAddress.requestFocus()
            }
            else if (emailAddress == null && password == null){
                Toast.makeText(baseContext, "Fields are empty", Toast.LENGTH_SHORT).show()
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

    private fun updateUI(user: FirebaseUser?) {

    }
}