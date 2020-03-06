package com.osama.market

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter (val list:ArrayList<Item>,val userList:ArrayList<User>,val context: Context)
    : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.imageItem != null) {
            holder.imageItem.setImageResource(list.get(position).imageId)
            holder.publisherName.text = userList[list.get(position).userId].name
            holder.publisherImage.setImageResource(userList[list.get(position).userId].image)
            holder.publishDate.text = list.get(position).publishDate
            holder.itemName.text = list.get(position).name
            val returnedState: State = list.get(position).state
            val state : String = when(returnedState){
                State.OLD -> "مستعمل"
                State.NEW -> "جديد"
                State.ARTIFICIAL -> "صناعية"
                State.DESERT -> "صحراوية"
                State.AGRICULTURE -> "زراعية"
            }
            holder.itemState.text = state
            val price : String = list.get(position).price.toString()
            holder.itemPrice.text = price
            holder.itemDescription.text = list.get(position).description
        }
    }
}

class ViewHolder (view : View): RecyclerView.ViewHolder(view){
    val imageItem = view.findViewById<ImageView>(R.id.item_image)
    val publisherName = view.findViewById<TextView>(R.id.publisher_name)
    val publisherImage = view.findViewById<ImageView>(R.id.publisher_image)
    val publishDate = view.findViewById<TextView>(R.id.publish_date)
    val itemName = view.findViewById<TextView>(R.id.item_name)
    val itemState = view.findViewById<TextView>(R.id.item_state)
    val itemPrice = view.findViewById<TextView>(R.id.item_price)
    val itemDescription = view.findViewById<TextView>(R.id.item_description)

}