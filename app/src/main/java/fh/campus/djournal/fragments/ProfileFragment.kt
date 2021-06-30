package fh.campus.djournal.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import fh.campus.djournal.R
import fh.campus.djournal.activities.LoginActivity
import fh.campus.djournal.databinding.FragmentProfileBinding
import javax.annotation.Nullable

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        auth = Firebase.auth
        storage = FirebaseStorage.getInstance()
        var storageRef = storage.reference

        setButtonsListeners()
        setEmailNotVerifiedButton()
        resetPasswordButton()
        changeProfileImage()
        deleteAcc()
        changeEmail()

        //Load picture when starting Profile fragment
        var profileRef = storageRef.child(
            "users/" + auth.uid + "/profile.jpg"
        )
        profileRef.downloadUrl.addOnSuccessListener { uri ->
            val imageView = binding.imageViewProfileImage
            Picasso.get().load(uri).into(imageView)
        }

        return binding.root
    }


    private fun deleteAcc(){
        binding.buttonDeleteAcc.setOnClickListener {
            val text = EditText(context)
            val deleteAccDialog = AlertDialog.Builder(context)
            deleteAccDialog.setTitle("Delete account")
            deleteAccDialog.setMessage("Are you sure you want to delete your account?")
            deleteAccDialog.setView(text)
            deleteAccDialog.setPositiveButton(
                "Yes"
            ) { dialog, which ->
                val user = auth.currentUser!!
                user.delete().addOnSuccessListener {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(
                        context,
                        "Delete Account successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                    .addOnFailureListener{
                        Toast.makeText(context, "Delete account failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            deleteAccDialog.setNegativeButton(
                "No"
            ) { dialog, which -> }
            deleteAccDialog.create().show()
        }

    }

    private fun changeEmail(){
        binding.buttonChangeEmail.setOnClickListener {
            val newEmail = EditText(context)
            val newEmailDialog = AlertDialog.Builder(context)
            newEmailDialog.setTitle("New Email")
            newEmailDialog.setMessage("Enter your new email")
            newEmailDialog.setView(newEmail)
            newEmailDialog.setPositiveButton(
                "Yes"
            ) { dialog, which ->
                val user = auth.currentUser
                user!!.updateEmail(newEmail.text.toString()).addOnSuccessListener {
                    Log.d("User", "Updates correctly")
                    Toast.makeText(context, "Your email is updated", Toast.LENGTH_SHORT)
                        .show()
                }
                    .addOnFailureListener {
                        Log.d("User", "Failed")
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                    }
            newEmailDialog.setNegativeButton(
                "No"
            ) { dialog, which -> }
            newEmailDialog.create().show()
        }
    }

    private fun changeProfileImage(){
        binding.buttonChangeProfile.setOnClickListener {
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(galleryIntent, 1000)
        }
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                val imageUri = data!!.data
                if (imageUri != null) {
                    uploadImageToFirebase(imageUri)
                }
            }
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri){
        storage = FirebaseStorage.getInstance()
        var storageRef = storage.reference
        val fileRef = storageRef.child("users/" + auth.uid + "/profile.jpg")
        fileRef.putFile(imageUri).addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                val imageView = binding.imageViewProfileImage
                Picasso.get().load(uri).into(imageView)
            }
            //Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_SHORT).show();
        }.addOnFailureListener { Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show() }
    }

    private fun resetPasswordButton() {
        binding.buttonResetPassword.setOnClickListener { v ->
            val resetPassword = EditText(v.context)
            val passwordResetDialog =
                AlertDialog.Builder(v.context)
            passwordResetDialog.setTitle("Change Password")
            passwordResetDialog.setMessage("Enter a new password")
            passwordResetDialog.setView(resetPassword)
            passwordResetDialog.setPositiveButton(
                "Yes"
            ) { dialog, which ->
                val newPassword = resetPassword.text.toString()
                val user = auth.currentUser
                user?.updatePassword(newPassword)?.addOnSuccessListener {
                    Toast.makeText(
                        v.context,
                        "Password reset successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }?.addOnFailureListener {
                    Toast.makeText(v.context, "Password reset failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            passwordResetDialog.setNegativeButton(
                "No"
            ) { dialog, which -> }
            passwordResetDialog.create().show()
        }
    }

    private fun setEmailNotVerifiedButton() {
        val user = auth.currentUser
        if (user != null) {
            if (!user.isEmailVerified) {
                binding.buttonResendCode.visibility = View.VISIBLE
                binding.buttonResendCode.setOnClickListener { v ->
                    user.sendEmailVerification()
                        .addOnSuccessListener { e: Void? ->
                            Toast.makeText(
                                v.context,
                                "Email",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { e: Exception? ->
                            Toast.makeText(
                                v.context,
                                "Cannot send",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
        }
    }

    private fun setButtonsListeners() {
        binding.buttonLogout.setOnClickListener { v ->
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}