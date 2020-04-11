package com.osama.market.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.osama.market.*
import com.osama.market.enums.Category
import com.osama.market.fragment.*
import kotlinx.android.synthetic.main.activity_first.*
import kotlinx.android.synthetic.main.nav_header.view.*

class FirstActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    lateinit var header : View
    lateinit var auth :FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()
        header = nav_view.getHeaderView(0)
        header.sign_out_button.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
        }
        header.nav_header_image.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
            moveToFragment(UpdateInfoFragment(),null)
        }

        val toggle : ActionBarDrawerToggle = object:ActionBarDrawerToggle(this,
            drawer_layout,
            toolbar as Toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        ){
            override fun onDrawerClosed(view: View){
                super.onDrawerClosed(view)

            }

            override fun onDrawerOpened(drawerView: View){
                super.onDrawerOpened(drawerView)
            }
        }

        toggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        defineViewAdapterAndAddFragments()
    }

    private fun defineViewAdapterAndAddFragments(){
        val viewAdapter = ViewPagerAdapter(supportFragmentManager)
        viewAdapter.addFragment(MainFragment(),"الرئيسية")
        viewAdapter.addFragment(FriendsFragment(),"الأصدقاء")
        viewAdapter.addFragment(ChatFragment(),"المحادثات")
        view_pager.adapter = viewAdapter
        tab_layout.setupWithViewPager(view_pager)
    }
    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if (user != null)
        updateUi(user)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_item_action -> {
                val i = Intent(this, UploadItemActivity::class.java)
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        menu?.findItem(R.id.add_phone_number)?.isVisible = false
        menu?.findItem(R.id.main_page)?.isVisible = false
        menu?.findItem(R.id.clothes_page)?.isVisible = false
        menu?.findItem(R.id.gold_page)?.isVisible = false
        menu?.findItem(R.id.land_page)?.isVisible = false
        menu?.findItem(R.id.information)?.isVisible = false
        menu?.findItem(R.id.support)?.isVisible = false
        menu?.findItem(R.id.all_goods)?.isVisible = false
        menu?.findItem(R.id.my_goods)?.isVisible = false
        return true
    }
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            var category : String?= null
            val fragment = when(item.itemId){
                R.id.main_page -> MainFragment()
                R.id.clothes_page -> {
                    category = Category.CLOTHES.value
                    CategoryFragment()
                }
                R.id.land_page -> {
                    category = Category.LANDS.value
                    CategoryFragment()
                }
                R.id.gold_page -> {
                    category = Category.GOLD.value
                    CategoryFragment()
                }
                R.id.my_goods ->{
                    category = Category.MY_GOODS.value
                    CategoryFragment()
                }
                R.id.all_goods ->{
                    category = Category.ALL_GOODS.value
                    CategoryFragment()
                }
                R.id.update_general_info -> UpdateInfoFragment()
                R.id.add_phone_number -> AddPhoneFragment()
                else -> MainFragment()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        moveToFragment(fragment,category)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    private fun updateUi(currentUser:FirebaseUser?){
        currentUser.let {
            if (it?.photoUrl == null)
                header.header_image_view.setImageResource(R.drawable.ic_image_placeholder)
            else{
                Glide.with(this)
                    .load(it.photoUrl)
                    .into(header.header_image_view)
                }
        }
        header.header_name.text = currentUser?.displayName
        header.header_address.text = currentUser?.email

    }
    private fun moveToFragment(fragment:Fragment, category: String?){
        val ft = supportFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putString("category",category)
        fragment.arguments = bundle
        ft.replace(R.id.frame_container,fragment)
        ft.addToBackStack(null)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.commit()
    }

    class ViewPagerAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        var fragments = ArrayList<Fragment>()
        var titles = ArrayList<String>()

        init {
            this.fragments = ArrayList()
            this.titles = ArrayList()
        }
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }

        fun addFragment(fragment: Fragment,title:String){
            this.fragments.add(fragment)
            this.titles.add(title)
        }
    }
}
