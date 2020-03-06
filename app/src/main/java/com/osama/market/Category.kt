package com.osama.market

enum class Category (val value:String){
    UN_KNOWN("غير معروف"),CLOTHES("ملابس"),GOLD("ذهب"),LANDS("أراضى");
    companion object {
        fun getCategories():List<String>{
            return  listOf(UN_KNOWN.value,CLOTHES.value,GOLD.value,LANDS.value)
        }
    }
}