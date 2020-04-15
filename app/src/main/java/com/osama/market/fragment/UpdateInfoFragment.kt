package com.osama.market.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.osama.market.R
import com.osama.market.activity.FirstActivity
import com.osama.market.toast
import kotlinx.android.synthetic.main.activity_upload_item.*
import kotlinx.android.synthetic.main.fragment_update_info.*
import kotlinx.android.synthetic.main.fragment_update_info.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 */
class UpdateInfoFragment : Fragment() {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val REQUEST_IMAGE_CAPTURE = 100
    private lateinit var imageUri: Uri
    var name: String = "بلا اسم"
    var photo: Uri? = null
    val storageRef = FirebaseStorage.getInstance()
        .reference
        .child("pics")
    lateinit var databaseRef: FirebaseDatabase
    lateinit var userRef: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_info, container, false)

        updateUi(view)
        view.save_changes.setOnClickListener {
            name = view.user_name.text.toString().trim()
            if (name.isEmpty()) {
                view.user_name.error = "name required"
                view.user_name.requestFocus()
                return@setOnClickListener
            }
            photo = when {
                ::imageUri.isInitialized -> imageUri
                else -> currentUser?.photoUrl
            }

            val updates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(photo)
                .build()

            view.progressbar.visibility = View.VISIBLE
            currentUser?.updateProfile(updates)
                ?.addOnCompleteListener { task ->
                    view.progressbar.visibility = View.INVISIBLE
                    view.click_to_upload.visibility = View.INVISIBLE
                    if (task.isSuccessful) {
                        databaseRef = FirebaseDatabase.getInstance()
                        userRef = databaseRef.getReference("users")

                        context?.toast("تم التحديث")
                        startActivity(Intent(activity, FirstActivity::class.java))
                    } else {
                        context?.toast(task.exception?.message!!)
                    }
                }
            it.isClickable = false
        }
        view.delete_image.setOnClickListener {
            storageRef.child(currentUser?.uid!!).delete()
                .addOnCompleteListener {
                    view.personal_image_to_upload.setImageResource(R.drawable.ic_image_placeholder)
                    val updates = UserProfileChangeRequest.Builder()
                        .setPhotoUri(null)
                        .build()
                    currentUser.updateProfile(updates)
                    activity?.toast("تم ازالة الصورة")
                }
        }
        view.personal_image_to_upload.setOnClickListener {
            openFileChooser()
        }
        return view
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    private fun updateUi(view: View) {

        currentUser.let {
            val photo = it?.photoUrl
            if (photo == null) {
                view.personal_image_to_upload.setImageResource(R.drawable.ic_image_placeholder)
                return@let
            }
            Glide.with(this)
                .load(photo)
                .into(view.personal_image_to_upload)
            view.click_to_upload.visibility = View.GONE

        }
        if (currentUser?.displayName != null)
            view.user_name.setText(currentUser.displayName.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageUri = data?.data as Uri
            personal_image_to_upload.setImageURI(imageUri)
            uploadImageAndSaveUri(imageUri)
        }
    }

    private fun uploadImageAndSaveUri(image: Uri) {
        val upload = storageRef
            .child(currentUser?.uid!!)
            .putFile(image)

        view!!.progressbar.visibility = View.VISIBLE
        upload.addOnCompleteListener { uploadTask ->
            view!!.progressbar.visibility = View.INVISIBLE

            if (uploadTask.isSuccessful) {
                storageRef.child(currentUser.uid).downloadUrl.addOnCompleteListener { urlTask ->
                    urlTask.result?.let {
                        imageUri = it
                        view?.personal_image_to_upload?.setImageURI(image)
                    }
                }
            } else {
                uploadTask.exception?.let {
                    activity?.toast(it.message!!)
                }
            }

        }
    }

}
