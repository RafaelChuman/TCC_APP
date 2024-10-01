package br.univesp.tcc.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import br.univesp.tcc.R
import br.univesp.tcc.database.DataSource
import br.univesp.tcc.database.model.User
import br.univesp.tcc.databinding.ActivityUserMgmtBinding
import br.univesp.tcc.repository.UserRepository
import br.univesp.tcc.ui.activity.USER_ID
import br.univesp.tcc.webclient.UserWebClient
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime

private const val TAG = "UserMgmtActivity"

class UserMgmtActivity : AuthBaseActivity() {

    private var userId: String = ""
    private var userSearched = User()

    private val binding by lazy {
        ActivityUserMgmtBinding.inflate(layoutInflater)
    }
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
        Log.i(TAG, "onCreate - savedInstanceState: $savedInstanceState")

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
        Log.i(TAG, "onCreateOptionsMenu - menu: $menu")

        menuInflater.inflate(R.menu.mgmt_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(menu: MenuItem): Boolean {

        Log.i(TAG, "onOptionsItemSelected - menu: $menu")

        when (menu.itemId) {
            R.id.mgmt_menu_save -> {
                save()
            }

            R.id.mgmt_menu_remove -> {
                remove()
            }
        }
        return super.onOptionsItemSelected(menu)
    }

    private suspend fun getUser() {

        userId = intent.getStringExtra(USER_ID) ?: ""
        Log.i(TAG, "getUser - userId: $userId")

        if (userId.isEmpty()) return

        val userList = userRepository.getById(userId).firstOrNull()
        Log.i(TAG, "getUser - userList: $userList")

        if (userList.isNullOrEmpty()) return

        userSearched = userList.first()

        binding.textInputEditTextName.setText(userSearched.name.toString())
        binding.textInputEditTextUserName.setText(userSearched.userName.toString())
        binding.textInputEditTextImgPath.setText(userSearched.imgPath.toString())
        binding.textInputEditTextEmail.setText(userSearched.email.toString())
        binding.textInputEditTextCellphone.setText(userSearched.cellphone.toString())
        binding.textInputEditTextTelegram.setText(userSearched.telegram.toString())
    }

    private fun remove() {
        Log.i(TAG, "remove - userId: $userId")
        lifecycleScope.launch {

            if (userId.isNotEmpty()) {
                userRepository.delete(listOf(userId))
            }

            finish()
        }
    }

    private fun save() {

        Log.i(TAG, "save - userId: $userId")

        val userCreated = createNewUser()

        if (userId.isEmpty()) {
            lifecycleScope.launch {
                userRepository.insert(userCreated)
                Log.i(TAG, "save - userCreated: $userCreated")
            }
        } else {
            lifecycleScope.launch {
                userRepository.update(userCreated)
                Log.i(TAG, "save - userUpdated: $userCreated")
            }
        }
        finish()
    }

    private fun createNewUser(): User {

        Log.i(TAG, "createNewUser")

        val updatedAt = LocalDateTime.now()

        val userName = binding.textInputEditTextUserName.text.toString()
        val name = binding.textInputEditTextName.text.toString()
        val password = binding.textInputEditTextPassword.text.toString()
        val imgPath = binding.textInputEditTextImgPath.text.toString()
        val email = binding.textInputEditTextEmail.text.toString()
        val cellphone = binding.textInputEditTextCellphone.text.toString()
        val telegram = binding.textInputEditTextTelegram.text.toString()
        val user = User()

        user.userName = userName
        user.name = name
        user.password = password
        user.imgPath = imgPath
        user.email = email
        user.cellphone = cellphone
        user.telegram = telegram
        user.isAdmin = userSearched.isAdmin
        user.createdAt = userSearched.createdAt
        user.updated = updatedAt

        if (userId.isNotEmpty()) {
            user.id = userSearched.id
            user.isAdmin = userSearched.isAdmin
            user.createdAt = userSearched.createdAt
        }

        return user
    }
}