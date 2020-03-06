package com.osama.market


import android.content.Intent
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
class LoginFragment : Fragment() {

    lateinit var txtRegister : TextView
    lateinit var btnLoginAction : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_login, container, false)
        txtRegister = view.findViewById<TextView>(R.id.go_to_register)
        txtRegister.setOnClickListener{
            var fragment = RegisterFragment()
            var ft = fragmentManager!!.beginTransaction()
            ft.replace(R.id.fragment_container_login_signup,fragment)
            ft.addToBackStack(null)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.commit()
        }

        btnLoginAction = view.findViewById(R.id.login_button)
        btnLoginAction.setOnClickListener{
            val i = Intent(activity, FirstActivity::class.java)
            startActivity(i)
        }

        return view
    }


}
