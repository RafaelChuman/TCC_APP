package br.univesp.tcc.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.univesp.tcc.database.DataSource
import br.univesp.tcc.database.model.UserToken
import br.univesp.tcc.extensions.RedirectTo
import br.univesp.tcc.extensions.dataStore
import br.univesp.tcc.repository.AuthenticationRepository
import br.univesp.tcc.webclient.AuthenticationWebClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


private const val TAG = "AuthBaseActivity"

abstract class AuthBaseActivity : AppCompatActivity() {

    private val userTokenDAO by lazy {
        DataSource.getDatabase(this).UserTokenDAO()
    }

    private val authenticationWebClient by lazy{
        AuthenticationWebClient()
    }

    private val authenticationRepository by lazy {
        AuthenticationRepository(
            userTokenDAO,
            authenticationWebClient,
            dataStore
        )
    }

//    private val userDao by lazy {
//        DataSource.instance(this).UserDao()
//    }

//    private var _userToken : MutableStateFlow<UserToken?> = MutableStateFlow(null)
//    val userToken: StateFlow<UserToken?> = _userToken



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            checkAuthentication()
        }
    }

    private suspend fun checkAuthentication() {
//        dataStore.data.collect { preferences ->
//            preferences[userIdLogged]?.let { userId ->
//                getUserById(userId)
//            } ?: redirectToLogin()
//        }

        val token = authenticationRepository.checkUserAuthenticated()

        if(token == null) {
            authenticationRepository.logout()
            redirectToLogin()
        }
//        else
//        {
//            _userToken.value = token
//        }
    }

//    private suspend fun getUserById(userId: String): User? {
//        return userDao.getById(userId)?.firstOrNull().also { usr ->
//            _user.value = usr
//        }
//
//    }

//    protected suspend fun logout() {
//
//    }

    suspend fun redirectToLogin() {

        Log.i(TAG, "redirectToLogin")
        authenticationRepository.logout()

        RedirectTo(LoginActivity::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        finish()
    }
}