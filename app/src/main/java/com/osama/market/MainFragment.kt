package com.osama.market


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {
    lateinit var landsCardView : CardView
    lateinit var GoldCardView : CardView
    lateinit var clothesCardView : CardView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)


        landsCardView = view.findViewById(R.id.lands_card)
        landsCardView.setOnClickListener {
            replaceFragment(LandsFragment())
        }
        clothesCardView = view.findViewById(R.id.clothes_card)
        clothesCardView.setOnClickListener {
            replaceFragment(ClothesFragment())
        }
        return view
    }

    fun replaceFragment(fragment : Fragment){
        val frg = fragment
        val ft = fragmentManager?.beginTransaction()
        ft?.replace(R.id.frame_container_categories,frg)
        ft?.addToBackStack(null)
        ft?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft?.commit()
    }
}
