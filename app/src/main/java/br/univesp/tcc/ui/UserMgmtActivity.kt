package br.univesp.tcc.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import br.univesp.tcc.R
import br.univesp.tcc.database.DataSource
import br.univesp.tcc.databinding.ActivityItemMgmtBinding
import br.univesp.tcc.databinding.ActivityUserMgmtBinding
import br.univesp.tcc.extensions.dataStore
import br.univesp.tcc.repository.UserRepository
import br.univesp.tcc.ui.activity.NavigationDrawer
import br.univesp.tcc.ui.activity.USER_ID
import br.univesp.tcc.webclient.UserWebClient
import br.univesp.tcc.webclient.model.DTOCreateUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

private const val TAG = "UserMgmtActivity"

class UserMgmtActivity : AuthBaseActivity() {

    private val binding by lazy {
        ActivityUserMgmtBinding.inflate(layoutInflater)
    }

    private var userId: String = ""

    private val userDao by lazy {
        DataSource.getDatabase(this).UserDao()
    }

    private val userWebClient by lazy {
        UserWebClient()
    }

    private val userRepository by lazy {
        UserRepository(
            userDao,
            userWebClient,
            this.baseContext
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)

        setContentView(binding.root)

        lifecycleScope.launch {
            launch {
                getUser()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mgmt_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(user: MenuItem): Boolean {

        when (user.itemId) {
            R.id.mgmt_menu_save -> {
                save()
            }

            R.id.mgmt_menu_remove -> {
                remove()
            }
        }
        return super.onOptionsItemSelected(user)
    }

    private suspend fun  getUser() {

        userId = intent.getStringExtra(USER_ID).toString()
        Log.i(TAG, "getUser - userId: $userId")

        if(userId.isNotBlank() ){

            val userList = userRepository.getById(userId).firstOrNull()
            Log.i(TAG, "getUser - userList: $userList")

            if(!userList.isNullOrEmpty())
            {
                userList.forEach { user ->
                    binding.textInputEditTextName.setText(user.name.toString())
                    binding.textInputEditTextUserName.setText(user.userName.toString())
                    binding.textInputEditTextPassword.setText(user.password.toString())
                    binding.textInputEditTextPassword.setText(user.password.toString())
                    binding.textInputEditTextImgPath.setText(user.imgPath.toString())
                    binding.textInputEditTextEmail.setText(user.email.toString())
                    binding.textInputEditTextCellphone.setText(user.cellphone.toString())
                    binding.textInputEditTextTelegram.setText(user.telegram.toString())
                }
            }
        }
    }

    private fun remove() {
        lifecycleScope.launch {

            if(userId.isNotBlank() ){
                userRepository.delete(listOf(userId) )
            }

            finish()
        }
    }

    private fun save() {

        val userCreated = createNewUser()

        lifecycleScope.launch {
            userRepository.insert(userCreated)
            Log.i(TAG, "save: $userCreated")

            finish()
        }
    }

    private fun createNewUser(): DTOCreateUser {
        val userName = binding.textInputEditTextUserName.text.toString()
        val name = binding.textInputEditTextName.text.toString()
        val password = binding.textInputEditTextPassword.text.toString()
        val imgPath = binding.textInputEditTextImgPath.text.toString()
        val email = binding.textInputEditTextEmail.text.toString()
        val cellphone = binding.textInputEditTextCellphone.text.toString()
        val telegram = binding.textInputEditTextTelegram.text.toString()

        val user: DTOCreateUser = DTOCreateUser(
            userName = userName,
            name = name,
            password = password,
            imgPath = imgPath,
            email = email,
            cellphone = cellphone,
            telegram = telegram,
            isAdmin = false,
        )
        return user
    }
}