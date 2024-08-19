package br.univesp.tcc.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import br.univesp.tcc.database.DataSource
import br.univesp.tcc.database.model.User
import br.univesp.tcc.extensions.RedirectTo
import br.univesp.tcc.extensions.dataStore
import br.univesp.tcc.extensions.userIdLogged
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

abstract class AuthBaseActivity : AppCompatActivity() {
    private val userDao by lazy {
        DataSource.instance(this).UserDao()
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
            preferences[userIdLogged]?.let { userId ->
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
            preferences.remove(userIdLogged)
        }
    }

    private fun redirectToLogin() {
        RedirectTo(LoginActivity::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        finish()
    }
}