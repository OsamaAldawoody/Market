package com.osama.market.fragment


import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.osama.market.R
import com.osama.market.login
import com.osama.market.toast

import kotlinx.android.synthetic.main.fragment_login.view.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    lateinit var email : String
    lateinit var password : String
    private  val mAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_login, container, false)
        view.go_to_register.setOnClickListener{
            moveToFragment(RegisterFragment())
        }
        view.login_button.setOnClickListener{
            email = view.email_address.text.trim().toString()
            password = view.password.text.trim().toString()

            if (email.isEmpty()) {
                view.email_address.error = "email required"
                view.email_address.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                view.email_address.error = "email is not valid"
                view.email_address.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty() || password.length < 8) {
                view.password.error = "password required or should be at least 8 character"
                view.password.requestFocus()
                return@setOnClickListener
            }
            loginUser(email, password)
        }

        return view
    }
    private fun loginUser(email: String, password: String) {
        view!!.progressbar.visibility = View.VISIBLE
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                view?.progressbar?.visibility = View.GONE
                if (task.isSuccessful) {
                    context?.login()
                } else {
                    task.exception?.message?.let {
                        context?.toast(it)
                    }
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val user = mAuth.currentUser
        if (user != null){
            context?.login()
        }
    }
    private fun moveToFragment(fragment:Fragment){

        val ft = fragmentManager?.beginTransaction()
        ft?.replace(R.id.fragment_container_login_signup,fragment)
        ft?.addToBackStack(null)
        ft?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft?.commit()

    }
}
