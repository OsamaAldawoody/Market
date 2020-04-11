package com.osama.market.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.osama.market.ItemAdapter
import com.osama.market.R
import com.osama.market.enums.Category
import com.osama.market.model.Item
import com.osama.market.toast
import kotlinx.android.synthetic.main.fragment_category.view.*


/**
 * A simple [Fragment] subclass.
 */
class CategoryFragment : Fragment() {

    private var valueEventListener: ValueEventListener? = null
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseRef : DatabaseReference= firebaseDatabase.getReference("Items")
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val category = arguments?.getString("category")
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        updateUiCategory(view,category!!)
        return view
    }

    private fun updateUiCategory(view:View, category : String){
        val listItem = ArrayList<Item>()
        when (category){
            Category.MY_GOODS.value -> {
                valueEventListener = databaseRef.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(e: DatabaseError) {
                        displayErrorConnection(view)
                        Toast.makeText(activity,e.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onDataChange(snapShot: DataSnapshot) {
                        listItem.clear()
                        for (categoryF in snapShot.children){
                            for (itemF in categoryF.children){
                                val item = itemF.getValue(Item::class.java)
                                item?.itemId = itemF.key
                                item?.userImageUrl = auth.currentUser?.photoUrl.toString()
                                item?.userName = auth.currentUser?.displayName
                                item?.userNumber = auth.currentUser?.phoneNumber
                                item?.uid = auth.uid
                                if (item?.uid == FirebaseAuth.getInstance().currentUser?.uid)
                                    listItem.add(item!!)
                            }
                        }
                        ifNoItemDisplayEmptyFlag(listItem,view)
                        view.progressbar.visibility = View.INVISIBLE
                        val recyclerView = view.findViewById<RecyclerView>(R.id.category_recycler_view)
                        if (recyclerView != null){
                            recyclerView.layoutManager = LinearLayoutManager(activity)
                            recyclerView.setHasFixedSize(true)
                            val adapter = ItemAdapter(listItem, activity!!)
                            adapter.setOnItemClickListener(mListener)
                            recyclerView.adapter = adapter
                        }
                    }
                })
            }
            Category.ALL_GOODS.value -> {
                valueEventListener = databaseRef.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(e: DatabaseError) {
                        displayErrorConnection(view)
                        Toast.makeText(activity,e.message, Toast.LENGTH_LONG).show()
                    }
                    
                    override fun onDataChange(snapShot: DataSnapshot) {
                        listItem.clear()
                        for (categoryF in snapShot.children){
                            for (itemF in categoryF.children){
                                val item = itemF.getValue(Item::class.java)
                                item?.itemId = itemF.key
                                listItem.add(item!!)
                            }
                        }

                        ifNoItemDisplayEmptyFlag(listItem,view)
                        view.progressbar.visibility = View.INVISIBLE
                        val recyclerView = view.findViewById<RecyclerView>(R.id.category_recycler_view)
                        if (recyclerView != null){
                            recyclerView.layoutManager = LinearLayoutManager(activity)
                            recyclerView.setHasFixedSize(true)
                            recyclerView.adapter = ItemAdapter(listItem, activity!!)
                        }
                    }
                })
            }
            else -> {
                valueEventListener = databaseRef.child(category).addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(e: DatabaseError) {
                        displayErrorConnection(view)
                        Toast.makeText(activity,e.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onDataChange(snapShot: DataSnapshot) {
                        listItem.clear()
                        for (itemF in snapShot.children){
                            val item = itemF.getValue(Item::class.java)
                            item?.itemId = itemF.key
                            listItem.add(item!!)
                        }
                        ifNoItemDisplayEmptyFlag(listItem,view)

                        view.progressbar.visibility = View.INVISIBLE
                        val recyclerView = view.findViewById<RecyclerView>(R.id.category_recycler_view)
                        if (recyclerView != null){
                            recyclerView.layoutManager = LinearLayoutManager(activity)
                            recyclerView.setHasFixedSize(true)
                            recyclerView.adapter = ItemAdapter(listItem, activity!!)
                        }
                    }
                })
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        databaseRef.removeEventListener(valueEventListener!!)
        Log.v("Category Fragment","destroyed")
    }

    override fun onStop() {
        super.onStop()
        databaseRef.removeEventListener(valueEventListener!!)
        Log.v("Category Fragment","closed")
    }


    private val mListener = object : ItemAdapter.OnItemClickListener {
        override fun onClick(position: Int) {
            activity?.toast("item $position clicked")
        }

        override fun onDeleteClick(itemCategory: String, itemId: String,url: String) {
            val databaseRef = FirebaseDatabase.getInstance().getReference("Items")

            val builder = AlertDialog.Builder(activity!!)
            builder.setTitle("مهلا !")
            builder.setMessage("هل انت متأكد من أنك تريد مسح هذه السلعة؟")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("نعم"){_, _ ->
                databaseRef.child(itemCategory).child(itemId).removeValue().addOnCompleteListener{
                    val image = FirebaseStorage.getInstance().getReferenceFromUrl(url)
                    image.delete()
                    activity?.toast("تم المسح")
                }
            }
            builder.setNeutralButton("الغاء"){_ , _ -> }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()

        }

        override fun onHideClick(itemId: String, position: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onNotifyClick(userId: String, itemId: String) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    fun ifNoItemDisplayEmptyFlag(list:ArrayList<Item>, view: View){
        if (list.isEmpty()) {
            view.empty_items.visibility = View.VISIBLE
        }
    }
    fun displayErrorConnection(view: View){
        view.empty_items.visibility = View.VISIBLE
    }
}
