package apus.developers.mitienda.view

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import apus.developers.mitienda.R
import apus.developers.mitienda.view.home.HomeFragment
import apus.developers.mitienda.view.sale.SaleFragment
import apus.developers.mitienda.view.supply.SupplyFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val TAG = "MainActivity"
        const val environment = "environment/dev"
        //val environment = "environment/prod"
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
        try {
            val pInfo = this.packageManager.getPackageInfo(packageName, 0)
            val header = nav_view.getHeaderView(0)
            val text: TextView = header.findViewById(R.id.version_name_menu)
            text.text = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, e.message, e)
        }

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

            item.isChecked = true
            supportActionBar?.title = item.title
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun changeFragment(fragment: Fragment?){
        supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit()
    }
}
