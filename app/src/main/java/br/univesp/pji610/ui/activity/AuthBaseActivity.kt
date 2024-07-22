package br.univesp.pji610.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import br.univesp.pji610.database.DataSource
import br.univesp.pji610.database.model.User
import br.univesp.pji610.extensions.RedirectTo
import br.univesp.pji610.extensions.dataStore
import br.univesp.pji610.extensions.userPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

abstract class AuthBaseActivity : AppCompatActivity() {
    private val userDao by lazy {
        DataSource.instance(this).userDao()
    }

    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    protected val user: StateFlow<User?> = _user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            checkAuthentication()
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