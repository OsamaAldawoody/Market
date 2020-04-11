package com.osama.market.model

import com.google.firebase.database.Exclude

data class Item (

    val itemName:String,
    val category: String,
    val price:Double,
    val description:String,
    val publishDate:String,
    val imageUrl : String){
    constructor():this("","",0.0,"","","")

    @get:Exclude
    var itemId: String? = null
    @get:Exclude
    var userImageUrl: String? = null
    @get:Exclude
    var userName: String? = null
    @get:Exclude
    var userNumber: String? = null
    @get:Exclude
    var uid: String? = null
}