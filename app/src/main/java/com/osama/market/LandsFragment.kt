package com.osama.market


import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A simple [Fragment] subclass.
 */
class LandsFragment : Fragment() {

    public val users : ArrayList<User> = arrayListOf(User(1,name = "اسامة",phone = 1093,password= 123,gender= Gender.MALE,image = android.R.drawable.btn_star),
        User(2,name = "احمد",phone = 1093,password= 123,gender= Gender.MALE,image = android.R.drawable.btn_star),
        User(3,name = "محمود",phone = 1093,password= 123,gender= Gender.MALE,image = android.R.drawable.btn_star),
        User(4,name = "على",phone = 1093,password= 123,gender= Gender.MALE,image = android.R.drawable.btn_star),
        User(5,name = "عبد الله",phone = 1093,password= 123,gender= Gender.MALE,image = android.R.drawable.btn_star))

    public val lands : ArrayList<Item> = arrayListOf(
        Item("حمدى",Category.LANDS,R.drawable.lands,900.0,0,State.AGRICULTURE, "kjdsfsdjkf sdjfnsjkdfn ksjdfnsdjfn","16-10-2019" ),
        Item("احمد",Category.LANDS,R.drawable.lands,1000.0,4,State.AGRICULTURE,"kjdsfsdjkf sdjfnsjkdfn ksjdfnsdjfn","16-10-2019"),
        Item("على",Category.LANDS,R.drawable.lands,800.0,3,State.DESERT, "kjdsfsdjkf sdjfnsjkdfn ksjdfnsdjfn","16-10-2019"),
        Item("محمود",Category.LANDS,R.drawable.lands,750.0,0,State.ARTIFICIAL, "kjdsfsdjkf sdjfnsjkdfn ksjdfnsdjfn","16-10-2019"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_lands, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.lands_recycler_view)
        if (recyclerView != null){
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = ItemAdapter(lands,users,activity as Context)
        }
        return view
    }


}
