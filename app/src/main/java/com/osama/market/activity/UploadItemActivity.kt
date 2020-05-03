package com.osama.market.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.osama.market.enums.Category
import com.osama.market.R
import com.osama.market.model.Item
import com.osama.market.toast
import kotlinx.android.synthetic.main.activity_upload_item.*
import kotlinx.android.synthetic.main.item_view.*
import java.util.*


class UploadItemActivity : AppCompatActivity() {


    private val PIC_IMAGE_REQUEST = 3
    private lateinit var uriImage : Uri
    lateinit var uploadTask : StorageTask<UploadTask.TaskSnapshot>
    var databaseRef : DatabaseReference =  FirebaseDatabase.getInstance().getReference("Items")
    var storageRef : FirebaseStorage = FirebaseStorage.getInstance()
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_item)

        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (toolbar as Toolbar).setNavigationOnClickListener{
            finish()
        }
        val categories = Category.getCategories()
        val categoryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, categories
        )
        item_category_spinner.adapter = categoryAdapter

        image_uploaded!!.setOnClickListener {
            openFileChooser()
        }

        btn_upload.setOnClickListener {
            if (item_name_edit_text.text.toString().isEmpty()){
                item_name_edit_text.error = "ضع اسما "
                this.toast("يجب عليك تحديد اسم للمنتج !")
                return@setOnClickListener
            }
            if (item_category_spinner.selectedItem == "اختر"){
                val textError = item_category_spinner.selectedView as TextView
                textError.error = ""
                textError.setTextColor(ContextCompat.getColor(this,R.color.errorInput))
                this.toast("ما هو نوع المنتج الذى تعرضه")
                return@setOnClickListener
            }
            if (item_price_edit_text.text.toString().isEmpty()){
                item_price_edit_text.error = "ضع سعرا"
                this.toast("يجب ادخال سعر للمنتج !")
                return@setOnClickListener
            }
            if (::uploadTask.isInitialized && uploadTask.isInProgress){
                this.toast("Upload in progress")
                return@setOnClickListener
            }
            if(!::uriImage.isInitialized){
                uploadImageWaringMessage()
                return@setOnClickListener
            }


            uploadItem()
        }
    }

    private fun uploadImageWaringMessage(){
        this.toast(resources.getString(R.string.upload_image_warning_message))
    }
    private fun uploadItem(){
        if (::uriImage.isInitialized){
            val imageRef = storageRef.getReference("pics").child(System.currentTimeMillis().toString() + '.'
                    + getFileExtension(uriImage))
            progress_bar.visibility = View.VISIBLE
            uploadTask = imageRef.putFile(uriImage).addOnSuccessListener {task ->
                Handler().postDelayed( {
                    progress_bar.progress = 0
                },1000)
                this.toast("Upload successful")
                task.storage.downloadUrl.addOnSuccessListener {
                    val newItem = Item(
                        item_name_edit_text.text.toString(),
                        item_category_spinner.selectedItem.toString(),
                        item_price_edit_text.text.toString().toDouble(),
                        item_description_edit_text.text.toString(),
                        Date().toString(),
                        it.toString(),"","","",auth.currentUser?.uid!!)
                    val uploadId = databaseRef.push().key
                    databaseRef
                        .child(item_category_spinner.selectedItem.toString())
                        .child(uploadId!!).setValue(newItem)
                    progress_bar.visibility = View.INVISIBLE
                    startActivity(Intent(this,FirstActivity::class.java))
                    finish()
                }
            }.addOnProgressListener {
                val progress = (100.0 * it.bytesTransferred / it.totalByteCount)
                progress_bar.progress = progress.toInt()
            }.addOnFailureListener{
                this.toast(it.message.toString())
            }

        }else{
            this.toast("يجب أن تختار صورة")
        }
    }

    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PIC_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null){
            uriImage = data.data as Uri
            image_uploaded.setImageURI(uriImage)
        }
    }
    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,PIC_IMAGE_REQUEST)
    }
    private fun getFileExtension(uri: Uri):String{
        val mim = MimeTypeMap.getSingleton()
        return mim.getExtensionFromMimeType(contentResolver.getType(uri))!!
    }
}
