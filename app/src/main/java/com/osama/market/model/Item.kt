package com.osama.market.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Item (
    val itemName:String,
    val category: String,
    val price:Double,
    val description:String,
    val publishDate:String,
    val imageUrl : String,
    var userImageUrl: String,
    var userName: String,
    var userNumber: String,
    var uid: String): Parcelable{
    constructor():this("","",0.0,"",Date().toString(),"","","","","")

    @IgnoredOnParcel
    @get:Exclude
    var itemId: String? = null

}