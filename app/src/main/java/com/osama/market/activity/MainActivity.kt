package com.osama.market.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.osama.market.fragment.LoginFragment
import com.osama.market.R

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)



    }

    override fun onStart() {
        super.onStart()
        val fragment = LoginFragment()
        val ft : FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container_login_signup,fragment)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.addToBackStack(null)
        ft.commit()
    }


}
