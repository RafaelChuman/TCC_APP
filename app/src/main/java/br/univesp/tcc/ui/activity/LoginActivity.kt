package br.univesp.tcc.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.univesp.tcc.database.DataSource
import br.univesp.tcc.databinding.ActivityLoginBinding
import br.univesp.tcc.extensions.RedirectTo
import br.univesp.tcc.extensions.ToastMessage
import br.univesp.tcc.extensions.dataStore
import br.univesp.tcc.repository.AuthenticationRepository
import br.univesp.tcc.webclient.AuthenticationWebClient
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val userTokenDAO by lazy {
        DataSource.instance(this).UserTokenDAO()
    }

    private val authenticationWebClient by lazy{
        AuthenticationWebClient()
    }

    private val authenticationRepository by lazy {
        AuthenticationRepository(
            DataSource.instance(this).UserTokenDAO(),
            AuthenticationWebClient(),
            dataStore
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



//        lifecycleScope.launch {
//
//            userRepository.updateUser()
//
//            val users= userRepository.getUsers()
//
//            Log.i("List Users", "onCreate: ${users?.map { usr -> usr }}")
//        }


/*        val users: Call<List<UserResponse>> = RetrofitInicializador().userService.getUsers();
        users.enqueue(object : Callback<List<UserResponse>?> {
            override fun onResponse(
                p0: Call<List<UserResponse>?>,
                resp: Response<List<UserResponse>?>
            ) {
                resp.body()?.let { userResp ->
                    val user: List<User> = userResp.map { it.user }
                    Log.i("List Users", "onCreate: $user")
                }
            }

            override fun onFailure(p0: Call<List<UserResponse>?>, p1: Throwable) {
                TODO("Not yet implemented")
                Log.e("ListUsers", "onFailure: $p0")
            }
        })*/
    }


    private fun authentication(userName: String, password: String) {
        lifecycleScope.launch {

            val isAuthenticated = authenticationRepository.authenticateUser(userName, password)

            if(isAuthenticated){
                RedirectTo(CarActivity::class.java)
                finish()
                ToastMessage("Usuário Authenticado.")
            }

            ToastMessage("Falha na autenticação.")
        }
    }

    fun authenticationOnClick(view: View) {

        val userName = binding.activityLoginTextInputEditTextUserName.text.toString()
        val password = binding.activityLoginTextInputEditTextPassword.text.toString()

        authentication(userName, password)
    }

    fun addUserOnClick(view: View) {
        binding.activityLoginButtonAddUser.setOnClickListener {
            RedirectTo(InsertUserActivity::class.java)
        }
    }
}