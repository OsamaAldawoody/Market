package com.osama.market

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.osama.market.model.Chat

class MessageAdapter (val messageList:ArrayList<Chat>, val context: Context, val imageUrl: String): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    companion object{
         const val MSG_TYPE_LEFT = 1
         const val MSG_TYPE_RIGHT = 0
    }
    private val fUser: FirebaseAuth = FirebaseAuth.getInstance()
    class ViewHolder (view: View): RecyclerView.ViewHolder(view) {
        val userImage = view.findViewById<ImageView>(R.id.user_image)
        val textMessage = view.findViewById<TextView>(R.id.message_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == MSG_TYPE_LEFT){
            ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false)
            )
        }else{
            ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messageList[position]
        holder.textMessage.text = message.message
        if (imageUrl.contentEquals("null")){
            holder.userImage.setImageResource(R.drawable.ic_image_placeholder)
        }else{
            Glide.with(this.context).load(imageUrl).into(holder.userImage)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (fUser.currentUser?.uid == messageList[position].sender)
            MSG_TYPE_RIGHT
        else
            MSG_TYPE_LEFT
    }

}