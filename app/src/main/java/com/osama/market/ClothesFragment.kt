package com.osama.market


import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_clothes.*

/**
 * A simple [Fragment] subclass.
 */
class ClothesFragment : Fragment() {


    public val users : ArrayList<User> = arrayListOf(User(1,name = "اسامة",phone = 1093,password= 123,gender= Gender.MALE,image = R.drawable.osama_custom),
        User(2,name = "احمد",phone = 1093,password= 123,gender= Gender.MALE,image = R.drawable.osama_custom),
        User(3,name = "محمود",phone = 1093,password= 123,gender= Gender.MALE,image = R.drawable.osama_custom),
        User(4,name = "على",phone = 1093,password= 123,gender= Gender.MALE,image = R.drawable.osama_custom),
        User(5,name = "عبد الله",phone = 1093,password= 123,gender= Gender.MALE,image = R.drawable.osama_custom))

    public val clothes : ArrayList<Item> = arrayListOf(
        Item("تشيرت",Category.CLOTHES,R.drawable.clothes,60.0,4,State.NEW,"dsdfs fsdfsdfsdf sdfsdfsdfs","16-10-2019"),
        Item("قميص",Category.CLOTHES,R.drawable.clothes,60.0,0,State.NEW,"dsdfs fsdfsdfsdf sdfsdfsdfs","16-10-2019"),
        Item("بنطلون",Category.CLOTHES,R.drawable.clothes,60.0,2,State.NEW,"dsdfs fsdfsdfsdf sdfsdfsdfs","16-10-2019"),
        Item("جزمة",Category.CLOTHES,R.drawable.clothes,60.0,1,State.NEW,"dsdfs fsdfsdfsdf sdfsdfsdfs","16-10-2019"))
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_clothes, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.clothes_recycler_view)
        if (recyclerView != null){
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = ItemAdapter(clothes,users,activity as Context)
        }
        return view
    }


}
