package com.osama.market

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.osama.market.activity.FirstActivity
import com.osama.market.model.Item

fun Context.toast(message: String) {
    Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
}

fun Context.login(){
    startActivity(Intent(this,FirstActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    })
}





