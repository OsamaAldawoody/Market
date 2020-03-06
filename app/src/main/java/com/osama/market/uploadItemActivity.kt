package com.osama.market

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class uploadItemActivity : AppCompatActivity() {

    lateinit var categorySpinner : Spinner
    lateinit var stateSpinner : Spinner
    private var fileAttach: Button? = null
    private var cameraAttach: Button? = null
    private var imageUploaded: ImageView? = null
    private val GALLERY = 3
    private val CAMERA = 4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_item)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val categories = Category.getCategories()

        categorySpinner = findViewById(R.id.item_category_spinner)

        val categoryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, categories
        )
        categorySpinner.adapter = categoryAdapter

        val states = State.getAll()

        stateSpinner = findViewById(R.id.item_state_spinner)

        val stateAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, states
        )
        stateSpinner.adapter = stateAdapter

        fileAttach = findViewById<View>(R.id.file_attach) as Button
        cameraAttach = findViewById<View>(R.id.camera_attach) as Button
        imageUploaded = findViewById<View>(R.id.image_uploaded) as ImageView

        fileAttach!!.setOnClickListener { choosePhotoFromGallary() }
        cameraAttach!!.setOnClickListener { takePhotoFromCamera() }

    }


    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)

    }
    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
         if (resultCode == RESULT_CANCELED)
         {
         return
         }
        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                val contentURI = data.data
                try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    val path = saveImage(bitmap)
                    Toast.makeText(this@uploadItemActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
                    imageUploaded!!.setImageBitmap(bitmap)

                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@uploadItemActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        }
        else if (requestCode == CAMERA)
        {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            imageUploaded!!.setImageBitmap(thumbnail)
            saveImage(thumbnail)
            Toast.makeText(this@uploadItemActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        Log.d("fee",wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {

            wallpaperDirectory.mkdirs()
        }

        try
        {
            Log.d("heel",wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                .getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    companion object {
        private val IMAGE_DIRECTORY = "/market"
    }

}
