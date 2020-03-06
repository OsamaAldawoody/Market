package com.osama.market


import android.graphics.Outline
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    lateinit var txtLogin : TextView
    lateinit var btnRegister : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        txtLogin = view.findViewById(R.id.go_to_login)
        txtLogin.setOnClickListener{
            val fragment = LoginFragment()
            var ft = fragmentManager!!.beginTransaction()
            ft.replace(R.id.fragment_container_login_signup,fragment)
            ft.addToBackStack(null)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.commit()
        }


        return view
    }


}
