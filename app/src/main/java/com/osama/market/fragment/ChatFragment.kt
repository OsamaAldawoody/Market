package com.osama.market.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.osama.market.R
import com.osama.market.UserAdapter
import com.osama.market.model.Chat
import com.osama.market.model.User
import com.osama.market.toast

/**
 * A simple [Fragment] subclass.
 */
class ChatFragment: Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var userAdapter: UserAdapter
    lateinit var usersList: ArrayList<User>
    lateinit var usersIdsList: ArrayList<String>
    lateinit var fUser: FirebaseUser
    lateinit var chatRef: DatabaseReference
    lateinit var usersRef: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        recyclerView = view.findViewById(R.id.chats_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        usersIdsList = ArrayList()
        usersList = ArrayList()
        return view
    }
    private fun readChats(){
        usersRef = FirebaseDatabase.getInstance().getReference("users")
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                activity?.toast("هناك خطأ ما")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                usersList.clear()

                for (userF in snapshot.children){
                    val user = userF.getValue(User::class.java)
                    //display one user from chat
                    for (id in usersIdsList){
                        if (user?.uid == id){
                            if (usersList.size != 0){
                                for (userL in usersList){
                                    if (user.uid != userL.uid){
                                        usersList.add(user)
                                    }
                                }
                            }else{
                                usersList.add(user)
                            }
                        }
                    }
                }
                userAdapter = UserAdapter(usersList,activity!!)
                recyclerView.adapter = userAdapter
            }
        })
    }
}
