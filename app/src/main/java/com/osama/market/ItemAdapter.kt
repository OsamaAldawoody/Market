package com.osama.market

import android.content.Context
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.osama.market.model.Item

class ItemAdapter (val itemList:ArrayList<Item>, val context: Context)
    : RecyclerView.Adapter<ViewHolder>() {
    var mListener : OnItemClickListener? = null
    interface OnItemClickListener{
        fun onClick(position: Int)
        fun onDeleteClick(itemCategory:String, itemId: String,url:String)
        fun onHideClick(itemId: String,position: Int)
        fun onNotifyClick(userId:String, itemId: String)
    }
    fun setOnItemClickListener(listener:OnItemClickListener){
        mListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.publisherName.text = itemList[position].userName

        Glide.with(this.context)
            .load(itemList[position].userImageUrl)
            .centerCrop()
            .fitCenter()
            .into(holder.publisherImage)

        Glide.with(this.context)
            .load(itemList[position].imageUrl)
            .centerCrop()
            .fitCenter()
            .into(holder.imageItem)

        holder.publishDate.text = itemList[position].publishDate
        holder.itemName.text = itemList[position].itemName
        val price = itemList[position].price.toString() + "ج.م"
        holder.itemPrice.text = price
        holder.itemDescription.text = itemList[position].description
        holder.sellerNumber.text = itemList[position].userNumber
        holder.callButton.setOnClickListener {
            val pos = holder.adapterPosition
            if(pos != RecyclerView.NO_POSITION){
                mListener?.onClick(pos)
            }
        }

        holder.itemView.setOnCreateContextMenuListener { menu, _, _ ->
            val delete = menu?.add(Menu.NONE,1,1,"مسح")
            delete?.setOnMenuItemClickListener {
                val pos = holder.adapterPosition
                if(pos != RecyclerView.NO_POSITION){
                    mListener?.onDeleteClick(itemList[position].category,itemList[position].itemId!!,itemList[position].imageUrl)
                }
                false
            }
            val notify = menu?.add(Menu.NONE,1,1,"إبلاغ")
            notify?.setOnMenuItemClickListener {
                val pos = holder.adapterPosition
                if(pos != RecyclerView.NO_POSITION){
                    mListener?.onNotifyClick(itemList[position].uid!!, itemList[position].itemId!!)
                }
                false
            }
        }
    }
}

class ViewHolder (view : View): RecyclerView.ViewHolder(view){
    val imageItem = view.findViewById<ImageView>(R.id.item_image)
    val publisherName = view.findViewById<TextView>(R.id.publisher_name)
    val publisherImage = view.findViewById<ImageView>(R.id.publisher_image)
    val publishDate = view.findViewById<TextView>(R.id.publish_date)
    val itemName = view.findViewById<TextView>(R.id.item_name)
    val itemPrice = view.findViewById<TextView>(R.id.item_price)
    val itemDescription = view.findViewById<TextView>(R.id.item_description)
    val callButton = view.findViewById<ImageButton>(R.id.call_button_image)
    val messageButton = view.findViewById<ImageButton>(R.id.message_button_image)
    val sellerNumber = view.findViewById<TextView>(R.id.seller_number)
}