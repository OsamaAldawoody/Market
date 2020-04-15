package com.osama.market.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.osama.market.MessageAdapter
import com.osama.market.R
import com.osama.market.model.Chat
import com.osama.market.model.User
import com.osama.market.toast
import kotlinx.android.synthetic.main.activity_chat_room.*

class ChatRoomActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()
    private val databaseRef: FirebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var  messageList : ArrayList<Chat>
    lateinit var messageAdapter: MessageAdapter
    lateinit var recyclerView: RecyclerView
    private lateinit var userRef: DatabaseReference
    private lateinit var chatRef: DatabaseReference
    private lateinit var userEventListener: ValueEventListener
    private lateinit var chatEventListener: ValueEventListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }


        val userId: String = intent.getStringExtra("userId")!!
        val fUser = auth.currentUser
        userRef = databaseRef.getReference("users").child(userId)

        userEventListener = userRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                this@ChatRoomActivity.toast("هناك خطأ ما !")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                user_name.text = user?.name
                if (user?.photoUrl.equals("null")) {
                    user_image.setImageResource(R.drawable.ic_image_placeholder)
                } else {

                    Glide.with(this@ChatRoomActivity)
                        .load(user?.photoUrl)
                        .into(user_image)
                }
                readMessage(fUser?.uid!!, userId,user?.photoUrl!!)

            }
        })



        recyclerView = findViewById(R.id.messages_recycler_view)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        linearLayoutManager.reverseLayout = false
        recyclerView.layoutManager = linearLayoutManager



        send_button.setOnClickListener {
            if (message_edit_text.text.isEmpty())
                return@setOnClickListener
            sendMessage(auth.currentUser?.uid!!, userId, message_edit_text.text.toString())
            message_edit_text.text.clear()
        }
    }


    private fun sendMessage(sender: String, receiver: String, message: String) {
        val messageObject = Chat(message, receiver, sender)
        val chatRef = databaseRef.getReference("chats")
        chatRef.push().setValue(messageObject)
    }

    private fun readMessage(myId: String, userId: String,imageUrl: String) {
        messageList = ArrayList()
        chatRef = FirebaseDatabase.getInstance().getReference("chats")
        chatEventListener = chatRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                this@ChatRoomActivity.toast("هناك خطأ ما !")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (message in snapshot.children) {
                    val mMessage = message.getValue(Chat::class.java)
                    if (myId == mMessage?.receiver && userId == mMessage.sender ||
                        mMessage?.sender == myId && mMessage.receiver == userId
                    ) {
                        messageList.add(mMessage)
                    }
                    messageAdapter = MessageAdapter(messageList, this@ChatRoomActivity,imageUrl )
                    recyclerView.adapter = messageAdapter
                    messageAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        chatRef.removeEventListener(chatEventListener)
        userRef.removeEventListener(userEventListener)
    }
}
