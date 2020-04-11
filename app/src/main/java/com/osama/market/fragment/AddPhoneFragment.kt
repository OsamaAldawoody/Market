package com.osama.market.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

import com.osama.market.R
import com.osama.market.toast
import kotlinx.android.synthetic.main.fragment_add_phone.view.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class AddPhoneFragment : Fragment() {

    private var verificationId : String? = null
    var phoneNumber : String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_phone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        view.add_number_button.setOnClickListener {
            phoneNumber = generatePhone(view.phone_edit_text?.text.toString().trim(),view)
            addMyPhoneNumber(phoneNumber,view)
        }
        view.verify_phone_button.setOnClickListener {
            val code = view.code_verification_edit_text.text.trim().toString()
            verifyPhoneNumber(code,view)
        }

        view.resend.setOnClickListener {
            verifyPhoneNumber(phoneNumber)
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().currentUser.let {
            view?.phone_number?.text = it?.phoneNumber
        }
    }
    private val phoneAuthCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            phoneAuthCredential.let {
                addPhoneNumber(it)
            }
        }

        override fun onVerificationFailed(exception: FirebaseException) {
            context?.toast(exception.message!!)
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(verificationId, token)
            this@AddPhoneFragment.verificationId = verificationId
        }
    }

    private fun addPhoneNumber(phoneAuthCredential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().currentUser
            ?.updatePhoneNumber(phoneAuthCredential)
            ?.addOnCompleteListener{task ->
                if (task.isSuccessful){
                    context?.toast("تم إضافة الرقم")
                }else
                    context?.toast("هناك خطأ ما !")
            }
    }
    private fun addMyPhoneNumber(number: String, view : View){
        if (number == FirebaseAuth.getInstance().currentUser?.phoneNumber ){
            activity?.toast(resources.getString(R.string.number_exist))
        }else if (number.isEmpty() || number.length < 13){
            activity?.toast(resources.getString(R.string.number_incorrect))
        }else{
            verifyPhoneNumber(number)
            view.resend.visibility = View.VISIBLE
            view.send_verification_layout.visibility = View.GONE
            view.verify_phone_layout.visibility = View.VISIBLE
        }
    }

    private fun verifyPhoneNumber(phone: String){
        PhoneAuthProvider.getInstance()
            .verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                requireActivity(),
                phoneAuthCallback
            )
    }
    private fun verifyPhoneNumber(verificationCode:String,view:View){
        view.send_verification_layout.visibility = View.VISIBLE
        view.verify_phone_layout.visibility = View.GONE
        if (verificationCode.isEmpty()){
            view.code_verification_edit_text.error = "الكود مطلوب"
            view.code_verification_edit_text.requestFocus()
            return
        }
        verificationCode.let {
            val credential = PhoneAuthProvider.getCredential(it, verificationId!!)
            addPhoneNumber(credential)
        }
        moveToFragment(MainFragment())
    }
    private fun generatePhone(phone:String,view:View):String{
         return '+' + view.ccp?.selectedCountryCode!! + phone
    }

    private fun moveToFragment(fragment:Fragment){

        val ft = fragmentManager?.beginTransaction()
        ft?.replace(R.id.frame_container,fragment)
        ft?.addToBackStack(null)
        ft?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft?.commit()
    }
}
