package com.alidevs.instaapp.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.alidevs.instaapp.R
import com.alidevs.instaapp.fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_dashboard2.*
import kotlinx.android.synthetic.main.app_bar_dashboard.*
import com.alidevs.instaapp.adapter.ViewPagerAdapter
import com.alidevs.instaapp.fragment.MyWatchesFragment
import kotlinx.android.synthetic.main.content_dashboard.*
import kotlinx.android.synthetic.main.nav_header_dashboard.*


class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard2)
        setSupportActionBar(toolbar)
        //setupHomeFragment()
        setupViewPager()
        val actionBar = supportActionBar
        actionBar!!.title = ""
        actionBar.elevation = 4.0F
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayUseLogoEnabled(true)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        toolbar.setNavigationIcon(R.drawable.ic_menu_arrow)
        nav_view.setNavigationItemSelectedListener(this)
        close.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    private fun setupViewPager() {
        val myPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        pager.adapter = myPagerAdapter
        tablayout.setupWithViewPager(pager)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.my_watches -> {
                setupHomeFragment(MyWatchesFragment())
            }
            R.id.settings -> {
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setupHomeFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.popBackStack()
        fragmentManager.beginTransaction().add(R.id.content_main, fragment).commit()
    }
}
