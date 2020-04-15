package com.osama.market.model

data class Chat (val message:String, val receiver: String, val sender: String){
        constructor():this("","","")
}