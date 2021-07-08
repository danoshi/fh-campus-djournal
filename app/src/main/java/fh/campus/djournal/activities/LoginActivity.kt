package fh.campus.djournal.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fh.campus.djournal.R
import fh.campus.djournal.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        checkLogin()
        switchSignUp()
        binding.textViewForgetPassword.setOnClickListener {
            forgetPw()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun checkLogin() {
        var emailAddress = binding.editTextTextEmailAddress
        var password = binding.editTextTextPassword
        var login = binding.buttonSignIn

        login.setOnClickListener {

            if (emailAddress.text.toString().isEmpty() && password.text.toString().isEmpty()) {
                Toast.makeText(baseContext, "Fields are empty", Toast.LENGTH_SHORT).show()
            } else if (emailAddress.text.toString().isEmpty()) {
                binding.editTextTextEmailAddress.setError("Provide an email address")
                binding.editTextTextEmailAddress.requestFocus()
            } else if (password.text.toString().isEmpty()) {
                binding.editTextTextPassword.setError("Provide an password")
                binding.editTextTextPassword.requestFocus()
            } else if (!(emailAddress.text.toString().isEmpty() && password.text.toString()
                    .isEmpty())
            ) {
                signIn(emailAddress.text.toString(), password.text.toString())
            }
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignIn", "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Sign In", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun forgetPw() {
        val editText = EditText(this)
        val alertDialog = AlertDialog.Builder(this)

        alertDialog.setTitle("Reset Password")
        alertDialog.setMessage("Enter your Email to receive a reset Link")
        alertDialog.setView(editText)

        alertDialog.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
            val email = editText.text.toString();
            auth.sendPasswordResetEmail(email).addOnSuccessListener {
                Toast.makeText(this, "Reset link was successful send", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(this, "Error! Reset link could not be send", Toast.LENGTH_SHORT)
                        .show()
                }
        })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->

            })
        alertDialog.create().show();
    }

    private fun switchSignUp() {
        var signUpLabel = binding.textViewSignUp
        signUpLabel.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        }
    }

    private fun updateUI(user: FirebaseUser?) {

    }

    private fun reload() {

    }

}