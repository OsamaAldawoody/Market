package com.osama.market

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.osama.market.UserAdapter.ViewHolder
import com.osama.market.activity.ChatRoomActivity
import com.osama.market.model.User


class UserAdapter(val userList:ArrayList<User>, val context: Context): RecyclerView.Adapter<ViewHolder>(){

    class ViewHolder (view: View): RecyclerView.ViewHolder(view) {
        val userImage = view.findViewById<ImageView>(R.id.user_image)
        val userName = view.findViewById<TextView>(R.id.user_name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.user_view, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.userName.text = user.name


        if (user.photoUrl.isNotEmpty())
            Glide.with(this.context)
                .load(user.photoUrl)
                .into(holder.userImage)
        else
            holder.userImage.setImageResource(R.drawable.ic_image_placeholder)

        holder.itemView.setOnClickListener {
            if (position != RecyclerView.NO_POSITION)
                listener?.onClick(userList[position].uid)
        }
    }
    private var listener:OnClickListener? = null
    interface OnClickListener{
        fun onClick(userId: String)
    }
    fun setOnClickListener(listener:OnClickListener){
        this.listener = listener
    }

}