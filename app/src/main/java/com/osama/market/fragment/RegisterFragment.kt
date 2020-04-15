package com.osama.market.fragment


import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.osama.market.R
import com.osama.market.login
import com.osama.market.toast
import kotlinx.android.synthetic.main.fragment_register.view.*

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    private lateinit var txtLogin: TextView
    private val auth = FirebaseAuth.getInstance()
    private val databaseRef = FirebaseDatabase.getInstance().getReference("users")
    lateinit var email: String
    lateinit var userName: String
    lateinit var password: String
    lateinit var repeatPassword: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        view.register_button.setOnClickListener {
            email = view.email_address.text.trim().toString()
            password = view.password_field.text.trim().toString()
            repeatPassword = view.repeat_password.text.trim().toString()
            userName = view.user_name.text.trim().toString()

            if (userName.isEmpty()) {
                view.user_name.error = "الاسم مطلوب"
                view.user_name.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                view.email_address.error = "البريد الالكترونى غير صالح"
                view.email_address.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                view.email_address.error = "البريد الاكلترونى مطلوب"
                view.email_address.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty() || password.length < 8) {
                view.password_field.error = "لابد ان تكون كلمة السر 8 رموز على الاقل"
                view.password_field.requestFocus()
                return@setOnClickListener
            }
            if (password != repeatPassword) {
                view.password_field.error = "كلمة السر غير متطابقة"
                view.password_field.requestFocus()
                return@setOnClickListener
            }
            registerUser(email, password)
        }
        txtLogin = view.findViewById(R.id.go_to_login)
        txtLogin.setOnClickListener {
            moveToFragment(LoginFragment())
        }
        return view
    }

    private fun registerUser(email: String, password: String) {
        view!!.progressbar.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    view!!.progressbar.visibility = View.VISIBLE
                    activity!!.toast("تم تسجيلك ")
                    addUserToDatabase(userName)

                    activity!!.login()
                } else {
                    activity!!.toast(task.exception!!.message!!)
                }
            }
    }

    private fun moveToFragment(fragment: Fragment) {
        val ft = fragmentManager?.beginTransaction()
        ft?.replace(R.id.fragment_container_login_signup, fragment)
        ft?.addToBackStack(null)
        ft?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft?.commit()
    }

    private fun addUserToDatabase(userName: String) {
        auth.currentUser?.updateProfile(
            UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build()
        )
        databaseRef.child(auth.currentUser?.uid!!)
            .setValue(
                getUserInfo(
                    userName,
                    auth.currentUser?.email.toString(),
                    auth.currentUser?.uid!!
                )
            )
    }

    private fun getUserInfo(name: String, email: String, uid: String): HashMap<String, String> {
        val hashMap = HashMap<String, String>()
        hashMap["name"] = name
        hashMap["email"] = email
        hashMap["uid"] = uid
        return hashMap
    }
}
