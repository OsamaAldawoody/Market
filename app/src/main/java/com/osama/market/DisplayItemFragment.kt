package com.osama.market

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.osama.market.model.Item
import kotlinx.android.synthetic.main.fragment_display_item.view.*

/**
 * A simple [Fragment] subclass.
 */
class DisplayItemFragment : Fragment() {

    val auth : FirebaseUser? by lazy {
         FirebaseAuth.getInstance().currentUser
    }
    val databaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("Items")
    }
    private var valueEventListener:ValueEventListener? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_display_item, container, false)
        val key = arguments?.getString("key")
        valueEventListener = databaseReference.child(key!!).addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                activity?.toast("هناك خطأ ما")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val item = snapshot.getValue(Item::class.java)
                Glide.with(activity!!)
                    .load(item?.userImageUrl)
                    .centerCrop()
                    .fitCenter()
                    .into(view.publisher_image)
                view.publisher_name.text = item?.userName
                view.publish_date.text = item?.publishDate.toString()
                view.item_name.text = item?.itemName
                view.item_price.text = item?.price.toString()
                view.item_description.text = item?.description
                view.seller_number.text = item?.userNumber
                Glide.with(activity!!)
                    .load(item?.imageUrl)
                    .centerCrop()
                    .fitCenter()
                    .into(view.item_image)
            }
        })
        return view
    }

}
