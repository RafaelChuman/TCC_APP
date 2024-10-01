package br.univesp.tcc.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import br.univesp.tcc.R
import br.univesp.tcc.ui.AuthBaseActivity
import br.univesp.tcc.ui.CarActivity
import br.univesp.tcc.ui.OrdersActivity
import br.univesp.tcc.ui.UserActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

private const val TAG = "NavigationDrawer"

class NavigationDrawer : AuthBaseActivity(), NavigationView.OnNavigationItemSelectedListener {


    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.drawer_navigation)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_navigation)
        val toolbar = findViewById<Toolbar>(R.id.drawer_navigation_toolbar)
        val navigationView = findViewById<NavigationView>(R.id.drawer_navigation_navigationView)

//        lifecycleScope.launch {
//           //checkAuthentication()
//        }
        setSupportActionBar(toolbar)

        navigationView.setNavigationItemSelectedListener(this)

        val toggle =
            ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openNav, R.string.closeNav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.drawer_navigation_frameLayout, ChartActivity()).commit()
            navigationView.setCheckedItem(R.id.navigation_menu_home)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.i(TAG, "onNavigationItemSelected: $item.itemId")
        when (item.itemId) {
            R.id.navigation_menu_home -> supportFragmentManager.beginTransaction()
                .replace(R.id.drawer_navigation_frameLayout, CarActivity()).commit()

            R.id.navigation_menu_car -> supportFragmentManager.beginTransaction()
                .replace(R.id.drawer_navigation_frameLayout, CarActivity()).commit()

            R.id.navigation_menu_user -> supportFragmentManager.beginTransaction()
                .replace(R.id.drawer_navigation_frameLayout, UserActivity()).commit()

            R.id.navigation_menu_orders -> supportFragmentManager.beginTransaction()
                .replace(R.id.drawer_navigation_frameLayout, OrdersActivity()).commit()

            R.id.navigation_menu_logout -> lifecycleScope.launch {
                Log.i(TAG, "onNavigationItemSelected: navigation_menu_logout")
                logout()
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun getOnBackInvokedDispatcher(): OnBackInvokedDispatcher {

        if (::drawerLayout.isInitialized) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
        return super.getOnBackInvokedDispatcher()
    }

    private suspend fun logout() {

        this.redirectToLogin()

    }
}