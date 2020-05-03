package com.osama.market

import android.content.Context
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.osama.market.model.Item

class ItemAdapter (val itemList:ArrayList<Item>, val context: Context)
    : RecyclerView.Adapter<ViewHolder>() {
    var longPressListener : OnItemLongPressListener? = null
    var clickListener : OnItemClickListener? = null

    interface OnItemClickListener{
        fun onClick(key: String)
    }
    interface OnItemLongPressListener{

        fun onDeleteClick(itemCategory:String, itemId: String,url:String)
        fun onHideClick(itemId: String,position: Int)
        fun onNotifyClick(userId:String, itemId: String)
    }
    fun setOnItemLongPressListener(listener:OnItemLongPressListener){
        longPressListener = listener
    }
    fun setOnItemClickListener(listener:OnItemClickListener){
        clickListener = listener
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


        holder.itemView.setOnClickListener {
            val pos = holder.adapterPosition
            if(pos != RecyclerView.NO_POSITION){
                clickListener?.onClick(itemList[position].itemId!!)
            }
        }
        holder.itemView.setOnCreateContextMenuListener { menu, _, _ ->
            val delete = menu?.add(Menu.NONE,1,1,"مسح")
            delete?.setOnMenuItemClickListener {
                val pos = holder.adapterPosition
                if(pos != RecyclerView.NO_POSITION){
                    longPressListener?.onDeleteClick(itemList[pos].category,itemList[pos].itemId!!,itemList[pos].imageUrl)
                }
                false
            }
            val notify = menu?.add(Menu.NONE,1,1,"إبلاغ")
            notify?.setOnMenuItemClickListener {
                val pos = holder.adapterPosition
                if(pos != RecyclerView.NO_POSITION){
                    longPressListener?.onNotifyClick(itemList[position].uid, itemList[position].itemId!!)
                }
                false
            }
        }
    }
}

class ViewHolder (view : View): RecyclerView.ViewHolder(view){
    val imageItem = itemView.findViewById<ImageView>(R.id.item_image)
    val publisherName = view.findViewById<TextView>(R.id.publisher_name)
    val publisherImage = view.findViewById<ImageView>(R.id.publisher_image)
    val publishDate = view.findViewById<TextView>(R.id.publish_date)
    val itemName = view.findViewById<TextView>(R.id.item_name)
    val itemPrice = view.findViewById<TextView>(R.id.item_price)
    val itemDescription = view.findViewById<TextView>(R.id.item_description)
}