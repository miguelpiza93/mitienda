package apus.developers.mitienda.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import apus.developers.mitienda.R
import apus.developers.mitienda.view.home.HomeFragment
import apus.developers.mitienda.view.sale.SaleFragment
import apus.developers.mitienda.view.supply.SupplyFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        val TAG = "MainActivity"
        val environment = "environment/dev"//"environment/prod"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.setCheckedItem(R.id.nav_camera)
        changeFragment(HomeFragment.newInstance("", ""))
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        var fragmentTransaction = false
        var fragment: Fragment? = null

        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
                fragment = HomeFragment.newInstance("", "")
                fragmentTransaction = true
            }
            R.id.nav_gallery -> {
                fragment = SaleFragment.newInstance("", "")
                fragmentTransaction = true
            }
            R.id.nav_slideshow -> {
                val sFragment = SupplyFragment.newInstance("", "")
                sFragment.action = 2
                fragment = sFragment
                fragmentTransaction = true
            }
        }

        if(fragmentTransaction) {
            changeFragment(fragment)

            item.setChecked(true)
            getSupportActionBar()?.setTitle(item.getTitle())
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun changeFragment(fragment: Fragment?){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit()
    }
}
