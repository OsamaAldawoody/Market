package com.osama.market

enum class State(val value:String)  {
    NEW("جديد"),OLD("قديم"),AGRICULTURE("زراعية"),DESERT("صحراوية"),ARTIFICIAL("صناعية");

    companion object{
        fun getGoodsStates():List<String>{
            return  listOf(NEW.value,OLD.value)
        }

        fun getLandsStates():List<String>{
            return  listOf(AGRICULTURE.value,DESERT.value,AGRICULTURE.value)
        }

        fun getAll():List<String>{
            return  listOf(NEW.value,OLD.value,AGRICULTURE.value,DESERT.value,AGRICULTURE.value)
        }
    }

}