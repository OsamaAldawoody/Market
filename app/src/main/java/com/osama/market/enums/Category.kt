package com.osama.market.enums

enum class Category (val value:String){
    UN_KNOWN("آخر"),CLOTHES("ملابس"),GOLD("ذهب"),LANDS("أراضى"),ALL_GOODS("كل السلع"),MY_GOODS("سلعى");
    companion object {
        fun getCategories():List<String>{
            return  listOf(
                "اختر",
                UN_KNOWN.value,
                CLOTHES.value,
                GOLD.value,
                LANDS.value)
        }
    }
}