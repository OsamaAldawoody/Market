package com.osama.market.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.osama.market.R
import com.osama.market.UserAdapter
import com.osama.market.activity.ChatRoomActivity
import com.osama.market.model.User
import com.osama.market.toast

class FriendsFragment : Fragment(){

    lateinit var databaseRef: FirebaseDatabase
    lateinit var listener:UserAdapter.OnClickListener
    lateinit var usersRef:DatabaseReference
    lateinit var changeListener:ValueEventListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        databaseRef = FirebaseDatabase.getInstance()
        val userList = ArrayList<User>()
        usersRef = databaseRef.getReference("users")
        val view = inflater.inflate(R.layout.fragment_friends, container, false)
        changeListener = usersRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                activity?.toast("حدث خطأ ما")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (userF in snapshot.children){
                    val user = userF.getValue(User::class.java)
                    if (user?.uid != FirebaseAuth.getInstance().currentUser?.uid)
                        userList.add(user!!)
                }
                val recyclerView = view.findViewById<RecyclerView>(R.id.friends_recycler_view)
                if (recyclerView != null){
                    recyclerView.layoutManager = LinearLayoutManager(activity)
                    recyclerView.setHasFixedSize(true)
                    val adapter = UserAdapter(userList, activity!!)
                    listener = object :UserAdapter.OnClickListener {
                        override fun onClick(userId: String) {
                            val intent = Intent(activity,ChatRoomActivity::class.java)
                            intent.putExtra("userId",userId)
                            startActivity(intent)
                        }
                    }
                    adapter.setOnClickListener(listener)
                    recyclerView.adapter = adapter
                }
            }
        })
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        usersRef.removeEventListener(changeListener)
    }

}
