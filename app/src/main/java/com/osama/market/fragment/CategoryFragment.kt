package com.osama.market.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.osama.market.DisplayItemFragment
import com.osama.market.ItemAdapter
import com.osama.market.R
import com.osama.market.enums.Category
import com.osama.market.model.Item
import com.osama.market.model.User
import com.osama.market.toast
import kotlinx.android.synthetic.main.fragment_category.view.*


/**
 * A simple [Fragment] subclass.
 */
class CategoryFragment : Fragment() {

    private var itemEventListener: ValueEventListener? = null
    private var userEventListener: ValueEventListener? = null
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseRef: DatabaseReference = firebaseDatabase.getReference("Items")
    private val userRef: DatabaseReference = firebaseDatabase.getReference("users")
    private var adapter : ItemAdapter? = null
    private val listItem: ArrayList<Item> by lazy {
           ArrayList<Item>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val category = arguments?.getString("category")
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        updateUiCategory(view, category!!)
        return view
    }

    private fun updateUiCategory(view: View, category: String) {
        when (category) {
            Category.MY_GOODS.value -> {
                itemEventListener = databaseRef.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(e: DatabaseError) {
                        displayErrorConnection(view)
                        Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onDataChange(snapShot: DataSnapshot) {
                        listItem.clear()
                        for (categoryF in snapShot.children) {
                            for (itemF in categoryF.children) {
                                val item = itemF.getValue(Item::class.java)
                                item?.itemId = itemF.key
                                userEventListener = userRef.child(item?.uid!!)
                                    .addValueEventListener(object : ValueEventListener {
                                        override fun onCancelled(p0: DatabaseError) {
                                            activity?.toast("هناك خطأ ما")
                                        }

                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            extractUserFromSnapshotAndCompleteItemObject(
                                                snapshot,
                                                item
                                            )
                                            if (item.uid == FirebaseAuth.getInstance().currentUser?.uid)
                                                listItem.add(item)

                                            hideProgressBar(view.progressbar)
                                            displayEmptyFlagIfNoItem(listItem, view)
                                            adapter = ItemAdapter(listItem, activity!!)
                                            createRecyclerView(view).adapter = adapter
                                            adapter!!.setOnItemLongPressListener(mLongPressListener)
                                            adapter!!.setOnItemClickListener(mClickListener)
                                        }

                                    })

                            }
                        }

                    }
                })
            }
            Category.ALL_GOODS.value -> {
                itemEventListener = databaseRef.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(e: DatabaseError) {
                        displayErrorConnection(view)
                        Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onDataChange(snapShot: DataSnapshot) {
                        if (!snapShot.exists()){
                            listItem.clear()
                            displayEmptyFlagIfNoItem(listItem, view)
                        }
                        listItem.clear()
                        for (categoryF in snapShot.children) {
                            for (itemF in categoryF.children) {
                                val item = itemF.getValue(Item::class.java)
                                item?.itemId = itemF.key
                                userEventListener = nestedFetching(item!!, listItem, view)
                            }
                        }
                    }
                })
            }
            else -> {
                itemEventListener =
                    databaseRef.child(category).addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(e: DatabaseError) {
                            displayErrorConnection(view)
                            Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                        }

                        override fun onDataChange(snapShot: DataSnapshot) {
                            if (!snapShot.exists()){
                                listItem.clear()
                                displayEmptyFlagIfNoItem(listItem, view)
                            }
                            listItem.clear()
                            for (itemF in snapShot.children) {
                                val item = itemF.getValue(Item::class.java)
                                item?.itemId = itemF.key
                                userEventListener = nestedFetching(item!!, listItem, view)

                            }

                        }
                    })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseRef.removeEventListener(itemEventListener!!)
        Log.v("Category Fragment", "destroyed")
    }

    override fun onStop() {
        super.onStop()
        databaseRef.removeEventListener(itemEventListener!!)
        Log.v("Category Fragment", "closed")
    }

    private val mClickListener = object : ItemAdapter.OnItemClickListener {
        override fun onClick(key: String) {
            moveToFragment(DisplayItemFragment(),key)
        }

    }
    private val mLongPressListener = object : ItemAdapter.OnItemLongPressListener {
        override fun onDeleteClick(itemCategory: String, itemId: String, url: String) {
            val databaseRef = FirebaseDatabase.getInstance().getReference("Items")

            val builder = AlertDialog.Builder(activity!!)
            builder.setTitle("مهلا !")
            builder.setMessage("هل انت متأكد من أنك تريد مسح هذه السلعة؟")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("نعم") { _, _ ->
                databaseRef.child(itemCategory).child(itemId).removeValue().addOnCompleteListener {
                    adapter?.notifyDataSetChanged()
                    val image = FirebaseStorage.getInstance().getReferenceFromUrl(url)
                    image.delete()
                    activity?.toast("تم المسح")
                }
            }
            builder.setNeutralButton("الغاء") { _, _ -> }
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

    private fun displayEmptyFlagIfNoItem(list: ArrayList<Item>, view: View) {
        if (list.isEmpty()) {
            view.empty_items.visibility = View.VISIBLE
            hideProgressBar(view.progressbar)
        }else
            view.empty_items.visibility = View.INVISIBLE
    }

    fun displayErrorConnection(view: View) {
        view.empty_items.visibility = View.VISIBLE
    }

    //TODO("...")
    private fun moveToFragment(fragment: Fragment, key: String?) {
        val ft = fragmentManager?.beginTransaction()
        val bundle = Bundle()
        bundle.putString("key", key)
        fragment.arguments = bundle
        ft?.replace(R.id.frame_container, fragment)
        ft?.addToBackStack(null)
        ft?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft?.commit()
    }

    private fun extractUserFromSnapshotAndCompleteItemObject(snapshot: DataSnapshot, item: Item) {
        val user = snapshot.getValue(User::class.java)
        item.userImageUrl = user?.photoUrl!!
        item.userNumber = user.phoneNumber
        item.userName = user.name
    }

    private fun hideProgressBar(view: View) {
        view.visibility = View.INVISIBLE
    }

    private fun createRecyclerView(view: View): RecyclerView {
        val recyclerView = view.findViewById<RecyclerView>(R.id.category_recycler_view)
        if (recyclerView != null) {
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.setHasFixedSize(true)
        }
        return recyclerView
    }

    private fun nestedFetching(
        item: Item,
        listItem: ArrayList<Item>,
        view: View
    ): ValueEventListener {
        return userRef.child(item.uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                activity?.toast("هناك خطأ ما")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                extractUserFromSnapshotAndCompleteItemObject(snapshot, item)
                listItem.add(item)
                displayEmptyFlagIfNoItem(listItem, view)
                hideProgressBar(view.progressbar)
                adapter = ItemAdapter(listItem, activity!!)
                createRecyclerView(view).adapter = adapter
                adapter!!.setOnItemClickListener(mClickListener)
            }
        })
    }
}
