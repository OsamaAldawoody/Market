package com.osama.market.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.osama.market.R
import com.osama.market.fragment.ChatFragment
import com.osama.market.fragment.FriendsFragment
import kotlinx.android.synthetic.main.activity_first.*
import kotlinx.android.synthetic.main.activity_friends_chat.*
import kotlinx.android.synthetic.main.activity_friends_chat.toolbar

class FriendsChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_chat)

        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (toolbar as Toolbar).setNavigationOnClickListener{finish()}
        defineViewAdapterAndAddFragments()
    }

    private fun defineViewAdapterAndAddFragments(){
        val viewAdapter = ViewPagerAdapter(supportFragmentManager)
        viewAdapter.addFragment(FriendsFragment(),"الأصدقاء")
        viewAdapter.addFragment(ChatFragment(),"المحادثات")
        view_pager.adapter = viewAdapter
        tab_layout.setupWithViewPager(view_pager)
    }

    class ViewPagerAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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

        fun addFragment(fragment: Fragment, title:String){
            this.fragments.add(fragment)
            this.titles.add(title)
        }
    }
}
