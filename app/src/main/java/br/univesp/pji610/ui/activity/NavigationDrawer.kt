package br.univesp.pji610.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.datastore.preferences.core.edit
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import br.univesp.pji610.R
import br.univesp.pji610.database.DataSource
import br.univesp.pji610.database.model.User
import br.univesp.pji610.extensions.RedirectTo
import br.univesp.pji610.extensions.dataStore
import br.univesp.pji610.extensions.userPreferences
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class NavigationDrawer : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private lateinit var drawerLayout: DrawerLayout

    private val userDao by lazy {
        DataSource.instance(this).userDao()
    }

    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    protected val user: StateFlow<User?> = _user


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.drawer_navigation)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_navigation)
        val toolbar = findViewById<Toolbar>(R.id.drawer_navigation_toolbar)
        val navigationView = findViewById<NavigationView>(R.id.drawer_navigation_navigationView)


        lifecycleScope.launch {
           checkAuthentication()
        }


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
        when (item.itemId) {
            R.id.navigation_menu_home -> supportFragmentManager.beginTransaction()
                .replace(R.id.drawer_navigation_frameLayout, ChartActivity()).commit()

            R.id.navigation_menu_iot -> supportFragmentManager.beginTransaction()
                .replace(R.id.drawer_navigation_frameLayout, IoTActivity()).commit()

            R.id.navigation_menu_group -> supportFragmentManager.beginTransaction()
                .replace(R.id.drawer_navigation_frameLayout, GroupIotActivity()).commit()

            R.id.navigation_menu_logout -> lifecycleScope.launch {
                logout()
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onBackPressed(){

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }





    private suspend fun checkAuthentication() {
        dataStore.data.collect { preferences ->
            preferences[userPreferences]?.let { userId ->
                getUserById(userId)
            } ?: redirectToLogin()
        }
    }

    private suspend fun getUserById(userId: String): User? {
        return userDao.getById(userId)?.firstOrNull().also { usr ->
            _user.value = usr
        }

    }

    protected suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.remove(userPreferences)
        }
    }

    private fun redirectToLogin() {
        RedirectTo(LoginActivity::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        finish()
    }
}