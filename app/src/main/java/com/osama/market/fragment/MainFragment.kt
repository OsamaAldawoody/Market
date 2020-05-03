package com.osama.market.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.osama.market.R
import com.osama.market.enums.Category
import com.osama.market.toast
import kotlinx.android.synthetic.main.fragment_main.view.*


/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {
    private val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        view.all_goods_menu_button.setOnClickListener {
            moveToFragment(CategoryFragment(),Category.ALL_GOODS.value)
        }
        view.my_goods_menu_button.setOnClickListener {
            moveToFragment(CategoryFragment(),Category.MY_GOODS.value)
        }
        view.update_info.setOnClickListener {
            moveToFragment(UpdateInfoFragment(),null)
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        if (!mAuth.currentUser?.isEmailVerified!!){
            Handler().postDelayed( {
                val builder = AlertDialog.Builder(activity!!)
                builder.setTitle("تنبيه !")
                builder.setMessage("اضغط لارسال رسالة تأكيد")
                builder.setPositiveButton("أرسل"){_, _ ->
                    FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener{
                            activity?.toast(resources.getString(R.string.verification_sent))
                            mAuth.signOut()
                        }
                }
                builder.setNegativeButton("إغلاق", null)
                val dialog: AlertDialog = builder.create()
                dialog.setCancelable(false)
                dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation;
                dialog.show()
            },5000)

        }
    }
    private fun moveToFragment(fragment:Fragment,category: String?){
        val ft = fragmentManager?.beginTransaction()
        val bundle = Bundle()
        bundle.putString("category",category)
        fragment.arguments = bundle
        ft?.replace(R.id.frame_container,fragment)
        ft?.addToBackStack(null)
        ft?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft?.commit()
    }

}
