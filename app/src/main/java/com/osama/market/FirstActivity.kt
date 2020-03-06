package com.osama.market

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_first.*

class FirstActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {


    fun Context.toast(text : String){
        Toast.makeText(this,"text",Toast.LENGTH_SHORT).show()
    }
    lateinit var toolbar : Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {

        toast("first extension method ")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)


        val toggle : ActionBarDrawerToggle = object:ActionBarDrawerToggle(this,
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer){
            override fun onDrawerClosed(view: View){
                super.onDrawerClosed(view)

            }

            override fun onDrawerOpened(drawerView: View){
                super.onDrawerOpened(drawerView)
            }
        }

        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)



        val fragment = MainFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frame_container_categories,fragment)
        ft.addToBackStack(null)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.uploadItem -> {
                val i = Intent(this,uploadItemActivity::class.java)
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment = when(item.itemId){
            R.id.main_page -> MainFragment()
            R.id.clothes_page -> ClothesFragment()
            R.id.land_page -> LandsFragment()
            R.id.gold_page -> GoldFragment()
            else -> MainFragment()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frame_container_categories,fragment)
        ft.addToBackStack(null)
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.commit()
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
