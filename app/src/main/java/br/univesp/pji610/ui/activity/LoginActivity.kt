package br.univesp.pji610.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import br.univesp.pji610.database.DataSource
import br.univesp.pji610.databinding.ActivityLoginBinding
import br.univesp.pji610.extensions.RedirectTo
import br.univesp.pji610.extensions.ToastMessage
import br.univesp.pji610.extensions.dataStore
import br.univesp.pji610.extensions.userPreferences
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val userDao by lazy {
        DataSource.instance(this).userDao()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    private fun authentication(userName: String, password: String) {
        lifecycleScope.launch {

            userDao.authentication(userName, password)?.let { user ->

                dataStore.edit { preferences ->
                    preferences[userPreferences] = user.id
                }

                RedirectTo(NavigationDrawer::class.java)

                finish()
            } ?: ToastMessage("Falha na autenticação.")
        }
    }

    fun authenticationOnClick(view: View) {

        val userName = binding.activityLoginTextInputEditTextUserName.text.toString()
        val password = binding.activityLoginTextInputEditTextUserName.text.toString()

        authentication(userName, password)
    }

    fun addUserOnClick(view: View) {
        binding.activityLoginButtonAddUser.setOnClickListener {
            RedirectTo(InsertUserActivity::class.java)
        }
    }
}