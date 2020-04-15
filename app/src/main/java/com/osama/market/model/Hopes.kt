package com.osama.market.model

data class Hopes (val userId:Int,
             val name:String,
             val price:Pair<Int,Int>,
             val description:String)
{
}