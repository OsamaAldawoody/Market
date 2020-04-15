package com.osama.market.model

data class User (val uid: String,
            val name: String,
            val email: String,
            val phoneNumber: String,
            val photoUrl: String) {
    constructor():this("", "", "", "", "")
}